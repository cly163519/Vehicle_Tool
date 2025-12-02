package cn.tedu.tool_backend.dictoption.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DictOptionSaveParam {
    @ApiModelProperty(value = "Dict id")
    private Long id;
    @ApiModelProperty(value = "Dict items id")
    private Long dictId;
    @ApiModelProperty(value = "Dict items name")
    private String label;
    @ApiModelProperty(value = "Dict items value")
    private String value;
    @ApiModelProperty(value = "Dict items order")
    private String sort;
    @ApiModelProperty(value = "Remark")
    private String remark;
}
