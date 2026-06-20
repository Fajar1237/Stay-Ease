package stayease.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Model untuk tabel `payments`.
 * Berisi: atribut private, constructor, getter/setter, dan toString().
 */
public class Payment {

    private int           paymentId;     // payment_id    INT (PK, auto increment)
    private int           bookingId;     // booking_id    INT (FK -> bookings)
    private BigDecimal    uangBayar;     // uang_bayar    DECIMAL(12,2)
    private BigDecimal    kembalian;     // kembalian     DECIMAL(12,2)
    private LocalDateTime tanggalBayar;  // tanggal_bayar DATETIME

    /** Constructor kosong. */
    public Payment() {
    }

    /** Untuk membuat pembayaran baru sebelum disimpan (id & tanggal diisi oleh DB). */
    public Payment(int bookingId, BigDecimal uangBayar, BigDecimal kembalian) {
        this.bookingId = bookingId;
        this.uangBayar = uangBayar;
        this.kembalian = kembalian;
    }

    // ---- getter & setter ----
    public int getPaymentId() {
        return paymentId;
    }
    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getBookingId() {
        return bookingId;
    }
    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public BigDecimal getUangBayar() {
        return uangBayar;
    }
    public void setUangBayar(BigDecimal uangBayar) {
        this.uangBayar = uangBayar;
    }

    public BigDecimal getKembalian() {
        return kembalian;
    }
    public void setKembalian(BigDecimal kembalian) {
        this.kembalian = kembalian;
    }

    public LocalDateTime getTanggalBayar() {
        return tanggalBayar;
    }
    public void setTanggalBayar(LocalDateTime tanggalBayar) {
        this.tanggalBayar = tanggalBayar;
    }

    @Override
    public String toString() {
        return "Payment{"
                + "paymentId=" + paymentId
                + ", bookingId=" + bookingId
                + ", uangBayar=" + uangBayar
                + ", kembalian=" + kembalian
                + ", tanggalBayar=" + tanggalBayar
                + '}';
    }
}
