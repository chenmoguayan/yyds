package org.hj.yygh.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hj.yygh.common.exception.YyghException;
import org.hj.yygh.common.helper.HttpRequestHelper;
import org.hj.yygh.common.result.Result;
import org.hj.yygh.common.result.ResultCodeEnum;
import org.hj.yygh.common.utils.MD5;
import org.hj.yygh.controller.BaseController;
import org.hj.yygh.model.hosp.Department;
import org.hj.yygh.model.hosp.Hospital;
import org.hj.yygh.model.hosp.Schedule;
import org.hj.yygh.service.DepartmentService;
import org.hj.yygh.service.HospitalService;
import org.hj.yygh.service.ScheduleService;
import org.hj.yygh.vo.hosp.DepartmentQueryVo;
import org.hj.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @author hj
 * @data 2023/3/27 18:27
 */
@Api(tags = "医院管理API接口")
@RestController
@RequestMapping("/api/hosp")
public class ApiController extends BaseController {

    @Autowired
    private HospitalService hospitalServiceImpl;

    @Autowired
    private DepartmentService departmentServiceImpl;

    @Autowired
    private ScheduleService scheduleServiceImpl;
    @ApiOperation(value = "上传医院")
    @PostMapping("saveHospital")
    public Result saveHospital(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> paramMap =
                HttpRequestHelper.switchMap(parameterMap);
//签名校验
        String hoscode = (String) paramMap.get("hoscode");
        if (!StringUtils.hasLength(hoscode)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        String signKey = (String) paramMap.get("sign");
// 根据医院编号获取医院秘钥
        String utfSignKey = hospitalServiceImpl.getSignKey(hoscode);
// 判断秘钥是否为null
        if (!StringUtils.hasLength(utfSignKey)) {
            try {
                byte[] bytes = utfSignKey.getBytes("UTF-8");
                utfSignKey = new String(bytes);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
// 对秘钥进行加密
        String signKeyMD5 = MD5.encrypt(utfSignKey);
// 进行加密后秘钥比对，如果不同就报错
        if (!signKey.equals(signKeyMD5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
// -------------------- 秘钥验证成功！ -------------------
// 注意：此处应该进行其他重要字段的校验，此处省略，也可以封装至BaseController中。
// 图片Base64处理
// 图片在网络传输中，会将 + 自动转换为 空格
// 需要使用图片工具类，将 空格 再转换为 +
        String logoDataString = (String) paramMap.get("logoData");
        if (StringUtils.hasLength(logoDataString)) {
            String logoData = logoDataString.replaceAll(" ", "+");
            paramMap.put("logoData", logoData);
        }
// 上面处理都结束，验证成功，进行新增或修改操作。
        hospitalServiceImpl.save(paramMap);
        return Result.ok();
    }

    @ApiOperation(value = "获取医院信息")
    @PostMapping("hospital/show")
    public Result hospital(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(parameterMap);
//签名校验
        String hoscode = (String) paramMap.get("hoscode");
        if (!StringUtils.hasLength(hoscode)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        String signKey = (String) paramMap.get("sign");
        String utfSignKey = hospitalServiceImpl.getSignKey(hoscode);
        if (!StringUtils.hasLength(utfSignKey)) {
            try {
                byte[] bytes = utfSignKey.getBytes("UTF-8");
                utfSignKey = new String(bytes);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        String signKeyMD5 = MD5.encrypt(utfSignKey);
        if (!signKey.equals(signKeyMD5)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        Hospital hospital = hospitalServiceImpl.getByHoscode((String)
                paramMap.get("hoscode"));
        return Result.ok(hospital);
    }

    @ApiOperation(value = "上传科室")
    @PostMapping("saveDepartment")
    public Result saveDepartment(HttpServletRequest request) {
        Map<String, Object> paramMap = getParamMap(request);
        checkSignKey(paramMap, hospitalServiceImpl);
        departmentServiceImpl.save(paramMap);
        return Result.ok();
    }

    @ApiOperation(value = "获取科室分页列表")
    @PostMapping("department/list")
    public Result department(HttpServletRequest request) {
        Map<String, Object> paramMap = getParamMap(request);
        checkSignKey(paramMap, hospitalServiceImpl);
// 条件查询
// 医院编号
        String hoscode = (String) paramMap.get("hoscode");
// 科室编号
        String depcode = (String) paramMap.get("depcode");
// 分页参数
        int page = !StringUtils.hasLength((String) paramMap.get("page")) ? 1 :
                Integer.parseInt((String) paramMap.get("page"));
        int limit = !StringUtils.hasLength((String) paramMap.get("limit")) ? 10 :
                Integer.parseInt((String) paramMap.get("limit"));
        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        departmentQueryVo.setHoscode(hoscode);
        departmentQueryVo.setDepcode(depcode);
        Page<Department> pageModel = departmentServiceImpl.selectPage(page, limit,
                departmentQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation(value = "删除科室")
    @PostMapping("department/remove")
    public Result removeDepartment(HttpServletRequest request) {
        Map<String, Object> paramMap = getParamMap(request);
        checkSignKey(paramMap, hospitalServiceImpl);

        String hoscde = (String) paramMap.get("hoscode");
        String depcode = (String) paramMap.get("depcode");

        departmentServiceImpl.remove(hoscde, depcode);
        return Result.ok();
    }

    @ApiOperation(value = "上传排班")
    @PostMapping("saveSchedule")
    public Result saveSchedule(HttpServletRequest request){
        Map<String, Object> paramMap = getParamMap(request);
        checkSignKey(paramMap, hospitalServiceImpl);

        scheduleServiceImpl.save(paramMap);

        return Result.ok();
    }

    @ApiOperation(value = "排班分页列表")
    @PostMapping("schedule/list")
    public Result scheduleList(HttpServletRequest request){
        Map<String, Object> paramMap = getParamMap(request);
        checkSignKey(paramMap, hospitalServiceImpl);

        String hoscode = (String) paramMap.get("hoscode");
        String depcode = (String) paramMap.get("depcode");
        int page = !StringUtils.hasLength((String) paramMap.get("page")) ? 1 :
                Integer.parseInt((String) paramMap.get("page"));
        int limit = !StringUtils.hasLength((String) paramMap.get("limit")) ? 10 :
                Integer.parseInt((String) paramMap.get("limit"));
        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hoscode);
        scheduleQueryVo.setDepcode(depcode);
        Page<Schedule> pageModel = scheduleServiceImpl.selectPage(page, limit,
                scheduleQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation(value = "删除排班")
    @PostMapping("schedule/remove")
    public Result removeSchedule(HttpServletRequest request) {
        Map<String, Object> paramMap = getParamMap(request);
        checkSignKey(paramMap, hospitalServiceImpl);
        String hoscode = (String) paramMap.get("hoscode");
        String hosScheduleId = (String) paramMap.get("hosScheduleId");
        scheduleServiceImpl.remove(hoscode, hosScheduleId);
        return Result.ok();
    }
}
