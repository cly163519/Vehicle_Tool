package cn.tedu.tool_backend.dictoption.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DictOptionQuery {
    @ApiModelProperty(value = "Dict itmes id")
    private Long id;
    @ApiModelProperty(value = "Dict id")
    private Long dictId;
    @ApiModelProperty(value = "Dict items name")
    private String label;
    @ApiModelProperty(value = "Dict item value")
    private String value;
}
