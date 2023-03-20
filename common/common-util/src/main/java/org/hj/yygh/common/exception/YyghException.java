package org.hj.yygh.common.exception;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hj.yygh.common.result.ResultCodeEnum;

/**
 * @author hj
 * @data 2023/3/20 17:36
 */
@Data
@ApiModel(value = "自定义全局异常类")
public class YyghException extends RuntimeException {
    @ApiModelProperty(value = "异常状态码")
    private Integer code;

    /**
     * @param code 错误码
     * @param message 错误信息
     *
     */
    public YyghException(String message ,Integer code) {
        super(message);
        this.code = code;
    }

    /**
     * 接收枚举类对象
     */
    public YyghException(ResultCodeEnum resultCodeEnum){
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    @Override
    public String toString() {
        return "YyghException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }
}
