package stayease.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Model untuk tabel `bookings`.
 * Berisi: atribut private, constructor, getter/setter, dan toString().
 *
 * Catatan: field `hotelNama` BUKAN kolom di tabel bookings. Ia hanya pelengkap
 * tampilan yang diisi dari hasil JOIN ke tabel hotels (dipakai BookingDAO untuk
 * menampilkan nama hotel di riwayat tanpa query tambahan).
 */
public class Booking {

    private int           bookingId;     // booking_id    INT (PK, auto increment)
    private int           userId;        // user_id       INT (FK -> users)
    private int           hotelId;       // hotel_id      INT (FK -> hotels)
    private LocalDate     checkIn;       // check_in      DATE
    private LocalDate     checkOut;      // check_out     DATE
    private int           jumlahKamar;   // jumlah_kamar  INT
    private BigDecimal    totalBayar;    // total_bayar   DECIMAL(12,2)
    private String        status;        // status        ENUM('Belum Bayar','Sudah Bayar','Dibatalkan')
    private LocalDateTime tanggalPesan;  // tanggal_pesan DATETIME

    private String        hotelNama;     // pelengkap tampilan (BUKAN kolom tabel)

    /** Constructor kosong. */
    public Booking() {
    }

    /** Untuk membuat booking baru sebelum disimpan (id & tanggalPesan diisi oleh DB). */
    public Booking(int userId, int hotelId, LocalDate checkIn, LocalDate checkOut,
                   int jumlahKamar, BigDecimal totalBayar, String status) {
        this.userId      = userId;
        this.hotelId     = hotelId;
        this.checkIn     = checkIn;
        this.checkOut    = checkOut;
        this.jumlahKamar = jumlahKamar;
        this.totalBayar  = totalBayar;
        this.status      = status;
    }

    // ---- getter & setter ----
    public int getBookingId() {
        return bookingId;
    }
    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getHotelId() {
        return hotelId;
    }
    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }
    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }
    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public int getJumlahKamar() {
        return jumlahKamar;
    }
    public void setJumlahKamar(int jumlahKamar) {
        this.jumlahKamar = jumlahKamar;
    }

    public BigDecimal getTotalBayar() {
        return totalBayar;
    }
    public void setTotalBayar(BigDecimal totalBayar) {
        this.totalBayar = totalBayar;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTanggalPesan() {
        return tanggalPesan;
    }
    public void setTanggalPesan(LocalDateTime tanggalPesan) {
        this.tanggalPesan = tanggalPesan;
    }

    public String getHotelNama() {
        return hotelNama;
    }
    public void setHotelNama(String hotelNama) {
        this.hotelNama = hotelNama;
    }

    @Override
    public String toString() {
        return "Booking{"
                + "bookingId=" + bookingId
                + ", userId=" + userId
                + ", hotelId=" + hotelId
                + ", checkIn=" + checkIn
                + ", checkOut=" + checkOut
                + ", jumlahKamar=" + jumlahKamar
                + ", totalBayar=" + totalBayar
                + ", status='" + status + '\''
                + ", tanggalPesan=" + tanggalPesan
                + '}';
    }
}
