package com.fqrmix.authcenterback.dto.response.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "with")
public class ErrorObject {
    String error;
    String description;
}
