package com.geewaza.code.springboot.web.rest.api.v1;

import com.geewaza.code.springboot.aop.log.ULog;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p></p>
 *
 * @author : wangheng
 * @date : 2021-03-04 17:00
 **/
@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    @GetMapping("hello")
    @ULog
    public String getHello(@RequestParam(name = "name", required = false) String name) {
        return "Hello, " + (StringUtils.isEmpty(name)? "Geewaza" : name);
    }


}
