package org.hj.yygh.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hj.yygh.common.result.Result;
import org.hj.yygh.service.DepartmentService;
import org.hj.yygh.vo.departmant.DepartmentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author hj
 * @data 2023/3/29 17:09
 */
@Api(value = "科室处理")
@RestController
@RequestMapping("/admin/hosp/department")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentServiceImpl;
    @ApiOperation(value = "查询科室树")
    @GetMapping("getDeptTree/{hosCode}")
    public Result getDeptTree(@PathVariable String hosCode) {
        List<DepartmentVo> deptList =
                departmentServiceImpl.getDeptTree(hosCode);
        return Result.ok(deptList);
    }

}
