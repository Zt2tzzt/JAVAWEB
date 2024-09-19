package com.kkcf.controller;

import com.kkcf.pojo.EmpPageBean;
import com.kkcf.pojo.Result;
import com.kkcf.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/emps")
public class EmpController {
    @Autowired
    private EmpService empService;

    @GetMapping
    public Result<EmpPageBean> listWithPageAndCount(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("查询员工信息，分页：{},{}", page, pageSize);

        EmpPageBean empPageBean = empService.listWithPageAndCount(page, pageSize);
        return Result.success(empPageBean);
    }
}
