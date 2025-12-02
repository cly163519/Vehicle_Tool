package cn.tedu.tool_backend.vehicle.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class VehicleVO {
    @ApiModelProperty(value = "Vehicle id")
    private Long id;
    @ApiModelProperty(value = "Vehicle brand")
    private String brand;
    @ApiModelProperty(value = "Vehicle number")
    private String license;
    @ApiModelProperty(value = "Vehicle model")
    private String model;
    @ApiModelProperty(value = "Vehicle Identification Number")
    private String code;
    @ApiModelProperty(value = "Vehicle displacement")
    private String displacement;
    @ApiModelProperty(value = "Vehicle status")
    private String status;
    @ApiModelProperty(value = "Vehicle type")
    private String type;
    @ApiModelProperty(value = "Vehicle color")
    private String color;
    @ApiModelProperty(value = "Vehicle mileage")
    private String kilometers;
    @ApiModelProperty(value = "Vehicle registration time")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date regTime;
    @ApiModelProperty(value = "Vehicle purchase time")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date buyTime;
    @ApiModelProperty(value = "Vehicle purchase price")
    private String price;
    @ApiModelProperty(value = "Vehicle battery type")
    private String batteryType;
 
    @ApiModelProperty(value = "Vehicle creation time")
    private Date createTime;
    @ApiModelProperty(value = "Vehicle update time")
    private Date updateTime;
    @ApiModelProperty(value = "Electronic fence binding status")
    private String geofenceBindStatus;
    @ApiModelProperty(value = "Electric fence id")
    private Long geofenceId;
}
