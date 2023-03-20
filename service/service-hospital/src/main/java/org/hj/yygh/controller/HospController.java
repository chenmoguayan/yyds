package org.hj.yygh.controller;

import org.hj.yygh.model.hosp.HospitalSet;
import org.hj.yygh.service.HospService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author hj
 * @data 2023/3/20 15:27
 */
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospController {
    @Autowired
    private HospService hospServiceImpl;

    @GetMapping("/findAll")
    public List<HospitalSet> findAllHospSet() {
        List<HospitalSet> list = hospServiceImpl.list();
        return list;
    }

    @DeleteMapping("{id}")
    public boolean removeHospSet(@PathVariable Long id) {
        return hospServiceImpl.removeById(id);
    }
}
