package cn.tedu.tool_backend.application.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ApplicationQuery {
    @ApiModelProperty(value="Departure point")
    private String departureAddr;
    @ApiModelProperty(value="Destination")
    private String destinationAddr;
    @ApiModelProperty(value="Application status")
    private String status;
    @ApiModelProperty(value="Applicant's Name")
    private String username;
}
