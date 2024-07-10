package com.swssu.flexrate.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    USERNAME_DUPLICATED(409, HttpStatus.CONFLICT,""),
    USERNAME_NOT_FOUND(404, HttpStatus.NOT_FOUND,""),
    NICKNAME_BAD_REQUEST(400, HttpStatus.BAD_REQUEST,""),
    PASSWORD_BAD_REQUEST(400, HttpStatus.BAD_REQUEST,""),
    INVALID_PASSWORD(401, HttpStatus.UNAUTHORIZED,""),

    ENUMTYPE_BAD_REQUEST(400, HttpStatus.BAD_REQUEST,""),
    CREDITRATINGINFO_NOT_FOUND(404, HttpStatus.NOT_FOUND,""),
    LOAN_LIMIT_EXCEEDED(409, HttpStatus.CONFLICT,""),
    BANK_NOT_FOUND(404, HttpStatus.NOT_FOUND,""),
    ALREADY_INPROGESS(409, HttpStatus.CONFLICT,""),
    LOAN_NOT_FOUND(404, HttpStatus.NOT_FOUND,""),

    MODEL_SERVER_ERR(409, HttpStatus.CONFLICT,""),

    CONFLICT(409, HttpStatus.CONFLICT,"Conflict"),
    NOT_FOUND(404, HttpStatus.NOT_FOUND, "Not Found"),
    FORBIDDEN(403, HttpStatus.FORBIDDEN, "Forbidden"),
    UNAUTHORIZED(401, HttpStatus.UNAUTHORIZED, "Unauthorized"),
    BAD_REQUEST(400, HttpStatus.BAD_REQUEST, "Bad Request");

    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;
}