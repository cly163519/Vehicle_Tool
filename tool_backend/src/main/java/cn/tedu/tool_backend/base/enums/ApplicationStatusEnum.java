package cn.tedu.tool_backend.base.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApplicationStatusEnum {
    PENDING("10","Initiated"),
    CANCEL("20","Cancel"),
    AUDIT("30","Under review"),
    REJECT("40","Rejection"),
    AUDITED("50","Reviewed"),
    ALLOCATION("60","Vehicles assigned"),
    END("70","Closed");

    private String code;
    private String msg;
}
