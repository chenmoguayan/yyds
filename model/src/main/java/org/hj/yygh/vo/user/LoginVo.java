package org.hj.yygh.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hj
 * @data 2023/3/31 10:18
 */
@Data
@ApiModel(description = "登陆对象")
public class LoginVo {
    @ApiModelProperty(value = "openid")
    private String openid;
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "密码")
    private String code;
    @ApiModelProperty(value = "IP")
    private String ip;

}
