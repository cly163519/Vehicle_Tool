package cn.tedu.tool_backend.dictoption.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class DictOptionVO {
    @ApiModelProperty(value = "Dict id")
    private Long id;
    @ApiModelProperty(value = "Dict items id")
    private Long dictId;
    @ApiModelProperty(value = "Dict items name")
    private String label;
    @ApiModelProperty(value = "Dict items value")
    private String value;
    @ApiModelProperty(value = "Dict items value")
    private String sort;
    @ApiModelProperty(value = "Remarks")
    private String remark;
    @ApiModelProperty(value = "Create time")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date createTime;
}
