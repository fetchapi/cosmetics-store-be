package com.nhan.service.rolePermission;

import com.nhan.exception.NotFoundException;
import com.nhan.model.dto.response.ResponseModelDTO;
import com.nhan.model.dto.rolePermission.RolePermissionCreateDTO;

import java.util.List;
import java.util.UUID;

public interface RolePermissionService {
    ResponseModelDTO createRolePermission(RolePermissionCreateDTO rolePermissionCreateDTO) throws NotFoundException;

    ResponseModelDTO updatePermissionByRoleId(UUID roleId, List<UUID> permissionList) throws NotFoundException;

    void softDeleteById(UUID id) throws NotFoundException;
}
