package com.nhan.mapper;

import com.nhan.model.dto.role.RoleBasicDTO;
import com.nhan.model.dto.role.RoleCreateDTO;
import com.nhan.model.dto.role.RoleDetailDTO;
import com.nhan.model.entity.role.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role fromCreateToEntity(RoleCreateDTO roleCreateDTO);

    RoleBasicDTO fromEntityToBasic(Role role);

    RoleDetailDTO fromEntityToDetail(Role role);

}
