package com.geewaza.code.dubbo.test;


import com.geewaza.code.geewaza.facade.HomeIpManager;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = GeewazaApplication.class)
public class HomeIpConsumerT {

    @DubboReference(group = "geewaza", version = "1.0")
    HomeIpManager homeIpManager;

    @Test
    public void getIp() {
        System.out.println(homeIpManager.getPublicIp());
    }


}
