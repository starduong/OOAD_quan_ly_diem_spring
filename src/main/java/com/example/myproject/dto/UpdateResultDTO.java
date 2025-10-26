package com.example.myproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO để trả về kết quả sau khi cập nhật điểm
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateResultDTO {
    private boolean success;
    private Float finalScore; // Điểm trung bình cuối cùng
    private String message;
}
