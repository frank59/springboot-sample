package com.geewaza.code.springboot.aop.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * <p></p>
 *
 * @author : wangheng
 * @date : 2021-03-04 17:37
 **/
@Aspect
@Component
public class ApiLogAspect {
    @Pointcut("@annotation(com.geewaza.code.springboot.aop.log.ApiLog)")
    public void annotationPointCut() {
    }

    @Before("annotationPointCut()")
    public void before(JoinPoint joinPoint) {
        MethodSignature sign = (MethodSignature) joinPoint.getSignature();
        Method method = sign.getMethod();
        System.out.print("接受方法：" + method.getName() + " 前置日志");
    }
}
