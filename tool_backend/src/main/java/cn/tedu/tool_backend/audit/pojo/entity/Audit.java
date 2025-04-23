package cn.tedu.tool_backend.audit.pojo.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class Audit {
    @ApiModelProperty(value="审批单id")
    private Long id;
    @ApiModelProperty(value="申请单id")
    private Long applicationId;
    @ApiModelProperty(value="审批人id")
    private Long auditUserId;
    @ApiModelProperty(value="审批状态")
    private String auditStatus;
    @ApiModelProperty(value="审批顺序")
    private Integer auditSort;
    @ApiModelProperty(value="创建时间")
    private Date createTime;
    @ApiModelProperty(value="更新时间")
    private Date updateTime;
}
