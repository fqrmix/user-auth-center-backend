package com.fqrmix.authcenterback.models.enums;

import lombok.Getter;

@Getter
public enum ErrorsConstants {
    BAD_CREDENTIALS("BadCredentials", "Неверный логин или пароль"),
    INTERNAL_SERVER_ERROR("InternalServerError", "Внутренняя ошибка сервера"),
    USER_ALREADY_EXISTS("UserAlreadyExists", "Такой пользователь уже существует"),
    ACCESS_DENIED("Access Denied", "Недостаточно прав для совершения операции");

    private final String error;
    private final String description;

    ErrorsConstants(String error, String description) {
        this.error = error;
        this.description = description;
    }
}
