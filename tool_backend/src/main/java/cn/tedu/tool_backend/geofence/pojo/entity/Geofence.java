package cn.tedu.tool_backend.geofence.pojo.entity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
@Data
public class Geofence {
    @ApiModelProperty(value = "Fence id")
    private Long id;
    @ApiModelProperty(value = "Fence name")
    private String name;
    @ApiModelProperty(value = "Fence status")
    private String status;
    @ApiModelProperty(value = "Fence coordinate")
    private String position;
    @ApiModelProperty(value = "Create time")
    private Date createTime;
    @ApiModelProperty(value = "Update time")
    private Date updateTime;
}
