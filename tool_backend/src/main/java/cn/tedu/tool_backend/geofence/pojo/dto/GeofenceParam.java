package cn.tedu.tool_backend.geofence.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GeofenceParam {
    @ApiModelProperty(value="Fence number")
    private Long id;
    @ApiModelProperty(value="Fence name")
    private String name;
    @ApiModelProperty(value="Fence coordinates")
    private String position;
    @ApiModelProperty(value="Fence status")
    private String status;
}
