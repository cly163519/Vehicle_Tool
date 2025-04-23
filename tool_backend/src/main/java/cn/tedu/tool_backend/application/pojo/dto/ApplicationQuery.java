package cn.tedu.tool_backend.application.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ApplicationQuery {
    @ApiModelProperty(value="出发地")
    private String departureAddr;
    @ApiModelProperty(value="目的地")
    private String destinationAddr;
    @ApiModelProperty(value="申请单状态")
    private String status;
    @ApiModelProperty(value="申请人姓名")
    private String username;
}
