package com.geewaza.code.springboot.aop.log;

import java.lang.annotation.*;

/**
 * <p></p>
 *
 * @author : wangheng
 * @date : 2021-03-04 17:19
 **/
@Documented
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ULog {

}
