package cn.tedu.tool_backend.geofence.service.impl;

import cn.tedu.tool_backend.base.exception.ServiceException;
import cn.tedu.tool_backend.base.response.StatusCode;
import cn.tedu.tool_backend.geofence.mapper.GeofenceMapper;
import cn.tedu.tool_backend.geofence.pojo.dto.GeofenceParam;
import cn.tedu.tool_backend.geofence.pojo.dto.GeofenceQuery;
import cn.tedu.tool_backend.geofence.pojo.entity.Geofence;
import cn.tedu.tool_backend.geofence.pojo.vo.GeofenceVO;
import cn.tedu.tool_backend.geofence.service.GeofenceService;
import cn.tedu.tool_backend.vehicle.mapper.VehicleMapper;
import cn.tedu.tool_backend.vehicle.pojo.dto.VehicleQuery;
import cn.tedu.tool_backend.vehicle.pojo.vo.VehicleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Slf4j
@Service
public class GeofenceServiceImpl implements GeofenceService {
    @Autowired
    GeofenceMapper geofenceMapper;
    @Autowired
    VehicleMapper vehicleMapper;
    @Override
    public List<GeofenceVO> selectGeofence(GeofenceQuery geofenceQuery) {
        log.debug("查询围栏:geofenceQuery:geofenceQuery{}",geofenceQuery);
        List<GeofenceVO> list = geofenceMapper.selectGeofence(geofenceQuery);
        for(int i=0;i<list.size();i++){
            GeofenceVO geofenceVO = list.get(i);
            VehicleQuery vehicleQuery = new VehicleQuery();
            vehicleQuery.setGeofenceId(geofenceVO.getId());
            List<VehicleVO> vehicleVOList = vehicleMapper.selectVehicle(vehicleQuery);
            int totalNum = vehicleVOList.size();
            int availableNum = 0;
            for(VehicleVO vehicleVO : vehicleVOList){
                if(vehicleVO.getStatus().equals("1")){
                    availableNum++;
                }
            }
            if(availableNum>0){
                geofenceVO.setAvailableNum(availableNum);
            }else{
                geofenceVO.setAvailableNum(0);
            }
            geofenceVO.setVehicleList(vehicleVOList);
        }
        return list;
    }

    @Override
    public void saveGeofence(GeofenceParam geofenceParam) {
        log.debug("保存围栏业务:geofenceParam={}",geofenceParam);
        Geofence geofence = new Geofence();
        BeanUtils.copyProperties(geofenceParam,geofence);
        if(geofence.getId() != null){
            geofence.setUpdateTime(new Date());
            geofenceMapper.update(geofence);
        }else{
            geofence.setStatus("1");
            geofence.setCreateTime(new Date());
            geofenceMapper.insert(geofence);
        }
    }

    @Override
    public void deleteGeofence(Long id) {
        log.debug("删除围栏业务信息:id={}",id);
        VehicleQuery vehicleQuery = new VehicleQuery();
        vehicleQuery.setGeofenceId(id);
        List<VehicleVO> list = vehicleMapper.selectVehicle(vehicleQuery);
        if(list.size()>0 && list != null){
            throw new ServiceException(StatusCode.OPERATION_FAILED);
        }else{
            geofenceMapper.deleteById(id);
        }

    }


}
