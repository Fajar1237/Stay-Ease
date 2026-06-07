-- =====================================================
-- HOTEL BOOKING SYSTEM
-- FINAL DATABASE
-- MYSQL / XAMPP / PHPMYADMIN
-- =====================================================

DROP DATABASE IF EXISTS hotel_booking;
CREATE DATABASE hotel_booking;
USE hotel_booking;

-- =====================================================
-- TABLE USERS
-- =====================================================

CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,

    username VARCHAR(50) NOT NULL UNIQUE,

    name VARCHAR(100) NOT NULL,

    phone VARCHAR(20) NOT NULL UNIQUE,

    password VARCHAR(255) NOT NULL,

    role ENUM('admin','user')
    DEFAULT 'user',

    status ENUM('active','inactive')
    DEFAULT 'active',

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- TABLE HOTELS
-- =====================================================

CREATE TABLE hotels (
    hotel_id INT AUTO_INCREMENT PRIMARY KEY,

    hotel_name VARCHAR(150) NOT NULL,

    hotel_address TEXT NOT NULL,

    hotel_phone VARCHAR(20),

    hotel_description TEXT,

    hotel_image VARCHAR(255),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- TABLE HOTEL DETAILS
-- =====================================================

CREATE TABLE hotel_details (
    detail_id INT AUTO_INCREMENT PRIMARY KEY,

    hotel_id INT NOT NULL,

    room_type VARCHAR(50) NOT NULL,

    room_price DECIMAL(12,2) NOT NULL,

    room_capacity INT NOT NULL DEFAULT 2,

    room_facility TEXT,

    room_stock INT NOT NULL DEFAULT 1,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_hotel_details_hotel
        FOREIGN KEY (hotel_id)
        REFERENCES hotels(hotel_id)
        ON DELETE CASCADE
);

-- =====================================================
-- TABLE BOOKINGS
-- =====================================================

CREATE TABLE bookings (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,

    booking_code VARCHAR(20) NOT NULL UNIQUE,

    user_id INT NOT NULL,

    detail_id INT NOT NULL,

    check_in DATE NOT NULL,

    check_out DATE NOT NULL,

    total_guest INT NOT NULL DEFAULT 1,

    total_night INT NOT NULL,

    total_price DECIMAL(12,2) NOT NULL,

    notes TEXT,

    status ENUM(
        'pending',
        'confirmed',
        'cancelled'
    ) DEFAULT 'pending',

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_booking_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_booking_detail
        FOREIGN KEY (detail_id)
        REFERENCES hotel_details(detail_id)
        ON DELETE CASCADE
);

-- =====================================================
-- TABLE PAYMENTS
-- =====================================================

CREATE TABLE payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,

    booking_id INT NOT NULL,

    payment_amount DECIMAL(12,2) NOT NULL,

    change_amount DECIMAL(12,2) NOT NULL DEFAULT 0,

    payment_date DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_payment_booking
        FOREIGN KEY (booking_id)
        REFERENCES bookings(booking_id)
        ON DELETE CASCADE
);

INSERT INTO users (
    username,
    name,
    phone,
    password,
    role
)
VALUES (
    'admin',
    'Administrator',
    '081234567890',
    'admin123',
    'admin'
);