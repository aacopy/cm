package cn.aacopy.cm.auth.service.impl;

import cn.aacopy.cm.auth.model.dto.AppDTO;
import cn.aacopy.cm.auth.service.AppService;
import org.springframework.stereotype.Service;

/**
 * @author cmyang
 * @date 2025/10/26
 */
@Service
public class AppServiceImpl implements AppService {

    @Override
    public boolean save(AppDTO appDTO) {
        // 新增或者更新应用信息
        return false;
    }
}
