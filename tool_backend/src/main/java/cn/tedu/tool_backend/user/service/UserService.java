package cn.tedu.tool_backend.user.service;

import cn.tedu.tool_backend.user.pojo.dto.UserLoginParam;
import cn.tedu.tool_backend.user.pojo.dto.UserQuery;
import cn.tedu.tool_backend.user.pojo.dto.UserSaveParam;
import cn.tedu.tool_backend.user.pojo.vo.UserVO;

import java.util.List;

public interface UserService {//UserService 接口声明了一个方法 login，
                            // 这个方法接受一个 UserLoginParam 对象作为参数，并返回一个 UserVO 对象。
    UserVO login(UserLoginParam userLoginParam);//UserLoginParam 可能包含用户登录所需的参数（如用户名和密码）。
                                                //UserVO 可能是用户登录成功后返回的用户信息对象。
    void saveUser(UserSaveParam userSaveParam);
    List<UserVO> selectUser(UserQuery userQuery);
    void resetPassword(Long userId);
    void updateStatus(Long userId, String status);
    void deleteUser(Long userId);

    List<UserVO> selectAuditList(Long parentId);
}
