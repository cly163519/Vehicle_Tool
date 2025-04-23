package cn.tedu.tool_backend.geofence.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GeofenceParam {
    @ApiModelProperty(value="围栏编号")
    private Long id;
    @ApiModelProperty(value="围栏名称")
    private String name;
    @ApiModelProperty(value="围栏坐标点")
    private String position;
    @ApiModelProperty(value="围栏状态")
    private String status;
}
