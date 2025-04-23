package cn.tedu.tool_backend.dict.service;

import cn.tedu.tool_backend.dict.pojo.dto.DictQuery;
import cn.tedu.tool_backend.dict.pojo.dto.DictSaveParam;
import cn.tedu.tool_backend.dict.pojo.vo.DictVO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface DictService {

    List<DictVO> selectDict(DictQuery dictQuery);

    void saveDict(DictSaveParam dictSaveParam);

    void deleteDict(Long id);
}
