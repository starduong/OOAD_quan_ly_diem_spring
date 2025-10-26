-- ===========================================
-- SCRIPT INSERT SAMPLE DATA FOR UC1 TESTING
-- ===========================================
USE [QUAN_LY_DIEM]
GO

-- ===========================================
-- CLEANUP: Delete all existing data
-- ===========================================
PRINT N'🗑️ Cleaning up existing data...';

-- Delete in correct order (child tables first)
DELETE FROM DonPhucKhao;
DELETE FROM CapNhatDiem;
DELETE FROM BangDiem;
DELETE FROM LopTinChi;
DELETE FROM SinhVien;
DELETE FROM GiangVien;
DELETE FROM NhanVienPKT;
DELETE FROM Lop;
DELETE FROM MonHoc;
DELETE FROM Khoa;
DELETE FROM TaiKhoan;
DELETE FROM Quyen;

-- Reset IDENTITY columns to 1
DBCC CHECKIDENT ('Quyen', RESEED, 0);
DBCC CHECKIDENT ('TaiKhoan', RESEED, 0);

PRINT N'✅ Cleanup completed!';
PRINT N'';
GO

-- 1. Insert Quyen (Permissions) - IDENTITY column, không insert MaQuyen
INSERT INTO Quyen (TenQuyen, MoTaQuyen) VALUES
(N'Quản trị viên', N'Toàn quyền trên hệ thống'),
(N'Giảng viên', N'Nhập và quản lý điểm'),
(N'Sinh viên', N'Xem điểm cá nhân');
GO

-- 2. Insert TaiKhoan (Accounts) - IDENTITY column, không insert MaTK
-- MaQuyen: 1=Admin, 2=GV, 3=SV
INSERT INTO TaiKhoan (Username, Password, LoaiTK, MaQuyen) VALUES
('gv001', '123456', N'Giảng viên', 2),
('gv002', '123456', N'Giảng viên', 2),
('sv001', '123456', N'Sinh viên', 3),
('sv002', '123456', N'Sinh viên', 3),
('sv003', '123456', N'Sinh viên', 3);
GO

-- 3. Insert Khoa (Departments)
INSERT INTO Khoa (MaKhoa, TenKhoa) VALUES
('CNTT', N'Công nghệ thông tin'),
('KTPM', N'Kỹ thuật phần mềm');
GO

-- 4. Insert Lop (Classes) - SinhVien cần MaLop
INSERT INTO Lop (MaLop, MaKhoa) VALUES
('CNTT2021', 'CNTT'),
('KTPM2021', 'KTPM');
GO

-- 5. Insert GiangVien (Lecturers) - GioiTinh: 1=Nam, 0=Nữ (BIT)
-- MaTK sẽ là 1, 2 (auto-generated từ TaiKhoan)
INSERT INTO GiangVien (MaGV, Ho, Ten, NgaySinh, GioiTinh, Email, SoDT, HocVi, MaKhoa, MaTK) VALUES
('GV001', N'Nguyễn Văn', N'An', '1980-05-15', 1, 'nvann@university.edu.vn', '0901234567', N'Tiến sĩ', 'CNTT', 1),
('GV002', N'Trần Thị', N'Bình', '1985-08-20', 0, 'ttbinh@university.edu.vn', '0912345678', N'Thạc sĩ', 'KTPM', 2);
GO

-- 6. Insert SinhVien (Students) - GioiTinh: 1=Nam, 0=Nữ (BIT)
-- Cần MaLop (không phải MaKhoa) và DiaChi
-- MaTK sẽ là 3, 4, 5 (auto-generated từ TaiKhoan)
INSERT INTO SinhVien (MaSV, Ho, Ten, NgaySinh, GioiTinh, Email, SoDT, DiaChi, MaLop, MaTK) VALUES
('SV001', N'Lê Văn', N'Cường', '2003-01-10', 1, 'lvcuong@student.edu.vn', '0923456789', N'123 Lê Lợi, Q1, TPHCM', 'CNTT2021', 3),
('SV002', N'Phạm Thị', N'Dung', '2003-03-15', 0, 'ptdung@student.edu.vn', '0934567890', N'456 Nguyễn Huệ, Q1, TPHCM', 'CNTT2021', 4),
('SV003', N'Hoàng Văn', N'Em', '2003-05-20', 1, 'hvem@student.edu.vn', '0945678901', N'789 Trần Hưng Đạo, Q5, TPHCM', 'KTPM2021', 5);
GO

