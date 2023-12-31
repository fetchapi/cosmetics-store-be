package com.nhan.service.permission;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


import com.nhan.exception.NotFoundException;
import com.nhan.mapper.PermissionMapper;
import com.nhan.model.dto.permission.PermissionCreateDTO;
import com.nhan.exception.BadRequestException;
import com.nhan.model.dto.permission.PermissionDetailDTO;
import com.nhan.model.dto.response.ResponseModelDTO;
import com.nhan.model.dto.response.ResponsePageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.nhan.model.entity.permission.Permission;
import com.nhan.repository.PermissionRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PermissionServiceImpl implements PermissionService {

	@Autowired
	private PermissionRepository permissionRepository;

	@Autowired
	private PermissionMapper permissionMapper;

	@Override
	public ResponseModelDTO createNewPermission(PermissionCreateDTO permissionCreateDTO) {

		if (permissionRepository.existsByCode(permissionCreateDTO.getCode())) {
			throw new BadRequestException("Permission with this code already exists");
		}

		Permission permission = permissionMapper.fromCreateToEntity(permissionCreateDTO);
		PermissionDetailDTO permissionDetailDTO = permissionMapper.fromEntityToDetail(permissionRepository.save(permission));

		return ResponseModelDTO.builder()
				.data(permissionDetailDTO)
				.isSuccess(true)
				.build();
	}

	@Override
	public ResponseModelDTO updateById(UUID id, PermissionCreateDTO permissionCreateDTO) throws NotFoundException {
		Permission permission = permissionRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Not found permission with id: " + id));

		Optional<Permission> permissionOptional = permissionRepository.findByCode(permissionCreateDTO.getCode());
		if (permissionOptional.isPresent() && !permissionOptional.get().getId().equals(id)) {
			throw new BadRequestException("Permission with this code already exists");
		}

		permission.setCode(permissionCreateDTO.getCode());
		permission.setName(permissionCreateDTO.getName());

		PermissionDetailDTO permissionDetailDTO = permissionMapper.fromEntityToDetail(permissionRepository.save(permission));

		return ResponseModelDTO.builder()
				.data(permissionDetailDTO)
				.isSuccess(true)
				.build();
	}

	@Override
	public ResponseModelDTO findAll() {
		List<Permission> permissionList = permissionRepository.findAll();

		List<PermissionDetailDTO> permissionDetailDTOList = new ArrayList<>();

		for(Permission permission : permissionList) {
			PermissionDetailDTO permissionDetailDTO = permissionMapper.fromEntityToDetail(permission);
			permissionDetailDTOList.add(permissionDetailDTO);
		}

		return ResponseModelDTO.builder()
				.data(permissionDetailDTOList)
				.isSuccess(true)
				.build();
	}

	@Override
	public ResponsePageDTO findAll(String keyword, Pageable pageable) {
		Page<Permission> permissionPage = permissionRepository.findAll(keyword, pageable);

		List<PermissionDetailDTO> permissionDetailDTOList = new ArrayList<>();

		for(Permission permission : permissionPage.getContent()) {
			PermissionDetailDTO permissionDetailDTO = permissionMapper.fromEntityToDetail(permission);
			permissionDetailDTOList.add(permissionDetailDTO);
		}

		return ResponsePageDTO.builder()
				.data(permissionDetailDTOList)
				.limit(permissionPage.getSize())
				.currentPage(permissionPage.getNumber())
				.totalItems(permissionPage.getTotalElements())
				.totalPages(permissionPage.getTotalPages())
				.build();
	}

	@Override
	public ResponseModelDTO findById(UUID id) throws NotFoundException {
		Permission permission = permissionRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Not found permission with id: " + id));

		return ResponseModelDTO.builder()
				.data(permissionMapper.fromEntityToDetail(permission))
				.isSuccess(true)
				.build();
	}

	@Override
	@Transactional
	public void softDeleteById(UUID id) throws NotFoundException {
		if (permissionRepository.existsById(id))
			permissionRepository.softDeleteById(id);
		else throw new NotFoundException("Not found permission with id: " + id);
	}


}
