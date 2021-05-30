package com.geewaza.code.springboot.aop.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Date;

/**
 * <p></p>
 *
 * @author : wangheng
 * @date : 2021-03-04 17:37
 **/
@Aspect
@Component
public class ApiLogAspect {

    @Around("@annotation(com.geewaza.code.springboot.aop.log.ApiLog)")
    public Object around(ProceedingJoinPoint joinPoint) {
        MethodSignature sign = (MethodSignature) joinPoint.getSignature();
        Method method = sign.getMethod();
        System.out.print("接受方法：" + method.getName() + " 前置日志");
        ApiLog apiLog = method.getAnnotation(ApiLog.class);
        String prefix = apiLog.prefix();
        Logs log = apiLog.logger();
        Object result = null;
        try {
            //让代理方法执行
            result = joinPoint.proceed();
            // 2.相当于后置通知(方法成功执行之后走这里)
            log.getLogger().info("正常");// 设置操作结果
        } catch (Throwable e) {
            // 3.相当于异常通知部分
            log.getLogger().info("失败");// 设置操作结果
        } finally {
            // 4.相当于最终通知
            log.getLogger().info("{}", new Date());// 设置操作日期
            log.getLogger().info(prefix);// 添加日志记录
        }
        return result;

    }
}
