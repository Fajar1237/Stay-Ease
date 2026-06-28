/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package stayease.dao;

import stayease.model.Booking;
import stayease.util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Akses data tabel `bookings` memakai JDBC + PreparedStatement.
 *
 * Kolom tabel: booking_id, user_id, hotel_id, check_in, check_out,
 *              jumlah_kamar, total_bayar, status, tanggal_pesan.
 * Fitur: insert booking, get booking by user, get all booking, update status.
 *
 * Catatan: getBookingsByUser() & getAllBookings() melakukan JOIN ke tabel
 * `hotels` untuk mengisi field bantu `hotelNama` pada objek Booking
 * (hotelNama BUKAN kolom tabel bookings) agar nama hotel bisa langsung
 * ditampilkan di riwayat tanpa query tambahan.
 */
public class BookingDAO {

    /**
     * INSERT BOOKING — menambah pesanan baru.
     * Kolom booking_id dan tanggal_pesan diisi otomatis oleh database;
     * status dikirim eksplisit dari objek Booking (mis. "Unpaid").
     *
     * @param booking data booking yang akan disimpan.
     * @return booking_id yang ter-generate, atau -1 bila gagal.
     */
    public int insert(Booking booking) {
        String sql = "INSERT INTO bookings "
                   + "(user_id, hotel_id, check_in, check_out, jumlah_kamar, total_bayar, status) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, booking.getUserId());
            ps.setInt(2, booking.getHotelId());
            ps.setDate(3, Date.valueOf(booking.getCheckIn()));   // LocalDate -> SQL DATE
            ps.setDate(4, Date.valueOf(booking.getCheckOut()));
            ps.setInt(5, booking.getJumlahKamar());
            ps.setBigDecimal(6, booking.getTotalBayar());
            ps.setString(7, booking.getStatus());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal menyimpan booking: " + e.getMessage(), e);
        }
        return -1;
    }

    /**
     * GET BOOKING BY USER — riwayat booking milik satu user,
     * lengkap dengan nama hotel (hasil JOIN ke tabel hotels).
     *
     * @param userId id user pemilik booking.
     * @return daftar Booking milik user (kosong bila belum ada), urut terbaru dulu.
     */
    public List<Booking> getBookingsByUser(int userId) {
        String sql = "SELECT b.*, h.nama AS hotel_nama "
                   + "FROM bookings b JOIN hotels h ON b.hotel_id = h.hotel_id "
                   + "WHERE b.user_id = ? "
                   + "ORDER BY b.tanggal_pesan DESC";
        List<Booking> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Booking b = mapRow(rs);
                    b.setHotelNama(rs.getString("hotel_nama"));   // isi field bantu dari JOIN
                    list.add(b);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mengambil riwayat booking: " + e.getMessage(), e);
        }
        return list;
    }

    /**
     * GET ALL BOOKING — seluruh booking (untuk laporan / pemantauan admin),
     * lengkap dengan nama hotel (hasil JOIN), urut terbaru dulu.
     *
     * @return daftar semua Booking (kosong bila tabel kosong).
     */
    public List<Booking> getAllBookings() {
        String sql = "SELECT b.*, h.nama AS hotel_nama "
                   + "FROM bookings b JOIN hotels h ON b.hotel_id = h.hotel_id "
                   + "ORDER BY b.tanggal_pesan DESC";
        List<Booking> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Booking b = mapRow(rs);
                b.setHotelNama(rs.getString("hotel_nama"));
                list.add(b);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mengambil daftar booking: " + e.getMessage(), e);
        }
        return list;
    }

    /**
     * UPDATE STATUS BOOKING — mengubah status sebuah booking.
     * Nilai status yang sah sesuai ENUM tabel:
     * "Unpaid", "Paid", "Cancelled".
     *
     * @param bookingId id booking yang diubah.
     * @param status    status baru.
     * @return true bila ada baris yang diperbarui.
     */
    public boolean updateStatus(int bookingId, String status) {
        String sql = "UPDATE bookings SET status = ? WHERE booking_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, bookingId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mengubah status booking: " + e.getMessage(), e);
        }
    }

    // ----------------------------------------------------------------
    /** Mengubah satu baris ResultSet menjadi objek Booking. */
    private Booking mapRow(ResultSet rs) throws SQLException {
        Booking b = new Booking();
        b.setBookingId(rs.getInt("booking_id"));
        b.setUserId(rs.getInt("user_id"));
        b.setHotelId(rs.getInt("hotel_id"));
        b.setCheckIn(rs.getDate("check_in").toLocalDate());       // SQL DATE -> LocalDate
        b.setCheckOut(rs.getDate("check_out").toLocalDate());
        b.setJumlahKamar(rs.getInt("jumlah_kamar"));
        b.setTotalBayar(rs.getBigDecimal("total_bayar"));
        b.setStatus(rs.getString("status"));
        Timestamp ts = rs.getTimestamp("tanggal_pesan");          // DATETIME -> LocalDateTime
        if (ts != null) {
            b.setTanggalPesan(ts.toLocalDateTime());
        }
        return b;
    }
}
