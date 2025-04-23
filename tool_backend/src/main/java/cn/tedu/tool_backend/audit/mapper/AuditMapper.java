package cn.tedu.tool_backend.audit.mapper;

import cn.tedu.tool_backend.audit.pojo.dto.AuditQuery;
import cn.tedu.tool_backend.audit.pojo.entity.Audit;
import cn.tedu.tool_backend.audit.pojo.vo.AuditVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditMapper {
    void insert(Audit audit);

    List<AuditVO> selectAuditByApplicationId(Long id);

    void deleteByApplicationId(Long id);//注意这里是applicationId

    List<AuditVO> selectAudit(AuditQuery auditQuery);

    void update(Audit audit);

    Integer selectRestAuditCount(AuditQuery auditQuery);

    void deleteById(Long id);
}
