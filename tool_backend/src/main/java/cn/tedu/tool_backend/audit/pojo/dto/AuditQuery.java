package cn.tedu.tool_backend.audit.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AuditQuery {
    @ApiModelProperty(value = "审批单id")
    private Long id;
    @ApiModelProperty(value = "申请单id")
    private Long applicationId;
    @ApiModelProperty(value = "审批人id")
    private Long auditUserId;
    @ApiModelProperty(value = "审批状态")
    private String auditStatus;
    @ApiModelProperty(value = "审批排序")
    private Integer auditSort;
    //因为搜索卡片里有这个字段,可以根据用车人姓名查审批数据
    @ApiModelProperty(value = "用车人姓名")
    private String username;
}