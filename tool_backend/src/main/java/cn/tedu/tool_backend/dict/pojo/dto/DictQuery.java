package cn.tedu.tool_backend.dict.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DictQuery {
    @ApiModelProperty(value="Dict id")
    private Long id;
    @ApiModelProperty(value="Dict name")
    private String name;
    @ApiModelProperty(value="Dict code")
    private String code;
}
