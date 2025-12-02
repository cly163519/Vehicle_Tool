package cn.tedu.tool_backend.application.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class ApplicationSaveParam {
    @ApiModelProperty(value="Application form id")
    private Long id;
    @ApiModelProperty(value="Applicant's id")
    private Long userId;
    @ApiModelProperty(value="Applicant's name")
    private String username;
    @ApiModelProperty(value="Vehicle start time")
    @DateTimeFormat(pattern = "yyyy-MM-ss HH:mm:ss")
    private Date startTime;
    @ApiModelProperty(value="Vehicle end time")
    @DateTimeFormat(pattern = "yyyy-MM-ss HH:mm:ss")
    private Date endTime;
    @ApiModelProperty(value="Driver's license image")
    private String imgUrl;
    @ApiModelProperty(value="Departure")
    private String departureAddr;
    @ApiModelProperty(value="destination")
    private String destinationAddr;
    @ApiModelProperty(value="Reason for application")
    private String reason;
    @ApiModelProperty(value="Remark")
    private String remark;
    @ApiModelProperty(value="Application status")
    private String status;
    @ApiModelProperty(value="Vehicle allocation id")
    private Long vehicleId;
    @ApiModelProperty(value="Approver id set")
    private List<Long> auditUserIdList; //[106,103]
}
