package cn.tedu.tool_backend.geofence.service;

import cn.tedu.tool_backend.geofence.pojo.dto.GeofenceParam;
import cn.tedu.tool_backend.geofence.pojo.dto.GeofenceQuery;
import cn.tedu.tool_backend.geofence.pojo.vo.GeofenceVO;

import java.util.List;

public interface GeofenceService {
    List<GeofenceVO> selectGeofence(GeofenceQuery geofenceQuery);

    void saveGeofence(GeofenceParam geofenceParam);

    void deleteGeofence(Long id);
}
