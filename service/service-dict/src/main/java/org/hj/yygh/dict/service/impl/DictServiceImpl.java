package org.hj.yygh.dict.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hj.yygh.dict.listener.DictListener;
import org.hj.yygh.dict.mapper.DictMapper;
import org.hj.yygh.dict.service.DictService;
import org.hj.yygh.model.dict.Dict;
import org.hj.yygh.vo.dict.DictEeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hj
 * @data 2023/3/23 14:58
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Cacheable(value = "dict",keyGenerator = "keyGenerator")
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

    @Override
    public void exportExcel(HttpServletResponse response) {
        try {
            // 设置下载信息
            response.setContentType("application/vnd.mapper");
            response.setCharacterEncoding("UTF-8");
            String fileName = URLEncoder.encode("数据字典", StandardCharsets.UTF_8);
            // 设置头信息：以下载方式打开
            response.setHeader("Content-disposition", "attachment;filename=" +
                    fileName + ".xlsx");
            List<Dict> dicts = baseMapper.selectList(null);

            List<DictEeVo> dictEeVos = new ArrayList<>();

            for (Dict dict : dicts) {
                DictEeVo edv = new DictEeVo();
                BeanUtils.copyProperties(dict, edv);
                dictEeVos.add(edv);
            }

            EasyExcel.write(response.getOutputStream(), DictEeVo.class).sheet("数据字典").doWrite(dictEeVos);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @CacheEvict(value = "dict",allEntries = true)
    @Override
    public void importExcel(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(),DictEeVo.class,new DictListener(baseMapper))
                    .sheet()
                    .doRead();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
