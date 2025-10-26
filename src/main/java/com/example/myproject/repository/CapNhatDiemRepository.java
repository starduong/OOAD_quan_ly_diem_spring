package com.example.myproject.repository;

import com.example.myproject.entity.CapNhatDiem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface cho entity CapNhatDiem
 * Quản lý lịch sử cập nhật điểm thi của sinh viên
 */
@Repository
public interface CapNhatDiemRepository extends JpaRepository<CapNhatDiem, Integer> {

    /**
     * Tìm bản ghi cập nhật điểm theo ID bảng điểm
     * (Mỗi BangDiem chỉ có 1 bản ghi CapNhatDiem mới nhất)
     * 
     * @param idBangDiem ID của bảng điểm
     * @return Optional<CapNhatDiem> - Bản ghi cập nhật điểm nếu tồn tại
     */
    Optional<CapNhatDiem> findByIdBangDiem(Integer idBangDiem);

    /**
     * Tìm tất cả lịch sử cập nhật điểm của một nhân viên
     * 
     * @param maNV Mã nhân viên phòng khảo thí
     * @return List<CapNhatDiem> - Danh sách lịch sử cập nhật
     */
    List<CapNhatDiem> findByMaNV(String maNV);

    /**
     * Tìm các bản ghi cập nhật điểm trong khoảng thời gian
     * 
     * @param startDate Ngày bắt đầu
     * @param endDate Ngày kết thúc
     * @return List<CapNhatDiem> - Danh sách cập nhật trong khoảng thời gian
     */
    List<CapNhatDiem> findByNgayNhapBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Kiểm tra xem bảng điểm đã được cập nhật điểm thi chưa
     * 
     * @param idBangDiem ID của bảng điểm
     * @return true nếu đã cập nhật, false nếu chưa
     */
    boolean existsByIdBangDiem(Integer idBangDiem);

    /**
     * Tìm bản ghi cập nhật điểm mới nhất của một bảng điểm
     * (Trường hợp có nhiều lần cập nhật, lấy lần gần nhất)
     * 
     * @param idBangDiem ID của bảng điểm
     * @return Optional<CapNhatDiem> - Bản ghi cập nhật mới nhất
     */
    @Query("SELECT cnd FROM CapNhatDiem cnd " +
           "WHERE cnd.idBangDiem = :idBangDiem " +
           "ORDER BY cnd.ngayNhap DESC")
    Optional<CapNhatDiem> findLatestByIdBangDiem(@Param("idBangDiem") Integer idBangDiem);

    /**
     * Lấy lịch sử cập nhật điểm của một lớp tín chỉ kèm thông tin chi tiết
     * 
     * @param maLopTC Mã lớp tín chỉ
     * @return List<CapNhatDiem> - Danh sách cập nhật điểm của lớp
     */
    @Query("SELECT cnd FROM CapNhatDiem cnd " +
           "JOIN FETCH cnd.bangDiem bd " +
           "JOIN FETCH bd.sinhVien sv " +
           "WHERE bd.maLopTC = :maLopTC " +
           "ORDER BY cnd.ngayNhap DESC")
    List<CapNhatDiem> findByMaLopTCWithDetails(@Param("maLopTC") String maLopTC);

    /**
     * Đếm số lượng sinh viên đã được cập nhật điểm thi trong lớp
     * 
     * @param maLopTC Mã lớp tín chỉ
     * @return Số lượng sinh viên đã có điểm thi
     */
    @Query("SELECT COUNT(cnd) FROM CapNhatDiem cnd " +
           "JOIN cnd.bangDiem bd " +
           "WHERE bd.maLopTC = :maLopTC")
    long countByMaLopTC(@Param("maLopTC") String maLopTC);

    /**
     * Tìm các bản ghi cập nhật điểm theo nhân viên và khoảng thời gian
     * 
     * @param maNV Mã nhân viên
     * @param startDate Ngày bắt đầu
     * @param endDate Ngày kết thúc
     * @return List<CapNhatDiem> - Danh sách cập nhật điểm
     */
    @Query("SELECT cnd FROM CapNhatDiem cnd " +
           "WHERE cnd.maNV = :maNV " +
           "AND cnd.ngayNhap BETWEEN :startDate AND :endDate " +
           "ORDER BY cnd.ngayNhap DESC")
    List<CapNhatDiem> findByMaNVAndDateRange(@Param("maNV") String maNV,
                                              @Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate);

    /**
     * Xóa tất cả bản ghi cập nhật điểm của một bảng điểm
     * 
     * @param idBangDiem ID của bảng điểm
     */
    void deleteByIdBangDiem(Integer idBangDiem);
}
