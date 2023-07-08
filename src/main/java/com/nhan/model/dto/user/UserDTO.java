package com.nhan.model.dto.user;

import java.util.UUID;

import com.nhan.model.dto.role.RoleDetailDTO;
import lombok.Data;

@Data
public class UserDTO  {
	private UUID userId;
	private String email;
	private String firstname;
	private String lastname;
	private RoleDetailDTO role;
	private boolean isEnabled;

}
