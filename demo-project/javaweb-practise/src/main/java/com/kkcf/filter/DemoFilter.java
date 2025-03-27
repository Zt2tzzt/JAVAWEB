package com.kkcf.filter;

import jakarta.servlet.*;

import java.io.IOException;

//@WebFilter(urlPatterns = "/*")
public class DemoFilter implements Filter {
    /**
     * 此方法用于：初始化拦截器，只会调用一次
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("init 初始化方法执行了");
        Filter.super.init(filterConfig);
    }


    /**
     * 此方法用于：拦截请求，调用多次
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("拦截到了请求，放行前的逻辑……");
        // 放行
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("拦截到了请求，放行后的逻辑……");
    }

    /**
     * 此方法用于：销毁拦截器，只会调用一次
     */
    @Override
    public void destroy() {
        System.out.println("destroy 销毁方法执行了");
        Filter.super.destroy();
    }
}