-- 7. Insert MonHoc (Subjects)
INSERT INTO MonHoc (MaMon, TenMon, SoTietLT, SoTietTH, HeSoCC, HeSoBT, HeSoGK, HeSoCK) VALUES
('MH001', N'Lập trình hướng đối tượng', 45, 30, 0.1, 0.2, 0.2, 0.5),
('MH002', N'Cơ sở dữ liệu', 45, 30, 0.1, 0.2, 0.3, 0.4),
('MH003', N'Phân tích thiết kế hệ thống', 45, 15, 0.1, 0.1, 0.3, 0.5);
GO

-- 8. Insert LopTinChi (Credit Classes) - HocKi: INT (1=HK1, 2=HK2, 3=HK3), TrangThai: BIT (1=Mở, 0=Đóng)
INSERT INTO LopTinChi (MaLopTC, MaMon, MaGV, HocKi, NgayMoLop, SoLuongToiThieu, SoLuongToiDa, TrangThai) VALUES
('LTC001', 'MH001', 'GV001', 1, '2024-09-01', 20, 50, 1),
('LTC002', 'MH002', 'GV002', 1, '2024-09-01', 20, 50, 1),
('LTC003', 'MH003', 'GV001', 1, '2024-09-01', 20, 50, 1);
GO

-- 8.5. Insert NhanVienPKT (Exam Office Staff) - Required for CapNhatDiem.MaNV foreign key
-- Note: MaTK có UNIQUE constraint, chỉ được 1 row NULL, nên chỉ insert 1 nhân viên
INSERT INTO NhanVienPKT (MaNV, Ho, Ten, NgaySinh, SoDT, MaTK) VALUES
('NV001', N'Phạm Văn', N'Tài', '1985-03-10', '0956789012', NULL);
GO

-- 9. Insert BangDiem (Score Sheets) - Initially NULL scores
INSERT INTO BangDiem (IdBangDiem, MaSV, MaLopTC, DiemCC, DiemBT, DiemGK) VALUES
(1, 'SV001', 'LTC001', NULL, NULL, NULL),
(2, 'SV002', 'LTC001', NULL, NULL, NULL),
(3, 'SV003', 'LTC001', NULL, NULL, NULL),
(4, 'SV001', 'LTC002', 8.0, 7.5, 8.5),
(5, 'SV002', 'LTC003', 9.0, 8.0, 9.0);
GO

-- 10. Insert CapNhatDiem (Score Updates) - DiemThi for some students
-- MaNV phải tham chiếu NhanVienPKT (NV001, NV002)
INSERT INTO CapNhatDiem (IdCapNhatDiem, MaNV, IdBangDiem, NgayNhap, DiemThi) VALUES
(1, 'NV001', 4, '2024-12-01 10:30:00', 8.0),
(2, 'NV001', 5, '2024-12-01 11:00:00', 9.5);
GO

-- ===========================================
-- VERIFICATION QUERIES
-- ===========================================

PRINT N'';
PRINT N'✅ Sample data inserted successfully!';
PRINT N'';
PRINT N'📌 Test data summary:';
PRINT N'   - 2 Giảng viên (GV001, GV002)';
PRINT N'   - 2 Nhân viên PKT (NV001, NV002)';
PRINT N'   - 3 Sinh viên (SV001, SV002, SV003)';
PRINT N'   - 3 Môn học (MH001, MH002, MH003)';
PRINT N'   - 3 Lớp tín chỉ (LTC001, LTC002, LTC003)';
PRINT N'   - 3 Students in LTC001 with NULL scores (ready for input)';
PRINT N'';

-- Check LopTinChi LTC001
SELECT 
    ltc.MaLopTC, 
    mh.TenMon, 
    CONCAT(gv.Ho, ' ', gv.Ten) AS GiangVien,
    ltc.HocKi,
    CASE WHEN ltc.TrangThai = 1 THEN N'Đang mở' ELSE N'Đã đóng' END AS TrangThai
FROM LopTinChi ltc
JOIN MonHoc mh ON ltc.MaMon = mh.MaMon
JOIN GiangVien gv ON ltc.MaGV = gv.MaGV
WHERE ltc.MaLopTC = 'LTC001';

-- Check students in LTC001
SELECT 
    bd.IdBangDiem,
    CONCAT(sv.Ho, ' ', sv.Ten) AS HoTenSV,
    sv.MaSV,
    bd.DiemCC,
    bd.DiemBT,
    bd.DiemGK
FROM BangDiem bd
JOIN SinhVien sv ON bd.MaSV = sv.MaSV
WHERE bd.MaLopTC = 'LTC001'
ORDER BY sv.MaSV;

-- Check hệ số môn học
SELECT 
    MaMon, 
    TenMon, 
    HeSoCC, 
    HeSoBT, 
    HeSoGK, 
    HeSoCK 
FROM MonHoc 
WHERE MaMon = 'MH001';

PRINT N'';
PRINT N'🌐 Test UC1 at: http://localhost:8080/giang-vien/nhap-diem/LTC001';
PRINT N'';
GO
