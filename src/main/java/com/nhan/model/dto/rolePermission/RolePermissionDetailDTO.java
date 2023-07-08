package com.nhan.model.dto.rolePermission;

import com.nhan.model.dto.permission.PermissionDetailDTO;
import com.nhan.model.dto.role.RoleBasicDTO;
import lombok.Data;

import java.util.UUID;

@Data
public class RolePermissionDetailDTO {

    private UUID id;

    private RoleBasicDTO role;

    private PermissionDetailDTO permission;

}
