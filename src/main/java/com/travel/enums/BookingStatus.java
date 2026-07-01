package com.travel.enums;

/**
 * 预约订单状态。
 * 所有状态流转都从这里判断，Service 层不要散落硬编码的状态切换规则。
 */
public enum BookingStatus {
  PENDING( 0, "待确认" ),
  CONFIRMED( 1, "已确认" ),
  CANCELLED( 2, "已取消" ),
  COMPLETED( 3, "已完成" );

  private final int code;
  private final String desc;

  BookingStatus( int code, String desc ) {
    this.code = code;
    this.desc = desc;
  }

  public int getCode() {
    return code;
  }

  public String getDesc() {
    return desc;
  }

  public static BookingStatus fromCode( int code ) {
    for ( BookingStatus status : BookingStatus.values() ) {
      if ( status.getCode() == code ) {
        return status;
      }
    }
    throw new IllegalArgumentException( "非法的订单状态code： " + code );
  }

  /**
   * 判断当前状态能否切到目标状态。
   * 终态 CANCELLED/COMPLETED 不能再修改，避免订单被反复确认、取消或恢复。
   */
  public boolean canTransitionTo( BookingStatus target ) {
    return switch ( this ) {
      case PENDING -> target == CONFIRMED || target == CANCELLED;
      case CONFIRMED -> target == COMPLETED || target == CANCELLED;
      case CANCELLED, COMPLETED -> false; // 终态，不能再跳转
    };
  }

}
