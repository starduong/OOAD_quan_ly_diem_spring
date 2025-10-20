package com.example.myproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "BangDiem")
public class BangDiem {
    @Id
    @Column(name = "IdBangDiem")
    private int idBangDiem;

    @Column(name = "MaSV", nullable = false, length = 10)
    private String maSV;

    @Column(name = "MaLopTC", nullable = false, length = 10)
    private String maLopTC;

    @Column(name = "DiemCC")
    private Double diemCC;

    @Column(name = "DiemBT")
    private Double diemBT;

    @Column(name = "DiemGK")
    private Double diemGK;

    @ManyToOne
    @JoinColumn(name = "MaSV", referencedColumnName = "MaSV", insertable = false, updatable = false)
    private SinhVien sinhVien;

    @ManyToOne
    @JoinColumn(name = "MaLopTC", referencedColumnName = "MaLopTC", insertable = false, updatable = false)
    private LopTinChi lopTinChi;
}
