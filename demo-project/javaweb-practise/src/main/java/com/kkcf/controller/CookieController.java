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
public class CookieController {

    /**
     * 此方法用于：服务器端设置 Cookie
     *
     * @param res 响应
     * @return Result<Null 》
     */
    @GetMapping("/c1")
    public Result<Null> cookie1(HttpServletResponse res) {
        res.addCookie(new Cookie("username", "kkcf"));
        return Result.success();
    }

    /**
     * 此方法用于，服务器端解析 Cookie
     *
     * @param req 请求
     * @return Result<Null 》
     */
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
