package cn.aacopy.cm.auth.dao;

import cn.aacopy.cm.auth.mapper.AuthGroupMapper;
import cn.aacopy.cm.auth.pojo.entity.AuthGroupDO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

/**
 * @author cmyang
 * @date 2026/2/28
 */
@Repository
public class AuthGroupDao extends ServiceImpl<AuthGroupMapper, AuthGroupDO> {

}
