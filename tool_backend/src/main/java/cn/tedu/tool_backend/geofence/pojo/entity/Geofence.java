package cn.tedu.tool_backend.geofence.pojo.entity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
@Data
public class Geofence {
    @ApiModelProperty(value = "围栏id")
    private Long id;
    @ApiModelProperty(value = "围栏名称")
    private String name;
    @ApiModelProperty(value = "围栏状态")
    private String status;
    @ApiModelProperty(value = "围栏坐标点")
    private String position;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
