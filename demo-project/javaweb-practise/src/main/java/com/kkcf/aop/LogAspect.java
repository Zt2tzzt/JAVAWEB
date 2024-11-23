package com.kkcf.aop;

import com.alibaba.fastjson.JSONObject;
import com.kkcf.mapper.OperatorLogMapper;
import com.kkcf.pojo.OperateLog;
import com.kkcf.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@Component
@Aspect
public class LogAspect {
    private final OperatorLogMapper operatorLogMapper;
    private final HttpServletRequest request;

    @Autowired
    public LogAspect(OperatorLogMapper operatorLogMapper, HttpServletRequest request) {
        this.operatorLogMapper = operatorLogMapper;
        this.request = request;
    }

    @Around("@annotation(com.kkcf.anno.Log)")
    public Object recordLog(ProceedingJoinPoint pjp) throws Throwable {
        // 获取操作人 Id，解析 JWT 令牌
        String token = request.getHeader("token");
        Claims claims = JwtUtil.parseToken(token);
        Integer operatorUserId = (Integer) claims.get("id");

        // 操作时间
        LocalDateTime operateTime = LocalDateTime.now();
        // 操作类名
        String className = pjp.getTarget().getClass().getName();
        // 操作方法名
        String methodName = pjp.getSignature().getName();
        // 操作方法参数
        Object[] args = pjp.getArgs();
        String methodParams = Arrays.toString(args);

        long begin = System.currentTimeMillis();
        Object res = pjp.proceed(args); // 调用原始方法
        long end = System.currentTimeMillis();

        // 方法耗时
        long costTime = end - begin;
        // 方法返回值
        String resJSONStr = JSONObject.toJSONString(res);
        // 记录操作日志
        OperateLog operateLog = new OperateLog(null, operatorUserId, operateTime, className, methodName, methodParams, resJSONStr, costTime);
        operatorLogMapper.insert(operateLog);
        log.info("记录操作日志：{}", operateLog);

        return res;
    }
}
