package com.example.myproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "CapNhatDiem")
public class CapNhatDiem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdCapNhatDiem")
    private int idCapNhatDiem;

    @Column(name = "MaNV", length = 10)
    private String maNV;

    @Column(name = "IdBangDiem")
    private int idBangDiem;

    @Column(name = "NgayNhap")
    private LocalDateTime ngayNhap;

    @Column(name = "DiemThi")
    private Double diemThi;

    @ManyToOne
    @JoinColumn(name = "MaNV", referencedColumnName = "MaNV", insertable = false, updatable = false)
    private NhanVienPKT nhanVienPKT;

    @ManyToOne
    @JoinColumn(name = "IdBangDiem", referencedColumnName = "IdBangDiem", insertable = false, updatable = false)
    private BangDiem bangDiem;
}
