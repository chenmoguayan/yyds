package org.hj.yygh.dict.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.hj.yygh.dict.mapper.DictMapper;
import org.hj.yygh.model.dict.Dict;
import org.hj.yygh.vo.dict.DictEeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * @author hj
 * @data 2023/3/23 17:04
 */
@Component
public class DictListener extends AnalysisEventListener<DictEeVo> {
    private DictMapper dictMapper;

    public DictListener(DictMapper dictMapper){
        this.dictMapper = dictMapper;
    }

    @Override
    public void invoke(DictEeVo dictEeVo, AnalysisContext analysisContext) {
        Dict dict = new Dict();
        BeanUtils.copyProperties(dictEeVo, dict);
        dictMapper.insert(dict);

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
