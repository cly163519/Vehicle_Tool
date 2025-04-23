package cn.tedu.tool_backend.dictoption.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class DictOptionVO {
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
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date createTime;
}
