package com.example.myproject.repository;

import com.example.myproject.entity.BangDiem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface cho entity BangDiem
 * Quản lý các thao tác CRUD và truy vấn custom cho bảng điểm sinh viên
 */
@Repository
public interface BangDiemRepository extends JpaRepository<BangDiem, Integer> {

    /**
     * Tìm bảng điểm theo mã sinh viên và mã lớp tín chỉ
     * 
     * @param maSV Mã sinh viên
     * @param maLopTC Mã lớp tín chỉ
     * @return Optional<BangDiem> - Bảng điểm nếu tồn tại
     */
    Optional<BangDiem> findByMaSVAndMaLopTC(String maSV, String maLopTC);

    /**
     * Tìm tất cả bảng điểm của một lớp tín chỉ
     * 
     * @param maLopTC Mã lớp tín chỉ
     * @return List<BangDiem> - Danh sách bảng điểm của lớp
     */
    List<BangDiem> findByMaLopTC(String maLopTC);

    /**
     * Tìm tất cả bảng điểm của một sinh viên
     * 
     * @param maSV Mã sinh viên
     * @return List<BangDiem> - Danh sách bảng điểm của sinh viên
     */
    List<BangDiem> findByMaSV(String maSV);

    /**
     * Kiểm tra xem sinh viên đã có bảng điểm trong lớp tín chỉ chưa
     * 
     * @param maSV Mã sinh viên
     * @param maLopTC Mã lớp tín chỉ
     * @return true nếu đã tồn tại, false nếu chưa
     */
    boolean existsByMaSVAndMaLopTC(String maSV, String maLopTC);

    /**
     * Lấy danh sách bảng điểm của lớp tín chỉ kèm thông tin sinh viên và lớp
     * (Eager loading để tránh N+1 query problem)
     * 
     * @param maLopTC Mã lớp tín chỉ
     * @return List<BangDiem> - Danh sách bảng điểm với quan hệ đã load
     */
    @Query("SELECT bd FROM BangDiem bd " +
           "LEFT JOIN FETCH bd.sinhVien sv " +
           "LEFT JOIN FETCH bd.lopTinChi ltc " +
           "WHERE bd.maLopTC = :maLopTC")
    List<BangDiem> findByMaLopTCWithDetails(@Param("maLopTC") String maLopTC);

    /**
     * Tìm các bảng điểm chưa có điểm thi (DiemThi = null trong CapNhatDiem)
     * 
     * @param maLopTC Mã lớp tín chỉ
     * @return List<BangDiem> - Danh sách bảng điểm chưa có điểm thi
     */
    @Query("SELECT bd FROM BangDiem bd " +
           "WHERE bd.maLopTC = :maLopTC " +
           "AND NOT EXISTS (SELECT 1 FROM CapNhatDiem cnd WHERE cnd.idBangDiem = bd.idBangDiem)")
    List<BangDiem> findByMaLopTCWithoutFinalScore(@Param("maLopTC") String maLopTC);

    /**
     * Đếm số lượng sinh viên trong lớp tín chỉ
     * 
     * @param maLopTC Mã lớp tín chỉ
     * @return Số lượng sinh viên
     */
    long countByMaLopTC(String maLopTC);

    /**
     * Xóa tất cả bảng điểm của một lớp tín chỉ
     * (Thường dùng khi hủy lớp hoặc reset điểm)
     * 
     * @param maLopTC Mã lớp tín chỉ
     */
    void deleteByMaLopTC(String maLopTC);
}
