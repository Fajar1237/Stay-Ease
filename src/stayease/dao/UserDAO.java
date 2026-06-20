package stayease.dao;

import stayease.model.User;
import stayease.util.DBConnection;
import stayease.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Akses data tabel `users` memakai JDBC + PreparedStatement.
 *
 * Kolom yang dipakai SAMA PERSIS dengan tabel Anda:
 *   user_id, username, password, role, no_telepon
 * (tidak ada kolom nama atau email).
 *
 * Fitur: login, register, get user by id, get all users.
 */
public class UserDAO {

    /**
     * LOGIN — ambil user berdasarkan username, lalu verifikasi password.
     *
     * Verifikasi dilakukan di Java (lewat PasswordUtil), BUKAN di klausa SQL,
     * supaya tetap bekerja baik untuk password polos (akun admin bawaan) maupun
     * password yang sudah di-hash (hasil registrasi). Untuk keamanan, method ini
     * tidak membedakan "username salah" dan "password salah".
     *
     * @return objek User bila kredensial benar; null bila gagal.
     */
    public User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = mapRow(rs);
                    if (PasswordUtil.verify(password, user.getPassword())) {
                        return user;                 // login berhasil
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal memproses login: " + e.getMessage(), e);
        }
        return null;                                 // username tidak ada / password salah
    }

    /**
     * REGISTER — daftarkan user baru. Password di-hash otomatis sebelum disimpan,
     * dan username yang sudah dipakai akan ditolak (kolom username bersifat UNIQUE).
     *
     * @param user data user baru; password diisi polos lalu di-hash di sini.
     *             Jika role kosong, otomatis diisi "user".
     * @return true bila berhasil; false bila username sudah dipakai.
     */
    public boolean register(User user) {
        if (existsByUsername(user.getUsername())) {
            return false;                            // username sudah dipakai
        }
        String role = (user.getRole() == null || user.getRole().isEmpty())
                ? "user" : user.getRole();

        String sql = "INSERT INTO users (username, password, role, no_telepon) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, PasswordUtil.hash(user.getPassword()));   // simpan dalam bentuk hash
            ps.setString(3, role);
            ps.setString(4, user.getNoTelepon());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mendaftarkan user: " + e.getMessage(), e);
        }
    }

    /**
     * GET USER BY ID — ambil satu user berdasarkan primary key user_id.
     * @return User bila ditemukan; null bila tidak ada.
     */
    public User getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mengambil user: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * GET ALL USERS — ambil seluruh user, diurutkan berdasarkan user_id.
     * Berguna untuk UserManagementFrame (admin).
     * @return daftar User (kosong bila tabel kosong).
     */
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users ORDER BY user_id";
        List<User> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mengambil daftar user: " + e.getMessage(), e);
        }
        return list;
    }

    // ----------------------------------------------------------------
    /** Cek apakah username sudah terpakai (dipakai oleh register). */
    private boolean existsByUsername(String username) {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mengecek username: " + e.getMessage(), e);
        }
    }

    /** Ubah satu baris ResultSet menjadi objek User. */
    private User mapRow(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("user_id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("role"),
                rs.getString("no_telepon"));
    }
}
