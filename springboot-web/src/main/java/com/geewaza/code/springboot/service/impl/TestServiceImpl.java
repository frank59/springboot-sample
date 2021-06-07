package com.geewaza.code.springboot.service.impl;

import com.geewaza.code.springboot.service.TestService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TestServiceImpl implements TestService {
    /**
     * @param name
     * @return
     */
    @Override
    public String sayHello(String name) {
        return "Hello! ".concat(name).concat("!").concat(new Date().toString());
    }
}
