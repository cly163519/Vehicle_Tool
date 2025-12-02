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
        //DTO encapsulation dictionary lookup
        DictQuery dictQuery = new DictQuery();
        dictQuery.setCode(code);
        //Find the corresponding dictionary based on the code.
        List<DictVO> dictVOList = dictMapper.selectDictByCode(dictQuery);
        if(dictVOList!=null&&dictVOList.size()>0){
            DictVO dictVO = dictVOList.get(0);
            //DTO (Dictionary Item Query)
            DictOptionQuery dictOptionQuery = new DictOptionQuery();
            dictOptionQuery.setDictId(dictVO.getId());
            //Query the corresponding dictionary item
            List<DictOptionVO> dictOptionVOList = dictOptionMapper.selectDictOption(dictOptionQuery);
            return dictOptionVOList;
        }else{
            //If the corresponding dictionary cannot be found, an exception will be thrown from the business layer indicating that the data does not exist.
            throw new ServiceException(StatusCode.DATA_UNEXISTS);
        }
    }
}
