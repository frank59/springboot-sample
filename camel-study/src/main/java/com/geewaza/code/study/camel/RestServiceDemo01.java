package com.geewaza.code.study.camel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import java.util.concurrent.TimeUnit;

/**
 * <p></p>
 *
 * @author : wangheng
 * @date : 2021-03-19 14:12
 **/
public class RestServiceDemo01 {

    private static CamelContext camelContext;
    public static void main(String[] args) throws Exception {

        initContext();
        startContext();
        addRoute();
        standBy();

    }

    private static void standBy() throws InterruptedException {
        System.out.println("standBy...");
        // 通用没有具体业务意义的代码，只是为了保证主线程不退出
        synchronized (HttpServiceDemo01.class) {
            HttpServiceDemo01.class.wait();
        }
    }

    private static void startContext() {
        System.out.println("camel context start...");
        camelContext.start();
    }

    private static void addRoute() throws Exception {
        System.out.println("add route...");
        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {

                String routeId = "rest01";
                from("rest://get:hello/{id}")
                        .routeId(routeId)
                        .transform().simple("Bye ${header.id}")
                .process(exchange -> {
                    System.out.println("in route:" + routeId);
                    String body = exchange.getMessage().getBody(String.class);
                    body = markRouteId(JSONObject.parseObject(body), routeId);
                    System.out.println(body);
                    exchange.getMessage().setBody(body);
                })
                ;
            }
        });


    }

    public static void initContext() {
        System.out.println("init camel context...");
        camelContext = new DefaultCamelContext();
    }

    public static String markRouteId(JSONObject body, String routeId) {
        if (body == null) {
            body = new JSONObject();
        }
        JSONArray routeList = (JSONArray) body.computeIfAbsent("routes", k -> new JSONArray());
        routeList.add(routeId);
        return body.toJSONString();
    }

}
