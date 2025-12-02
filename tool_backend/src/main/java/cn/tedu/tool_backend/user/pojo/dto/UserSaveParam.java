package cn.tedu.tool_backend.user.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserSaveParam {
    @ApiModelProperty(value="user id")
    private Long id;
    @ApiModelProperty(value="user name")
    private String username;
    @ApiModelProperty(value="email")
    private String email;
    @ApiModelProperty(value="password")
    private String password;
    @ApiModelProperty(value="phone number")
    private String phone;
    @ApiModelProperty(value="age")
    private Integer age;
    @ApiModelProperty(value="sex")
    private String gender;
    @ApiModelProperty(value="user status")
    private String status;
    @ApiModelProperty(value="user rank")
    private String level;
    @ApiModelProperty(value="superior id")
    private Long parentId;
}
