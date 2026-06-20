package stayease.model;

/**
 * Model untuk tabel `users`.
 * Berisi: atribut private, constructor, getter/setter, dan toString().
 */
public class User {

    private int    userId;       // user_id     INT (PK, auto increment)
    private String username;     // username    VARCHAR(50)
    private String password;     // password    VARCHAR(255)  (hash / plaintext)
    private String role;         // role        ENUM('admin','user')
    private String noTelepon;    // no_telepon  VARCHAR(20)

    /** Constructor kosong. */
    public User() {
    }

    /** Untuk membuat data baru sebelum disimpan (user_id diisi otomatis oleh DB). */
    public User(String username, String password, String role, String noTelepon) {
        this.username  = username;
        this.password  = password;
        this.role      = role;
        this.noTelepon = noTelepon;
    }

    /** Constructor lengkap, biasanya dipakai saat membaca dari database. */
    public User(int userId, String username, String password, String role, String noTelepon) {
        this(username, password, role, noTelepon);
        this.userId = userId;
    }

    // ---- getter & setter ----
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    public String getNoTelepon() {
        return noTelepon;
    }
    public void setNoTelepon(String noTelepon) {
        this.noTelepon = noTelepon;
    }

    @Override
    public String toString() {
        // password sengaja TIDAK ditampilkan demi keamanan
        return "User{"
                + "userId=" + userId
                + ", username='" + username + '\''
                + ", role='" + role + '\''
                + ", noTelepon='" + noTelepon + '\''
                + '}';
    }
}
