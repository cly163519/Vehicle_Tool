package cn.tedu.tool_backend.vehicle.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class VehicleQuery {
    @ApiModelProperty(value="Vehicle id")
    private Long id;
    @ApiModelProperty(value="Vehicle Brand")
    private String brand;
    @ApiModelProperty(value="Vehicle number")
    private String license;
    @ApiModelProperty(value="Electronic fence binding status")
    private String geofenceBindStatus;
    @ApiModelProperty(value="Electronic fence id")
    private Long geofenceId;
}
