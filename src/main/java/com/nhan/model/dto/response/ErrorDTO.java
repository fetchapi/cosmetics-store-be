package com.nhan.model.dto.response;

import com.nhan.enums.StatusMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorDTO {
    private int code;
    private StatusMessage status;
    private Date timestamp;
    private String message;
    private String debugMessage;

}