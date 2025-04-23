package cn.tedu.tool_backend.vehicle.mapper;

import cn.tedu.tool_backend.vehicle.pojo.dto.VehicleQuery;
import cn.tedu.tool_backend.vehicle.pojo.entity.Vehicle;
import cn.tedu.tool_backend.vehicle.pojo.vo.VehicleVO;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface VehicleMapper {
    List<VehicleVO> selectVehicle(VehicleQuery vehicleQuery);
    void insert(Vehicle vehicle);
    void update(Vehicle vehicle);
    void delete(Long id);
    void updateNullValue(Vehicle vehicle);

}
