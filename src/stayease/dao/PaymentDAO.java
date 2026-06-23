/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package stayease.dao;

import stayease.model.Payment;
import stayease.util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Akses data tabel `payments` memakai JDBC + PreparedStatement.
 *
 * Kolom tabel: payment_id, booking_id, uang_bayar, kembalian, tanggal_bayar.
 * Fitur: simpan pembayaran, get semua pembayaran, hitung total pendapatan.
 */
public class PaymentDAO {

    /**
     * SIMPAN PEMBAYARAN — mencatat satu pembayaran ke tabel payments.
     * Kolom payment_id dan tanggal_bayar diisi otomatis oleh database.
     *
     * @param payment data pembayaran (booking_id, uang_bayar, kembalian).
     * @return payment_id yang ter-generate, atau -1 bila gagal.
     */
    public int insert(Payment payment) {
        String sql = "INSERT INTO payments (booking_id, uang_bayar, kembalian) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, payment.getBookingId());
            ps.setBigDecimal(2, payment.getUangBayar());
            ps.setBigDecimal(3, payment.getKembalian());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal menyimpan pembayaran: " + e.getMessage(), e);
        }
        return -1;
    }

    /**
     * GET SEMUA PEMBAYARAN — mengambil seluruh pembayaran, urut terbaru dulu.
     *
     * @return daftar Payment (kosong bila tabel kosong).
     */
    public List<Payment> getAllPayments() {
        String sql = "SELECT * FROM payments ORDER BY tanggal_bayar DESC";
        List<Payment> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mengambil daftar pembayaran: " + e.getMessage(), e);
        }
        return list;
    }

    /**
     * HITUNG TOTAL PENDAPATAN — jumlah uang bersih yang benar-benar diterima
     * dari seluruh pembayaran, yaitu SUM(uang_bayar - kembalian).
     * Bila belum ada pembayaran, mengembalikan 0.
     *
     * @return total pendapatan sebagai BigDecimal.
     */
    public BigDecimal hitungTotalPendapatan() {
        String sql = "SELECT COALESCE(SUM(uang_bayar - kembalian), 0) AS total FROM payments";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                BigDecimal total = rs.getBigDecimal("total");
                return (total != null) ? total : BigDecimal.ZERO;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal menghitung total pendapatan: " + e.getMessage(), e);
        }
        return BigDecimal.ZERO;
    }

    // ----------------------------------------------------------------
    /** Mengubah satu baris ResultSet menjadi objek Payment. */
    private Payment mapRow(ResultSet rs) throws SQLException {
        Payment p = new Payment();
        p.setPaymentId(rs.getInt("payment_id"));
        p.setBookingId(rs.getInt("booking_id"));
        p.setUangBayar(rs.getBigDecimal("uang_bayar"));
        p.setKembalian(rs.getBigDecimal("kembalian"));
        Timestamp ts = rs.getTimestamp("tanggal_bayar");          // DATETIME -> LocalDateTime
        if (ts != null) {
            p.setTanggalBayar(ts.toLocalDateTime());
        }
        return p;
    }
}