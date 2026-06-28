package stayease.controller;

import stayease.dao.BookingDAO;
import stayease.dao.PaymentDAO;
import stayease.model.Payment;

import java.math.BigDecimal;
import java.util.List;

/**
 * Mengatur logika pembayaran: validasi uang, hitung kembalian, simpan
 * pembayaran, lalu ubah status booking menjadi "Paid".
 */
public class PaymentController {

    private final PaymentDAO paymentDAO = new PaymentDAO();
    private final BookingDAO bookingDAO = new BookingDAO();

    /**
     * Memproses pembayaran sebuah booking.
     * Bila uang < total  -> melempar IllegalArgumentException("Uang tidak mencukupi").
     * Bila uang >= total -> simpan ke payments + ubah status booking "Paid".
     * @return kembalian (uang bayar - total bayar).
     */
    public BigDecimal prosesPembayaran(int bookingId, BigDecimal totalBayar, BigDecimal uangBayar) {
        if (uangBayar.compareTo(totalBayar) < 0) {
            throw new IllegalArgumentException("Uang tidak mencukupi");
        }
        BigDecimal kembalian = uangBayar.subtract(totalBayar);
        paymentDAO.insert(new Payment(bookingId, uangBayar, kembalian)); // simpan pembayaran
        bookingDAO.updateStatus(bookingId, "Paid");                      // ubah status
        return kembalian;
    }

    /** Semua pembayaran (untuk laporan admin). */
    public List<Payment> getSemuaPembayaran() {
        return paymentDAO.getAllPayments();
    }

    /** Total pendapatan keseluruhan. */
    public BigDecimal getTotalPendapatan() {
        return paymentDAO.hitungTotalPendapatan();
    }
}
