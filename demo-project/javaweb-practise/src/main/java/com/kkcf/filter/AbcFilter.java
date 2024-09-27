package com.kkcf.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

//@WebFilter(urlPatterns = "/*")
public class AbcFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("abc，放行前的逻辑……");

        filterChain.doFilter(servletRequest, servletResponse);

        System.out.println("abc，放行后的逻辑……");
    }
}
