package stayease.util;

/**
 * Session
 * -------
 * Menyimpan data user yang sedang login agar bisa diakses dari frame manapun
 * tanpa harus dioper lewat constructor. Data diisi saat login berhasil
 * dan dikosongkan saat logout.
 *
 * Field yang disimpan: user_id, nama, role.
 */
public class Session {
    
    private static Session instance;
    
    private static int userId;
    private static String nama;
    private static String role;   // "admin" atau "user"

    // Mencegah pembuatan objek; semua akses lewat method statis.
    private Session() {
    }

    /**
     * Mengisi session saat login berhasil.
     *
     * @param userId id user dari tabel users
     * @param nama   nama user
     * @param role   peran user: "admin" atau "user"
     */
    public static void login(int userId, String nama, String role) {
        Session.userId = userId;
        Session.nama = nama;
        Session.role = role;
    }
    
    public static int getUserId() {
        return userId;
    }

    public static String getNama() {
        return nama;
    }

    public static String getRole() {
        return role;
    }

    /** Cek apakah user yang login berperan sebagai admin. */
    public static boolean isAdmin() {
        return "admin".equalsIgnoreCase(role);
    }

    /** Cek apakah ada user yang sedang login. */
    public static boolean isLoggedIn() {
        return userId > 0;
    }

    /** Mengosongkan session saat logout. */
    public static void logout() {
        userId = 0;
        nama = null;
        role = null;
    }
}