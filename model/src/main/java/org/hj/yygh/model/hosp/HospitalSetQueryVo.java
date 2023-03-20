package org.hj.yygh.model.hosp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hj
 * @data 2023/3/20 15:23
 */
@Data
public class HospitalSetQueryVo {
    @ApiModelProperty(value = "医院名称")
    private String hosname;

    @ApiModelProperty(value = "医院编号")
    private String hoscode;
}
