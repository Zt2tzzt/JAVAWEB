package com.kkcf.controller;

import com.kkcf.pojo.Emp;
import com.kkcf.pojo.Result;
import com.kkcf.service.EmpService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EmpController {
    @Resource(name = "empServiceA")
    public EmpService empService;

    @RequestMapping("/listEmp")
    public Result<List<Emp>> listEmp() {
        List<Emp> empList = empService.listEmp();
        return Result.success(empList);
    }
}
