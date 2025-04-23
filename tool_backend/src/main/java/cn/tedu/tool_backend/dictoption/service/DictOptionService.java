package cn.tedu.tool_backend.dictoption.service;

import cn.tedu.tool_backend.dictoption.pojo.dto.DictOptionQuery;
import cn.tedu.tool_backend.dictoption.pojo.dto.DictOptionSaveParam;
import cn.tedu.tool_backend.dictoption.pojo.vo.DictOptionVO;

import java.util.List;

public interface DictOptionService {
    List<DictOptionVO> selectDictOption(DictOptionQuery dictOptionQuery);

    void saveOptionParam(DictOptionSaveParam dictOptionSaveParam);

    void deleteDictOption(Long id);

    List<DictOptionVO> selectDictOptionByCode(String code);
}
