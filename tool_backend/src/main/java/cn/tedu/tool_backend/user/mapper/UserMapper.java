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
    /*
        下面使用 User 对象作为参数，
        是因为这个方法需要接收一个包含完整用户信息的对象，
        以便进行更新操作。而 UserQuery 对象通常用于查询操作，
        不适合作为更新方法的参数。
     */
    void update(User user);

    void deleteById(Long userId);

    UserVO selectById(Long parentId);
}
