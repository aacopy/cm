package cn.aacopy.cm.auth.dao;

import cn.aacopy.cm.auth.mapper.UserMapper;
import cn.aacopy.cm.auth.model.entity.UserDO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

/**
 * @author cmyang
 * @date 2025/10/24
 */
@Repository
public class UserDao extends ServiceImpl<UserMapper, UserDO> {
}
