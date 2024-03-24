package com.fqrmix.authcenterback.dto.response.api;

import java.util.List;

public interface IErrorResponse extends ApiResponse{
    List<ErrorObject> getErrors();
}
