package com.geewaza.code.springboot.web.rest.api.v1;

import com.geewaza.code.springboot.aop.log.ApiLog;
import com.geewaza.code.springboot.aop.log.Logs;
import com.geewaza.code.springboot.aop.log.ULog;
import com.geewaza.code.springboot.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * <p></p>
 *
 * @author : wangheng
 * @date : 2021-03-04 17:00
 **/
@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("hello")
    @ApiLog(logger = Logs.DEFAULT, prefix = "hello")
    public String getHello(@RequestParam(name = "name", required = false) String name) {
        return testService.sayHello(Optional.ofNullable(name).orElse("Anybody"));
    }


}
