package com.nhan.model.dto.role;

import com.nhan.model.dto.permission.PermissionDetailDTO;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class RoleDetailDTO {
    private UUID id;

    private String name;

    private List<PermissionDetailDTO> permissions;

}
