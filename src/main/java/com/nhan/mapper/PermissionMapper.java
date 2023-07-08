package com.nhan.mapper;

import com.nhan.model.dto.permission.PermissionCreateDTO;
import com.nhan.model.dto.permission.PermissionDetailDTO;
import com.nhan.model.entity.permission.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    Permission fromCreateToEntity(PermissionCreateDTO permissionCreateDTO);

    PermissionDetailDTO fromEntityToDetail(Permission permission);

}
