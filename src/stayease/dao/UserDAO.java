package stayease.dao;

import stayease.model.User;
import stayease.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Akses data tabel users (login, register, CRUD user).
 * TODO: lengkapi isi setiap method.
 */
public class UserDAO {

    /** Dipakai LoginFrame: cek username + password, kembalikan User jika cocok. */
    public User login(String username, String password) {
        // TODO: SELECT * FROM users WHERE username=? AND password=?
        return null;
    }

    /** Dipakai RegisterFrame: simpan user baru (role default 'user'). */
    public boolean register(User user) {
        // TODO: INSERT INTO users (...)
        return false;
    }

    public List<User> getAllUsers() {
        // TODO: SELECT * FROM users
        return new ArrayList<>();
    }

    public boolean updateUser(User user) {
        // TODO: UPDATE users SET ... WHERE user_id=?
        return false;
    }

    public boolean deleteUser(int userId) {
        // TODO: DELETE FROM users WHERE user_id=?
        return false;
    }

    /** Cek apakah username sudah dipakai (untuk validasi register). */
    public boolean isUsernameTaken(String username) {
        // TODO: SELECT COUNT(*) FROM users WHERE username=?
        return false;
    }
}
