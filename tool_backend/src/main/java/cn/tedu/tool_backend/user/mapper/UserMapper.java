package cn.tedu.tool_backend.user.mapper;

import cn.tedu.tool_backend.user.pojo.dto.UserQuery;
import cn.tedu.tool_backend.user.pojo.entity.User;
import cn.tedu.tool_backend.user.pojo.vo.UserVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    UserVO selectByUsername(String username);
    void insert(User user);
    List<UserVO> selectUser(UserQuery userQuery);
  
    void update(User user);

    void deleteById(Long userId);

    UserVO selectById(Long parentId);
}
