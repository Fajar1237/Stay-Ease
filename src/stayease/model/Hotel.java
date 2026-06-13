package stayease.model;

import java.math.BigDecimal;

/** Representasi satu baris tabel hotels. */
public class Hotel {

    private int hotelId;
    private String nama;
    private String lokasi;
    private String deskripsi;
    private BigDecimal harga;
    private String gambar;   // path file gambar, mis. "images/hotel1.jpg"

    public Hotel() {
    }

    public int getHotelId() { return hotelId; }
    public void setHotelId(int hotelId) { this.hotelId = hotelId; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getLokasi() { return lokasi; }
    public void setLokasi(String lokasi) { this.lokasi = lokasi; }

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }

    public BigDecimal getHarga() { return harga; }
    public void setHarga(BigDecimal harga) { this.harga = harga; }

    public String getGambar() { return gambar; }
    public void setGambar(String gambar) { this.gambar = gambar; }
}
