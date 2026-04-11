package cn.aacopy.cm.auth.dao;

import cn.aacopy.cm.auth.mapper.AppMapper;
import cn.aacopy.cm.auth.pojo.entity.AppDO;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

/**
 * @author cmyang
 * @date 2026/4/11
 */
@Repository
public class AppDao extends ServiceImpl<AppMapper, AppDO> {

    /**
     * 根据 clientId 查询信息
     * @param clientId
     * @return
     */
    public AppDO getByClientId(String clientId) {
        if (StrUtil.isBlank(clientId)) {
            return null;
        }
        return lambdaQuery()
                .eq(AppDO::getClientId, clientId)
                .one();
    }
}
