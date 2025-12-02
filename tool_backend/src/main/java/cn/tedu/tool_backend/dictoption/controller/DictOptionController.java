package cn.tedu.tool_backend.dictoption.controller;

import cn.tedu.tool_backend.base.response.JsonResult;
import cn.tedu.tool_backend.dictoption.pojo.dto.DictOptionQuery;
import cn.tedu.tool_backend.dictoption.pojo.dto.DictOptionSaveParam;
import cn.tedu.tool_backend.dictoption.pojo.vo.DictOptionVO;
import cn.tedu.tool_backend.dictoption.service.DictOptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/dictoption")
public class DictOptionController {
    @Autowired
    DictOptionService dictOptionService;

    @GetMapping("select")
    public JsonResult selectDictOption(DictOptionQuery dictOptionQuery){
        log.debug("Query dictionary items business: dictOptionQuery={}",dictOptionQuery);
        List<DictOptionVO> list = dictOptionService.selectDictOption(dictOptionQuery);
        return JsonResult.ok(list);
    }
    @PostMapping("save")
    public JsonResult saveDictOption(DictOptionSaveParam dictOptionSaveParam){
        log.debug("Save dictionary items, dictOptionSaveParam={}",dictOptionSaveParam);
        dictOptionService.saveOptionParam(dictOptionSaveParam);
        return JsonResult.ok();
    }
    @PostMapping("delete/{id}")
    public JsonResult deleteDictOption(@PathVariable Long id){
        log.debug("Delete dictionary item: id={}",id);
        dictOptionService.deleteDictOption(id);
        return JsonResult.ok();
    }
    @GetMapping("select/{code}")
    public JsonResult selectDictOptionByCode(@PathVariable String code){
        log.debug("Looking up dictionary entries by dictionary code, code={}",code);
        List<DictOptionVO> list = dictOptionService.selectDictOptionByCode(code);
        return JsonResult.ok(list);
    }
}
