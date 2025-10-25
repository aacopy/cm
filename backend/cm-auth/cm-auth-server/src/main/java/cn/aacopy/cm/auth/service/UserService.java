package cn.aacopy.cm.auth.service;

import cn.aacopy.cm.auth.model.entity.UserDO;

/**
 * @author cmyang
 * @date 2025/10/13
 */
public interface UserService {

    /**
     * 根据账号查找用户，查询账号，手机号，邮箱
     * @param account
     * @return
     */
    UserDO findAccount(String account);
}
