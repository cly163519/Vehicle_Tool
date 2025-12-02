package cn.tedu.tool_backend.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuditStatusEnum {
    MY_PENDING("10","Needs to be reviewed by me."),
    PENDING("20","Needs to be reviewed by others."),
    AUDITED("30","Reviewed"),
    REJECT("40","Rejection");

    private String code;
    private String msg;
}
