package com.travel.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.travel.entity.BookingOrder;
import com.travel.entity.BookingSlot;
import com.travel.entity.EntertainmentItem;
import com.travel.entity.ScenicSpot;
import com.travel.enums.BookingStatus;
import com.travel.exception.BusinessException;
import com.travel.mapper.BookingOrderMapper;
import com.travel.mapper.BookingSlotMapper;
import com.travel.mapper.EntertainmentItemMapper;
import com.travel.mapper.ScenicSpotMapper;
import com.travel.service.BookingOrderService;
import com.travel.service.UserCouponService;
import com.travel.vo.BookingOrderAdminVO;
import com.travel.vo.BookingOrderVO;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class BookingOrderServiceImpl implements BookingOrderService {

  private static final long MAX_PAGE_SIZE = 100L;

  private final BookingOrderMapper orderMapper;
  private final BookingSlotMapper slotMapper;
  // 价格来源分别在两张不同的表，且字段名不同（scenic_spot.ticket_price / entertainment_item.price），
  // 两张表结构、字段都不通用，只能各自注入对应的Mapper，不能合并成一次查询。
  private final ScenicSpotMapper scenicSpotMapper;
  private final EntertainmentItemMapper entertainmentItemMapper;
  // 下单时用来校验优惠券是否可用、计算优惠金额，以及订单落库后标记优惠券已核销。
  private final UserCouponService userCouponService;

  public BookingOrderServiceImpl( BookingOrderMapper orderMapper, BookingSlotMapper slotMapper,
                                  ScenicSpotMapper scenicSpotMapper, EntertainmentItemMapper entertainmentItemMapper,
                                  UserCouponService userCouponService ) {
    this.orderMapper = orderMapper;
    this.slotMapper = slotMapper;
    this.scenicSpotMapper = scenicSpotMapper;
    this.entertainmentItemMapper = entertainmentItemMapper;
    this.userCouponService = userCouponService;
  }

  @Override
  @Transactional
  public Long createOrder( Long userId, Long slotId, Integer quantity, Long userCouponId ) {
    if ( userId == null || slotId == null ) {
      throw new BusinessException( 400, "用户ID和时段ID不能为空" );
    }
    // quantity 现在由前端传入，不能再假设它恒为1，所以要显式校验非空且大于0。
    // DTO 层的 @Min(1) 已经拦过一次 Controller 入口，这里是 Service 层的第二道防线——
    // 任何未来可能绕开 Controller 直接调用 Service 的场景（比如内部定时任务、RPC）都还能被兜住。
    if ( quantity == null || quantity <= 0 ) {
      throw new BusinessException( 400, "预约数量必须大于0" );
    }

    // 这里查一次 slot，目的只是拿 targetType/targetId 去算价格，不是用来判断"名额够不够"。
    // 为什么敢在这里用普通 SELECT，而不用加锁查询？因为"名额是否够"的判断被下放到了
    // 后面 deductCapacity 那一条原子 UPDATE 里完成，这次 SELECT 和真正的名额扣减之间
    // 就算有并发时间差也无所谓——最坏情况是这里查到的 slot 信息在极短时间内变了，
    // 但真正决定这次下单能不能成立的，是下面那条 UPDATE 返回的影响行数，而不是这次 SELECT。
    BookingSlot slot = slotMapper.selectById( slotId );
    if ( slot == null ) {
      throw new BusinessException( 400, "时段不存在" );
    }

    // 按 target_type 分支查单价：景点和玩乐项目的价格存在两张不同的表、不同的字段名，
    // 业务上没有办法用一次通用查询取到，只能在 getUnitPrice 里做分支。
    BigDecimal unitPrice = getUnitPrice( slot.getTargetType(), slot.getTargetId() );
    BigDecimal originalAmount = unitPrice.multiply( BigDecimal.valueOf( quantity ) );

    // 优惠券校验+计算优惠金额是纯读操作，不修改任何数据库状态，所以可以放心地在
    // "扣名额"之前先算出来。真正把优惠券标记为"已使用"（写操作）被推迟到订单插入成功之后，
    // 这是一个很重要的顺序安排，原因见下面 markAsUsed 调用处的注释。
    BigDecimal discountAmount = BigDecimal.ZERO;
    if ( userCouponId != null ) {
      discountAmount = userCouponService.validateAndCalculateDiscount( userId, userCouponId, originalAmount );
    }
    BigDecimal actualAmount = originalAmount.subtract( discountAmount );
    if ( actualAmount.signum() < 0 ) {
      // 理论上优惠金额不会超过原价，UserCouponService 内部也会校验这一点。
      // 这里仍然做一次兜底，是防御性编程的体现：即使未来优惠券配置出现"满减金额设置得
      // 比订单实际能达到的门槛还高"这类运营失误，也不能让 actualAmount 变成负数——
      // 负数金额对任何下游的支付网关来说都是非法输入，宁可在这里拦住，也不要让它流到支付环节。
      actualAmount = BigDecimal.ZERO;
    }

    // 扣名额和插订单必须在同一个事务里：任一步失败，都要让数据库回到下单前的状态，
    // 这也是 @Transactional 加在这个方法上的原因。
    int affected = slotMapper.deductCapacity( slotId, quantity );
    if ( affected == 0 ) {
      throw new BusinessException( 400, "该时段不存在、名额不足、已下架或已开始，请选择其他时段或减少数量" );
    }

    BookingOrder order = new BookingOrder();
    order.setUserId( userId );
    order.setSlotId( slotId );
    order.setQuantity( quantity );
    order.setOriginalAmount( originalAmount );
    order.setDiscountAmount( discountAmount );
    order.setActualAmount( actualAmount );
    order.setUserCouponId( userCouponId );
    order.setStatus( BookingStatus.PENDING.getCode() );
    order.setIsDeleted( 0 );
    try {
      orderMapper.insert( order );
    }
    catch ( DuplicateKeyException e ) {
      // 撞到 user_id + slot_id 唯一索引时抛业务异常；事务会回滚上面的名额扣减。
      throw new BusinessException( 400, "你已经预订过这个时段了" );
    }

    // 订单插入成功后才真正核销优惠券（调用 markAsUsed）。这个顺序是刻意的：
    // markAsUsed 内部没有自己单独的 @Transactional，Spring 会让它自动加入当前
    // createOrder 方法开启的事务。如果上面 insert 抛出异常，方法会在这里直接
    // 提前返回（异常向上抛出），markAsUsed 根本不会被执行到；就算它被执行了，
    // 只要事务最终因为异常回滚，这次核销也会跟着回滚。
    // 这样就保证了"优惠券被核销"和"订单创建成功"永远同生共死，不会出现
    // "券用掉了、但订单没建成"这种数据不一致的情况。
    if ( userCouponId != null ) {
      userCouponService.markAsUsed( userCouponId, order.getId() );
    }

    return order.getId();
  }

  /**
   * 按预约目标类型查单价。
   * 存在这个方法的唯一原因是：景点单价存在 scenic_spot.ticket_price，
   * 玩乐项目单价存在 entertainment_item.price（对应 Java 字段 avgPrice），
   * 两张表字段名不同、Mapper 也不同，没法写一段通用查询，只能按类型分支处理。
   */
  private BigDecimal getUnitPrice( String targetType, Long targetId ) {
    if ( "SCENIC".equals( targetType ) ) {
      ScenicSpot spot = scenicSpotMapper.selectById( targetId );
      if ( spot == null ) {
        throw new BusinessException( 400, "关联的景点不存在" );
      }
      return spot.getTicketPrice();
    } else if ( "ENTERTAINMENT".equals( targetType ) ) {
      EntertainmentItem item = entertainmentItemMapper.selectById( targetId );
      if ( item == null ) {
        throw new BusinessException( 400, "关联的场所不存在" );
      }
      // 注意：EntertainmentItem 的数据库列名是 price，但 Java 字段特意起名为 avgPrice
      // （见实体类上的 @TableField("price") private BigDecimal avgPrice），
      // 所以这里必须调用 getAvgPrice()，调用 getPrice() 是编译不过的——
      // 这类"列名和字段名不一致"的实体，最容易在跨文件调用时被想当然地按列名去猜方法名。
      return item.getAvgPrice();
    }
    throw new BusinessException( 400, "未知的预约目标类型：" + targetType );
  }

  @Override
  public void confirmOrder( Long orderId ) {
    BookingOrder order = orderMapper.selectById( orderId );
    if ( order == null ) {
      throw new BusinessException( 404, "订单不存在" );
    }
    BookingStatus current = BookingStatus.fromCode( order.getStatus() );
    if ( !current.canTransitionTo( BookingStatus.CONFIRMED ) ) {
      throw new BusinessException( 400, "当前状态[" + current.getDesc() + "]不能确认" );
    }
    int affected = orderMapper.updateStatusIfCurrent(
        orderId,
        current.getCode(),
        BookingStatus.CONFIRMED.getCode()
    );
    if ( affected == 0 ) {
      throw new BusinessException( 409, "订单状态已变化，请刷新后重试" );
    }
  }

  @Override
  @Transactional
  public void cancelOrder( Long userId, Long orderId, boolean isAdmin ) {
    BookingOrder order = orderMapper.selectById( orderId );
    if ( order == null ) {
      throw new BusinessException( 404, "订单不存在" );
    }

    if ( !isAdmin && !order.getUserId().equals( userId ) ) {
      throw new BusinessException( 403, "无权操作他人的订单" );
    }
    BookingStatus current = BookingStatus.fromCode( order.getStatus() );
    if ( !current.canTransitionTo( BookingStatus.CANCELLED ) ) {
      throw new BusinessException( 400, "当前状态[" + current.getDesc() + "]不能取消" );
    }

    int orderAffected = orderMapper.updateStatusIfCurrent(
        orderId,
        current.getCode(),
        BookingStatus.CANCELLED.getCode()
    );
    if ( orderAffected == 0 ) {
      throw new BusinessException( 409, "订单状态已变化，请刷新后重试" );
    }

    // 恢复名额要按订单当初实际占用的 quantity 份数恢复，而不是固定恢复1份——
    // 直接从订单记录里读 quantity，保证"扣了多少、就还多少"，不需要重新计算。
    int slotAffected = slotMapper.restoreCapacity( order.getSlotId(), order.getQuantity() );
    if ( slotAffected == 0 ) {
      throw new BusinessException( 500, "恢复时段名额失败，请联系管理员" );
    }

    // 注意：这里没有把用过的优惠券退回给用户（不会把 user_coupon 状态改回 UNUSED）。
    // 这是当前的默认行为，属于业务政策层面的取舍，不是遗漏：
    // "取消订单是否应该退还优惠券"没有唯一正确答案，取决于产品是否允许用户
    // 通过"下单核销优惠券—再取消订单"这个路径反复复用同一张券。如果后续产品
    // 明确要支持"取消即退券"，只需要在这里追加一次 UserCouponService 的退还调用，
    // 并保证它同样加入当前事务即可。
  }

  @Override
  public Page < BookingOrderVO > getMyOrders( Long userId, int pageNum, int pageSize ) {
    long safePageNum = Math.max( 1, pageNum );
    long safePageSize = Math.min( MAX_PAGE_SIZE, Math.max( 1, pageSize ) );
    Page < BookingOrderVO > page = new Page <>( safePageNum, safePageSize );
    return orderMapper.selectMyOrders( page, userId );
  }

  @Override
  public Page < BookingOrderAdminVO > getAllOrders( Integer status, String targetType, int pageNum, int PageSize ) {
    long safePageNum = Math.max( 1, pageNum );
    long safePageSize = Math.min( MAX_PAGE_SIZE, Math.max( 1, PageSize ) );
    Page < BookingOrderAdminVO > page = new Page <>( safePageNum, safePageSize );
    return orderMapper.selectAllOrders( page, status, targetType );
  }
}
