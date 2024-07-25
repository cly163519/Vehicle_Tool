package cn.tedu.tool_backend.base.exception;

import cn.tedu.tool_backend.base.response.JsonResult;
import cn.tedu.tool_backend.base.response.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public JsonResult doHandleIllegalArgumentException(IllegalArgumentException ex){
        String message = ex.getMessage();
        log.error("IllegalArgumentException:"+message);
        return new JsonResult(StatusCode.OPERATION_FAILED,message);
    }

    @ExceptionHandler
    public JsonResult doHandleRuntimeException(RuntimeException ex){
        String message = ex.getMessage();
        log.error("RuntimeException:" + message);
        return new JsonResult(StatusCode.OPERATION_FAILED,message);
    }

    @ExceptionHandler
    public JsonResult doHandelMethodArgumentNotWalidException(MethodArgumentNotValidException ex){
        String message = ex.getFieldError().getDefaultMessage();
        log.error("MethodArgumentNotValidException:" + message);
        return new JsonResult(StatusCode.VALIDATE_ERROR, message);
    }
    @ExceptionHandler
    public JsonResult doHandleConstraintViolationException(ConstraintViolationException ex){
        String message = ex.getMessage().split(":")[1].trim();
        log.error("ConstraintViolationException:" + message);
        return new JsonResult(StatusCode.VALIDATE_ERROR,message);
    }
}


