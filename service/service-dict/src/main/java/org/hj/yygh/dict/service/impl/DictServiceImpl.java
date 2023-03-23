package org.hj.yygh.dict.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hj.yygh.dict.mapper.DictMapper;
import org.hj.yygh.dict.service.DictService;
import org.hj.yygh.model.dict.Dict;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hj
 * @data 2023/3/23 14:58
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    @Override
    public List<Dict> findChildrenDataList(Long id) {
        // 根据父id查询子数据
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        List<Dict> dicts = baseMapper.selectList(wrapper);

        // 为hasChildren属性赋值，前段页面需要
        for (Dict dict : dicts){
            Long dictId = dict.getId();
            boolean isChild = this.isChindren(dictId);
            dict.setHasChildren(isChild);
        }
        return dicts;
    }
    /**
     * 判断是否存在子数据
     */
    private boolean isChindren(Long id){
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        Long count;
        count = baseMapper.selectCount(wrapper);
        return count > 0;
    }
}
