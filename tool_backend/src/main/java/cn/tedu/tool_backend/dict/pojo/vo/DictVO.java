package cn.tedu.tool_backend.dict.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
@Data
public class DictVO {
    @ApiModelProperty(value="Dict id")
    private Long id;
    @ApiModelProperty(value="Dict name")
    private String name;
    @ApiModelProperty(value="Dict code")
    private String code;
    @ApiModelProperty(value="Remarks")
    private String remark;
    @ApiModelProperty(value="Create time")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date createTime;
}
