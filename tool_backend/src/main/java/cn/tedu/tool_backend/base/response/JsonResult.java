package cn.tedu.tool_backend.base.response;
/*
多个构造函数的目的是为了方便在不同的情况下创建 JsonResult 对象。
通过不同的构造函数，可以用不同的方式初始化对象，提供灵活性和简便性。
 */

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JsonResult {// JsonResult 类，用于封装 API 响应结果
    private Integer code;
    private String msg;
    private Object data;

    public JsonResult(Integer code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public JsonResult(StatusCode statusCode){
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
    }

    public JsonResult (StatusCode statusCode, Object data){
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
        this.data = data;
    }

    public JsonResult(Object data){
        this.code = StatusCode.SUCCESS.getCode();
        this.msg = StatusCode.SUCCESS.getMsg();
        this.data = data;
    }

    public static JsonResult ok(Object data){  //使用静态方法的目的是为了提供更简便的创建 JsonResult 对象的方式。
                                                // 静态方法不需要创建类的实例即可调用，更加直观和方便。
        return new JsonResult(data);
    }

    public static JsonResult ok(){
        return ok(null);
    }
}
