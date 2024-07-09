package com.swssu.flexrate.exception;

import com.swssu.flexrate.exception.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AppException extends RuntimeException{
    private ErrorCode errorCode;
    private String message;
}
