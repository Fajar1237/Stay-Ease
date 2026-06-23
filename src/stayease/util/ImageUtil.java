/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package stayease.util;
 
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Image;
import java.net.URL;
 
/**
 * Memuat & menampilkan gambar hotel yang disimpan sebagai RESOURCE di dalam
 * Source Packages, pada paket:  images/HotelCardImages/
 *
 * PENTING: karena gambar berada di dalam classpath (Source Packages), gambar
 * dimuat dengan getResource(...), BUKAN dengan path file biasa. Cara ini tetap
 * jalan baik saat dijalankan dari NetBeans maupun saat aplikasi di-package.
 *
 * Kolom `gambar` di database cukup menyimpan NAMA FILE-nya saja, mis. "hotel1.jpg".
 * (Kalaupun DB menyimpan path lengkap seperti "images/hotel1.jpg", method di bawah
 *  otomatis mengambil bagian nama filenya saja, jadi tetap aman.)
 */
public class ImageUtil {
 
    /** Lokasi paket gambar di dalam Source Packages (awali & akhiri dengan "/"). */
    private static final String BASE = "/images/HotelCardImages/";
 
    private ImageUtil() {}
 
    /**
     * Menampilkan gambar ke sebuah JLabel, diskalakan sesuai ukuran label.
     * @param label    JLabel tujuan (mis. label gambar pada kartu).
     * @param namaFile nilai kolom `gambar` dari DB, mis. "hotel1.jpg".
     */
    public static void tampilkanGambar(JLabel label, String namaFile) {
        ImageIcon icon = muatIcon(namaFile, label.getWidth(), label.getHeight());
        if (icon != null) {
            label.setIcon(icon);
            label.setText(null);
        } else {
            label.setIcon(null);
            label.setText("Gambar tidak ditemukan");
        }
    }
 
    /**
     * Memuat gambar sebagai ImageIcon yang sudah diskalakan.
     * @param namaFile nilai kolom `gambar` dari DB (mis. "hotel1.jpg").
     * @param lebar    lebar target px (bila <= 0 dipakai 230).
     * @param tinggi   tinggi target px (bila <= 0 dipakai 150).
     * @return ImageIcon siap pakai, atau null bila file tidak ada.
     */
    public static ImageIcon muatIcon(String namaFile, int lebar, int tinggi) {
        if (namaFile == null || namaFile.trim().isEmpty()) return null;
 
        // ambil nama filenya saja (jaga-jaga DB menyimpan path lengkap)
        String file = namaFile.replace('\\', '/');
        file = file.substring(file.lastIndexOf('/') + 1);
 
        URL url = ImageUtil.class.getResource(BASE + file);
        if (url == null) return null;   // file tidak ada di paket HotelCardImages
 
        if (lebar <= 0)  lebar = 230;
        if (tinggi <= 0) tinggi = 150;
 
        Image img = new ImageIcon(url).getImage()
                        .getScaledInstance(lebar, tinggi, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}
