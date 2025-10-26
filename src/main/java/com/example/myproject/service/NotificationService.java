package com.example.myproject.service;

import com.example.myproject.entity.MonHoc;
import com.example.myproject.entity.SinhVien;
import com.example.myproject.repository.MonHocRepository;
import com.example.myproject.repository.SinhVienRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service xử lý gửi thông báo email
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final SinhVienRepository sinhVienRepo;
    private final MonHocRepository monHocRepo;

    /**
     * Gửi thông báo điểm cho sinh viên
     */
    public void sendScoreNotification(String maSV, String maMon, Float diemTB) {
        try {
            // 1. Lấy thông tin sinh viên
            SinhVien sinhVien = sinhVienRepo.findById(maSV)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên"));

            // 2. Lấy thông tin môn học
            MonHoc monHoc = monHocRepo.findById(maMon)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy môn học"));

            // 3. Build nội dung email
            String email = sinhVien.getEmail();
            String subject = "Thông báo điểm môn học - " + monHoc.getTenMon();
            String body = buildEmailContent(sinhVien, monHoc, diemTB);

            // 4. Gửi email (mock - trong thực tế dùng JavaMailSender)
            log.info("📧 Sending email to: {}", email);
            log.info("Subject: {}", subject);
            log.info("Body:\n{}", body);

            // TODO: Implement actual email sending
            // mailSender.send(new SimpleMailMessage()...)

        } catch (Exception e) {
            log.error("Error sending score notification", e);
        }
    }

    /**
     * Xây dựng nội dung email
     */
    private String buildEmailContent(SinhVien sinhVien, MonHoc monHoc, Float diemTB) {
        return String.format("""
                Kính gửi sinh viên %s %s,
                
                Bộ môn thông báo điểm môn học:
                
                📚 Môn học: %s (%s)
                📊 Điểm trung bình: %.2f
                
                Chi tiết điểm:
                - Điểm chuyên cần (CC): Đã cập nhật
                - Điểm bài tập (BT): Đã cập nhật
                - Điểm giữa kỳ (GK): Đã cập nhật
                - Điểm thi cuối kỳ (CK): Đã cập nhật
                
                Vui lòng đăng nhập hệ thống để xem chi tiết điểm thành phần.
                
                Trân trọng,
                Phòng Đào tạo
                """,
                sinhVien.getHo(),
                sinhVien.getTen(),
                monHoc.getTenMon(),
                monHoc.getMaMon(),
                diemTB
        );
    }
}
