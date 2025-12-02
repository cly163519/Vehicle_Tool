package cn.tedu.tool_backend.dict.controller;

import cn.tedu.tool_backend.base.response.JsonResult;
import cn.tedu.tool_backend.dict.pojo.dto.DictQuery;
import cn.tedu.tool_backend.dict.pojo.dto.DictSaveParam;
import cn.tedu.tool_backend.dict.pojo.vo.DictVO;
import cn.tedu.tool_backend.dict.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/dict")
public class DictController {
    @Autowired
    DictService dictService;

    @GetMapping("select")
    public JsonResult selectDict(DictQuery dictQuery){
        log.debug("Dictionary lookup service:dictQuery={}",dictQuery);
        List<DictVO> list = dictService.selectDict(dictQuery);
        return JsonResult.ok();
    }
    @PostMapping("save")
    public JsonResult saveDict(DictSaveParam dictSaveParam){
        log.debug("Save dictionary:dictSaveParam={}",dictSaveParam);
        dictService.saveDict(dictSaveParam);
        return JsonResult.ok();
    }
    @PostMapping("delete/{id}")
    public JsonResult deleteDice(@PathVariable Long id){
        log.debug("Delete dictionary:id={}",id);
        dictService.deleteDict(id);
        return JsonResult.ok();
    }
}
