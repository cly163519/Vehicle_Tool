package cn.tedu.tool_backend.application.service;

import cn.tedu.tool_backend.application.pojo.dto.ApplicationQuery;
import cn.tedu.tool_backend.application.pojo.dto.ApplicationSaveParam;
import cn.tedu.tool_backend.application.pojo.vo.ApplicationVO;

import java.util.List;

public interface ApplicationService {
    void save(ApplicationSaveParam applicationSaveParam);

    List<ApplicationVO> selectApplication(ApplicationQuery applicationQuery);

    void cancel(Long id);

    void distribute(Long applicationId, Long vehicleId);

    void back(Long applicationId, Long vehicleId);
}
