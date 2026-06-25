package stayease.controller;

import stayease.dao.BookingDAO;
import stayease.model.Booking;
import stayease.util.Session;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Mengatur logika pemesanan. Aturan bisnis
 * (total = harga x jumlah kamar x jumlah malam) berada di sini, bukan di view.
 */
public class BookingController {

    private final BookingDAO bookingDAO = new BookingDAO();

    /** Menghitung total bayar = harga x jumlah kamar x jumlah malam. */
    public BigDecimal hitungTotal(BigDecimal harga, int jumlahKamar,
                                  LocalDate checkIn, LocalDate checkOut) {
        long malam = ChronoUnit.DAYS.between(checkIn, checkOut);
        return harga.multiply(BigDecimal.valueOf(jumlahKamar))
                    .multiply(BigDecimal.valueOf(malam));
    }

    /**
     * Membuat & menyimpan booking baru milik user yang sedang login,
     * dengan status awal "Belum Bayar".
     * @return booking_id hasil simpan (atau -1 bila gagal).
     */
    public int buatBooking(int hotelId, LocalDate checkIn, LocalDate checkOut,
                           int jumlahKamar, BigDecimal harga) {
        BigDecimal total = hitungTotal(harga, jumlahKamar, checkIn, checkOut);
        Booking booking = new Booking(
                Session.getUserId(), hotelId, checkIn, checkOut,
                jumlahKamar, total, "Belum Bayar");
        return bookingDAO.insert(booking);
    }

    /** Riwayat booking milik user yang sedang login. */
    public List<Booking> getRiwayatUser() {
        return bookingDAO.getBookingsByUser(Session.getUserId());
    }

    /** Semua booking (untuk laporan admin). */
    public List<Booking> getSemuaBooking() {
        return bookingDAO.getAllBookings();
    }

    /** Mengubah status sebuah booking. */
    public boolean ubahStatus(int bookingId, String status) {
        return bookingDAO.updateStatus(bookingId, status);
    }
}
