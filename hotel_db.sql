-- ============================================================
--  DATABASE : hotel_db   (REVISI)
--  Aplikasi : Hotel Booking System (Java Swing + MySQL/XAMPP)
-- ============================================================

-- Hapus database lama jika ada, lalu buat ulang (aman untuk pengembangan)
DROP DATABASE IF EXISTS hotel_db;
CREATE DATABASE hotel_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci;

USE hotel_db;

-- ============================================================
--  TABEL 1 : users
--  Menyimpan akun pengguna (admin & user biasa)
-- ============================================================
CREATE TABLE users (
    user_id     INT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)             NOT NULL UNIQUE,
    password    VARCHAR(255)            NOT NULL,   -- cukup untuk hash BCrypt (~60 char)
    role        ENUM('admin','user')    NOT NULL DEFAULT 'user',
    no_telepon  VARCHAR(20)
) ENGINE=InnoDB;

-- ============================================================
--  TABEL 2 : hotels
--  Menyimpan data hotel yang bisa dipesan
-- ============================================================
CREATE TABLE hotels (
    hotel_id    INT AUTO_INCREMENT PRIMARY KEY,
    nama        VARCHAR(150)    NOT NULL,
    lokasi      VARCHAR(150)    NOT NULL,
    deskripsi   TEXT,
    harga       DECIMAL(12,2)   NOT NULL,
    gambar      VARCHAR(255),

    -- (OPSIONAL) jumlah kamar tersedia untuk mencegah overbooking.
    -- Aktifkan baris di bawah bila ingin fitur stok kamar:
    -- stok_kamar  INT           NOT NULL DEFAULT 10,

    CONSTRAINT chk_hotels_harga CHECK (harga >= 0)
) ENGINE=InnoDB;

-- ============================================================
--  TABEL 3 : bookings
--  Menyimpan transaksi pemesanan kamar
-- ============================================================
CREATE TABLE bookings (
    booking_id      INT             AUTO_INCREMENT PRIMARY KEY,
    user_id         INT             NOT NULL,
    hotel_id        INT             NOT NULL,
    check_in        DATE            NOT NULL,
    check_out       DATE            NOT NULL,
    jumlah_kamar    INT             NOT NULL DEFAULT 1,
    total_bayar     DECIMAL(12,2)   NOT NULL,
    status          ENUM('Belum Bayar','Sudah Bayar','Dibatalkan')
                                    NOT NULL DEFAULT 'Belum Bayar',
    tanggal_pesan   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_bookings_user
        FOREIGN KEY (user_id)  REFERENCES users(user_id)
        ON UPDATE CASCADE ON DELETE CASCADE,

    CONSTRAINT fk_bookings_hotel
        FOREIGN KEY (hotel_id) REFERENCES hotels(hotel_id)
        ON UPDATE CASCADE ON DELETE CASCADE,

    -- Penjaga integritas data (aman & non-breaking):
    CONSTRAINT chk_bookings_tanggal CHECK (check_out > check_in),
    CONSTRAINT chk_bookings_kamar   CHECK (jumlah_kamar > 0),
    CONSTRAINT chk_bookings_total   CHECK (total_bayar >= 0)
) ENGINE=InnoDB;

-- ============================================================
--  TABEL 4 : payments
--  Menyimpan detail pembayaran dari sebuah booking
-- ============================================================
CREATE TABLE payments (
    payment_id      INT             AUTO_INCREMENT PRIMARY KEY,
    booking_id      INT             NOT NULL,
    uang_bayar      DECIMAL(12,2)   NOT NULL,
    kembalian       DECIMAL(12,2)   NOT NULL DEFAULT 0,
    tanggal_bayar   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_payments_booking
        FOREIGN KEY (booking_id) REFERENCES bookings(booking_id)
        ON UPDATE CASCADE ON DELETE CASCADE,

    -- (OPSIONAL) 1 booking = 1 pembayaran. Aktifkan baris di bawah
    -- HANYA bila Anda TIDAK mengizinkan pembayaran bertahap/cicilan:
    -- CONSTRAINT uq_payments_booking UNIQUE (booking_id),

    CONSTRAINT chk_payments_uang     CHECK (uang_bayar >= 0),
    CONSTRAINT chk_payments_kembali  CHECK (kembalian  >= 0)
) ENGINE=InnoDB;

-- ============================================================
--  DATA AWAL : Admin default
--  username : admin
--  password : admin123
-- ============================================================
INSERT INTO users (username, password, role, no_telepon)
VALUES ('admin', 'admin123', 'admin', '08123456789');

-- ============================================================
--  DATA DUMMY : 5 hotel
--  (INSERT memakai daftar kolom eksplisit, sehingga tetap jalan
--   walau kolom opsional 'stok_kamar' nanti diaktifkan)
-- ============================================================
INSERT INTO hotels (nama, lokasi, deskripsi, harga, gambar) VALUES
('Grand Surya Hotel', 'Jakarta',
 'Hotel bintang 4 di pusat kota dengan kolam renang dan rooftop bar.',
 850000.00,  'images/hotel1.jpg'),

('Bali Paradise Resort', 'Bali',
 'Resort tepi pantai dengan pemandangan sunset dan private villa.',
 1500000.00, 'images/hotel2.jpg'),

('Malioboro Heritage Inn', 'Yogyakarta',
 'Penginapan bernuansa klasik, dekat Malioboro dan Keraton.',
 450000.00,  'images/hotel3.jpg'),

('Bromo Mountain Lodge', 'Probolinggo',
 'Lodge pegunungan dengan udara sejuk dan akses mudah ke Bromo.',
 600000.00,  'images/hotel4.jpg'),

('Lombok Sunset Villa', 'Lombok',
 'Villa tepi pantai dengan suasana tenang cocok untuk bulan madu.',
 1200000.00, 'images/hotel5.jpg');