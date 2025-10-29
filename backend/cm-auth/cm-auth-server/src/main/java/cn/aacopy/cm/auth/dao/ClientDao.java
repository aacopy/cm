package cn.aacopy.cm.auth.dao;

import cn.aacopy.cm.auth.mapper.ClientMapper;
import cn.aacopy.cm.auth.model.entity.ClientDO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

/**
 * @author cmyang
 * @date 2025/10/28
 */
@Repository
public class ClientDao extends ServiceImpl<ClientMapper, ClientDO> {
}
