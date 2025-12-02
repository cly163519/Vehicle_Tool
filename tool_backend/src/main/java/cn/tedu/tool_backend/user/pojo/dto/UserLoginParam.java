package cn.tedu.tool_backend.user.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserLoginParam {
    @ApiModelProperty(value="user")
    private String username;
    @ApiModelProperty(value="password")
    private String password;

}
