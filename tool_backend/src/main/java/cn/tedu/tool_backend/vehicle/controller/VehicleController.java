package cn.tedu.tool_backend.vehicle.controller;

import cn.tedu.tool_backend.base.response.JsonResult;
import cn.tedu.tool_backend.vehicle.pojo.dto.VehicleQuery;
import cn.tedu.tool_backend.vehicle.pojo.dto.VehicleSaveParam;
import cn.tedu.tool_backend.vehicle.pojo.vo.VehicleVO;
import cn.tedu.tool_backend.vehicle.service.VehicleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/vehicle")
public class VehicleController {
    @Autowired
    VehicleService vehicleService;
    @GetMapping("select")
    public JsonResult selectVehicle(VehicleQuery vehicleQuery){
        log.debug("Vehicle search: vehicle={}",vehicleQuery);
        List<VehicleVO> list = vehicleService.selectVehicle(vehicleQuery);
        return JsonResult.ok(list);
    }
    @PostMapping("save")
    public JsonResult saveVehicle(VehicleSaveParam vehicleSaveParam){
        log.debug("Save vehicle: saveVehicleParam{}",vehicleSaveParam);
        vehicleService.saveVehicle(vehicleSaveParam);
        return JsonResult.ok();
    }

    @PostMapping("delete/{id}")
    public JsonResult deleteVehicle(@PathVariable Long id){
        log.debug("Save vehicle: deleteVehicle{}",id);
        vehicleService.deleteVehicle(id);
        return JsonResult.ok();
    }

    @PostMapping("unbind/{vehicleId}")
    public JsonResult unbindVehicle(@PathVariable Long vehicleId){
        log.debug("Unbind vehicle, vehicleId={}",vehicleId);
        vehicleService.unbindVehicle(vehicleId);
        return JsonResult.ok();

    }
    @PostMapping("bind/{geofenceId}/{vehicleId}")
    public JsonResult bindVehicle(@PathVariable Long geofenceId, @PathVariable Long vehicleId){
        log.debug("Bind vehicle: geofenceId={},vehicleId{}",geofenceId,vehicleId);
        vehicleService.bindVehicle(geofenceId,vehicleId);
        return JsonResult.ok();
    }
}
