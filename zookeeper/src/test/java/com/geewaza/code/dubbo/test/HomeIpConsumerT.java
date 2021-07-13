package com.geewaza.code.dubbo.test;


import com.alibaba.fastjson.JSONObject;
import com.geewaza.code.geewaza.facade.HomeIpManager;
import com.google.common.collect.Maps;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.service.GenericService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = GeewazaApplication.class)
public class HomeIpConsumerT {

    @DubboReference(group = "geewaza", version = "1.0")
    HomeIpManager homeIpManager;

    @Test
    public void getIp() {
        System.out.println(homeIpManager.getPublicIp());
    }



    @Test
    public void genericInvoke01() {


        ReferenceConfig<GenericService> reference = new ReferenceConfig<GenericService>();
        // 当前dubbo consumer的application配置，不设置会直接抛异常
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("generic_test_service");
        // 注册中心配置
        RegistryConfig registryConfig = new RegistryConfig();
        // 注册中心这里需要配置上注册中心协议，例如下面的zookeeper
        registryConfig.setAddress("zookeeper://192.168.10.10:2181");
//        registryConfig.setGroup("geewaza");
        reference.setApplication(applicationConfig);
        reference.setRegistry(registryConfig);
        // 设置调用的reference属性，下面只设置了协议、接口名、版本、超时时间
        reference.setProtocol("dubbo");
        reference.setInterface("com.geewaza.code.geewaza.facade.HomeIpManager");
        reference.setVersion("1.0");
        reference.setGroup("geewaza");
        reference.setTimeout(1000);
        // 声明为泛化接口
        reference.setGeneric(true);
        // GenericService可以接住所有的实现
        GenericService genericService = reference.get();
        // 构造复杂参数，下面的示例中，头两个参数为string类型，后一个是一个复杂类型，但都可以通过map构造。
        Map<String, Object> param = new HashMap<>();
        param.put("test1", "a");
        param.put("test2", "b");
        Map<String,Object> thirdParam = new HashMap<>();
        thirdParam.put("class","java.util.Map");
        thirdParam.put("subParam1","c");
        thirdParam.put("subParam2","d");
        param.put("test3",thirdParam);
        Object result = genericService.$invoke("getPublicIp", new String[]{}, new Object[]{});
        System.out.println(JSONObject.toJSONString(result));
    }


    @Test
    public void genericInvoke02() {


        GenericReqModel reqModel = new GenericReqModel();
        reqModel.setRegistry("zookeeper://192.168.10.10:2181");
        reqModel.setService("com.geewaza.code.geewaza.facade.HomeIpManager");
        reqModel.setVersion("1.0");
        reqModel.setMethod("getPublicIp");
        reqModel.setGroup("geewaza");


        ReferenceConfig<GenericService> reference = new ReferenceConfig<GenericService>();
        // 当前dubbo consumer的application配置，不设置会直接抛异常
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("generic_test_service");
        // 注册中心配置
        RegistryConfig registryConfig = new RegistryConfig();
        // 注册中心这里需要配置上注册中心协议，例如下面的zookeeper
        registryConfig.setAddress("zookeeper://192.168.10.10:2181");
//        registryConfig.setGroup("geewaza");
        reference.setApplication(applicationConfig);
        reference.setRegistry(registryConfig);
        // 设置调用的reference属性，下面只设置了协议、接口名、版本、超时时间
        reference.setProtocol("dubbo");
        reference.setInterface("com.geewaza.code.geewaza.facade.HomeIpManager");
        reference.setVersion("1.0");
        reference.setGroup("geewaza");
        reference.setTimeout(1000);
        // 声明为泛化接口
        reference.setGeneric(true);
        // GenericService可以接住所有的实现
        GenericService genericService = reference.get();
        // 构造复杂参数，下面的示例中，头两个参数为string类型，后一个是一个复杂类型，但都可以通过map构造。
        Map<String, Object> param = new HashMap<>();
        param.put("test1", "a");
        param.put("test2", "b");
        Map<String,Object> thirdParam = new HashMap<>();
        thirdParam.put("class","java.util.Map");
        thirdParam.put("subParam1","c");
        thirdParam.put("subParam2","d");
        param.put("test3",thirdParam);
        Object result = genericService.$invoke("getPublicIp", new String[]{}, new Object[]{});
        System.out.println(JSONObject.toJSONString(result));
    }


    static class DubboUtils {


        private static ApplicationConfig application = new ApplicationConfig(
                "generic-reference");

        private static Map<String, GenericService> serviceCache = Maps.newConcurrentMap();

        /**
         * 获取泛化服务接口(如有缓存, 则从缓存取)
         * @param reqModel
         * @return
         */
        public static GenericService fetchGenericService(GenericReqModel reqModel) {
            // 参数设置
            String serviceInterface = reqModel.getService();
            String serviceGroup = reqModel.getGroup();
            String serviceVersion = reqModel.getVersion();

            // 从缓存中获取服务
            String serviceCacheKey = serviceInterface + "-" + serviceGroup + "-"
                    + serviceVersion;
            GenericService service = serviceCache.get(serviceCacheKey);
            if (service != null) {
                System.out.println("fetched generic service from cache");
                return service;
            }

            // 配置调用信息
            ReferenceConfig<GenericService> reference = new ReferenceConfig<GenericService>();
            reference.setApplication(application);
            reference.setInterface(serviceInterface);
            reference.setGroup(serviceGroup);
            reference.setVersion(serviceVersion);
            reference.setGeneric(true); // 声明为泛化接口

            // 获取对应服务
            service = reference.get();

            // 缓存
            serviceCache.put(serviceCacheKey, service);

            return service;
        }

        /**
         * 根据接口类名及方法名通过反射获取参数类型
         *
         * @param interfaceName 接口名
         * @param methodName	方法名
         * @return
         * @throws ClassNotFoundException
         */
        public static String[] getMethodParamType(String interfaceName,
                                                  String methodName) throws ClassNotFoundException {
            String[] paramTypeList = null;

            // 创建类
            Class<?> clazz = Class.forName(interfaceName);
            // 获取所有的公共的方法
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {

                    Class<?>[] paramClassList = method.getParameterTypes();
                    paramTypeList = new String[paramClassList.length];

                    int i = 0;
                    for (Class<?> className : paramClassList) {
                        paramTypeList[i] = className.getTypeName();
                        i++;
                    }
                    break;
                }
            }

            return paramTypeList;
        }
    }


}
