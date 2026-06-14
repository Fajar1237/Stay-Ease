package stayease.main;

/**
 * Titik awal aplikasi StayEase (Hotel Booking System).
 *
 * Setelah LoginFrame dibuat di NetBeans (view package), aktifkan baris
 * di bawah ini untuk menampilkannya saat aplikasi dijalankan.
 */
public class Main {

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            // new com.stayease.view.LoginFrame().setVisible(true);
            System.out.println("StayEase siap. "
                    + "Buat LoginFrame di package view, lalu aktifkan baris di Main.");
        });
    }
}
