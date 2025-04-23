package cn.tedu.tool_backend.vehicle.service;

import cn.tedu.tool_backend.vehicle.pojo.dto.VehicleQuery;
import cn.tedu.tool_backend.vehicle.pojo.dto.VehicleSaveParam;
import cn.tedu.tool_backend.vehicle.pojo.vo.VehicleVO;

import java.util.List;

public interface VehicleService {
    List<VehicleVO> selectVehicle(VehicleQuery vehicleQuery);
    void saveVehicle(VehicleSaveParam vehicleSaveParam);
    void deleteVehicle(Long id);
    void unbindVehicle(Long vehicleId);
    void bindVehicle(Long geofenceId, Long vehicleId);

}
