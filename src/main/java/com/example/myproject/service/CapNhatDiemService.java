package com.example.myproject.service;

import com.example.myproject.dto.ClassScoreDTO;
import com.example.myproject.dto.ScoreUpdateDTO;
import com.example.myproject.dto.UpdateResultDTO;
import com.example.myproject.entity.*;
import com.example.myproject.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service xử lý nghiệp vụ cập nhật điểm thi
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CapNhatDiemService {

    private final BangDiemRepository bangDiemRepo;
    private final CapNhatDiemRepository capNhatDiemRepo;
    private final LopTinChiRepository lopTinChiRepo;
    private final MonHocRepository monHocRepo;
    private final SinhVienRepository sinhVienRepo;
    private final NotificationService notificationService;

    /**
     * Lấy dữ liệu lớp tín chỉ để hiển thị form nhập điểm
     */
    public ClassScoreDTO getClassScoreData(String maLopTC) {
        log.info("Getting class score data for LopTinChi: {}", maLopTC);

        // 1. Load thông tin lớp tín chỉ
        LopTinChi lopTinChi = lopTinChiRepo.findById(maLopTC)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp tín chỉ: " + maLopTC));

        // 2. Load thông tin môn học (hệ số điểm)
        MonHoc monHoc = lopTinChi.getMonHoc();

        // 3. Load danh sách bảng điểm (bao gồm cả sinh viên)
        List<BangDiem> bangDiemList = bangDiemRepo.findByMaLopTC(maLopTC);

        // 4. Tạo DTO cho từng sinh viên
        List<ClassScoreDTO.StudentScoreDTO> students = new ArrayList<>();
        for (BangDiem bd : bangDiemList) {
            SinhVien sv = sinhVienRepo.findById(bd.getMaSV())
                    .orElse(null);
            
            if (sv != null) {
                Float diemTB = calculateFinalScore(bd, monHoc);
                students.add(new ClassScoreDTO.StudentScoreDTO(sv, bd, diemTB));
            }
        }

        return new ClassScoreDTO(lopTinChi, monHoc, students);
    }

    /**
     * Cập nhật điểm cho sinh viên
     */
    @Transactional
    public UpdateResultDTO updateScore(ScoreUpdateDTO dto, String maNV) {
        log.info("Updating score for student {} in class {}", dto.getMaSV(), dto.getMaLopTC());

        try {
            // 1. Tìm bảng điểm
            BangDiem bangDiem = bangDiemRepo.findByMaSVAndMaLopTC(dto.getMaSV(), dto.getMaLopTC())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy bảng điểm"));

            // 2. Cập nhật điểm thành phần (convert Float to Double)
            bangDiem.setDiemCC(dto.getDiemCC() != null ? dto.getDiemCC().doubleValue() : null);
            bangDiem.setDiemBT(dto.getDiemBT() != null ? dto.getDiemBT().doubleValue() : null);
            bangDiem.setDiemGK(dto.getDiemGK() != null ? dto.getDiemGK().doubleValue() : null);
            bangDiemRepo.save(bangDiem);

            // 3. Tạo bản ghi cập nhật điểm (lưu điểm thi)
            CapNhatDiem capNhatDiem = new CapNhatDiem();
            capNhatDiem.setMaNV(maNV != null ? maNV : "NV001"); // Default nếu không có
            capNhatDiem.setIdBangDiem(bangDiem.getIdBangDiem());
            capNhatDiem.setNgayNhap(LocalDateTime.now());
            capNhatDiem.setDiemThi(dto.getDiemThi() != null ? dto.getDiemThi().doubleValue() : null);
            capNhatDiemRepo.save(capNhatDiem);

            // 4. Tính điểm trung bình
            LopTinChi ltc = lopTinChiRepo.findById(dto.getMaLopTC()).orElseThrow();
            MonHoc monHoc = ltc.getMonHoc();

            Float diemTB = calculateFinalScoreWithDiemThi(
                    dto.getDiemCC(),
                    dto.getDiemBT(),
                    dto.getDiemGK(),
                    dto.getDiemThi(),
                    monHoc
            );

            // 5. Gửi thông báo email
            if (diemTB != null) {
                notificationService.sendScoreNotification(
                        dto.getMaSV(),
                        monHoc.getMaMon(),
                        diemTB
                );
            }

            log.info("Score updated successfully. Final score: {}", diemTB);
            return new UpdateResultDTO(true, diemTB, "Cập nhật điểm thành công!");

        } catch (Exception e) {
            log.error("Error updating score", e);
            return new UpdateResultDTO(false, null, "Lỗi: " + e.getMessage());
        }
    }

    /**
     * Tính điểm trung bình từ BangDiem
     */
    private Float calculateFinalScore(BangDiem bangDiem, MonHoc monHoc) {
        if (bangDiem.getDiemCC() == null || bangDiem.getDiemBT() == null ||
                bangDiem.getDiemGK() == null) {
            return null; // Chưa đủ điểm
        }

        // Tìm điểm thi từ CapNhatDiem (convert Double to Float)
        Double diemThiDouble = capNhatDiemRepo.findByIdBangDiem(bangDiem.getIdBangDiem())
                .stream()
                .findFirst()
                .map(CapNhatDiem::getDiemThi)
                .orElse(null);

        if (diemThiDouble == null) {
            return null;
        }

        // Convert Double to Float
        Float diemThi = diemThiDouble.floatValue();

        return calculateFinalScoreWithDiemThi(
                bangDiem.getDiemCC().floatValue(),
                bangDiem.getDiemBT().floatValue(),
                bangDiem.getDiemGK().floatValue(),
                diemThi,
                monHoc
        );
    }

    /**
     * Tính điểm trung bình với công thức hệ số
     */
    private Float calculateFinalScoreWithDiemThi(Float diemCC, Float diemBT, Float diemGK,
                                                   Float diemThi, MonHoc monHoc) {
        if (diemCC == null || diemBT == null || diemGK == null || diemThi == null) {
            return null;
        }

        // Convert Double to Float cho các hệ số
        Float heSoCC = monHoc.getHeSoCC() != null ? monHoc.getHeSoCC().floatValue() : 0.1f;
        Float heSoBT = monHoc.getHeSoBT() != null ? monHoc.getHeSoBT().floatValue() : 0.2f;
        Float heSoGK = monHoc.getHeSoGK() != null ? monHoc.getHeSoGK().floatValue() : 0.2f;
        Float heSoCK = monHoc.getHeSoCK() != null ? monHoc.getHeSoCK().floatValue() : 0.5f;

        // DiemTB = DiemCC * HeSoCC + DiemBT * HeSoBT + DiemGK * HeSoGK + DiemThi * HeSoCK
        float result = (diemCC * heSoCC) + (diemBT * heSoBT) + (diemGK * heSoGK) + (diemThi * heSoCK);

        // Làm tròn 2 chữ số thập phân
        return Math.round(result * 100.0f) / 100.0f;
    }
}
