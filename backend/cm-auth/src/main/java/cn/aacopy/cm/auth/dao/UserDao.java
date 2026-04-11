package cn.aacopy.cm.auth.dao;

import cn.aacopy.cm.auth.mapper.UserMapper;
import cn.aacopy.cm.auth.pojo.entity.UserDO;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

/**
 * @author cmyang
 * @date 2026/2/27
 */
@Repository
public class UserDao extends ServiceImpl<UserMapper, UserDO> {

    /**
     * 根据邮箱查询用户
     * @param email 邮箱
     * @return 用户信息
     */
    public UserDO findByEmail(Long authGroupId, String email) {
        if (authGroupId == null || StrUtil.isBlank(email)) {
            return null;
        }
        return lambdaQuery()
                .eq(UserDO::getAuthGroupId, authGroupId)
                .eq(UserDO::getEmail, email)
                .one();
    }
}
