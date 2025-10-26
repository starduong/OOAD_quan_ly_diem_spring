package com.example.myproject.repository;

import com.example.myproject.entity.MonHoc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface cho entity MonHoc
 * Quản lý các thao tác CRUD và truy vấn cho môn học
 */
@Repository
public interface MonHocRepository extends JpaRepository<MonHoc, String> {

    /**
     * Tìm môn học theo tên môn (chính xác)
     * 
     * @param tenMon Tên môn học
     * @return Optional<MonHoc> - Môn học nếu tồn tại
     */
    Optional<MonHoc> findByTenMon(String tenMon);

    /**
     * Tìm kiếm môn học theo từ khóa (mã môn hoặc tên môn)
     * 
     * @param keyword Từ khóa tìm kiếm
     * @param pageable Phân trang
     * @return Page<MonHoc> - Trang kết quả tìm kiếm
     */
    @Query("SELECT mh FROM MonHoc mh WHERE " +
           "LOWER(mh.maMon) LIKE %:keyword% OR " +
           "LOWER(mh.tenMon) LIKE %:keyword%")
    Page<MonHoc> search(@Param("keyword") String keyword, Pageable pageable);

    /**
     * Tìm các môn học theo số tín chỉ lý thuyết
     * 
     * @param soTietLT Số tiết lý thuyết
     * @return List<MonHoc> - Danh sách môn học
     */
    List<MonHoc> findBySoTietLT(Integer soTietLT);

    /**
     * Tìm các môn học theo số tín chỉ thực hành
     * 
     * @param soTietTH Số tiết thực hành
     * @return List<MonHoc> - Danh sách môn học
     */
    List<MonHoc> findBySoTietTH(Integer soTietTH);

    /**
     * Tìm các môn học có tổng số tiết trong khoảng
     * 
     * @param minTiet Số tiết tối thiểu
     * @param maxTiet Số tiết tối đa
     * @return List<MonHoc> - Danh sách môn học
     */
    @Query("SELECT mh FROM MonHoc mh " +
           "WHERE (mh.soTietLT + mh.soTietTH) BETWEEN :minTiet AND :maxTiet")
    List<MonHoc> findByTotalCreditsRange(@Param("minTiet") Integer minTiet, 
                                         @Param("maxTiet") Integer maxTiet);

    /**
     * Kiểm tra môn học có tồn tại theo mã môn
     * 
     * @param maMon Mã môn học
     * @return true nếu tồn tại, false nếu không
     */
    boolean existsByMaMon(String maMon);

    /**
     * Kiểm tra môn học có tồn tại theo tên môn
     * 
     * @param tenMon Tên môn học
     * @return true nếu tồn tại, false nếu không
     */
    boolean existsByTenMon(String tenMon);

    /**
     * Lấy tất cả môn học có sắp xếp theo tên môn
     * 
     * @return List<MonHoc> - Danh sách môn học đã sắp xếp
     */
    @Query("SELECT mh FROM MonHoc mh ORDER BY mh.tenMon ASC")
    List<MonHoc> findAllOrderByTenMon();

    /**
     * Tìm các môn học có hệ số điểm cụ thể
     * (Dùng để kiểm tra cấu trúc điểm đặc biệt)
     * 
     * @param heSoCC Hệ số chuyên cần
     * @param heSoBT Hệ số bài tập
     * @param heSoGK Hệ số giữa kỳ
     * @param heSoCK Hệ số cuối kỳ
     * @return List<MonHoc> - Danh sách môn học
     */
    @Query("SELECT mh FROM MonHoc mh " +
           "WHERE mh.heSoCC = :heSoCC " +
           "AND mh.heSoBT = :heSoBT " +
           "AND mh.heSoGK = :heSoGK " +
           "AND mh.heSoCK = :heSoCK")
    List<MonHoc> findByScoreStructure(@Param("heSoCC") Double heSoCC,
                                      @Param("heSoBT") Double heSoBT,
                                      @Param("heSoGK") Double heSoGK,
                                      @Param("heSoCK") Double heSoCK);

    /**
     * Tìm các môn học có cấu trúc điểm mặc định
     * (CC: 10%, BT: 20%, GK: 20%, CK: 50%)
     * 
     * @return List<MonHoc> - Danh sách môn học có cấu trúc điểm mặc định
     */
    @Query("SELECT mh FROM MonHoc mh " +
           "WHERE mh.heSoCC = 0.1 " +
           "AND mh.heSoBT = 0.2 " +
           "AND mh.heSoGK = 0.2 " +
           "AND mh.heSoCK = 0.5")
    List<MonHoc> findByDefaultScoreStructure();

    /**
     * Tìm các môn học có cấu trúc điểm không chuẩn
     * (Khác với cấu trúc mặc định)
     * 
     * @return List<MonHoc> - Danh sách môn học có cấu trúc điểm đặc biệt
     */
    @Query("SELECT mh FROM MonHoc mh " +
           "WHERE mh.heSoCC IS NULL OR mh.heSoBT IS NULL " +
           "OR mh.heSoGK IS NULL OR mh.heSoCK IS NULL " +
           "OR (mh.heSoCC <> 0.1 OR mh.heSoBT <> 0.2 " +
           "OR mh.heSoGK <> 0.2 OR mh.heSoCK <> 0.5)")
    List<MonHoc> findByCustomScoreStructure();

    /**
     * Đếm tổng số môn học trong hệ thống
     * 
     * @return Số lượng môn học
     */
    @Query("SELECT COUNT(mh) FROM MonHoc mh")
    long countAllMonHoc();

    /**
     * Lấy danh sách môn học có số tiết lý thuyết lớn hơn thực hành
     * 
     * @return List<MonHoc> - Danh sách môn học lý thuyết
     */
    @Query("SELECT mh FROM MonHoc mh WHERE mh.soTietLT > mh.soTietTH")
    List<MonHoc> findTheoryFocusedSubjects();

    /**
     * Lấy danh sách môn học có số tiết thực hành lớn hơn hoặc bằng lý thuyết
     * 
     * @return List<MonHoc> - Danh sách môn học thực hành
     */
    @Query("SELECT mh FROM MonHoc mh WHERE mh.soTietTH >= mh.soTietLT")
    List<MonHoc> findPracticeFocusedSubjects();
}
