package com.kkcf.controller;

import com.kkcf.pojo.Result;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.Null;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class SessionController {

    @GetMapping("/c1")
    public Result<Null> cookie1(HttpServletResponse res) {
        res.addCookie(new Cookie("username", "kkcf"));
        return Result.success();
    }

    @GetMapping("/c2")
    public Result<Null> cookie2(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        for (Cookie cookie : cookies) {
            if ("username".equals(cookie.getName())) {
                log.info("username: {}", cookie.getValue());
            }
        }

        return Result.success();
    }
}
