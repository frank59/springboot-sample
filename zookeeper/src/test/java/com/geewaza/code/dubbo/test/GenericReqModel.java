package com.geewaza.code.dubbo.test;

import lombok.Data;

import java.io.Serializable;

@Data
public class GenericReqModel implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -7389007812411509827L;

    private String registry; // 注册中心地址
    private String service; // 服务包下类名
    private String version; // 版本
    private String group; // 服务组
    private String method; // 方法名
    private String paramTypes; // 参数类型
    private String paramValues; // 参数值
}
