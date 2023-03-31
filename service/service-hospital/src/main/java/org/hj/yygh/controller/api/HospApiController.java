package org.hj.yygh.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hj.yygh.common.result.Result;
import org.hj.yygh.model.hosp.Hospital;
import org.hj.yygh.service.DepartmentService;
import org.hj.yygh.service.HospitalService;
import org.hj.yygh.vo.departmant.DepartmentVo;
import org.hj.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author hj
 * @data 2023/3/30 15:10
 */
@Api(value = "用户系统-首页-医院相关接口")
@RestController
@RequestMapping("/api/hosp/hospital")
public class HospApiController {
    @Autowired
    private HospitalService hospitalServiceImpl;

    @Autowired
    private DepartmentService departmentServiceImpl;

    @ApiOperation(value = "查询医院列表")
    @GetMapping("findHospList/{page}/{limit}")
    public Result findHospList(@PathVariable Integer page, @PathVariable Integer limit, HospitalQueryVo vo) {
        Page<Hospital> hospitalPage = hospitalServiceImpl.selectHospPage(page, limit, vo);
        return Result.ok(hospitalPage);
    }
    @ApiOperation(value = "根据医院名称模糊查询")
    @GetMapping("/findByHosName/{hosName}")
    public Result findByHosName(@PathVariable String hosName) {
        List<Hospital> hospList = hospitalServiceImpl.findByHosName(hosName);
        return Result.ok(hospList);
    }
    @ApiOperation(value = "获取科室列表")
    @GetMapping("department/{hoscode}")
    public Result index(@PathVariable String hoscode){
        List<DepartmentVo> deptList = departmentServiceImpl.getDeptTree(hoscode);
        return Result.ok(deptList);
    }

    @ApiOperation(value = "医院预约挂号详情")
    @GetMapping("{hoscode}")
    public Result item(@PathVariable String hoscode) {
        return Result.ok(hospitalServiceImpl.item(hoscode));
    }

}
