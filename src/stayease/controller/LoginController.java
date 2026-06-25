package stayease.controller;

import stayease.dao.UserDAO;
import stayease.model.User;
import stayease.util.Session;

/**
 * Mengatur proses login & logout.
 * View hanya menyerahkan username/password; controller yang memverifikasi
 * lewat UserDAO dan menyimpan identitas ke Session.
 */
public class LoginController {

    private final UserDAO userDAO = new UserDAO();

    /**
     * Memproses login. Bila cocok, identitas disimpan ke Session.
     * @return objek User bila berhasil, atau null bila gagal.
     */
    public User login(String username, String password) {
        User user = userDAO.login(username, password);
        if (user != null) {
            Session.setCurrentUser(user);   // simpan sesi user yang login
        }
        return user;
    }

    /** Apakah user yang sedang login berperan admin? */
    public boolean isAdmin() {
        return Session.isAdmin();
    }

    /** Mengakhiri sesi (logout). */
    public void logout() {
        Session.logout();
    }
}
