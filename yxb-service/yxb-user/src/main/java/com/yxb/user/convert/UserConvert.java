package com.yxb.user.convert;

import com.yxb.api.user.dto.UserDTO;
import com.yxb.user.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 用户对象转换器
 */
@Mapper
public interface UserConvert {

    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    /**
     * Entity -> DTO
     */
    UserDTO toDTO(User user);

    /**
     * Entity List -> DTO List
     */
    List<UserDTO> toDTOList(List<User> users);
}
