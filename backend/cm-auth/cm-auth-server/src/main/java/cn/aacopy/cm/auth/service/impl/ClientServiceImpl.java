package cn.aacopy.cm.auth.service.impl;

import cn.aacopy.cm.auth.dao.ClientDao;
import cn.aacopy.cm.auth.model.entity.ClientDO;
import cn.aacopy.cm.auth.service.ClientService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author cmyang
 * @date 2025/10/28
 */
@Service
public class ClientServiceImpl implements ClientService {

    @Resource
    private ClientDao clientDao;

    @Override
    public ClientDO findByClientId(String clientId) {
        return clientDao.getOne(new QueryWrapper<ClientDO>().eq(ClientDO.COL_CLIENT_ID, clientId));
    }
}
