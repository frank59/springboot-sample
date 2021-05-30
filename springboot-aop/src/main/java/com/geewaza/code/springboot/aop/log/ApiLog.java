package com.geewaza.code.springboot.aop.log;


import java.lang.annotation.*;

/**
 * <p></p>
 *
 * @author : wangheng
 * @date : 2021-03-04 17:30
 **/
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiLog {
    Logs logger();

    String prefix();

}
