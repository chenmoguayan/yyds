package org.hj.yygh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hj.yygh.mapper.HospMapper;
import org.hj.yygh.model.hosp.HospitalSet;
import org.hj.yygh.service.HospService;
import org.springframework.stereotype.Service;

/**
 * @author hj
 * @data 2023/3/20 15:29
 */
@Service
public class HospServiceImpl extends ServiceImpl<HospMapper, HospitalSet> implements HospService {
}
