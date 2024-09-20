package com.kkcf.controller;

import com.kkcf.pojo.EmpPageBean;
import com.kkcf.pojo.Result;
import com.kkcf.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/emps")
public class EmpController {
    @Autowired
    private EmpService empService;

    @GetMapping
    public Result<EmpPageBean> listWithPageAndCount(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            String name,
            int gender,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    ) {
        log.info("查询员工信息，分页：{},{},{},{},{}, {}", page, pageSize, name, gender, startDate, endDate);

        EmpPageBean empPageBean = empService.listWithPageAndCount(page, pageSize, name, gender, startDate, endDate);

        return Result.success(empPageBean);
    }
}
