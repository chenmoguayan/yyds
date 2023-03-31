package org.hj.yygh.service;

import org.hj.yygh.model.hosp.Schedule;
import org.hj.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author hj
 * @data 2023/3/28 10:28
 */
public interface ScheduleService {
    /**
     * 上传排班信息
     * @param paramMap
     */
    void save(Map<String, Object> paramMap);

    /**
     * 分页查询
     * @param page 当前页码
     * @param limit 每页记录数
     * @param scheduleQueryVo 查询条件
     * @return
     */
    Page<Schedule> selectPage(Integer page, Integer limit, ScheduleQueryVo scheduleQueryVo);

    /**
     * 删除排班
     * @param hoscode
     * @param hosScheduleId
     */
    void remove(String hoscode, String hosScheduleId);

    /**
     * 根据医院编号、科室编号，查询排班规则数据
     */
    Map<String, Object> getScheduleRule(long page, long limit, String hosCode,
                                        String depCode);

    List<Schedule> getScheduleDetail(String hosCode, String depCode, String workDate);
}
