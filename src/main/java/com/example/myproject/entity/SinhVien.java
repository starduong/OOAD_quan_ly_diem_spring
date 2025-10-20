package com.example.myproject.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "SinhVien")
public class SinhVien {
    @Id
    @Column(name = "MaSV", length = 10)
    private String maSV;

    @Column(name = "Ho", nullable = false, length = 50)
    private String ho;

    @Column(name = "Ten", nullable = false, length = 50)
    private String ten;

    @Column(name = "GioiTinh")
    private Boolean gioiTinh;

    @Column(name = "SoDT", length = 10)
    private String soDT;

    @Column(name = "DiaChi", columnDefinition = "nvarchar(max)")
    private String diaChi;

    @Column(name = "NgaySinh", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private LocalDate ngaySinh;

    @Column(name = "Email", length = 50, unique = true)
    private String email;

    @ManyToOne
    @JoinColumn(name = "MaLop", nullable = false)
    private Lop lop;

    @ManyToOne
    @JoinColumn(name = "MaTK")
    private TaiKhoan taiKhoan;

}