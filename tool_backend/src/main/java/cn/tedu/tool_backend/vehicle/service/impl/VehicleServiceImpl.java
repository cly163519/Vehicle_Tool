package cn.tedu.tool_backend.vehicle.service.impl;

import cn.tedu.tool_backend.vehicle.mapper.VehicleMapper;
import cn.tedu.tool_backend.vehicle.pojo.dto.VehicleQuery;
import cn.tedu.tool_backend.vehicle.pojo.dto.VehicleSaveParam;
import cn.tedu.tool_backend.vehicle.pojo.entity.Vehicle;
import cn.tedu.tool_backend.vehicle.pojo.vo.VehicleVO;
import cn.tedu.tool_backend.vehicle.service.VehicleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Slf4j
@Service
public class VehicleServiceImpl implements VehicleService {
    @Autowired
    VehicleMapper vehicleMapper;
    @Override
    public List<VehicleVO> selectVehicle(VehicleQuery vehicleQuery) {
        log.debug("Vehicle search: vehicleQuery={}",vehicleQuery);
        List<VehicleVO> list = vehicleMapper.selectVehicle(vehicleQuery);
        return list;
    }
    @Override
    public void saveVehicle(VehicleSaveParam vehicleSaveParam){
        log.debug("Save vehicle parameters: {}",vehicleSaveParam);
        Vehicle vehicle = new Vehicle();
        BeanUtils.copyProperties(vehicleSaveParam, vehicle);
        if(vehicle.getId()==null){
            vehicle.setCreateTime(new Date());
            vehicle.setStatus("1");
            vehicleMapper.insert(vehicle);
        }else{
            vehicle.setUpdateTime(new Date());
            vehicleMapper.update(vehicle);
        }
    }
    @Override
    public void deleteVehicle(Long id){
        vehicleMapper.delete(id);
    }

    @Override
    public void unbindVehicle(Long vehicleId) {
        log.debug("Unbind vehicle business: unbindVehicle{}",vehicleId);
        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleId);
        vehicle.setGeofenceId(null);
        vehicle.setGeofenceBindStatus("0");
        vehicle.setUpdateTime(new Date());
        vehicleMapper.updateNullValue(vehicle);
    }

    @Override
    public void bindVehicle(Long geofenceId, Long vehicleId) {
        log.debug("Vehicle binding service:bindVehicle{}", geofenceId, vehicleId);
        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleId);
        vehicle.setGeofenceId(geofenceId);
        vehicle.setGeofenceBindStatus("1");
        vehicle.setUpdateTime(new Date());
        vehicleMapper.update(vehicle);
    }

}
