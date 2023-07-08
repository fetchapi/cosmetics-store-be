package com.nhan.model.dto.user;

import lombok.Data;

import java.util.UUID;

@Data
public class UserBasicDTO {

    private UUID id;

    private String firstname;

    private String lastname;

    private String email;

}
