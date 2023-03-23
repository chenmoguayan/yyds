package org.hj.yygh.dict.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.hj.yygh.model.dict.Dict;

import java.util.List;

/**
 * @author hj
 * @data 2023/3/23 14:59
 */
public interface DictService extends IService<Dict> {
    // 根据ID查询子数据
    List<Dict> findChildrenDataList(Long id);
}