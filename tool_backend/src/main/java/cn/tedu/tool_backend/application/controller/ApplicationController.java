package cn.tedu.tool_backend.application.controller;

import cn.tedu.tool_backend.application.pojo.dto.ApplicationQuery;
import cn.tedu.tool_backend.application.pojo.dto.ApplicationSaveParam;
import cn.tedu.tool_backend.application.pojo.vo.ApplicationVO;
import cn.tedu.tool_backend.application.service.ApplicationService;
import cn.tedu.tool_backend.base.response.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/application")
public class ApplicationController {
    @Autowired
    ApplicationService applicationService;

    @PostMapping("save")
    public JsonResult savaApplication(ApplicationSaveParam applicationSaveParam){
        log.debug("Save application business: applicationSaveParam={}",applicationSaveParam);
        applicationService.save(applicationSaveParam);
        return JsonResult.ok();
    }
    @GetMapping("select")
    public JsonResult select(ApplicationQuery applicationQuery){
        log.debug("Query application list data, applicationQuery={}",applicationQuery);
        List<ApplicationVO> list = applicationService.selectApplication(applicationQuery);
        return JsonResult.ok(list);
    }
    @PostMapping("cancel/{id}")
    public JsonResult cancel(@PathVariable Long id){
        log.debug("Withdrawal application form: id{}",id);
        applicationService.cancel(id);
        return JsonResult.ok();

    }
    @PostMapping("distribute/{applicationId}/{vehicleId}")
    public JsonResult distribute(@PathVariable Long applicationId, @PathVariable Long vehicleId){
        log.debug("Vehicle allocation: Application number = "+applicationId+"Vehicle number = "+vehicleId);
        applicationService.distribute(applicationId,vehicleId);
        return JsonResult.ok();
    }
    @PostMapping("back/{applicationId}/{vehicleId}")
    public JsonResult back(@PathVariable Long applicationId,@PathVariable Long vehicleId){
        log.debug("Vehicle return service: Application form number = "+applicationId+"Vehicle number="+vehicleId );
        applicationService.back(applicationId,vehicleId);
        return JsonResult.ok();
    }
}
