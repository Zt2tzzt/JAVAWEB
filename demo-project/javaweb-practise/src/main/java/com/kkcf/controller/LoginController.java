package com.kkcf.controller;

import com.kkcf.pojo.Emp;
import com.kkcf.pojo.Result;
import com.kkcf.service.EmpService;
import com.kkcf.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private EmpService empService;

    @PostMapping
    public Result<String> login(@RequestBody Emp emp) {
        log.info("员工登录，员工信息：{}", emp);

        Emp res = empService.loginEmp(emp);

        return res != null ? Result.success(JwtUtil.generateToken(new HashMap<>(Map.of(
                "id", res.getId(),
                "username", res.getUsername(),
                "name", res.getName()
        )))) : Result.error("登录失败");
    }
}
