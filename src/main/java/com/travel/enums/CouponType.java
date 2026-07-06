package com.travel.enums;

/**
 * 优惠券类型。
 * coupon 表的 coupon_type 字段存的是这里枚举名对应的字符串（如 "FULL_REDUCTION"），
 * 不是数字 code —— 因为该字段建表时用的是 varchar，可读性优先于 BookingStatus 那种 tinyint 场景。
 */
public enum CouponType {
  FULL_REDUCTION( "满减券" ),
  CASH( "无门槛现金券" );

  private final String desc;

  CouponType( String desc ) {
    this.desc = desc;
  }

  public String getDesc() {
    return desc;
  }

  /**
   * 把数据库里存的字符串转回枚举。
   * 用 name() 比对而不是新造一个 code 字段，是因为 varchar 字段本身存的就是可读值，
   * 没必要再包一层数字映射，那样反而要多维护一张"数字-字符串"对照表。
   */
  public static CouponType fromValue( String value ) {
    for ( CouponType type : CouponType.values() ) {
      if ( type.name().equals( value ) ) {
        return type;
      }
    }
    throw new IllegalArgumentException( "非法的优惠券类型： " + value );
  }

}
