package org.hj.yygh.dict.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hj.yygh.common.result.Result;
import org.hj.yygh.dict.service.DictService;
import org.hj.yygh.model.dict.Dict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author hj
 * @data 2023/3/23 15:03
 */
@Api(value = "数据字典接口")
@RestController
@RequestMapping("/admin/dict/dictionary")
public class DictController {
    @Autowired
    private DictService dictServiceImpl;

    @ApiOperation(value = "根据数据id查询子数据列表")
    @GetMapping("findChildrenData/{id}")
    public Result findChildrenData(@PathVariable Long id){
        List<Dict> childrenDataList = dictServiceImpl.findChildrenDataList(id);
        return Result.ok(childrenDataList);
    }
}
