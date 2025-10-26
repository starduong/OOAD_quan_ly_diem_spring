package com.example.myproject.repository;

import com.example.myproject.entity.LopTinChi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface cho entity LopTinChi
 * Quản lý các thao tác CRUD và truy vấn cho lớp tín chỉ
 */
@Repository
public interface LopTinChiRepository extends JpaRepository<LopTinChi, String> {

    /**
     * Tìm lớp tín chỉ kèm thông tin môn học và giảng viên
     * (Eager loading để tránh lazy loading exception)
     * 
     * @param maLopTC Mã lớp tín chỉ
     * @return Optional<LopTinChi> - Lớp tín chỉ với quan hệ đã load
     */
    @Query("SELECT ltc FROM LopTinChi ltc " +
           "LEFT JOIN FETCH ltc.monHoc mh " +
           "LEFT JOIN FETCH ltc.giangVien gv " +
           "WHERE ltc.maLopTC = :maLopTC")
    Optional<LopTinChi> findByIdWithDetails(@Param("maLopTC") String maLopTC);

    /**
     * Tìm tất cả lớp tín chỉ của một giảng viên
     * 
     * @param maGV Mã giảng viên
     * @return List<LopTinChi> - Danh sách lớp tín chỉ
     */
    @Query("SELECT ltc FROM LopTinChi ltc " +
           "JOIN FETCH ltc.monHoc mh " +
           "WHERE ltc.giangVien.maGV = :maGV")
    List<LopTinChi> findByGiangVienMaGV(@Param("maGV") String maGV);

    /**
     * Tìm tất cả lớp tín chỉ của một môn học
     * 
     * @param maMon Mã môn học
     * @return List<LopTinChi> - Danh sách lớp tín chỉ
     */
    List<LopTinChi> findByMonHocMaMon(String maMon);

    /**
     * Tìm các lớp tín chỉ theo học kỳ
     * 
     * @param hocKi Học kỳ (1, 2, 3)
     * @return List<LopTinChi> - Danh sách lớp tín chỉ trong học kỳ
     */
    @Query("SELECT ltc FROM LopTinChi ltc " +
           "JOIN FETCH ltc.monHoc mh " +
           "JOIN FETCH ltc.giangVien gv " +
           "WHERE ltc.hocKi = :hocKi")
    List<LopTinChi> findByHocKi(@Param("hocKi") Integer hocKi);

    /**
     * Tìm các lớp tín chỉ đang mở (trangThai = true)
     * 
     * @return List<LopTinChi> - Danh sách lớp đang mở
     */
    @Query("SELECT ltc FROM LopTinChi ltc " +
           "JOIN FETCH ltc.monHoc mh " +
           "JOIN FETCH ltc.giangVien gv " +
           "WHERE ltc.trangThai = true")
    List<LopTinChi> findOpenClasses();

    /**
     * Tìm các lớp tín chỉ theo giảng viên và học kỳ
     * 
     * @param maGV Mã giảng viên
     * @param hocKi Học kỳ
     * @return List<LopTinChi> - Danh sách lớp tín chỉ
     */
    @Query("SELECT ltc FROM LopTinChi ltc " +
           "JOIN FETCH ltc.monHoc mh " +
           "WHERE ltc.giangVien.maGV = :maGV AND ltc.hocKi = :hocKi")
    List<LopTinChi> findByGiangVienAndHocKi(@Param("maGV") String maGV, 
                                            @Param("hocKi") Integer hocKi);

    /**
     * Tìm các lớp tín chỉ được mở trong khoảng thời gian
     * 
     * @param startDate Ngày bắt đầu
     * @param endDate Ngày kết thúc
     * @return List<LopTinChi> - Danh sách lớp tín chỉ
     */
    List<LopTinChi> findByNgayMoLopBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Tìm kiếm lớp tín chỉ theo từ khóa (mã lớp, tên môn, tên giảng viên)
     * 
     * @param keyword Từ khóa tìm kiếm
     * @param pageable Phân trang
     * @return Page<LopTinChi> - Trang kết quả tìm kiếm
     */
    @Query("SELECT ltc FROM LopTinChi ltc " +
           "JOIN ltc.monHoc mh " +
           "JOIN ltc.giangVien gv " +
           "WHERE LOWER(ltc.maLopTC) LIKE %:keyword% OR " +
           "LOWER(mh.tenMon) LIKE %:keyword% OR " +
           "LOWER(gv.ho) LIKE %:keyword% OR " +
           "LOWER(gv.ten) LIKE %:keyword%")
    Page<LopTinChi> search(@Param("keyword") String keyword, Pageable pageable);

    /**
     * Đếm số lượng lớp tín chỉ của một giảng viên trong học kỳ
     * 
     * @param maGV Mã giảng viên
     * @param hocKi Học kỳ
     * @return Số lượng lớp
     */
    long countByGiangVienMaGVAndHocKi(String maGV, Integer hocKi);

    /**
     * Kiểm tra xem lớp tín chỉ có đang mở không
     * 
     * @param maLopTC Mã lớp tín chỉ
     * @return true nếu đang mở, false nếu đã đóng
     */
    @Query("SELECT CASE WHEN COUNT(ltc) > 0 THEN true ELSE false END " +
           "FROM LopTinChi ltc " +
           "WHERE ltc.maLopTC = :maLopTC AND ltc.trangThai = true")
    boolean isClassOpen(@Param("maLopTC") String maLopTC);

    /**
     * Lấy danh sách lớp tín chỉ theo môn học và học kỳ
     * 
     * @param maMon Mã môn học
     * @param hocKi Học kỳ
     * @return List<LopTinChi> - Danh sách lớp tín chỉ
     */
    @Query("SELECT ltc FROM LopTinChi ltc " +
           "JOIN FETCH ltc.monHoc mh " +
           "JOIN FETCH ltc.giangVien gv " +
           "WHERE ltc.monHoc.maMon = :maMon AND ltc.hocKi = :hocKi")
    List<LopTinChi> findByMonHocAndHocKi(@Param("maMon") String maMon, 
                                         @Param("hocKi") Integer hocKi);
}
