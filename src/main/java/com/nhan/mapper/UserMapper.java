package com.nhan.mapper;

import com.nhan.model.dto.user.UserBasicDTO;
import com.nhan.model.dto.user.UserCreateDTO;
import com.nhan.model.dto.user.UserRegisterDTO;
import com.nhan.model.entity.user.AppUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    AppUser fromRegisterToEntity(UserRegisterDTO userRegisterDTO);

    AppUser fromCreateToEntity(UserCreateDTO userCreateDTO);

    UserBasicDTO fromEntityToBasic(AppUser appUser);
}
