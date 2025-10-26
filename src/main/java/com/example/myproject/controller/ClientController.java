package com.example.myproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.myproject.dto.DoiMatKhauRequest;
import com.example.myproject.entity.GiangVien;
import com.example.myproject.entity.SinhVien;
import com.example.myproject.repository.GiangVienRepository;
import com.example.myproject.repository.SinhVienRepository;
import com.example.myproject.service.TaiKhoanService;

@Controller
@RequestMapping("/client")
public class ClientController {
    @Autowired
    private SinhVienRepository sinhVienRepository;

    @Autowired
    private GiangVienRepository giangVienRepository;
    @Autowired
    private TaiKhoanService taiKhoanService ;
    @GetMapping("/public/home")
    public String home() {
        return "client/public/home";
    }
    
    @GetMapping("/public/password")
    public String password() {
        return "client/public/password";
    }
    @GetMapping("/sv/xemDiem")
    public String sv() {
        return "client/sv/xemDiem";
    }
    @GetMapping("/public/userinfo")
    public String showUserInfo(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_SINH_VIEN"))) {
            SinhVien sinhVien = sinhVienRepository.findByTaiKhoanUsername(username)
                    .orElseThrow(() -> new RuntimeException("Sinh viên không tồn tại"));
            model.addAttribute("userInfo", sinhVien);
        } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_GIANG_VIEN"))) {
            GiangVien giangVien = giangVienRepository.findByTaiKhoanUsername(username)
                    .orElseThrow(() -> new RuntimeException("Giảng viên không tồn tại"));
            model.addAttribute("userInfo", giangVien);
        }

        return "client/public/userinfo";
    }


    @GetMapping("/public/change-password")
    public String hienThiFormDoiMatKhau(Model model) {
        model.addAttribute("doiMatKhauRequest", new DoiMatKhauRequest());
        return "client/public/changePassword"; // Thymeleaf template
    }

    // Xử lý đổi mật khẩu
    @PostMapping("/public/change-password")
    public String doiMatKhau(@ModelAttribute DoiMatKhauRequest request,
                            Authentication authentication,
                            RedirectAttributes redirectAttributes) {
        String username = authentication.getName();
        try {
            taiKhoanService.doiMatKhau(username, request);
            redirectAttributes.addFlashAttribute("message", "Đổi mật khẩu thành công");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/client/public/change-password";
    }

}
