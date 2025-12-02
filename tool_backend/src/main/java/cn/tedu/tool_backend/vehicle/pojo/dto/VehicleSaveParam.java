package cn.tedu.tool_backend.vehicle.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;


import java.util.Date;
@Data
public class VehicleSaveParam {
    @ApiModelProperty(value="Vehicle id")
    private Long id;
    @ApiModelProperty(value="Vehicle brand")
    private String brand;
    @ApiModelProperty(value="Vehicle license")
    private String license;
    @ApiModelProperty(value="Vehicle model")
    private String model;
    @ApiModelProperty(value="Vehicle code")
    private String code;
    @ApiModelProperty(value="Vehicle displacement")
    private String displacement;
    @ApiModelProperty(value="Vehicle status")
    private String status;
    @ApiModelProperty(value="Vehicle type")
    private String type;
    @ApiModelProperty(value="Vehicle color")
    private String color;
    @ApiModelProperty(value="Vehicle mileage")
    private String kilometers;
    @ApiModelProperty(value="Vehicle registration time")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date regTime;
    @ApiModelProperty(value="Vehicle purchase time")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date buyTime;
    @ApiModelProperty(value="Vehicle purchase price")
    private String price;
    @ApiModelProperty(value="Vehicle battery type")
    private String batteryType;
    //@ApiModelProperty(value="Vehicle creation time")
   // private String createTime;
    @ApiModelProperty(value="Vehicle update time")
    private String updateTime;
    @ApiModelProperty(value="Electric fence bending status")
    private String geofenceBindStatus;
    @ApiModelProperty(value="Electric fence id")
    private Long geofenceId;
}
