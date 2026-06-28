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
    status          ENUM('Unpaid','Paid','Cancelled')
                                    NOT NULL DEFAULT 'Unpaid',
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
 'A downtown hotel with a pool swimming and a rooftop bar.',
 850000.00,  'images/HotelCardImages/Grand Surya Hotel.png'),

('Bali Paradise Resort', 'Bali',
 'A beachfront resort with sunset views and private villas.',
 1500000.00, 'images/HotelCardImages/Bali Paradise Resort.png'),

('Malioboro Heritage Inn', 'Yogyakarta',
 'A classic-style inn, near Malioboro and the Keraton.',
 450000.00,  'images/HotelCardImages/Malioboro Heritage Inn.png'),

('Bromo Mountain Lodge', 'Probolinggo',
 'A mountain lodge with cool air and easy access to Bromo.',
 600000.00,  'images/HotelCardImages/Bromo Mountain Lodge.png'),

('Lombok Sunset Villa', 'Lombok',
 'A beachfront villa with a peaceful for a honeymoon.',
 1200000.00, 'images/HotelCardImages/Lombok Sunset Villa.png'),

 ('Hiltton Bali Resort', 'Bali',
 'A luxury resort featuring a spacious pool.',
 2800000.00, 'images/HotelCardImages/Hilton Bali Resort.png');

SELECT b.*, h.nama AS hotel_nama
FROM bookings b
JOIN hotels h ON b.hotel_id = h.hotel_id
WHERE b.user_id = 1
ORDER BY b.tanggal_pesan DESC;

SELECT * FROM payments ORDER BY tanggal_bayar DESC;

SELECT b.booking_id, h.nama AS hotel_nama, b.check_in, b.check_out,
       b.jumlah_kamar, b.total_bayar, b.status, p.tanggal_bayar
FROM bookings b
JOIN hotels h     ON b.hotel_id   = h.hotel_id
LEFT JOIN payments p ON p.booking_id = b.booking_id
WHERE b.user_id = 1
ORDER BY b.tanggal_pesan DESC;

ALTER TABLE bookings
    MODIFY status ENUM('Belum Bayar','Sudah Bayar','Dibatalkan',
                       'Unpaid','Paid','Cancelled')
    NOT NULL DEFAULT 'Unpaid';
    
UPDATE bookings SET status = 'Unpaid'    WHERE status = 'Belum Bayar';
UPDATE bookings SET status = 'Paid'      WHERE status = 'Sudah Bayar';
UPDATE bookings SET status = 'Cancelled' WHERE status = 'Dibatalkan';

ALTER TABLE bookings
    MODIFY status ENUM('Unpaid','Paid','Cancelled')
    NOT NULL DEFAULT 'Unpaid';
