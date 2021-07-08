package com.geewaza.code.geewaza.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wangh
 */
@Data
public class HomeIpVO implements Serializable {

    /**
     * ip地址
     */
    private String ip;

    /**
     * 感知日期  yyyy-MM-dd HH:mm:ss
     */
    private String touchTime;

}
