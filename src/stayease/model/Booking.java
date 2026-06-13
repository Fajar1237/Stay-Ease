package stayease.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/** Representasi satu baris tabel bookings. */
public class Booking {

    private int bookingId;
    private int userId;
    private int hotelId;
    private Date checkIn;
    private Date checkOut;
    private int jumlahKamar;
    private BigDecimal totalBayar;
    private String status;        // 'Belum Bayar', 'Sudah Bayar', 'Dibatalkan'
    private Timestamp tanggalPesan;

    public Booking() {
    }

    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getHotelId() { return hotelId; }
    public void setHotelId(int hotelId) { this.hotelId = hotelId; }

    public Date getCheckIn() { return checkIn; }
    public void setCheckIn(Date checkIn) { this.checkIn = checkIn; }

    public Date getCheckOut() { return checkOut; }
    public void setCheckOut(Date checkOut) { this.checkOut = checkOut; }

    public int getJumlahKamar() { return jumlahKamar; }
    public void setJumlahKamar(int jumlahKamar) { this.jumlahKamar = jumlahKamar; }

    public BigDecimal getTotalBayar() { return totalBayar; }
    public void setTotalBayar(BigDecimal totalBayar) { this.totalBayar = totalBayar; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getTanggalPesan() { return tanggalPesan; }
    public void setTanggalPesan(Timestamp tanggalPesan) { this.tanggalPesan = tanggalPesan; }
}
