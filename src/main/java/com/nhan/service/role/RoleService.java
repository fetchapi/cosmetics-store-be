package com.nhan.service.role;

import java.util.UUID;

import com.nhan.exception.NotFoundException;
import com.nhan.model.dto.response.ResponseModelDTO;
import com.nhan.model.dto.response.ResponsePageDTO;
import com.nhan.model.dto.role.RoleCreateDTO;
import org.springframework.data.domain.Pageable;


public interface RoleService {

	ResponseModelDTO createNewRole(RoleCreateDTO roleCreateDTO);

	void softDeleteById(UUID id) throws NotFoundException;

	ResponseModelDTO findById(UUID id) throws NotFoundException;

	ResponsePageDTO findAll(Pageable pageable);

}
