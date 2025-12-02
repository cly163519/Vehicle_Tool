package cn.tedu.tool_backend.dictoption.pojo.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class DictOption {
    @ApiModelProperty(value="Dict id")
    private Long id;
    @ApiModelProperty(value = "Dict items id")
    private Long dictId;
    @ApiModelProperty(value = "Dict items name")
    private String label;
    @ApiModelProperty(value = "Dict items value")
    private String value;
    @ApiModelProperty(value = "Dict items order")
    private String sort;
    @ApiModelProperty(value = "Remarks")
    private String remark;
    @ApiModelProperty(value = "Creation time")
    private Date createTime;
    @ApiModelProperty(value = "Update time")
    private Date updateTime;
}
