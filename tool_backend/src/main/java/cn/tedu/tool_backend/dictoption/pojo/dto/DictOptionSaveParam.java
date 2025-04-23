package cn.tedu.tool_backend.dictoption.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DictOptionSaveParam {
    @ApiModelProperty(value = "字典id")
    private Long id;
    @ApiModelProperty(value = "字典项id")
    private Long dictId;
    @ApiModelProperty(value = "字典项名称")
    private String label;
    @ApiModelProperty(value = "字典项值")
    private String value;
    @ApiModelProperty(value = "字典项排序")
    private String sort;
    @ApiModelProperty(value = "备注信息")
    private String remark;
}
