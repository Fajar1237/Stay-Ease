package stayease.util;

import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Helper untuk menyimpan & memverifikasi password secara aman.
 *
 * Memakai PBKDF2 (bawaan JDK — TIDAK perlu library tambahan). Hash disimpan
 * dalam format: "iterasi:salt(base64):hash(base64)".
 *
 * Catatan migrasi:
 *  - User baru (registrasi) akan disimpan ter-hash via {@link #hash(String)}.
 *  - Akun lama yang masih plaintext (mis. admin/admin123 dari file SQL) tetap
 *    bisa login karena {@link #verify(String, String)} punya fallback plaintext.
 *    Setelah semua akun ter-hash, fallback tersebut bisa dihapus.
 *
 * Jika Anda lebih suka BCrypt, cukup ganti isi kedua method di bawah dan
 * tambahkan JAR jBCrypt ke Libraries NetBeans — pemanggil tidak perlu diubah.
 */
public class PasswordUtil {

    private static final String ALGORITHM   = "PBKDF2WithHmacSHA256";
    private static final int    ITERATIONS  = 120_000;
    private static final int    KEY_LENGTH  = 256;          // bit
    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordUtil() {
    }

    /** Hasilkan hash untuk disimpan ke kolom users.password. */
    public static String hash(String plainPassword) {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        byte[] derived = pbkdf2(plainPassword.toCharArray(), salt, ITERATIONS);
        return ITERATIONS + ":"
                + Base64.getEncoder().encodeToString(salt) + ":"
                + Base64.getEncoder().encodeToString(derived);
    }

    /**
     * Cek apakah `plainPassword` cocok dengan nilai tersimpan.
     * @param plainPassword input dari form login
     * @param stored        nilai dari kolom users.password
     */
    public static boolean verify(String plainPassword, String stored) {
        if (stored == null) {
            return false;
        }
        String[] parts = stored.split(":");
        if (parts.length == 3) {                 // format hash kita
            try {
                int    iterations = Integer.parseInt(parts[0]);
                byte[] salt       = Base64.getDecoder().decode(parts[1]);
                byte[] expected   = Base64.getDecoder().decode(parts[2]);
                byte[] actual     = pbkdf2(plainPassword.toCharArray(), salt, iterations);
                return constantTimeEquals(expected, actual);
            } catch (RuntimeException ignored) {
                // bila gagal di-parse, anggap bukan hash -> jatuh ke plaintext
            }
        }
        // FALLBACK akun lama yang masih plaintext (mis. admin123)
        return stored.equals(plainPassword);
    }

    // ---------------------------------------------------------------
    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
            return factory.generateSecret(spec).getEncoded();
        } catch (Exception e) {
            throw new RuntimeException("Gagal melakukan hashing password", e);
        }
    }

    /** Perbandingan byte yang tahan timing-attack. */
    private static boolean constantTimeEquals(byte[] a, byte[] b) {
        if (a.length != b.length) {
            return false;
        }
        int result = 0;
        for (int i = 0; i < a.length; i++) {
            result |= a[i] ^ b[i];
        }
        return result == 0;
    }
}
