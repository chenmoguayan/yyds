package org.hj.yygh.vo.hosp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hj
 * @data 2023/3/28 10:04
 */
@Data
@ApiModel(description = "Department")
public class DepartmentQueryVo {
    @ApiModelProperty(value = "医院编号")
    private String hoscode;
    @ApiModelProperty(value = "科室编号")
    private String depcode;
    @ApiModelProperty(value = "科室名称")
    private String depname;
    @ApiModelProperty(value = "大科室编号")
    private String bigcode;
    @ApiModelProperty(value = "大科室名称")
    private String bigname;

}
