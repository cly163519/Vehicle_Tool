package cn.tedu.tool_backend.dict.service.impl;

import cn.tedu.tool_backend.dict.mapper.DictMapper;
import cn.tedu.tool_backend.dict.pojo.dto.DictQuery;
import cn.tedu.tool_backend.dict.pojo.dto.DictSaveParam;
import cn.tedu.tool_backend.dict.pojo.entity.Dict;
import cn.tedu.tool_backend.dict.pojo.vo.DictVO;
import cn.tedu.tool_backend.dict.service.DictService;
import cn.tedu.tool_backend.dictoption.mapper.DictOptionMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
@Service
public class DictServiceImpl implements DictService {
    @Autowired
    DictMapper dictMapper;
    @Autowired
    DictOptionMapper dictOptionMapper;

    @Override
    public List<DictVO> selectDict(DictQuery dictQuery) {
        List<DictVO> list = dictMapper.selectDict(dictQuery);
        return list;
    }

    @Override
    public void saveDict(DictSaveParam dictSaveParam) {
        Dict dict = new Dict();
        BeanUtils.copyProperties(dictSaveParam,dict);
        if(dict.getId()==null){
            dict.setCreateTime(new Date());
            dictMapper.insert(dict);
        }else{
            dictMapper.update(dict);
        }
    }

    @Override
    public void deleteDict(Long id) {
        dictMapper.deleteById(id);
        dictOptionMapper.deleteByDictId(id);
    }
}
