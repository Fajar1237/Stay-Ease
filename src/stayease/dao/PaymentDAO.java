package stayease.dao;

import stayease.model.Payment;

import java.util.ArrayList;
import java.util.List;

/**
 * Akses data tabel payments.
 * TODO: lengkapi isi setiap method.
 */
public class PaymentDAO {

    /**
     * Simpan pembayaran sebuah booking.
     * Disarankan: bungkus dengan transaksi agar insert payment + update
     * status booking jadi 'Sudah Bayar' dilakukan bersama-sama.
     */
    public boolean createPayment(Payment payment) {
        // TODO: INSERT INTO payments (...)
        return false;
    }

    public Payment getPaymentByBooking(int bookingId) {
        // TODO: SELECT * FROM payments WHERE booking_id=?
        return null;
    }

    public List<Payment> getAllPayments() {
        // TODO: SELECT * FROM payments
        return new ArrayList<>();
    }
}
