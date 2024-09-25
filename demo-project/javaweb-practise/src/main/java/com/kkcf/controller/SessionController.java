package com.kkcf.controller;

import com.kkcf.pojo.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.Null;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class SessionController {
    @GetMapping("/s1")
    public Result<Null> generateSession(HttpSession session) {
        log.info("session hashcode: {}", session.hashCode());

        session.setAttribute("loginUser", "tom"); // 在 session 中存储数据
        return Result.success();
    }

    @GetMapping("/s2")
    public Result<String> parseSession(HttpServletRequest req) {
        HttpSession session = req.getSession();
        log.info("session hashcode: {}", session.hashCode());

        Object loginUser = session.getAttribute("loginUser"); // 从 session 中获取数据
        log.info("loginUser: {}", loginUser);
        return Result.success((String) loginUser);
    }
}
