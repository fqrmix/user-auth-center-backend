package com.fqrmix.authcenterback.dto.response.api;

public interface ISuccessResponse<T> extends ApiResponse {
    T getData();
}
