package com.geewaza.code.geewaza.facade;

import com.geewaza.code.geewaza.model.vo.HomeIpVO;

/**
 * 监控家庭网络的公网ip
 * @author wangh
 */
public interface HomeIpManager {

    /**
     * 获取家庭网络的公网IP
     * @return 公网IP
     */
    HomeIpVO getPublicIp();

    /**
     * 从外部接口获取当前HomeIp，如果与缓存不同，则保存到数据库
     */
    void updateHomeIp();

}
