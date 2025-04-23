package cn.tedu.tool_backend.geofence.mapper;

import cn.tedu.tool_backend.geofence.pojo.dto.GeofenceQuery;
import cn.tedu.tool_backend.geofence.pojo.entity.Geofence;
import cn.tedu.tool_backend.geofence.pojo.vo.GeofenceVO;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface GeofenceMapper {
    List<GeofenceVO> selectGeofence(GeofenceQuery geofenceQuery);

    void update(Geofence geofence);

    void insert(Geofence geofence);

    void deleteById(Long id);
}
