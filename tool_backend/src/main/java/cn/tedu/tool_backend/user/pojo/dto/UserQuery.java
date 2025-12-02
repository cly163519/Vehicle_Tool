package cn.tedu.tool_backend.user.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserQuery {
    @ApiModelProperty(value="id")
    private Long id;
    @ApiModelProperty(value="user name")
    private String username;
    @ApiModelProperty(value="user status")
    private String status;
    @ApiModelProperty(value="User Rank")
    private String level;
    @ApiModelProperty(value="Superior id")
    private Long parentId;
}
