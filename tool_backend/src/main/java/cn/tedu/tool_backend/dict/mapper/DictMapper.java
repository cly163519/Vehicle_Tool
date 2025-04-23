package cn.tedu.tool_backend.dict.mapper;

import cn.tedu.tool_backend.dict.pojo.dto.DictQuery;
import cn.tedu.tool_backend.dict.pojo.entity.Dict;
import cn.tedu.tool_backend.dict.pojo.vo.DictVO;

import java.util.List;

public interface DictMapper {
    List<DictVO> selectDict(DictQuery dictQuery);

    void insert(Dict dict);

    void update(Dict dict);

    void deleteById(Long id);

    List<DictVO> selectDictByCode(DictQuery dictQuery);
}
