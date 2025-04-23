package cn.tedu.tool_backend.application.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class ApplicationSaveParam {
    @ApiModelProperty(value="申请表id")
    private Long id;
    @ApiModelProperty(value="申请人id")
    private Long userId;
    @ApiModelProperty(value="申请人姓名")
    private String username;
    @ApiModelProperty(value="用车开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-ss HH:mm:ss")
    private Date startTime;
    @ApiModelProperty(value="用车结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-ss HH:mm:ss")
    private Date endTime;
    @ApiModelProperty(value="驾照图片")
    private String imgUrl;
    @ApiModelProperty(value="出发地")
    private String departureAddr;
    @ApiModelProperty(value="目的地")
    private String destinationAddr;
    @ApiModelProperty(value="申请原因")
    private String reason;
    @ApiModelProperty(value="备注")
    private String remark;
    @ApiModelProperty(value="申请单状态")
    private String status;
    @ApiModelProperty(value="分配车辆id")
    private Long vehicleId;
    @ApiModelProperty(value="审批人id集合")
    private List<Long> auditUserIdList; //[106,103]
}
