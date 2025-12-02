package cn.tedu.tool_backend.dict.pojo.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class Dict {
    @ApiModelProperty(value="Dict id")
    private Long id;
    @ApiModelProperty(value="Dict name")
    private String name;
    @ApiModelProperty(value="Dict code")
    private String code;
    @ApiModelProperty(value="Remarks")
    private String remark;
    @ApiModelProperty(value="Creation time")
    private Date createTime;
    @ApiModelProperty(value="Update time")
    private Date updateTime;
}
