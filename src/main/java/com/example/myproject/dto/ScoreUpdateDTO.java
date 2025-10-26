package com.example.myproject.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO để nhận dữ liệu cập nhật điểm từ form
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreUpdateDTO {
    
    @NotNull(message = "Mã sinh viên không được để trống")
    private String maSV;
    
    @NotNull(message = "Mã lớp tín chỉ không được để trống")
    private String maLopTC;
    
    @DecimalMin(value = "0.0", message = "Điểm chuyên cần phải từ 0 đến 10")
    @DecimalMax(value = "10.0", message = "Điểm chuyên cần phải từ 0 đến 10")
    private Float diemCC;
    
    @DecimalMin(value = "0.0", message = "Điểm bài tập phải từ 0 đến 10")
    @DecimalMax(value = "10.0", message = "Điểm bài tập phải từ 0 đến 10")
    private Float diemBT;
    
    @DecimalMin(value = "0.0", message = "Điểm giữa kỳ phải từ 0 đến 10")
    @DecimalMax(value = "10.0", message = "Điểm giữa kỳ phải từ 0 đến 10")
    private Float diemGK;
    
    @DecimalMin(value = "0.0", message = "Điểm thi phải từ 0 đến 10")
    @DecimalMax(value = "10.0", message = "Điểm thi phải từ 0 đến 10")
    private Float diemThi;
    
    private String maNV; // Mã nhân viên cập nhật
}
