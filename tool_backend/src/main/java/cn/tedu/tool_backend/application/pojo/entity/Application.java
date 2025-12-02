package cn.tedu.tool_backend.application.pojo.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Application {
    @ApiModelProperty(value="Application form id")
    private Long id;
    @ApiModelProperty(value="Applicant's id")
    private Long userId;
    @ApiModelProperty(value="Applicant's name")
    private String username;
    @ApiModelProperty(value="Vehicle start time")
    private Date startTime;
    @ApiModelProperty(value="Vehicle end time")
    private Date endTime;
    @ApiModelProperty(value="Driver's license image")
    private String imgUrl;
    @ApiModelProperty(value="Departure")
    private String departureAddr;
    @ApiModelProperty(value="Destination")
    private String destinationAddr;
    @ApiModelProperty(value="Application reason")
    private String reason;
    @ApiModelProperty(value="Remarks")
    private String remark;
    @ApiModelProperty(value="Application form status")
    private String status;
    @ApiModelProperty(value="Assign vehicle id")
    private Long vehicleId;
    @ApiModelProperty(value="Reasons for rejection")
    private String rejectReason;
    @ApiModelProperty(value="Creation time")
    private Date createTime;
    @ApiModelProperty(value="Update time")
    private Date updateTime;
    @ApiModelProperty(value="Approver id set")
    private List<Long> auditUserIdList; //[106,103]
    @ApiModelProperty(value="Approver's name, string separator")
    private String auditUsernameList;

}
