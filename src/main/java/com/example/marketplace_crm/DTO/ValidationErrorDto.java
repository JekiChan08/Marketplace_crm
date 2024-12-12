package com.example.marketplace_crm.DTO;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ValidationErrorDto {
    private List<Error> errors;

    @Builder
    @Data
    public static class Error {
        String field;
        String message;
    }
}