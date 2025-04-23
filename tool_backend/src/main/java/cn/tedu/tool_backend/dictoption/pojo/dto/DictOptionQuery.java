package cn.tedu.tool_backend.dictoption.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DictOptionQuery {
    @ApiModelProperty(value = "字典项id")
    private Long id;
    @ApiModelProperty(value = "字典id")
    private Long dictId;
    @ApiModelProperty(value = "字典项名称")
    private String label;
    @ApiModelProperty(value = "字典项值")
    private String value;
}
