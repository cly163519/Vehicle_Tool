package cn.tedu.tool_backend.user.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class UserVO {
    @ApiModelProperty(value = "user id")
    private Long id;
    @ApiModelProperty(value = "username")
    private String username;
    @ApiModelProperty(value = "password")
    private String password;
    @ApiModelProperty(value = "email")
    private String email;
    @ApiModelProperty(value = "phone number")
    private String phone;
    @ApiModelProperty(value = "age")
    private Integer age;
    @ApiModelProperty(value = "sex")
    private String gender;
    @ApiModelProperty(value = "creation time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @ApiModelProperty(value = "update time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    @ApiModelProperty(value = "user status")
    private String status;
    @ApiModelProperty(value = "user rank")
    private String level;
    @ApiModelProperty(value = "superior id")
    private Long parentId;
}
