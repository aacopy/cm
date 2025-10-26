package cn.aacopy.cm.auth.service;

import cn.aacopy.cm.auth.model.dto.AuthGroupDTO;

/**
 * 认证组服务
 * @author cmyang
 * @date 2025/10/26
 */
public interface AuthGroupService {

    /**
     * 保存认证组
     * @param authGroupDTO
     * @return
     */
    Boolean save(AuthGroupDTO authGroupDTO);
}
