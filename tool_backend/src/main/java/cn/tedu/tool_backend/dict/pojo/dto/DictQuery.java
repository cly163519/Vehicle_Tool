package cn.tedu.tool_backend.dict.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DictQuery {
    @ApiModelProperty(value="字典id")
    private Long id;
    @ApiModelProperty(value="字典名称")
    private String name;
    @ApiModelProperty(value="字典编码")
    private String code;
}
