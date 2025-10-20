package com.example.myproject.service;

import com.example.myproject.entity.TaiKhoan;
import com.example.myproject.entity.Quyen;
import com.example.myproject.repository.TaiKhoanRepository;
import com.example.myproject.repository.QuyenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

@Service
public class TaiKhoanService {

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private QuyenRepository quyenRepository;

    public Page<TaiKhoan> listAll(Pageable pageable, String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return taiKhoanRepository.searchAccounts(keyword, pageable);
        }
        return taiKhoanRepository.findAll(pageable);
    }

    public Optional<TaiKhoan> getTaiKhoanById(Integer maTK) {
        return taiKhoanRepository.findById(maTK);
    }

    @Transactional
    public TaiKhoan saveTaiKhoan(TaiKhoan taiKhoan, String rawPassword) {
        if (taiKhoan.getMaTK() == null) {
            throw new IllegalArgumentException("Mã tài khoản không được để trống.");
        }
        if (taiKhoan.getUsername() == null || taiKhoan.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên đăng nhập không được để trống.");
        }
        if (taiKhoan.getQuyen() == null || taiKhoan.getQuyen().getMaQuyen() == null) {
            throw new IllegalArgumentException("Quyền không được để trống.");
        }
        if (taiKhoan.getLoaiTK() == null || taiKhoan.getLoaiTK().trim().isEmpty()) {
            throw new IllegalArgumentException("Loại tài khoản không được để trống.");
        }

        // Kiểm tra mã TK tồn tại khi thêm mới
        boolean isNew = !taiKhoanRepository.existsById(taiKhoan.getMaTK());
        if (isNew) {
            // Nếu là tài khoản mới, mật khẩu là bắt buộc
            if (rawPassword == null || rawPassword.trim().isEmpty()) {
                throw new IllegalArgumentException("Mật khẩu không được để trống khi tạo mới tài khoản.");
            }
            taiKhoan.setPassword(rawPassword); // Lưu mật khẩu trực tiếp hoặc có thể mã hóa nếu cần
        } else {
            // Nếu là cập nhật và có cung cấp mật khẩu mới
            if (rawPassword != null && !rawPassword.trim().isEmpty()) {
                taiKhoan.setPassword(rawPassword); // Cập nhật mật khẩu mới
            } else {
                // Giữ lại mật khẩu cũ nếu không cung cấp mật khẩu mới
                TaiKhoan existingTaiKhoan = taiKhoanRepository.findById(taiKhoan.getMaTK())
                        .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại để cập nhật mật khẩu."));
                taiKhoan.setPassword(existingTaiKhoan.getPassword());
            }
        }
        return taiKhoanRepository.save(taiKhoan);
    }

    @Transactional
    public void deleteTaiKhoanById(Integer maTK) {
        if (!taiKhoanRepository.existsById(maTK)) {
            throw new RuntimeException("Tài khoản không tồn tại: " + maTK);
        }
        // Kiểm tra ràng buộc trước khi xóa (quan trọng!)
        // Lưu ý: Các repository này đã bị xóa, cần xóa phần kiểm tra ràng buộc
        taiKhoanRepository.deleteById(maTK);
    }

    public List<Quyen> getAllQuyen() {
        return quyenRepository.findAll();
    }

    public Map<String, Map<String, String>> getUserDetailsMap(Page<TaiKhoan> taiKhoanPage) {
        Map<String, Map<String, String>> userMaps = new HashMap<>();
        for (TaiKhoan tk : taiKhoanPage.getContent()) {
            Map<String, String> details = new HashMap<>();
            String hoTen = "N/A";
            String email = "Không có";

            if ("ADMIN".equalsIgnoreCase(tk.getLoaiTK())) {
                hoTen = "Quản trị viên hệ thống";
                email = "";
            } else {
                hoTen = "Người dùng hệ thống";
                email = "Chưa có thông tin";
            }
            details.put("hoTen", hoTen);
            details.put("email", email);
            userMaps.put(tk.getMaTK().toString(), details);
        }
        return userMaps;
    }

    public boolean existsById(Integer maTK) {
        return taiKhoanRepository.existsById(maTK);
    }
}