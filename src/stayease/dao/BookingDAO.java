package stayease.dao;

import stayease.model.Booking;

import java.util.ArrayList;
import java.util.List;

/**
 * Akses data tabel bookings (buat pesanan, riwayat, ubah status).
 * TODO: lengkapi isi setiap method.
 */
public class BookingDAO {

    /** Simpan booking baru, status awal 'Belum Bayar'. */
    public boolean createBooking(Booking booking) {
        // TODO: INSERT INTO bookings (...)
        return false;
    }

    /** Riwayat booking milik satu user (untuk BookingHistoryFrame). */
    public List<Booking> getBookingsByUser(int userId) {
        // TODO: SELECT * FROM bookings WHERE user_id=?
        return new ArrayList<>();
    }

    /** Semua booking (untuk ReportFrame / admin). */
    public List<Booking> getAllBookings() {
        // TODO: SELECT * FROM bookings
        return new ArrayList<>();
    }

    public boolean updateStatus(int bookingId, String status) {
        // TODO: UPDATE bookings SET status=? WHERE booking_id=?
        return false;
    }
}
