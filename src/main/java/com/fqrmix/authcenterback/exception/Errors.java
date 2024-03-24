package com.fqrmix.authcenterback.exception;

import lombok.Getter;

@Getter
public enum Errors {
    BAD_CREDENTIALS("BadCredentials", "Неверный логин или пароль"),
    INTERNAL_SERVER_ERROR("InternalServerError", "Внутренняя ошибка сервера"),
    USER_ALREADY_EXISTS("UserAlreadyExists", "Такой пользователь уже существует");

    private final String error;
    private final String description;

    Errors(String error, String description) {
        this.error = error;
        this.description = description;
    }
}
