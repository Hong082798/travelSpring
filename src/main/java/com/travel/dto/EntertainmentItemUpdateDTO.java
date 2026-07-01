package com.travel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode ( callSuper = true )   // 继承父类字段，Lombok 需显式声明把父类字段纳入 equals/hashCode
@Schema ( description = "修改玩乐项目DTO，比新增多一个必填的 id" )
public class EntertainmentItemUpdateDTO extends EntertainmentItemAddDTO {

  @NotNull ( message = "修改时ID不能为空" )
  @Schema ( description = "要修改的玩乐项目ID" )
  private Long id;

}
