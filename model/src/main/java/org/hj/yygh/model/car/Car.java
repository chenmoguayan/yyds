package org.hj.yygh.model.car;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hj
 * @data 2023/3/25 8:45
 */
@Data
@ApiModel(description = "汽车列表")
@TableName("car")
public class Car {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "Id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "汽车名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "品牌")
    @TableField("brand")
    private String brand;

    @ApiModelProperty(value = "生产日期")
    @TableField("date")
    private String date;

    @ApiModelProperty(value = "引擎")
    @TableField("engine")
    private String engine;

    @ApiModelProperty(value = "马力")
    @TableField("horsepower")
    private String horsepower;

    @ApiModelProperty(value = "售价")
    @TableField("price")
    private Integer price;

    @ApiModelProperty(value = "逻辑删除")
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;


}
