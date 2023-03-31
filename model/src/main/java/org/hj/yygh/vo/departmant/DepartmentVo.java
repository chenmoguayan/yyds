package org.hj.yygh.vo.departmant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author hj
 * @data 2023/3/29 17:07
 */
@Data
@ApiModel(description = "Department")
public class DepartmentVo {
    @ApiModelProperty(value = "科室编号")
    private String depcode;
    @ApiModelProperty(value = "科室名称")
    private String depname;
    @ApiModelProperty(value = "下级节点")
    private List<DepartmentVo> children;

}
