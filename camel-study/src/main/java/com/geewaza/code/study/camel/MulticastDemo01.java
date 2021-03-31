package com.geewaza.code.study.camel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 *                     +------------+   +------------+
 *                     |            |   |            |
 *                 +--->  route01   +--->  return01  |
 * +-----------+   |   |            |   |            |
 * |           |   |   +------------+   +------------+
 * | jetty01   +---+
 * |           |   |
 * +-----------+   |   +------------+   +------------+
 *                 |   |            |   |            |
 *                 +--->  route02   +--->  return02  |
 *                     |            |   |            |
 *                     +------------+   +------------+
 * @author : wangheng
 * @date : 2021-03-31 11:40
 **/
public class MulticastDemo01 {

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
                            System.out.println("- in route:" + routeId);
                            String body = exchange.getMessage().getBody(String.class);
                            body = markRouteId(JSONObject.parseObject(body), routeId);
                            exchange.getMessage().setBody(body);
                        })
                        .multicast(new AggregationStrategy() {
                            @Override
                            public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
                                if (!Objects.isNull(oldExchange)) {
                                    if (!Objects.isNull(oldExchange.getMessage().getHeader("responseBody"))) {
                                        newExchange.getMessage().setHeader("responseBody", oldExchange.getMessage().getHeader("responseBody"));
                                    }
                                }
                                return newExchange;
                            }
                        }).parallelProcessing().streaming()
                        .to("direct:after_jetty01", "direct:after_jetty02")
                        .end()
                        .process(exchange -> {
                            System.out.println("- back in route:" + routeId);
                            if (!Objects.isNull(exchange.getMessage().getHeader("responseBody"))) {
                                exchange.getMessage().setBody(exchange.getMessage().getHeader("responseBody"));
                            } else {
                                String body = exchange.getMessage().getBody(String.class);
                                body = markRouteId(JSONObject.parseObject(body), routeId);
                                exchange.getMessage().setBody(body);
                            }
                        })
                ;
            }
        });

        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                String routeId = "route01";
                from("direct:after_jetty01")
                        .routeId(routeId)
                        .process(exchange -> {
                            System.out.println("- in route:" + routeId);
                            String body = exchange.getMessage().getBody(String.class);
                            body = markRouteId(JSONObject.parseObject(body), routeId);
                            int sleep = new Random().nextInt(5);
                            TimeUnit.SECONDS.sleep(sleep);
                            System.out.println("after sleep " +sleep+ "s in " + routeId);
                            System.out.println(body);
                            exchange.getMessage().setBody(body);
                        })
                        .multicast().parallelProcessing().streaming()
                        .to("direct:after_route01")

                ;
            }
        });

        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                String routeId = "return01";
                from("direct:after_route01")
                        .routeId(routeId)
                        .process(exchange -> {
                            System.out.println("- in route:" + routeId);
                            String body = exchange.getMessage().getBody(String.class);
                            body = markRouteId(JSONObject.parseObject(body), routeId);
                            System.out.println(body);
                            exchange.getMessage().setHeader("responseBody", body);
                            exchange.getMessage().setBody(body);
                        })
//                        .to("direct:after_route01")
                ;
            }
        });



        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                String routeId = "route02";
                from("direct:after_jetty02")
                        .routeId(routeId)
                        .process(exchange -> {
                            System.out.println("- in route:" + routeId);
                            String body = exchange.getMessage().getBody(String.class);
                            body = markRouteId(JSONObject.parseObject(body), routeId);
                            int sleep = new Random().nextInt(5);
                            TimeUnit.SECONDS.sleep(sleep);
                            System.out.println("after sleep " +sleep+ "s in " + routeId);
                            System.out.println(body);
                            exchange.getMessage().setBody(body);
                        })
                        .multicast().parallelProcessing().streaming()
                        .to("direct:after_route02");
            }
        });

        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                String routeId = "return02";
                from("direct:after_route02")
                        .routeId(routeId)
                        .process(exchange -> {
                            System.out.println("- in route:" + routeId);
                            String body = exchange.getMessage().getBody(String.class);
                            body = markRouteId(JSONObject.parseObject(body), routeId);
                            System.out.println(body);
                            exchange.getMessage().setHeader("responseBody", body);
                            exchange.getMessage().setBody(body);
                        })
//                        .to("direct:after_route01")
                ;
            }
        });


    }

    public static void initContext() {
        System.out.println("init camel context...");
        camelContext = new DefaultCamelContext();
        camelContext.setTracing(true);
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
