package com.example.myproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import com.example.myproject.compositeKey.DonPhucKhaoId;

@Getter
@Setter
@Entity
@Table(name = "DonPhucKhao")
public class DonPhucKhao {
    @EmbeddedId
    private DonPhucKhaoId id;

    @Column(name = "LyDo", columnDefinition = "ntext")
    private String lyDo;

    @Column(name = "NgayGui")
    private LocalDateTime ngayGui;

    @Column(name = "TrangThai")
    private Boolean trangThai;

    @Column(name = "MaNV", length = 10)
    private String maNV;

    @Column(name = "NgayDuyet")
    private LocalDateTime ngayDuyet;

    @ManyToOne
    @JoinColumn(name = "MaSV", referencedColumnName = "MaSV", insertable = false, updatable = false)
    private SinhVien sinhVien;

    @ManyToOne
    @JoinColumn(name = "IdCapNhatDiem", referencedColumnName = "IdCapNhatDiem", insertable = false, updatable = false)
    private CapNhatDiem capNhatDiem;

    @ManyToOne
    @JoinColumn(name = "MaNV", referencedColumnName = "MaNV", insertable = false, updatable = false)
    private NhanVienPKT nhanVienPKT;
}
