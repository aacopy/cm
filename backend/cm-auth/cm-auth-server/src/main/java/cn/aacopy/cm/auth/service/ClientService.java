package cn.aacopy.cm.auth.service;

import cn.aacopy.cm.auth.model.entity.ClientDO;

/**
 * @author cmyang
 * @date 2025/10/28
 */
public interface ClientService {

    /**
     * 根据clientId查询客户端信息
     */
    ClientDO findByClientId(String clientId);
}
