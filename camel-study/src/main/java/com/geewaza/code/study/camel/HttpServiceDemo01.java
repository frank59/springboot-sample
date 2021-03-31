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
 * @date : 2021-03-19 09:28
 **/
public class HttpServiceDemo01 {

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
                String routeId = "jetty01";
                from("jetty:http://localhost:8080/camel/jetty01")
                        .routeId(routeId)
                        .process(exchange -> {
                            System.out.println("in route:" + routeId);
                            String body = exchange.getMessage().getBody(String.class);
                            body = markRouteId(JSONObject.parseObject(body), routeId);
                            exchange.getMessage().setBody(body);
                        })
                .to("direct:after_jetty01")
                .to("direct:after_jetty02")
                ;
            }
        });

        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                String routeId = "return01";
                from("direct:after_jetty01")
                        .routeId(routeId)
                        .process(exchange -> {
                            System.out.println("in route:" + routeId);
                            String body = exchange.getMessage().getBody(String.class);
                            body = markRouteId(JSONObject.parseObject(body), routeId);
                            System.out.println(body);
                            exchange.getMessage().setBody(body);
                        })
//                .to("direct:after_sou01")
                ;
            }
        });

        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                String routeId = "sleep01";
                from("direct:after_jetty02")
                        .routeId(routeId)
                        .process(exchange -> {
                            System.out.println("in route:" + routeId);
                            TimeUnit.SECONDS.sleep(1);
                            String body = exchange.getMessage().getBody(String.class);
                            body = markRouteId(JSONObject.parseObject(body), routeId);
                            System.out.println(body);
                            System.out.println("after sleep:" + routeId);
                            exchange.getMessage().setBody(body);
                        })
                .to("direct:after_sleep01");
            }
        });


        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                String routeId = "log01";
                from("direct:after_sleep01")
                        .routeId(routeId)
                        .process(exchange -> {
                            System.out.println("in route:" + routeId);
                            String body = exchange.getMessage().getBody(String.class);
                            body = markRouteId(JSONObject.parseObject(body), routeId);
                            System.out.println(body);
                            exchange.getMessage().setBody(body);
                        })
                        .to("log:splitterRoute?showExchangeId=true");
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
