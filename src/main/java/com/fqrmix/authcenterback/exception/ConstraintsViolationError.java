package com.fqrmix.authcenterback.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConstraintsViolationError implements ErrorMessage {
    private String parameter;
    private String description;
}
