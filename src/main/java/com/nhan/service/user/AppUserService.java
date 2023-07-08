package com.nhan.service.user;

import java.io.IOException;

import com.nhan.exception.NotFoundException;
import com.nhan.model.dto.response.ResponseModelDTO;
import com.nhan.model.dto.response.ResponsePageDTO;
import com.nhan.model.dto.user.*;
import org.springframework.data.domain.Pageable;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

public interface AppUserService {

//	ResponseModelDTO findById(UUID id);

	ResponsePageDTO findAll(String keyword, Pageable pageable);

	ResponseModelDTO createNewUser(UserCreateDTO userCreateDTO, HttpServletRequest request) throws NotFoundException, MessagingException, IOException;

	ResponseModelDTO register(UserRegisterDTO userRegisterDTO, HttpServletRequest request) throws MessagingException, IOException, NotFoundException;

	ResponseModelDTO login(UserLoginDTO userLoginDTO);

	ResponseModelDTO logout(HttpServletRequest request);

	ResponseModelDTO refreshToken(UserRefreshTokenDTO refreshTokenDTO) throws NotFoundException;

	ResponseModelDTO findByEmail(String email) throws NotFoundException;

	ResponseModelDTO verify(String verificationCode);

	ResponseModelDTO updateProfile(String email, UserUpdateDTO userUpdateDTO) throws NotFoundException;

	ResponseModelDTO changePassword(String email, UserChangePasswordDTO userChangePasswordDTO) throws NotFoundException;

	void forgotPassword(UserForgotPasswordDTO forgotPasswordDTO) throws MessagingException, IOException, NotFoundException;

	ResponseModelDTO resetPassword(UserResetPasswordDTO userResetPasswordDTO);



}
