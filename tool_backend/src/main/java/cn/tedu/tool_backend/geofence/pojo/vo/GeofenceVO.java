package cn.tedu.tool_backend.geofence.pojo.vo;

import cn.tedu.tool_backend.vehicle.pojo.vo.VehicleVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class GeofenceVO {
    @ApiModelProperty(value="Fence id")
    private Long id;
    @ApiModelProperty(value="Fence name")
    private String name;
    @ApiModelProperty(value="Fence status")
    private String status;
    @ApiModelProperty(value="Fence coordinator")
    private String position;
    @ApiModelProperty(value="Fence creation time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @ApiModelProperty(value="The fence has been bound to the vehicle list")
    private List<VehicleVO> vehicleList;
    @ApiModelProperty(value="The total number of vehicles bound by the fence")
    private Integer totalNum;
    @ApiModelProperty(value="Number of vehicles available at the fence")
    private Integer availableNum;

}
