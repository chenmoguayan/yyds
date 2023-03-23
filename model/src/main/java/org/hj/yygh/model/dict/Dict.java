package org.hj.yygh.model.dict;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hj
 * @data 2023/3/23 14:29
 */
@Data
@ApiModel(description = "数据字典")
@TableName("dict")
public class Dict {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("update_time")
    private Date updateTime;

    @ApiModelProperty(value = "逻辑删除(1:已删除，0：未删除)")
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;
    @ApiModelProperty(value = "其他参数")
    @TableField(exist = false)
    private Map<String,Object> param = new HashMap<>();
    @ApiModelProperty(value = "上级id")
    @TableField("parent_id")
    private Long parentId;
    @ApiModelProperty(value = "名称")
    @TableField("name")
    private String name;
    @ApiModelProperty(value = "值")
    @TableField("value")
    private String value;
    @ApiModelProperty(value = "编码")
    @TableField("dict_code")
    private String dictCode;
// 表述是否存在层级关系
    @ApiModelProperty(value = "是否包含子节点")
// 表中不存在，使用exist=false标注，不然报错
    @TableField(exist = false)
    private boolean hasChildren;
}
