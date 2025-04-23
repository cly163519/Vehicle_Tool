package cn.tedu.tool_backend.dict.pojo.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class Dict {
    @ApiModelProperty(value="字典id")
    private Long id;
    @ApiModelProperty(value="字典名称")
    private String name;
    @ApiModelProperty(value="字典编码")
    private String code;
    @ApiModelProperty(value="备注信息")
    private String remark;
    @ApiModelProperty(value="创建时间")
    private Date createTime;
    @ApiModelProperty(value="更新时间")
    private Date updateTime;
}
