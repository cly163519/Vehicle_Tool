package cn.tedu.tool_backend.user.service.impl;

import cn.tedu.tool_backend.base.exception.ServiceException;
import cn.tedu.tool_backend.base.response.StatusCode;
import cn.tedu.tool_backend.user.mapper.UserMapper;
import cn.tedu.tool_backend.user.pojo.dto.UserLoginParam;
import cn.tedu.tool_backend.user.pojo.dto.UserQuery;
import cn.tedu.tool_backend.user.pojo.dto.UserSaveParam;
import cn.tedu.tool_backend.user.pojo.entity.User;
import cn.tedu.tool_backend.user.pojo.vo.UserVO;
import cn.tedu.tool_backend.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Override
    public UserVO login(UserLoginParam userLoginParam) {
        log.debug("用户登录,参数:{}",userLoginParam);
        UserVO userVO = userMapper.selectByUsername(userLoginParam.getUsername());
        if(userVO==null){
            throw new ServiceException(StatusCode.USERNAME_ERROR);
        }
        if(!userVO.getPassword().equals(userLoginParam.getPassword())){
            throw new ServiceException(StatusCode.PASSWORD_ERROR);
        }
        return userVO;
    }
    @Override
    public void saveUser(UserSaveParam userSaveParam){
        User user = new User();
        BeanUtils.copyProperties(userSaveParam,user);
        if(user.getId()==null){
            user.setPassword("123456");
            user.setCreateTime(new Date());
            userMapper.insert(user);
        }else{
            user.setUpdateTime(new Date());
            userMapper.update(user);
        }
    }
    @Override
    public List<UserVO> selectUser(UserQuery userQuery) {
        List<UserVO> list = userMapper.selectUser(userQuery);
        return list;
    }
    @Override
    public void resetPassword(Long userId){
        log.debug("重置用户密码: userId={}", userId);
        User user = new User();
        user.setId(userId);
        user.setPassword("root");
        user.setUpdateTime(new Date());
        userMapper.update(user);
    }
    @Override
    public void updateStatus(Long userId,String status){
        log.debug("更新用户状态:userId{},status{}",userId,status);
        User user = new User();
        user.setId(userId);
        user.setStatus(status);
        user.setUpdateTime(new Date());
        userMapper.update(user);
    }
    @Override
    public void deleteUser(Long userId){
        log.debug("删除用户:userId={}",userId);
        userMapper.deleteById(userId);
    }

    @Override
    public List<UserVO> selectAuditList(Long parentId) {
        log.debug("查询审核人集合业务:parentId={},parentId");
        ArrayList<UserVO> userVOList = new ArrayList<>();
        UserVO auditUser1 = userMapper.selectById(parentId);
        userVOList.add(auditUser1);
        if(auditUser1 != null &&auditUser1.getParentId()!=null){
            UserVO auditUser2 = userMapper.selectById(auditUser1.getParentId());
            userVOList.add(auditUser2);
        }
        return userVOList;
    }
}
