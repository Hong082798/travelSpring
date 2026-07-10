package com.travel.enums;

/**
 * 优惠券状态。
 *
 */
public enum UserCouponStatus {

  UNUSED( 0, "未使用" ),
  USED( 1, "已使用" ),
  EXPIRED( 2, "已过期" );

  private final int code;
  private final String desc;

  UserCouponStatus( int code, String desc ) {
    this.code = code;
    this.desc = desc;
  }

  public int getCode() {
    return code;
  }

  public String getDesc() {
    return desc;
  }

  public static UserCouponStatus fromCode( int code ) {
    for ( UserCouponStatus status : UserCouponStatus.values() ) {
      if ( status.getCode() == code ) {
        return status;
      }
    }
    throw new IllegalArgumentException( "非法的优惠券状态code： " + code );
  }

  /**
   * 判断当前状态能否切到目标状态。
   * 规则和 BookingStatus.canTransitionTo() 完全同源：
   * UNUSED 是唯一的非终态，能流向 USED（核销）或 EXPIRED（过期兜底任务判定）；
   * USED/EXPIRED 都是终态，不允许再变化——已核销的券不能"退回未使用"，
   * 已过期的券也不能"复活"，否则会破坏优惠券只能使用一次的核心约束。
   */
  public boolean canTransitionTo( UserCouponStatus target ) {
    return switch ( this ) {
      case UNUSED -> target == USED || target == EXPIRED;
      case USED, EXPIRED -> false; // 终态，不能再跳转
    };
  }


}
