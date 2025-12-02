package cn.tedu.tool_backend.user.service;

import cn.tedu.tool_backend.user.pojo.dto.UserLoginParam;
import cn.tedu.tool_backend.user.pojo.dto.UserQuery;
import cn.tedu.tool_backend.user.pojo.dto.UserSaveParam;
import cn.tedu.tool_backend.user.pojo.vo.UserVO;

import java.util.List;

public interface UserService {
    UserVO login(UserLoginParam userLoginParam);
    void saveUser(UserSaveParam userSaveParam);
    List<UserVO> selectUser(UserQuery userQuery);
    void resetPassword(Long userId);
    void updateStatus(Long userId, String status);
    void deleteUser(Long userId);

    List<UserVO> selectAuditList(Long parentId);
}
