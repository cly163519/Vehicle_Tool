package cn.tedu.tool_backend.user.controller;

import cn.tedu.tool_backend.base.response.JsonResult;
import cn.tedu.tool_backend.user.pojo.dto.UserLoginParam;
import cn.tedu.tool_backend.user.pojo.dto.UserQuery;
import cn.tedu.tool_backend.user.pojo.dto.UserSaveParam;
import cn.tedu.tool_backend.user.pojo.vo.UserVO;
import cn.tedu.tool_backend.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/user")
public class UserController {
    @Autowired
    UserService userService;
    @PostMapping("login")
        public JsonResult login(@RequestBody UserLoginParam userLoginParam){
            log.debug("User Login: userLoginParam={}",userLoginParam);
            UserVO userVO = userService.login(userLoginParam);
            return JsonResult.ok(userVO);
        }
    @PostMapping("save")
        public JsonResult saveUser(/*@RequestBody*/ UserSaveParam userSaveParam){
            log.debug("Save user: userSaveParam={}",userSaveParam);
            userService.saveUser(userSaveParam);
            return JsonResult.ok();

        }
    @GetMapping("select")
    public JsonResult selectUser(UserQuery userQuery){
        List<UserVO> list = userService.selectUser(userQuery);
        return JsonResult.ok(list);
    }
    @PostMapping("/update/password/{userId}")
    public JsonResult resetPassword(@PathVariable Long userId){
        log.debug("Reset password, userId={}",userId);
        userService.resetPassword(userId);
        return JsonResult.ok();
    }
    @PostMapping("/update/status/{userId}/{status}")
    public JsonResult updateStatus(@PathVariable Long userId, @PathVariable String status){
        log.debug("Modify user status, userId={},status={}",userId,status);
        userService.updateStatus(userId,status);
        return JsonResult.ok();
    }
    @PostMapping("/delete/{userId}")
        public JsonResult deleteUser(@PathVariable Long userId){
            log.debug("Delete user: userId={}",userId);
            userService.deleteUser(userId);
            return JsonResult.ok();
        }

    @GetMapping("/select/audit/{parentId}")
    public JsonResult selectAuditList(@PathVariable Long parentId){
        log.debug("Query the set of reviewers: parentId={}",parentId);
        List<UserVO> list = userService.selectAuditList(parentId);
        return JsonResult.ok();
    }
}

