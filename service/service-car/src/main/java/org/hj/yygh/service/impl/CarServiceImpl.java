package org.hj.yygh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hj.yygh.mapper.CarMapper;
import org.hj.yygh.model.car.Car;
import org.hj.yygh.service.CarService;
import org.springframework.stereotype.Service;

/**
 * @author hj
 * @data 2023/3/25 9:03
 */
@Service
public class CarServiceImpl extends ServiceImpl<CarMapper, Car> implements CarService {
}
