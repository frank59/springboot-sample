package com.geewaza.code.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

/**
 * <p></p>
 *
 * @author : wangheng
 * @date : 2021-03-04 17:02
 **/
@SpringBootApplication
public class GeewazaApplication {


    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(GeewazaApplication.class);
        Environment env = springApplication.run(args).getEnvironment();
//        log.info("Platform biz modules server has started : {}, CPU core : {}"
//                , Arrays.toString(env.getActiveProfiles()), Runtime.getRuntime().availableProcessors());
    }
}
