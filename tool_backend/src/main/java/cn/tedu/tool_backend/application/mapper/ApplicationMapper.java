package cn.tedu.tool_backend.application.mapper;

import cn.tedu.tool_backend.application.pojo.dto.ApplicationQuery;
import cn.tedu.tool_backend.application.pojo.entity.Application;
import cn.tedu.tool_backend.application.pojo.vo.ApplicationVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationMapper {
    void insert(Application application);

    List<ApplicationVO> selectApplication(ApplicationQuery applicationQuery);

    void update(Application application);
}
