package org.hj.yygh.dict.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hj.yygh.common.result.Result;
import org.hj.yygh.dict.service.DictService;
import org.hj.yygh.model.dict.Dict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
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
    /**
     * 导出数据字典
     */
    @GetMapping("exportData")
    public void exportData(HttpServletResponse response){
        dictServiceImpl.exportExcel(response);
    }
    /**
     * 导出数据字典
     */
    @PostMapping("importData")
    public Result importData(MultipartFile file){
        dictServiceImpl.importExcel(file);
        return Result.ok();
    }
}
