package cn.tedu.tool_backend.dictoption.service.impl;

import cn.tedu.tool_backend.base.exception.ServiceException;
import cn.tedu.tool_backend.base.response.StatusCode;
import cn.tedu.tool_backend.dict.mapper.DictMapper;
import cn.tedu.tool_backend.dict.pojo.dto.DictQuery;
import cn.tedu.tool_backend.dict.pojo.vo.DictVO;
import cn.tedu.tool_backend.dictoption.mapper.DictOptionMapper;
import cn.tedu.tool_backend.dictoption.pojo.dto.DictOptionQuery;
import cn.tedu.tool_backend.dictoption.pojo.dto.DictOptionSaveParam;
import cn.tedu.tool_backend.dictoption.pojo.entity.DictOption;
import cn.tedu.tool_backend.dictoption.pojo.vo.DictOptionVO;
import cn.tedu.tool_backend.dictoption.service.DictOptionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class DictOptionServiceImpl implements DictOptionService {
    @Autowired
    DictMapper dictMapper;
    @Autowired
    DictOptionMapper dictOptionMapper;

    @Override
    public List<DictOptionVO> selectDictOption(DictOptionQuery dictOptionQuery) {
        List<DictOptionVO> list = dictOptionMapper.selectDictOption(dictOptionQuery);
        return list;
    }

    @Override
    public void saveOptionParam(DictOptionSaveParam dictOptionSaveParam) {
        DictOption dictOption = new DictOption();
        BeanUtils.copyProperties(dictOptionSaveParam,dictOption);
        if(dictOption.getId()==null){
            dictOption.setCreateTime(new Date());
            dictOptionMapper.insert(dictOption);
        }else{
            dictOptionMapper.update(dictOption);
        }
    }

    @Override
    public void deleteDictOption(Long id) {
        dictOptionMapper.deleteById(id);
    }

    @Override
    public List<DictOptionVO> selectDictOptionByCode(String code) {
        //封装字典查询DTO
        DictQuery dictQuery = new DictQuery();
        dictQuery.setCode(code);
        //根据code查出对应的字典
        List<DictVO> dictVOList = dictMapper.selectDictByCode(dictQuery);
        if(dictVOList!=null&&dictVOList.size()>0){//list的值可能是null也可能是0,两个都需要预防
            DictVO dictVO = dictVOList.get(0);
            //封装字典项查询DTO
            DictOptionQuery dictOptionQuery = new DictOptionQuery();
            dictOptionQuery.setDictId(dictVO.getId());
            //查询对应的字典项
            List<DictOptionVO> dictOptionVOList = dictOptionMapper.selectDictOption(dictOptionQuery);
            return dictOptionVOList;
        }else{
            //若查不到对应的字典,则抛出业务层异常数据不存在
            throw new ServiceException(StatusCode.DATA_UNEXISTS);
        }
    }
}
