package cn.tedu.tool_backend.geofence.controller;

import cn.tedu.tool_backend.base.response.JsonResult;
import cn.tedu.tool_backend.geofence.pojo.dto.GeofenceParam;
import cn.tedu.tool_backend.geofence.pojo.dto.GeofenceQuery;
import cn.tedu.tool_backend.geofence.pojo.vo.GeofenceVO;
import cn.tedu.tool_backend.geofence.service.GeofenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/geofence")
public class GeofenceController {
    @Autowired
    GeofenceService geofenceService;

    @GetMapping("select")
    public JsonResult selectGeofence(GeofenceQuery geofenceQuery){
        log.debug("Query fence: geofenceQuery={}",geofenceQuery);
        List<GeofenceVO> list = geofenceService.selectGeofence(geofenceQuery);
        return JsonResult.ok(list);
    }
    @PostMapping("save")
    public JsonResult saveGeofence(GeofenceParam geofenceParam){
        log.debug("Preservation Fence Business: geofenceParam={}",geofenceParam);
        geofenceService.saveGeofence(geofenceParam);
        return JsonResult.ok();
    }
    @PostMapping("delete/{id}")
    public JsonResult deleteGeofence(@PathVariable Long id){
        log.debug("Fence removal service: id={}",id);
        geofenceService.deleteGeofence(id);
        return JsonResult.ok();
    }

}
