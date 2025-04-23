package cn.tedu.tool_backend.geofence.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GeofenceQuery {
    @ApiModelProperty(value = "围栏id")
    private Long id;
    @ApiModelProperty(value = "围栏名称")
    private String name;
    @ApiModelProperty(value = "围栏状态")
    private Long status;

}
