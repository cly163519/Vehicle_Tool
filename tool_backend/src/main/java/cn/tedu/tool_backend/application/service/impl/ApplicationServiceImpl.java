package cn.tedu.tool_backend.application.service.impl;

import cn.tedu.tool_backend.application.mapper.ApplicationMapper;
import cn.tedu.tool_backend.application.pojo.dto.ApplicationQuery;
import cn.tedu.tool_backend.application.pojo.dto.ApplicationSaveParam;
import cn.tedu.tool_backend.application.pojo.entity.Application;
import cn.tedu.tool_backend.application.pojo.vo.ApplicationVO;
import cn.tedu.tool_backend.application.service.ApplicationService;
import cn.tedu.tool_backend.audit.mapper.AuditMapper;
import cn.tedu.tool_backend.audit.pojo.vo.AuditVO;
import cn.tedu.tool_backend.audit.service.AuditService;
import cn.tedu.tool_backend.base.enums.ApplicationStatusEnum;
import cn.tedu.tool_backend.user.mapper.UserMapper;
import cn.tedu.tool_backend.user.pojo.vo.UserVO;
import cn.tedu.tool_backend.vehicle.mapper.VehicleMapper;
import cn.tedu.tool_backend.vehicle.pojo.entity.Vehicle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

@Transactional
@Slf4j
@Service
public class ApplicationServiceImpl implements ApplicationService {
    @Autowired
    ApplicationMapper applicationMapper;
    @Autowired
    AuditService auditService;
    @Autowired
    AuditMapper auditMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    VehicleMapper vehicleMapper;

    @Override
    public void save(ApplicationSaveParam applicationSaveParam){
        Application application = new Application();
        BeanUtils.copyProperties(applicationSaveParam,application);
        application.setStatus(ApplicationStatusEnum.PENDING.getCode());
        application.setCreateTime(new Date());

        applicationMapper.insert(application);
        auditService.insertAudit(application);
    }

    @Override
    public List<ApplicationVO> selectApplication(ApplicationQuery applicationQuery){
        log.debug("查询申请单列表业务:applicationQuery={}",applicationQuery);
        List<ApplicationVO> list = applicationMapper.selectApplication(applicationQuery);
        for(int i=0;i<list.size();i++){
            ApplicationVO applicationVO = list.get(i);
            assignAuditUserList(applicationVO);
        }
        return list;
    }

    private void assignAuditUserList(ApplicationVO applicationVO){
        List<String> auditUsernameList = new ArrayList<>();
        List<Long> auditUserIdList = new ArrayList<>();
        List<AuditVO> auditVOList = auditMapper.selectAuditByApplicationId(applicationVO.getId());
        for(int i=0;i<auditVOList.size();i++){
            AuditVO auditVO = auditVOList.get(i);
            Long id = auditVO.getAuditUserId();
            auditUserIdList.add(id);
            UserVO userVO = userMapper.selectById(id);
            auditUsernameList.add(userVO.getUsername());
        }
        applicationVO.setAuditUserIdList(auditUserIdList);
        StringJoiner stringJoiner = new StringJoiner(",");
        for(String username : auditUsernameList){
            stringJoiner.add(username);
        }
        applicationVO.setAuditUsernameList(stringJoiner.toString());
    }
    @Override
    public void cancel(Long id){
        log.debug("撤销申请单业务:id={}",id);
        Application application = new Application();
        application.setId(id);
        application.setStatus(ApplicationStatusEnum.CANCEL.getCode());
        application.setUpdateTime(new Date());
        applicationMapper.update(application);

        auditMapper.deleteByApplicationId(id);
    }
    @Override
    public void distribute(Long applicationId,Long vehicleId){
        log.debug("分配车辆业务:applicationId={},vehicleId={}",applicationId,vehicleId);
        Application application = new Application();
        application.setId(applicationId);
        application.setVehicleId(vehicleId);
        application.setStatus(ApplicationStatusEnum.ALLOCATION.getCode());
        application.setUpdateTime(new Date());
        applicationMapper.update(application);

        Vehicle vehicle= new Vehicle();
        vehicle.setId(vehicleId);
        vehicle.setStatus("2");
        vehicleMapper.update(vehicle);

    }
    @Override
    public void back(Long applicationId,Long vehicleId){
        log.debug("归还车辆业务:applicationId={},vehicleId={}",applicationId,vehicleId);
        Application application = new Application();
        application.setId(applicationId);//设置要更新哪个申请单
        application.setVehicleId(vehicleId);//设置给当前申请单分配的车辆
        application.setStatus(ApplicationStatusEnum.END.getCode());//设置申请单状态为"工单结束"
        application.setUpdateTime(new Date());//设置更新时间
        applicationMapper.update(application);//执行申请表更新操作
        //车辆也需要改为占用状态
        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleId);//设置要更新哪辆车
        vehicle.setStatus("1");//设置车辆状态为"空闲"
        vehicleMapper.update(vehicle);//执行车辆表更新操作
    }
}
