package com.example.myproject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.myproject.entity.TaiKhoan;
import com.example.myproject.repository.TaiKhoanRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    // Các repository chi tiết người dùng đã được loại bỏ khi thu gọn hệ thống

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TaiKhoan taiKhoan = taiKhoanRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Thu gọn: không nạp thông tin chi tiết người dùng
        return new CustomUserDetails(taiKhoan, null);
    }
}
