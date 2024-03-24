package com.fqrmix.authcenterback.dto.response.api;

public interface SuccessResponse<T> extends ApiResponse {
    T getData();
}
