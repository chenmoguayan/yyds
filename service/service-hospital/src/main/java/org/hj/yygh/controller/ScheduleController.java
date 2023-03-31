package org.hj.yygh.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hj.yygh.common.result.Result;
import org.hj.yygh.model.hosp.Schedule;
import org.hj.yygh.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author hj
 * @data 2023/3/29 17:39
 */
@Api(value = "排班处理")
@RestController
@RequestMapping("/admin/hosp/schedule")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleServiceImpl;
    /**
     * 根据医院编号、科室编号，查询排班规则数据
     */
    @ApiOperation(value = "根据医院编号、科室编号，查询排班规则数据")
    @GetMapping("getScheduleRule/{page}/{limit}/{hosCode}/{depCode}")
    public Result getScheduleRule(@PathVariable long page,
                                  @PathVariable long limit,
                                  @PathVariable String hosCode,
                                  @PathVariable String depCode ){
        Map<String,Object> map =
                scheduleServiceImpl.getScheduleRule(page,limit,hosCode,depCode);
        return Result.ok(map);
    }
    /**
     * 根据医院编号、科室编号和工作日期，查询排班
     */
    @ApiOperation(value = "查询排班信息")
    @GetMapping("getScheduleDetail/{hosCode}/{depCode}/{workDate}")
    public Result getScheduleDetail(@PathVariable String hosCode,
                                    @PathVariable String depCode,
                                    @PathVariable String workDate) {
        List<Schedule> scheduleList = scheduleServiceImpl.getScheduleDetail(hosCode,depCode,workDate);
        return Result.ok(scheduleList);
    }
}

