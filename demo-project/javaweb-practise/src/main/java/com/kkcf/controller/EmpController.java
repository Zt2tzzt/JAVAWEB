package com.kkcf.controller;

import com.kkcf.pojo.Emp;
import com.kkcf.pojo.EmpPageBean;
import com.kkcf.pojo.Result;
import com.kkcf.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;

@Slf4j
@RestController
@RequestMapping("/emps")
public class EmpController {
    @Autowired
    private EmpService empService;

    @GetMapping("/{id}")
    public Result<Emp> empById(@PathVariable int id) {
        log.info("查询员工信息，id：{}", id);

        Emp emp = empService.empById(id);

        return Result.success(emp);
    }

    @GetMapping
    public Result<EmpPageBean> listWithPageAndCount(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            String name,
            int gender,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    ) {
        log.info("查询员工信息，分页：{}, {}, {}, {}, {}, {}", page, pageSize, name, gender, startDate, endDate);

        EmpPageBean empPageBean = empService.listWithPageAndCount(page, pageSize, name, gender, startDate, endDate);

        return Result.success(empPageBean);
    }

    @DeleteMapping("/{ids}")
    public Result<Null> deleteEmp(@PathVariable int[] ids) {
        log.info("删除员工，ids：{}", Arrays.toString(ids));

        return empService.removeByIds(ids) > 0 ? Result.success() : Result.error("删除失败");
    }

    @PostMapping
    public Result<Null> addEmp(@RequestBody Emp emp) {
        log.info("新增员工，员工信息：{}", emp);

        return empService.addEmp(emp) > 0 ? Result.success() : Result.error("新增失败");
    }

    @PatchMapping
    public Result<String> updateEmp(@RequestBody Emp emp) {
        log.info("修改员工信息，员工信息：{}", emp);

        int i = empService.modifyEmp(emp);

        return Result.success("修改了" + i + "条数据");
    }
}
