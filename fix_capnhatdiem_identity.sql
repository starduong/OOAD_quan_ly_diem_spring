-- Fix CapNhatDiem table to add IDENTITY to IdCapNhatDiem column
-- This script will:
-- 1. Drop foreign key constraints
-- 2. Drop the old table
-- 3. Recreate table with IDENTITY column
-- 4. Restore foreign key constraints

USE [QUAN_LY_DIEM]
GO

PRINT 'Starting CapNhatDiem table fix...'
GO

-- Step 1: Drop foreign key constraint from DonPhucKhao table
IF EXISTS (SELECT * FROM sys.foreign_keys WHERE name = 'FK_DonPhucKhao_CapNhatDiem')
BEGIN
    PRINT 'Dropping FK_DonPhucKhao_CapNhatDiem...'
    ALTER TABLE [dbo].[DonPhucKhao] DROP CONSTRAINT [FK_DonPhucKhao_CapNhatDiem]
END
GO

-- Step 2: Drop the CapNhatDiem table
IF EXISTS (SELECT * FROM sys.tables WHERE name = 'CapNhatDiem')
BEGIN
    PRINT 'Dropping old CapNhatDiem table...'
    DROP TABLE [dbo].[CapNhatDiem]
END
GO

-- Step 3: Recreate CapNhatDiem table with IDENTITY column
PRINT 'Creating new CapNhatDiem table with IDENTITY...'
CREATE TABLE [dbo].[CapNhatDiem](
	[IdCapNhatDiem] [int] IDENTITY(1,1) NOT NULL,  -- *** CHANGED TO IDENTITY ***
	[MaNV] [varchar](10) NULL,
	[IdBangDiem] [int] NULL,
	[NgayNhap] [datetime] NULL,
	[DiemThi] [float] NULL,
 CONSTRAINT [PK_CapNhatDiem] PRIMARY KEY CLUSTERED 
(
	[IdCapNhatDiem] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
 CONSTRAINT [UK_BangDiem_NV] UNIQUE NONCLUSTERED 
(
	[MaNV] ASC,
	[IdBangDiem] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

-- Step 4: Add back foreign key constraints
PRINT 'Adding FK_CapNhatDiem_BangDiem...'
ALTER TABLE [dbo].[CapNhatDiem]  WITH CHECK ADD  CONSTRAINT [FK_CapNhatDiem_BangDiem] FOREIGN KEY([IdBangDiem])
REFERENCES [dbo].[BangDiem] ([IdBangDiem])
GO

ALTER TABLE [dbo].[CapNhatDiem] CHECK CONSTRAINT [FK_CapNhatDiem_BangDiem]
GO

PRINT 'Adding FK_CapNhatDiem_NhanVienPKT...'
ALTER TABLE [dbo].[CapNhatDiem]  WITH CHECK ADD  CONSTRAINT [FK_CapNhatDiem_NhanVienPKT] FOREIGN KEY([MaNV])
REFERENCES [dbo].[NhanVienPKT] ([MaNV])
GO

ALTER TABLE [dbo].[CapNhatDiem] CHECK CONSTRAINT [FK_CapNhatDiem_NhanVienPKT]
GO

-- Step 5: Restore foreign key from DonPhucKhao
PRINT 'Adding FK_DonPhucKhao_CapNhatDiem...'
ALTER TABLE [dbo].[DonPhucKhao]  WITH CHECK ADD  CONSTRAINT [FK_DonPhucKhao_CapNhatDiem] FOREIGN KEY([IdCapNhatDiem])
REFERENCES [dbo].[CapNhatDiem] ([IdCapNhatDiem])
GO

ALTER TABLE [dbo].[DonPhucKhao] CHECK CONSTRAINT [FK_DonPhucKhao_CapNhatDiem]
GO

PRINT 'CapNhatDiem table fix completed successfully!'
PRINT 'IdCapNhatDiem is now an IDENTITY column with auto-increment.'
GO

-- Verify the change
SELECT 
    c.name AS ColumnName,
    t.name AS DataType,
    c.is_identity AS IsIdentity,
    IDENT_SEED(OBJECT_NAME(c.object_id)) AS IdentitySeed,
    IDENT_INCR(OBJECT_NAME(c.object_id)) AS IdentityIncrement
FROM sys.columns c
JOIN sys.types t ON c.user_type_id = t.user_type_id
WHERE c.object_id = OBJECT_ID('CapNhatDiem') 
AND c.name = 'IdCapNhatDiem'
GO
