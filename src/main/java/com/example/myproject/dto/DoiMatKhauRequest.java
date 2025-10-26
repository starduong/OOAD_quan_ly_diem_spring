package com.example.myproject.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoiMatKhauRequest {
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}
