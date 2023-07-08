package com.nhan.mapper;

import com.nhan.model.dto.rolePermission.RolePermissionDetailDTO;
import com.nhan.model.entity.rolePermission.RolePermission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RolePermissionMapper {

    RolePermissionDetailDTO fromEntityToDetail(RolePermission permission);

}
