package org.hj.yygh.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.hj.yygh.common.exception.YyghException;
import org.hj.yygh.common.result.ResultCodeEnum;
import org.hj.yygh.dict.client.DictFeignClient;
import org.hj.yygh.enums.DictEnum;
import org.hj.yygh.mapper.HospMapper;
import org.hj.yygh.model.hosp.Hospital;
import org.hj.yygh.model.hosp.HospitalSet;
import org.hj.yygh.repository.HospitalRepository;
import org.hj.yygh.service.HospitalService;
import org.hj.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hj
 * @data 2023/3/27 18:21
 */
@Service
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private HospMapper hospMapper;

    @Autowired
    private DictFeignClient dictFeignClient;

    /**
     * 上传医院信息处理方法
     * paramMap: HttpServletRequest . getParameterMap()
     */
    @Override
    public void save(Map<String, Object> paramMap) {
// 1. 将paramMap 从Map类型的对象 转换成了Json的字符串
        String json = JSONObject.toJSONString(paramMap);
// 2. 将json字符串转换成了Hospital的实体类对象
        Hospital hospital = JSONObject.parseObject(json,Hospital.class);
// 3. 通过“模拟系统上传到平台的医院信息中的医院编号”去mongoDB中进行查询，并且返回记录
        Hospital targetHospital =
                hospitalRepository.getHospitalByHoscode(hospital.getHoscode());
// 4. 根据targetHospital是否为null来确定是执行新增操作还是修改操作
        if(null != targetHospital) {
// 5. 修改操作
            hospital.setStatus(targetHospital.getStatus()); // 手动填充，数据不变
            hospital.setCreateTime(targetHospital.getCreateTime()); // 手动填充，数据不变
            hospital.setUpdateTime(new Date()); // 修改时间 -> 当前系统时间
            hospital.setIsDeleted(0);
// 6. 执行修改操作
            hospitalRepository.save(hospital);
        } else {
// 5. 新增操作
// 需要补足json数据中没有的字段
//0：未上线 1：已上线
            hospital.setStatus(0); // 状态
            hospital.setCreateTime(new Date()); // 创建时间
            hospital.setUpdateTime(new Date()); // 修改时间
            hospital.setIsDeleted(0); // 逻辑删除状态
// 6. 执行修改操作
            hospitalRepository.save(hospital);
        }
    }
    /**
      * 获取秘钥方法：
      * 作为医院的模拟系统 所调用的任何平台接口，第一件事就是需要验证医院的秘钥
      * 如果秘钥和平台保存的秘钥解密后相同，此时就可以继续执行，否则退回
      *
      * hoscode : 医院编号
     */
    @Override
    public String getSignKey(String hoscode) {
// 通过医院编号，获取平台mysql数据中的医院设置数据
        HospitalSet hospitalSet = this.getHospitalSetByHoscode(hoscode);
        if(null == hospitalSet) {
// 没有查询到相关的医院设置数据
            throw new YyghException(ResultCodeEnum.HOSPITAL_OPEN);
        }
// status = 0 -> 未上线 status -> 上线
        if(hospitalSet.getStatus().intValue() == 0) {
// 判断医院是否上线，如果没有上线，报错并提示
            throw new YyghException(ResultCodeEnum.HOSPITAL_LOCK);
        }
// 返回根据医院编号查询到的秘钥
        return hospitalSet.getSignKey();
    }

    @Override
    public Hospital getByHoscode(String hoscode) {
        return hospitalRepository.getHospitalByHoscode(hoscode);
    }

    @Override
    public Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo vo) {
        Sort sort = Sort.by(Sort.Direction.DESC,"createTime");
        //0为第一页
        PageRequest pageRequest = PageRequest.of(page-1, limit,sort);
        //创建匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching() //构建对象
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) //改变默认字符匹配方式：模糊查询
                .withIgnoreCase(true);//改变默认大小写忽略方式：忽略大小写
        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(vo, hospital);
        //创建实例
        Example<Hospital> example = Example.of(hospital, exampleMatcher);
        Page<Hospital> all = hospitalRepository.findAll(example,pageRequest);

        // 循环调用dictFeignClient中注册的方法，查询数据字典中的相关数据
        all.getContent().stream().forEach(item -> {
            this.packHospital(item);
        });
        return all;
    }

    @Override
    public void updateHospStatus(String id, Integer status) {
        if(status.intValue() == 1 || status.intValue() == 0) {
            Hospital hospital = hospitalRepository.findById(id).get();
            hospital.setStatus(status);
            hospital.setUpdateTime(new Date());
            hospitalRepository.save(hospital);
        }
    }

    @Override
    public Map<String, Object> getHospById(String id) {
        Map<String,Object> result = new HashMap<>();
        Hospital hospital =
                this.packHospital(hospitalRepository.findById(id).get());
// 医院信息: 包含医院等级
        result.put("hospital",hospital);
// 获取预约信息
        result.put("bookingRule",hospital.getBookingRule());
// 不重复返回
        hospital.setBookingRule(null);
        return result;

    }

    @Override
    public String getHospName(String hosCode) {
        Hospital hospital = hospitalRepository.getHospitalByHoscode(hosCode);
        if(hospital != null) {
            return hospital.getHosname();
        }
        return null;
    }

    @Override
    public List<Hospital> findByHosName(String hosname) {
        return hospitalRepository.findHospitalByHosnameLike(hosname);
    }

    @Override
    public Map<String, Object> item(String hoscode) {
        Map<String, Object> result = new HashMap<>();
// 医院详情
        Hospital hospital = this.getByHoscode(hoscode);
        result.put("hospital",hospital);
        result.put("bookingRule",hospital.getBookingRule());
        return result;
    }



    private Hospital packHospital(Hospital hospital) {
        String hostypeName = dictFeignClient.getName(DictEnum.HOSTYPE.getDictCode(),hospital.getHostype());
        String provinceName = dictFeignClient.getName(hospital.getProvinceCode());
        String cityName = dictFeignClient.getName(hospital.getCityCode());
        String districtName = dictFeignClient.getName(hospital.getDistrictCode());
        hospital.getParam().put("hostypeName",hostypeName);
        hospital.getParam().put("fullAddress",provinceName + cityName +
                districtName);
        return hospital;
    }

    /**
     * 根据hoscode获取医院设置数据
     */
    private HospitalSet getHospitalSetByHoscode(String hoscode) {
// where hoscode = ?
        QueryWrapper wrapper = new QueryWrapper<HospitalSet>().eq("hoscode",
                hoscode);
        return hospMapper.selectOne(wrapper);
    }

}
