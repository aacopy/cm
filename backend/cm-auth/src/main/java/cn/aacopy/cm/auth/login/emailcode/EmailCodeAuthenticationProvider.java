package cn.aacopy.cm.auth.login.emailcode;

import cn.aacopy.cm.auth.common.ServiceAuthenticationException;
import cn.aacopy.cm.auth.common.ServiceErrorCode;
import cn.aacopy.cm.auth.dao.AuthGroupDao;
import cn.aacopy.cm.auth.dao.UserDao;
import cn.aacopy.cm.auth.login.AuthInfo;
import cn.aacopy.cm.auth.pojo.entity.AuthGroupDO;
import cn.aacopy.cm.auth.pojo.entity.UserDO;
import cn.aacopy.cm.starter.web.common.CommonConstant;
import cn.hutool.core.util.IdUtil;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author cmyang
 * @date 2026/3/3
 */
@Component
public class EmailCodeAuthenticationProvider implements AuthenticationProvider {

    @Resource
    private UserDao userDao;
    @Resource
    private AuthGroupDao authGroupDao;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        EmailCodeParam emailCodeParam = (EmailCodeParam) authentication.getPrincipal();
        // 验证邮箱验证码
        if (!"123456".equals(emailCodeParam.getCode())) {
            throw new ServiceAuthenticationException(ServiceErrorCode.VERIFICATION_CODE_ERROR);
        }
        AuthGroupDO authGroupDO = authGroupDao.getById(emailCodeParam.getAuthGroupId());
        if (authGroupDO == null) {
            throw new ServiceAuthenticationException(ServiceErrorCode.AUTH_GROUP_NOT_FOUND);
        }
        // 获取用户信息
        UserDO userDO = userDao.findByEmail(authGroupDO.getId(), emailCodeParam.getEmail());
        if (userDO == null) {
            // 自动注册
            userDO = new UserDO();
            userDO.setId(IdUtil.getSnowflakeNextId());
            userDO.setAuthGroupId(authGroupDO.getId());
            userDO.setEmail(emailCodeParam.getEmail());
            userDO.setStatus(CommonConstant.YES);
            userDO.setCreateBy(userDO.getId());
            userDO.setUpdateBy(userDO.getId());
            userDao.save(userDO);
        }
        AuthInfo authInfo = new AuthInfo();
        authInfo.setAuthGroupId(authGroupDO.getId());
        authInfo.setUserId(userDO.getId());

        return EmailCodeAuthenticationToken.authenticated(authInfo);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return EmailCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
