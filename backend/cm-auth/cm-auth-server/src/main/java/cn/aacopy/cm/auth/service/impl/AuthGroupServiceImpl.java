package cn.aacopy.cm.auth.service.impl;

import cn.aacopy.cm.auth.dao.AuthGroupDao;
import cn.aacopy.cm.auth.model.convert.AuthGroupConvert;
import cn.aacopy.cm.auth.model.dto.AuthGroupDTO;
import cn.aacopy.cm.auth.model.entity.AuthGroupDO;
import cn.aacopy.cm.auth.service.AuthGroupService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author cmyang
 * @date 2025/10/26
 */
@Service
public class AuthGroupServiceImpl implements AuthGroupService {

    @Resource
    private AuthGroupDao authGroupDao;

    @Override
    public Boolean save(AuthGroupDTO authGroupDTO) {

        AuthGroupDO authGroupDO = AuthGroupConvert.INSTANCE.toDO(authGroupDTO);
        if (authGroupDao.save(authGroupDO)) {
            return true;
        }
        return true;
    }
}
