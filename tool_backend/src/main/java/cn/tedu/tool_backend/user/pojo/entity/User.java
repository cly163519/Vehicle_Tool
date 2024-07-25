package cn.tedu.tool_backend.user.pojo.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/*
POJO 是 Plain Old Java Object 的缩写，
指的是一个普通的 Java 对象。它不依赖于任何特定的框架、库或容器。
 */
@Data
public class User {
    @ApiModelProperty(value="用户id")
    private Long id;
    @ApiModelProperty(value="用户名")
    private String username;
    @ApiModelProperty(value="密码")
    private String password;
    @ApiModelProperty(value="邮件")
    private String email;
    @ApiModelProperty(value="手机号")
    private String phone;
    @ApiModelProperty(value="年龄")
    private Integer age;
    @ApiModelProperty(value="性别")
    private String gender;
    @ApiModelProperty(value="创建时间")
    private Date createTime;
    @ApiModelProperty(value="更新时间")
    private Date updateTime;
    @ApiModelProperty(value="用户状态")
    private String status;
    @ApiModelProperty(value="用户级别")
    private String level;
    @ApiModelProperty(value="上级id")
    private Long parentId;


}
