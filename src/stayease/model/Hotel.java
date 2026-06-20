package stayease.model;

import java.math.BigDecimal;

/**
 * Model untuk tabel `hotels`.
 * Berisi: atribut private, constructor, getter/setter, dan toString().
 */
public class Hotel {

    private int        hotelId;    // hotel_id  INT (PK, auto increment)
    private String     nama;       // nama      VARCHAR(150)
    private String     lokasi;     // lokasi    VARCHAR(150)
    private String     deskripsi;  // deskripsi TEXT
    private BigDecimal harga;      // harga     DECIMAL(12,2)
    private String     gambar;     // gambar    VARCHAR(255)

    /** Constructor kosong. */
    public Hotel() {
    }

    /** Untuk membuat data baru sebelum disimpan (hotel_id diisi otomatis oleh DB). */
    public Hotel(String nama, String lokasi, String deskripsi, BigDecimal harga, String gambar) {
        this.nama      = nama;
        this.lokasi    = lokasi;
        this.deskripsi = deskripsi;
        this.harga     = harga;
        this.gambar    = gambar;
    }

    /** Constructor lengkap, biasanya dipakai saat membaca dari database. */
    public Hotel(int hotelId, String nama, String lokasi, String deskripsi, BigDecimal harga, String gambar) {
        this(nama, lokasi, deskripsi, harga, gambar);
        this.hotelId = hotelId;
    }

    // ---- getter & setter ----
    public int getHotelId() {
        return hotelId;
    }
    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public String getNama() {
        return nama;
    }
    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getLokasi() {
        return lokasi;
    }
    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getDeskripsi() {
        return deskripsi;
    }
    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public BigDecimal getHarga() {
        return harga;
    }
    public void setHarga(BigDecimal harga) {
        this.harga = harga;
    }

    public String getGambar() {
        return gambar;
    }
    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    @Override
    public String toString() {
        return "Hotel{"
                + "hotelId=" + hotelId
                + ", nama='" + nama + '\''
                + ", lokasi='" + lokasi + '\''
                + ", harga=" + harga
                + ", gambar='" + gambar + '\''
                + '}';
    }
}
