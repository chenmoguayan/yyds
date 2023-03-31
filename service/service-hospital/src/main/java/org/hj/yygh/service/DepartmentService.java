package org.hj.yygh.service;

import org.hj.yygh.model.hosp.Department;
import org.hj.yygh.vo.departmant.DepartmentVo;
import org.hj.yygh.vo.hosp.DepartmentQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author hj
 * @data 2023/3/28 9:49
 */
public interface DepartmentService {
    void save(Map<String,Object> paramMap);
    /**
     * 分页查询
     * @param page 当前页码
     * @param limit 每页记录数
     * @param departmentQueryVo 查询条件
     * @return
     */
    Page<Department> selectPage(Integer page, Integer limit, DepartmentQueryVo departmentQueryVo);

    /**
     * 删除科室
     * @param hoscode
     * @param depcode
     */
    void remove(String hoscode, String depcode);

    List<DepartmentVo> getDeptTree(String hosCode);

    /**
     * 根据医院编号、科室编号 查询科室名称
     */
    String getDepName(String hoscode, String depcode);

}
