package cn.tedu.tool_backend.audit.service.impl;

import cn.tedu.tool_backend.application.mapper.ApplicationMapper;
import cn.tedu.tool_backend.application.pojo.entity.Application;
import cn.tedu.tool_backend.audit.mapper.AuditMapper;
import cn.tedu.tool_backend.audit.pojo.dto.AuditQuery;
import cn.tedu.tool_backend.audit.pojo.dto.AuditSaveParam;
import cn.tedu.tool_backend.audit.pojo.entity.Audit;
import cn.tedu.tool_backend.audit.pojo.vo.AuditVO;
import cn.tedu.tool_backend.audit.service.AuditService;
import cn.tedu.tool_backend.base.enums.ApplicationStatusEnum;
import cn.tedu.tool_backend.base.enums.AuditStatusEnum;
import cn.tedu.tool_backend.user.mapper.UserMapper;
import cn.tedu.tool_backend.user.pojo.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

@Slf4j
@Transactional
@Service
public class AuditServiceImpl implements AuditService {
    @Autowired
    ApplicationMapper applicationMapper;
    @Autowired
    AuditMapper auditMapper;
    @Autowired
    UserMapper userMapper;

    @Override
    public void insertAudit(Application application){
        log.debug("生成申请单对应的审批数据:application={}",application);
        List<Long> userIdList = application.getAuditUserIdList();
        for(int i=0;i<userIdList.size();i++){
            Audit audit = new Audit();
            audit.setApplicationId(application.getId());
            audit.setAuditUserId(userIdList.get(i));
            audit.setAuditSort(i);
            if(i==0){
                audit.setAuditStatus(AuditStatusEnum.MY_PENDING.getCode());
            }else{
                audit.setAuditStatus(AuditStatusEnum.PENDING.getCode());
            }
            audit.setCreateTime(new Date());
            auditMapper.insert(audit);
        }
    }

    @Override
    public List<AuditVO> selectAudit(AuditQuery auditQuery){
        log.debug("查询审批单业务数据:auditQuery={}",auditQuery);
        List<AuditVO> auditVOList = auditMapper.selectAudit(auditQuery);
        for(int i=0;i<auditVOList.size();i++){
            AuditVO auditVO = auditVOList.get(i);
            assignAuditUserList(auditVO);
        }
        return auditVOList;
    }
    private void assignAuditUserList(AuditVO auditVO){
        List<String> auditUsernameList = new ArrayList<>();
        List<Long> auditUserIdList = new ArrayList<>();
        List<AuditVO> auditVOList = auditMapper.selectAuditByApplicationId(auditVO.getApplicationId());
        for(int i=0;i<auditVOList.size();i++){
            Long userId = auditVOList.get(i).getAuditUserId();
            auditUserIdList.add(userId);
            UserVO user = userMapper.selectById(userId);
            auditUsernameList.add(user.getUsername());
        }
        StringJoiner stringJoiner = new StringJoiner(",");
        for(String username : auditUsernameList){
            stringJoiner.add(username);
        }
        auditVO.setAuditUserIdList(auditUserIdList);
        auditVO.setAuditUsernameList(stringJoiner.toString());
    }
    @Override
    public void updateAudit(AuditSaveParam auditSaveParam){
        log.debug("更新审批业务:auditSaveParam={}",auditSaveParam);
        Audit audit = new Audit();
        BeanUtils.copyProperties(auditSaveParam,audit);
        audit.setUpdateTime(new Date());
        Application application = new Application();
        application.setId(audit.getApplicationId());
        application.setUpdateTime(new Date());

        if(audit.getAuditStatus().equals(AuditStatusEnum.AUDITED.getCode())){
            auditMapper.update(audit);
            AuditQuery auditQuery = new AuditQuery();
            auditQuery.setApplicationId(audit.getApplicationId());
            Integer count = auditMapper.selectRestAuditCount(auditQuery);
            if(count>0){
                auditQuery.setAuditSort(audit.getAuditSort()+1);
                List<AuditVO> auditVOList = auditMapper.selectAudit(auditQuery);
                if(auditVOList !=null && auditVOList.size()>0){
                    AuditVO auditVO = auditVOList.get(0);
                    Audit audit2 = new Audit();
                    audit2.setId(auditVO.getId());
                    audit2.setAuditStatus(AuditStatusEnum.MY_PENDING.getCode());
                    auditMapper.update(audit2);
                }
                application.setStatus(ApplicationStatusEnum.AUDIT.getCode());
                applicationMapper.update(application);
            }else{
                application.setStatus(ApplicationStatusEnum.AUDITED.getCode());
                applicationMapper.update(application);
            }

        }else if(audit.getAuditStatus().equals(AuditStatusEnum.REJECT.getCode())){
            auditMapper.update(audit);
            AuditQuery auditQuery = new AuditQuery();
            auditQuery.setApplicationId(audit.getApplicationId());
            List<AuditVO> auditVOList = auditMapper.selectAudit(auditQuery);
            if(auditVOList != null && auditVOList.size()>0){
                for(int i=0;i<auditVOList.size();i++){
                    AuditVO auditVO = auditVOList.get(i);
                    if(AuditStatusEnum.PENDING.getCode().equals(auditVO.getAuditStatus())){
                        auditMapper.deleteById(auditVO.getId());
                    }
                }
            }
            application.setStatus(ApplicationStatusEnum.REJECT.getCode());
            application.setRejectReason(auditSaveParam.getRejectReason());
            applicationMapper.update(application);
        }
    }
}
