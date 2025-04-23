package cn.tedu.tool_backend.dictoption.pojo.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class DictOption {
    @ApiModelProperty(value="字典id")
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
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
