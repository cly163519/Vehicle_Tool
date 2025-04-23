package cn.tedu.tool_backend.base.exception;

import cn.tedu.tool_backend.base.response.StatusCode;
import lombok.Getter;

public class ServiceException extends RuntimeException{
    @Getter
    private StatusCode statusCode;
    public ServiceException(StatusCode statusCode){
        this.statusCode = statusCode;
    }
}
