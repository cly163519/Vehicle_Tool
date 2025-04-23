package cn.tedu.tool_backend.audit.service;

import cn.tedu.tool_backend.application.pojo.entity.Application;
import cn.tedu.tool_backend.audit.pojo.dto.AuditQuery;
import cn.tedu.tool_backend.audit.pojo.dto.AuditSaveParam;
import cn.tedu.tool_backend.audit.pojo.vo.AuditVO;

import java.util.List;

public interface AuditService {
    void insertAudit(Application application);

    List<AuditVO> selectAudit(AuditQuery auditQuery);

    void updateAudit(AuditSaveParam auditSaveParam);
}
