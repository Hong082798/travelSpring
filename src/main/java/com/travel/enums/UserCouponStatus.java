package com.travel.enums;

/**
 * 优惠券状态。
 *
 */
public enum UserCouponStatus {

  UNUSED( "'未使用'" ),
  USED( "已使用" ),
  EXPIRED( "已过期" );

  private final String desc;

  UserCouponStatus( String desc ) {
    this.desc = desc;
  }

  public String getDesc() {
    return desc;
  }

  public static UserCouponStatus fromValue( String value ) {
    for ( UserCouponStatus status : UserCouponStatus.values() ) {
      if ( status.name().equals( value ) ) {
        return status;
      }
    }
    throw new IllegalArgumentException( "非法的优惠券状态： " + value );
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
