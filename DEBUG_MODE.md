# 🔓 Bypass Login for Debugging

## ✅ Đã cấu hình xong!

Có 2 security config:
- **SecurityConfig.java** - Dùng cho PRODUCTION (có authentication)
- **DevSecurityConfig.java** - Dùng cho DEVELOPMENT (bypass authentication)
- **TestController.java** - Test endpoints (chỉ active trong dev mode)

## 🚀 Cách sử dụng NHANH NHẤT:

### ⭐ Option 1: Dùng Batch File (KHUYẾN NGHỊ)

**Mở Command Prompt (cmd) và chạy:**
```cmd
cd F:\HK1N3\ISAAD\OOAD_quan_ly_diem_spring
run-dev.cmd
```

**Hoặc từ PowerShell:**
```powershell
cd F:\HK1N3\ISAAD\OOAD_quan_ly_diem_spring
.\run-dev.cmd
```

### Option 2: Manual Commands (PowerShell)

Copy và paste 3 dòng sau (PHẢI chạy cả 3 dòng cùng lúc):

```powershell
$env:JAVA_HOME="C:\jdk-17"; $env:PATH="$env:JAVA_HOME\bin;$env:PATH"
cd F:\HK1N3\ISAAD\OOAD_quan_ly_diem_spring
mvn spring-boot:run "-Dspring-boot.run.profiles=dev"
```

### Đợi cho đến khi thấy:
```
⚠️⚠️⚠️ WARNING: RUNNING IN DEV MODE - AUTHENTICATION DISABLED! ⚠️⚠️⚠️
Started MyprojectApplication in X seconds
```

### Mở browser và truy cập:
- 🌐 http://localhost:8080/ (Test page with links)
- 🌐 http://localhost:8080/admin/
- 🌐 http://localhost:8080/client/sv/
- 🌐 http://localhost:8080/client/gv/

**✅ KHÔNG CẦN LOGIN - Truy cập trực tiếp!**

---

## 📋 Chi tiết các options khác:

### Option 1: Command Line (Maven)
```powershell
# Set JAVA_HOME trước (nếu chưa set)
$env:JAVA_HOME="C:\jdk-17"
$env:PATH="$env:JAVA_HOME\bin;$env:PATH"

# Chạy với dev profile
cd F:\HK1N3\ISAAD\OOAD_quan_ly_diem_spring
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Option 2: IntelliJ IDEA
1. **Run > Edit Configurations...**
2. Chọn Spring Boot application
3. **Environment** tab
4. **VM options**: `-Dspring.profiles.active=dev`
5. Click **OK** và Run

### Option 3: Eclipse
1. **Run > Run Configurations...**
2. Chọn Spring Boot App
3. **Arguments** tab
4. **VM arguments**: `-Dspring.profiles.active=dev`
5. **Apply** và Run

### Option 4: Thêm vào application.properties (TẠM THỜI)
```properties
# Thêm dòng này vào application.properties (NHỚ XÓA SAU KHI DEBUG XONG!)
spring.profiles.active=dev
```

## 🧪 Test sau khi chạy:

Khi ứng dụng chạy với dev profile, bạn sẽ thấy:
```
⚠️ WARNING: RUNNING IN DEV MODE - AUTHENTICATION DISABLED! ⚠️
```

Giờ bạn có thể:
- ✅ Truy cập `/admin/**` mà không cần login
- ✅ Truy cập `/client/sv/**` mà không cần login
- ✅ Truy cập `/client/gv/**` mà không cần login
- ✅ Tất cả endpoints đều accessible

## 🔒 Chuyển về Production mode:

```powershell
# Không thêm profile hoặc dùng profile khác
mvn spring-boot:run

# Hoặc xóa dòng spring.profiles.active=dev trong application.properties
```

## 📝 Lưu ý:

1. **KHÔNG COMMIT** code với `spring.profiles.active=dev` trong `application.properties`
2. Chỉ dùng dev mode khi debug local
3. Production phải dùng security đầy đủ
4. Dev mode sẽ disable CSRF, Session, và tất cả authentication

## 🛠️ Troubleshooting:

Nếu vẫn bị redirect về login:
```powershell
# Clear target và rebuild
mvn clean compile
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

Kiểm tra log console phải có dòng:
```
⚠️⚠️⚠️ WARNING: RUNNING IN DEV MODE - AUTHENTICATION DISABLED! ⚠️⚠️⚠️
```
