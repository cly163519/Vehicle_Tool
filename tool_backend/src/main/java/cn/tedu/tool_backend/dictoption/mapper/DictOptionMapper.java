package cn.tedu.tool_backend.dictoption.mapper;

import cn.tedu.tool_backend.dictoption.pojo.dto.DictOptionQuery;
import cn.tedu.tool_backend.dictoption.pojo.entity.DictOption;
import cn.tedu.tool_backend.dictoption.pojo.vo.DictOptionVO;

import java.util.List;

public interface DictOptionMapper {
    void deleteByDictId(Long id);

    List<DictOptionVO> selectDictOption(DictOptionQuery dictOptionQuery);

    void insert(DictOption dictOption);

    void update(DictOption dictOption);

    void deleteById(Long id);
}
