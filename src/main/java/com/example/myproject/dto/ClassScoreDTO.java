package com.example.myproject.dto;

import com.example.myproject.entity.BangDiem;
import com.example.myproject.entity.LopTinChi;
import com.example.myproject.entity.MonHoc;
import com.example.myproject.entity.SinhVien;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO chứa dữ liệu đầy đủ của lớp tín chỉ để hiển thị form nhập điểm
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassScoreDTO {
    
    private LopTinChi lopTinChi;
    private MonHoc monHoc;
    private List<StudentScoreDTO> students;
    
    /**
     * DTO chứa thông tin sinh viên và điểm hiện tại
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudentScoreDTO {
        private SinhVien sinhVien;
        private BangDiem bangDiem;
        private Float diemTB; // Điểm trung bình đã tính
    }
}
