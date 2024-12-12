package com.example.marketplace_crm.DTO;

import lombok.Builder;

import java.util.Date;

@Builder
public record ErrorDetail(Date timeStamp, String message, String detail) {
}
