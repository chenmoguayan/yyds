package org.hj.yygh.vo.hosp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author hj
 * @data 2023/3/28 10:43
 */
@ApiModel(description = "Schedule")
@Data
public class ScheduleQueryVo {
    @ApiModelProperty(value = "医院编号")
    private String hoscode;

    @ApiModelProperty(value = "科室编号")
    private String depcode;

    @ApiModelProperty(value = "医生编号")
    private String doccode;

    @ApiModelProperty(value = "安排日期")
    private Date workDate;

    @ApiModelProperty(value = "安排时间（0：上午 1：下午）")
    private Integer workTime;
}
