package com.fqrmix.authcenterback.dto.response.api;

import com.fqrmix.authcenterback.models.ErrorObject;

import java.util.List;

public interface ErrorResponse extends ApiResponse{
    List<ErrorObject> getErrors();
}
