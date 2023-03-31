package org.hj.yygh.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.hj.yygh.model.hosp.Schedule;
import org.hj.yygh.repository.ScheduleRepository;
import org.hj.yygh.service.DepartmentService;
import org.hj.yygh.service.HospitalService;
import org.hj.yygh.service.ScheduleService;
import org.hj.yygh.vo.hosp.BookingScheduleRuleVo;
import org.hj.yygh.vo.hosp.ScheduleQueryVo;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hj
 * @data 2023/3/28 10:28
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private HospitalService hospitalServiceImpl;
    @Autowired
    private DepartmentService departmentServiceImpl;
    @Override
    public void save(Map<String, Object> paramMap) {
        Schedule schedule = JSONObject.parseObject(JSONObject.toJSONString(paramMap), Schedule.class);
        Schedule targetSchedule = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(schedule.getHoscode(), schedule.getHosScheduleId());
        if (null != targetSchedule) {
            targetSchedule.setUpdateTime(new Date());
            targetSchedule.setIsDeleted(0);
            scheduleRepository.save(targetSchedule);
        }else {
            schedule.setCreateTime(new Date());
            schedule.setUpdateTime(new Date());
            schedule.setIsDeleted(0);
            scheduleRepository.save(schedule);

        }
    }

    @Override
    public Page<Schedule> selectPage(Integer page, Integer limit, ScheduleQueryVo scheduleQueryVo) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        PageRequest pageable = PageRequest.of(page - 1, limit, sort);
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleQueryVo, schedule);
        schedule.setIsDeleted(0);
        //创建匹配器，即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) //改变默认字符串匹配方式：模糊查询
                .withIgnoreCase(true); //改变默认大小写忽略方式：忽略大小写
        //创建实例
        Example<Schedule> example = Example.of(schedule, matcher);
        Page<Schedule> all = scheduleRepository.findAll(example, pageable);

        return all;
    }

    @Override
    public void remove(String hoscode, String hosScheduleId) {
        Schedule schedule = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(hoscode, hosScheduleId);
        if (schedule != null) {
            scheduleRepository.deleteById(schedule.getId());
        }
    }

    @Override
    public Map<String, Object> getScheduleRule(long page, long limit, String hosCode, String depCode) {
        // 根据 医院编号 和 科室编号 查询
        Criteria criteria =
                Criteria.where("hoscode").is(hosCode).and("depcode").is(depCode);
// 根据工作日期进行分组
// 注意：引入org.springframework.data.mongodb.core.aggregation.Aggregation;
        Aggregation agg = Aggregation.newAggregation(
// 匹配条件
                Aggregation.match(criteria),
// 分组字段
                Aggregation.group("workDate")
                        .first("workDate").as("workDate")
                        .count().as("docCount")
                        .sum("reservedNumber").as("reservedNumber")
                        .sum("availableNumber").as("availableNumber"),
// 排序
                Aggregation.sort(Sort.Direction.DESC,"workDate"),
// 分页
                Aggregation.skip((page-1)*limit),
                Aggregation.limit(limit)
        );
        AggregationResults<BookingScheduleRuleVo> aggResults
                = mongoTemplate.aggregate(agg, Schedule.class,
                BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> voList = aggResults.getMappedResults();
// 分组查询总记录数
        Aggregation aggTotal = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate")
        );
        AggregationResults<BookingScheduleRuleVo> aggTotalResults
                = mongoTemplate.aggregate(aggTotal, Schedule.class,
                BookingScheduleRuleVo.class);
        int total = aggTotalResults.getMappedResults().size();
// 获取日期对应的是星期几
        for(BookingScheduleRuleVo vo : voList) {
            Date workDate = vo.getWorkDate();
            String dayOfWeek = this.getDayOfWeek(new DateTime(workDate));
            vo.setDayOfWeek(dayOfWeek);
        }
// 设置最终数据，进行返回
        Map<String,Object> result = new HashMap<>();
        result.put("bookingScheduleRuleList",voList);
        result.put("total",total);
// 获取医院名称
        String hospName = hospitalServiceImpl.getHospName(hosCode);
// 其他数据封装
        Map<String,String> baseMap = new HashMap<>();
        baseMap.put("hosname",hospName);
        result.put("baseMap",baseMap);
        return result;
    }

    @Override
    public List<Schedule> getScheduleDetail(String hosCode, String depCode, String workDate) {
        List<Schedule> scheduleList = scheduleRepository.findScheduleByHoscodeAndDepcodeAndWorkDate(hosCode, depCode, new DateTime(workDate).toDate());
        // 得到的集合遍历，补充数据：医院名称，科室名称，日期对应星期几
        scheduleList.stream().forEach(item -> {
            this.packageSchemule(item);
        });
        return scheduleList;

    }
    /**
     * 封装排班详情中的：医院名称，科室名称，日期对应星期几
     */
    private void packageSchemule(Schedule schedule) {
// 设置医院名称
        schedule.getParam().put("hosname",hospitalServiceImpl.getHospName(schedule.getHoscode()));
// 设置科室名称
        schedule.getParam().put("depname",departmentServiceImpl.getDepName(schedule.getHoscode(),schedule.getDepcode()));
// 设置日期对应的星期几
        schedule.getParam().put("dayOfWeek",this.getDayOfWeek(new
                DateTime(schedule.getWorkDate())));
    }

    /**
     * 根据日期获取周几数据
     * @param dateTime
     * @return
     */
    private String getDayOfWeek(DateTime dateTime) {
        String dayOfWeek = "";
        switch (dateTime.getDayOfWeek()) {
            case DateTimeConstants.SUNDAY:
                dayOfWeek = "周日";
                break;
            case DateTimeConstants.MONDAY:
                dayOfWeek = "周一";
                break;
            case DateTimeConstants.TUESDAY:
                dayOfWeek = "周二";
                break;
            case DateTimeConstants.WEDNESDAY:
                dayOfWeek = "周三";
                break;
            case DateTimeConstants.THURSDAY:
                dayOfWeek = "周四";
                break;
            case DateTimeConstants.FRIDAY:
                dayOfWeek = "周五";
                break;
            case DateTimeConstants.SATURDAY:
                dayOfWeek = "周六";
            default:
                break;
        }
        return dayOfWeek;
    }

}
