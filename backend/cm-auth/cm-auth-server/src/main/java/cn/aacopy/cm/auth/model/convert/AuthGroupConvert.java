package cn.aacopy.cm.auth.model.convert;

import cn.aacopy.cm.auth.model.dto.AuthGroupDTO;
import cn.aacopy.cm.auth.model.entity.AuthGroupDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author cmyang
 * @date 2025/10/26
 */
@Mapper
public interface AuthGroupConvert {

    AuthGroupConvert INSTANCE = Mappers.getMapper(AuthGroupConvert.class);

    AuthGroupDO toDO(AuthGroupDTO dto);
}
