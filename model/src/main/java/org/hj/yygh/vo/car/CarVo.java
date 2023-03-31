package org.hj.yygh.vo.car;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hj
 * @data 2023/3/25 11:05
 */
@Data
@ApiModel(value = "查询条件")
public class CarVo {

    @ApiModelProperty(value = "汽车名称")
    private String name;

    @ApiModelProperty(value = "品牌")
    private String brand;

    @ApiModelProperty(value = "生产日期")
    private String date;

    @ApiModelProperty(value = "引擎")
    private String engine;

    @ApiModelProperty(value = "马力")
    private String horsepower;

    @ApiModelProperty(value = "售价")
    private Integer price;

}
