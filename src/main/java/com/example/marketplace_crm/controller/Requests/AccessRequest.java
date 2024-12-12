package com.example.marketplace_crm.controller.Requests;

import lombok.Data;

@Data
public class AccessRequest {
    private String userLogin;
    private String accessToken;
}
