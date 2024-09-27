package com.kkcf.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.kkcf.pojo.Result;
import com.kkcf.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {
    /**
     * 此方法用于：目标资源方法前运行，返回 true 则放行，返回 false 则拦截，不放行
     *
     * @param req
     * @param res
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        System.out.println("preHandle……");

        // 1.获取请求 url
        String url = req.getRequestURL().toString();
        log.info("请求的 URL 地址是：{}", url);

        // 2.判断请求 url 中是否有 login，如果包含，说明是登录请求，可直接方向
        if (url.contains("login")) {
            log.info("登录请求，放行...");
            return true;
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
            return false;
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
            return false;
        }

        // 6.放行
        log.info("令牌合法，放行...");
        //return HandlerInterceptor.super.preHandle(request, response, handler);
        return true;
    }

    /**
     * 此方法用于：目标资源方法后运行，可以做资源清理工作
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle……");
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * 此方法用于：资源处理完成后运行，可以做资源清理工作
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("afterCompletion……");
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
