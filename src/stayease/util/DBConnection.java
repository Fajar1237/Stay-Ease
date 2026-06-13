package stayease.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection
 * ------------
 * Menyediakan koneksi ke database MySQL (hotel_db) lewat JDBC.
 * Konfigurasi sesuai default XAMPP: host localhost, user root, password kosong.
 */
public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/hotel_db?useSSL=false&serverTimezone=Asia/Jakarta";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // default XAMPP: kosong

    /**
     * Membuka dan mengembalikan koneksi baru ke hotel_db.
     * Pemanggil bertanggung jawab menutup koneksi (gunakan try-with-resources).
     *
     * @return objek Connection yang aktif
     * @throws SQLException jika driver tidak ada atau koneksi gagal
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Memuat driver MySQL. Untuk Connector/J versi baru baris ini opsional,
            // tapi aman tetap ditulis agar jelas dependensinya.
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                    "Driver MySQL tidak ditemukan. Pastikan mysql-connector-j.jar "
                    + "sudah ditambahkan ke Libraries project.", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Tes cepat koneksi. Jalankan file ini (Run File) untuk memastikan
     * aplikasi bisa tersambung ke hotel_db sebelum membangun fitur lain.
     */
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("Koneksi ke hotel_db BERHASIL!");
            } else {
                System.out.println("Koneksi GAGAL.");
            }
        } catch (SQLException e) {
            System.out.println("Koneksi GAGAL: " + e.getMessage());
        }
    }
}