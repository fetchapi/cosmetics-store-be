package com.nhan.service.permission;

import java.util.UUID;

import com.nhan.exception.NotFoundException;
import com.nhan.model.dto.permission.PermissionCreateDTO;
import com.nhan.model.dto.response.ResponseModelDTO;
import com.nhan.model.dto.response.ResponsePageDTO;
import org.springframework.data.domain.Pageable;


public interface PermissionService {

	ResponseModelDTO createNewPermission(PermissionCreateDTO permissionCreateDTO);

	ResponseModelDTO updateById(UUID id, PermissionCreateDTO permissionCreateDTO) throws NotFoundException;

	ResponseModelDTO findById(UUID id) throws NotFoundException;

	ResponseModelDTO findAll();

	ResponsePageDTO findAll(String keyword, Pageable pageable);

	void softDeleteById(UUID id) throws NotFoundException;

}
