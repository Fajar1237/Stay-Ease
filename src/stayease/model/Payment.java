package stayease.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/** Representasi satu baris tabel payments. */
public class Payment {

    private int paymentId;
    private int bookingId;
    private BigDecimal uangBayar;
    private BigDecimal kembalian;
    private Timestamp tanggalBayar;

    public Payment() {
    }

    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }

    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public BigDecimal getUangBayar() { return uangBayar; }
    public void setUangBayar(BigDecimal uangBayar) { this.uangBayar = uangBayar; }

    public BigDecimal getKembalian() { return kembalian; }
    public void setKembalian(BigDecimal kembalian) { this.kembalian = kembalian; }

    public Timestamp getTanggalBayar() { return tanggalBayar; }
    public void setTanggalBayar(Timestamp tanggalBayar) { this.tanggalBayar = tanggalBayar; }
}
