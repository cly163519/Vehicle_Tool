package cn.tedu.tool_backend.base.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum StatusCode {
    SUCCESS(2000,"OK"),
    LOGIN_SUCCESS(2001,"Login successful"),
    OPERATION_SUCCESS(2002,"Operation successful"),

    NOT_LOGIN(4000,"Not logged in"),
    PASSWORD_ERROR(4002,"Username or password incorrect."),
    USERNAME_ERROR(4003,"Username or password incorrect."),

    OPERATION_FAILED(5001,"Operation failed"),
    VALIDATE_ERROR(5002,"Incorrect request parameters"),
    USERNAME_ALREADY_EXISTS(5003,"Username is already taken."),
    DATA_UNEXISTS(5004,"The requested data does not exist.");

    private Integer code;  //Integer code = StatusCode.SUCCESS.getCode();
    private String msg;  //String message = StatusCode.SUCCESS.getMsg();


    }
