package com.foodmarket.model.mapping;

import com.foodmarket.model.dto.RegistrationDto;
import com.foodmarket.model.dto.UserDto;
import com.foodmarket.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AuthMapper {

    AuthMapper INSTANCE = Mappers.getMapper(AuthMapper.class);

    UserEntity registrationToUser(RegistrationDto registrationDto);

    UserDto userToDto(UserEntity userEntity);

}
