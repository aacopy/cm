package cn.aacopy.cm.auth.service;

import cn.aacopy.cm.auth.model.dto.AppDTO;

/**
 * 应用服务
 * @author cmyang
 * @date 2025/10/26
 */
public interface AppService {

    /**
     * 保存应用
     * @param appDTO
     * @return
     */
    boolean save(AppDTO appDTO);
}
