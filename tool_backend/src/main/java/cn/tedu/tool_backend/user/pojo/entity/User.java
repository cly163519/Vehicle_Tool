package cn.tedu.tool_backend.user.pojo.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class User {
    @ApiModelProperty(value="user id")
    private Long id;
    @ApiModelProperty(value="user name")
    private String username;
    @ApiModelProperty(value="password")
    private String password;
    @ApiModelProperty(value="email")
    private String email;
    @ApiModelProperty(value="phone number")
    private String phone;
    @ApiModelProperty(value="age")
    private Integer age;
    @ApiModelProperty(value="sex")
    private String gender;
    @ApiModelProperty(value="creation time")
    private Date createTime;
    @ApiModelProperty(value="update time")
    private Date updateTime;
    @ApiModelProperty(value="user status")
    private String status;
    @ApiModelProperty(value="user rank")
    private String level;
    @ApiModelProperty(value="superior id")
    private Long parentId;


}
