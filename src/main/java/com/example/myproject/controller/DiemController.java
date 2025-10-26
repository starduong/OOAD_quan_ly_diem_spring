package com.example.myproject.controller;

import com.example.myproject.dto.ClassScoreDTO;
import com.example.myproject.dto.ScoreUpdateDTO;
import com.example.myproject.dto.UpdateResultDTO;
import com.example.myproject.service.CapNhatDiemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller xử lý chức năng nhập điểm cho giảng viên
 */
@Controller
@RequestMapping("/giang-vien")
@RequiredArgsConstructor
public class DiemController {

    private final CapNhatDiemService capNhatDiemService;

    /**
     * Hiển thị form nhập điểm cho lớp tín chỉ
     * GET /giang-vien/nhap-diem/{maLopTC}
     */
    @GetMapping("/nhap-diem/{maLopTC}")
    public String showScoreInputPage(@PathVariable String maLopTC, Model model) {
        try {
            // Load dữ liệu lớp tín chỉ
            ClassScoreDTO classData = capNhatDiemService.getClassScoreData(maLopTC);
            
            model.addAttribute("classData", classData);
            model.addAttribute("scoreUpdate", new ScoreUpdateDTO());
            
            return "gv/nhapDiem"; // Template: templates/gv/nhapDiem.html
            
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi: " + e.getMessage());
            return "error";
        }
    }

    /**
     * Xử lý cập nhật điểm
     * POST /giang-vien/update-score
     */
    @PostMapping("/update-score")
    public String updateScore(
            @Valid @ModelAttribute ScoreUpdateDTO scoreUpdate,
            BindingResult bindingResult,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {
        
        // Validate dữ liệu
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", 
                    "Điểm không hợp lệ! Điểm phải từ 0 đến 10.");
            return "redirect:/giang-vien/nhap-diem/" + scoreUpdate.getMaLopTC();
        }

        try {
            // Lấy mã nhân viên từ authentication (nếu có)
            String maNV = authentication != null ? authentication.getName() : null;
            
            // Cập nhật điểm
            UpdateResultDTO result = capNhatDiemService.updateScore(scoreUpdate, maNV);
            
            if (result.isSuccess()) {
                redirectAttributes.addFlashAttribute("success", result.getMessage());
                redirectAttributes.addFlashAttribute("finalScore", result.getFinalScore());
            } else {
                redirectAttributes.addFlashAttribute("error", result.getMessage());
            }
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        
        return "redirect:/giang-vien/nhap-diem/" + scoreUpdate.getMaLopTC();
    }

    /**
     * AJAX endpoint - Validate điểm trước khi submit
     * POST /giang-vien/validate-score
     */
    @PostMapping("/validate-score")
    @ResponseBody
    public UpdateResultDTO validateScore(@RequestBody ScoreUpdateDTO scoreUpdate) {
        // Validate điểm từ 0-10
        if (!isValidScore(scoreUpdate.getDiemCC()) ||
            !isValidScore(scoreUpdate.getDiemBT()) ||
            !isValidScore(scoreUpdate.getDiemGK()) ||
            !isValidScore(scoreUpdate.getDiemThi())) {
            
            return new UpdateResultDTO(false, null, "Điểm phải từ 0 đến 10");
        }
        
        return new UpdateResultDTO(true, null, "Điểm hợp lệ");
    }

    /**
     * Helper method: Kiểm tra điểm hợp lệ
     */
    private boolean isValidScore(Float diem) {
        return diem != null && diem >= 0.0f && diem <= 10.0f;
    }
}
