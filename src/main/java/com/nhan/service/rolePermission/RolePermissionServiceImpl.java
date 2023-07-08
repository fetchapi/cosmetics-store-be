package com.nhan.service.rolePermission;

import com.nhan.exception.BadRequestException;
import com.nhan.exception.NotFoundException;
import com.nhan.mapper.PermissionMapper;
import com.nhan.mapper.RoleMapper;
import com.nhan.mapper.RolePermissionMapper;
import com.nhan.model.dto.permission.PermissionDetailDTO;
import com.nhan.model.dto.response.ResponseModelDTO;
import com.nhan.model.dto.rolePermission.RolePermissionCreateDTO;
import com.nhan.model.entity.permission.Permission;
import com.nhan.model.entity.role.Role;
import com.nhan.model.entity.rolePermission.RolePermission;
import com.nhan.repository.PermissionRepository;
import com.nhan.repository.RolePermissionRepository;
import com.nhan.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public ResponseModelDTO createRolePermission(RolePermissionCreateDTO rolePermissionCreateDTO) throws NotFoundException {

        UUID roleId = rolePermissionCreateDTO.getRoleId();
        UUID permissionId = rolePermissionCreateDTO.getPermissionId();

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new NotFoundException("Role not found: " + roleId));

        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new NotFoundException("Permission not found: " + permissionId));

        if (rolePermissionRepository.existsByRoleIdAndPermissionId(roleId, permissionId)) {
            throw new BadRequestException("The permission already exists in this role");
        }

        RolePermission rolePermission = RolePermission.builder()
                .role(role)
                .permission(permission)
                .build();

        return ResponseModelDTO.builder()
                .data(rolePermissionMapper.fromEntityToDetail(rolePermissionRepository.save(rolePermission)))
                .isSuccess(true)
                .build();
    }

    @Override
    @Transactional
    public ResponseModelDTO updatePermissionByRoleId(UUID roleId, List<UUID> permissionList) throws NotFoundException {

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new NotFoundException("Role not found: " + roleId));


        List<RolePermission> newRolePermissions = new ArrayList<>();

        for(UUID uuid : permissionList) {
			Permission permission = permissionRepository.findById(uuid)
					.orElseThrow(() -> new NotFoundException("Permission not found: " + uuid));

            RolePermission rolePermission = RolePermission.builder()
                    .role(role)
                    .permission(permission)
                    .build();

            newRolePermissions.add(rolePermission);
		}

        role.getPermissions().clear();
        rolePermissionRepository.deleteRolePermissionByRoleId(roleId);

        List<RolePermission> rolePermissions = rolePermissionRepository.saveAll(newRolePermissions);

        List<PermissionDetailDTO> permissionDetailDTOS  = new ArrayList<>();
        for(RolePermission rolePermission : rolePermissions) {
            permissionDetailDTOS.add(permissionMapper.fromEntityToDetail(rolePermission.getPermission()));
        }

        return ResponseModelDTO.builder()
                .data(permissionDetailDTOS)
                .isSuccess(true)
                .build();

    }

    @Override
    @Transactional
    public void softDeleteById(UUID id) throws NotFoundException {
        if (rolePermissionRepository.existsById(id))
            rolePermissionRepository.softDeleteById(id);
        else throw new NotFoundException("Not found role with id: " + id);
    }
}
