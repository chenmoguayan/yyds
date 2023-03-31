package org.hj.yygh.service;


import org.hj.yygh.model.hosp.Hospital;
import org.hj.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author hj
 * @data 2023/3/27 18:19
 */
public interface HospitalService {
    /**
     * 上传医院信息
     */
    void save(Map<String,Object> paramMap);
    /**
     * 获取签名key
     */
    String getSignKey(String hoscode);

    Hospital getByHoscode(String hoscode);


    Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo vo);

    /**
     * 更新医院上线状态
     */
    void updateHospStatus(String id, Integer status);

    Map<String,Object> getHospById(String id);

    String getHospName(String hosCode);

    List<Hospital> findByHosName(String hosname);

    /**
     * 医院预约挂号详情
     */
    Map<String,Object> item(String hoscode);

}
