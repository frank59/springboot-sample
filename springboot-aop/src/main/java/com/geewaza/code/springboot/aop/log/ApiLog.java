package com.geewaza.code.springboot.aop.log;


import org.slf4j.Logger;

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
    Logger logger = null;
    String prefix = "";
}
