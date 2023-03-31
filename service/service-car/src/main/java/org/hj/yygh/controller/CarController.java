package org.hj.yygh.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.hj.yygh.common.result.Result;
import org.hj.yygh.model.car.Car;
import org.hj.yygh.service.CarService;
import org.hj.yygh.vo.car.CarVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author hj
 * @data 2023/3/25 8:58
 */
@RestController
@RequestMapping("/admin/car")
public class CarController {
    @Autowired
    private CarService carServiceImpl;

    @GetMapping("/getCarById/{id}")
    public Result getCar(@PathVariable Integer id) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        List list = carServiceImpl.list(queryWrapper);
        return Result.ok(list);
    }
    @PostMapping("/findPageCar/{current}/{limit}")
    public Result findPageCar(@PathVariable Integer current, @PathVariable Integer limit, @RequestBody(required = false)CarVo vo) {
        Page<Car> page = new Page<>(current, limit);
        String brand = vo.getBrand();
        String name = vo.getName();
        String date = vo.getDate();
        Integer price = vo.getPrice();
        String engine = vo.getEngine();
        String horsepower = vo.getHorsepower();
        QueryWrapper queryWrapper = new QueryWrapper();
        if (StringUtils.hasLength(brand)){
            queryWrapper.like("brand", brand);
        }
        if (StringUtils.hasLength(name)){
            queryWrapper.like("name", name);
        }
//        if (StringUtils.hasLength(date)){
//            queryWrapper.like("date", date);
//        }
//        if (StringUtils.hasLength(String.valueOf(price))){
//            queryWrapper.like("price", price);
//        }
//        if (StringUtils.hasLength(engine)){
//            queryWrapper.like("engine", engine);
//        }
//        if (StringUtils.hasLength(horsepower)){
//            queryWrapper.like("horsepower", horsepower);
//        }
        Page result = carServiceImpl.page(page, queryWrapper);
        System.out.println(result.toString());
        return Result.ok(result);
    }
    @PostMapping("/saveCar")
    public Result saveCar(@RequestBody Car car) {
        boolean save = carServiceImpl.save(car);
        if (save) {
            return Result.ok();
        }else {
            return Result.fail();
        }
    }
    @DeleteMapping("/deleteCar/{id}")
    public Result deleteCar(@PathVariable Integer id) {
        boolean b = carServiceImpl.removeById(id);
        if (b) {
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @PutMapping("/updateCar")
    public Result updateCar(@RequestBody Car car){
        boolean b = carServiceImpl.updateById(car);
        if (b) {
            return Result.ok();
        }else {
            return Result.fail();
        }
    }
}
