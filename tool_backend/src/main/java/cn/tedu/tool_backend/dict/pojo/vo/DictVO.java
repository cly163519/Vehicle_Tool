package cn.tedu.tool_backend.dict.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
@Data
public class DictVO {
    @ApiModelProperty(value="字典id")
    private Long id;
    @ApiModelProperty(value="字典名称")
    private String name;
    @ApiModelProperty(value="字典编码")
    private String code;
    @ApiModelProperty(value="备注信息")
    private String remark;
    @ApiModelProperty(value="创建时间")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date createTime;
}
