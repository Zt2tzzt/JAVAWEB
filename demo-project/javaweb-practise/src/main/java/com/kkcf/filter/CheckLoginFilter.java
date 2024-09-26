package com.kkcf.filter;

import com.alibaba.fastjson.JSONObject;
import com.kkcf.pojo.Result;
import com.kkcf.utils.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Slf4j
@WebFilter(urlPatterns = "/*")
public class CheckLoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        // 1.获取请求 url
        String url = req.getRequestURL().toString();
        log.info("请求的 URL 地址是：{}", url);

        // 2.判断请求 url 中是否有 login，如果包含，说明是登录请求，可直接方向
        if (url.contains("login")) {
            log.info("登录请求，放行...");
            filterChain.doFilter(req, res);
            return;
        }

        // 3.获取请求头中的令牌 (token)
        String token = req.getHeader("token");

        // 4.判断令牌是否存在。如果不存在，返回错误结果（未登录）
        if (!StringUtils.hasLength(token)) {
            log.info("请求头中 token 为空，返回未登录信息...");
            Result<String> result = Result.error("NOT_LOGIN");
            // 手动将 Java 实例对象，转为 JSON 格式的数据，使用 Alibaba 提供的 fastJSON 工具包。
            String jsonStr = JSONObject.toJSONString(result);
            // 返回 JSON 数据
            res.getWriter().write(jsonStr);
            return;
        }

        // 5.解析 token，如果解析失败，返回错误结果（未登录）
        try {
            JwtUtil.parseToken(token);
        } catch (Exception e) {
            log.info("解析令牌失败，返回未登录信息...");
            Result<String> result = Result.error("NOT_LOGIN");
            // 手动将 Java 实例对象，转为 JSON 格式的数据，使用 Alibaba 提供的 fastJSON 工具包。
            String jsonStr = JSONObject.toJSONString(result);
            // 返回 JSON 数据
            res.getWriter().write(jsonStr);
            return;
        }

        // 6.放行
        log.info("令牌合法，放行...");
        filterChain.doFilter(req, res);
    }
}
