package cn.aacopy.cm.auth.service.impl;

import cn.aacopy.cm.auth.dao.UserDao;
import cn.aacopy.cm.auth.model.entity.UserDO;
import cn.aacopy.cm.auth.service.UserService;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author cmyang
 * @date 2025/10/13
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;


    @Override
    public UserDO findAccount(String account) {
        List<UserDO> list = userDao.list(new QueryWrapper<UserDO>().eq(UserDO.COL_USERNAME, account));
        if (CollUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }
}
