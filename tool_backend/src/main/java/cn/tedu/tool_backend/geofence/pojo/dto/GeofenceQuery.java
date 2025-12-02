package cn.tedu.tool_backend.geofence.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GeofenceQuery {
    @ApiModelProperty(value = "Fence id")
    private Long id;
    @ApiModelProperty(value = "Fence name")
    private String name;
    @ApiModelProperty(value = "Fence status")
    private Long status;

}
