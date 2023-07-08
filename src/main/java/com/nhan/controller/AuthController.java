package com.nhan.controller;

import java.io.IOException;

import com.nhan.exception.NotFoundException;
import com.nhan.model.dto.response.ResponseModelDTO;
import com.nhan.model.dto.user.UserRefreshTokenDTO;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nhan.model.dto.user.UserLoginDTO;
import com.nhan.model.dto.user.UserRegisterDTO;
import com.nhan.service.user.AppUserService;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {
	
	@Autowired
	private AppUserService appUserService;


	@PostMapping("/register")
	public ResponseEntity<ResponseModelDTO> register(HttpServletRequest request, @Valid @RequestBody UserRegisterDTO userRegisterDTO) throws MessagingException, IOException, NotFoundException {

		return ResponseEntity.ok().body(appUserService.register(userRegisterDTO, request));
	}

	@PostMapping("/login")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Login successfully", content = @Content(mediaType = "application/json",
					schema = @Schema(implementation = ResponseModelDTO.class))),
			@ApiResponse(responseCode = "400", description = "Incorrect username or password"),
	})
	public ResponseEntity<ResponseModelDTO> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
		return ResponseEntity.ok().body(appUserService.login(userLoginDTO));
	}

	@PostMapping("/refresh-token")
	public ResponseEntity<?> refreshToken(@Valid @RequestBody UserRefreshTokenDTO refreshTokenDTO) throws NotFoundException {
		return ResponseEntity.ok().body(appUserService.refreshToken(refreshTokenDTO));
	}

	@GetMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest httpServletRequest) {
		return ResponseEntity.ok().body(appUserService.logout(httpServletRequest));
	}
	
}
