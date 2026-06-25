/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package stayease.view;

/**
 *
 * @author malik
 */
import stayease.dao.PaymentDAO;
import stayease.util.DBConnection;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class Laporan extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Laporan.class.getName());

    /**
     * Creates new form Laporan
     */
    public Laporan() {
        setLocationRelativeTo(null);
        muatSemua();
    }

    private void muatSemua() {
        loadLaporanBooking();
        loadLaporanPembayaran();
        loadTotalPendapatan();
    }

    private final java.time.format.DateTimeFormatter FMT_TGL =
        java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final java.time.format.DateTimeFormatter FMT_WAKTU =
        java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

/** format angka jadi Rupiah */
    private String rp(java.math.BigDecimal v) {
    return "Rp " + String.format("%,.0f", v);
}

/** model tabel yang tidak bisa diedit */
    private DefaultTableModel modelKosong(String[] kolom) {
    return new DefaultTableModel(kolom, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    }
    private void loadLaporanBooking() {
    String sql = "SELECT b.booking_id, u.username, h.nama AS nama_hotel, "
               + "b.check_in, b.check_out, b.jumlah_kamar, b.total_bayar, b.status, b.tanggal_pesan "
               + "FROM bookings b "
               + "JOIN users u  ON b.user_id  = u.user_id "
               + "JOIN hotels h ON b.hotel_id = h.hotel_id "
               + "ORDER BY b.tanggal_pesan DESC";
    DefaultTableModel model = modelKosong(new String[]{
        "ID", "Username", "Nama Hotel", "Check In", "Check Out", "Kamar", "Total Bayar", "Status", "Tgl Pesan"});
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("booking_id"),
                rs.getString("username"),
                rs.getString("nama_hotel"),
                rs.getDate("check_in").toLocalDate().format(FMT_TGL),
                rs.getDate("check_out").toLocalDate().format(FMT_TGL),
                rs.getInt("jumlah_kamar"),
                rp(rs.getBigDecimal("total_bayar")),
                rs.getString("status"),
                rs.getTimestamp("tanggal_pesan").toLocalDateTime().format(FMT_WAKTU)
            });
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Gagal memuat laporan booking:\n" + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }
    tblBooking.setModel(model);
    }
    
    private void loadLaporanPembayaran() {
    String sql = "SELECT p.payment_id, p.booking_id, u.username, h.nama AS nama_hotel, "
               + "b.total_bayar, p.uang_bayar, p.kembalian, p.tanggal_bayar "
               + "FROM payments p "
               + "JOIN bookings b ON p.booking_id = b.booking_id "
               + "JOIN users u    ON b.user_id    = u.user_id "
               + "JOIN hotels h   ON b.hotel_id   = h.hotel_id "
               + "ORDER BY p.tanggal_bayar DESC";
    DefaultTableModel model = modelKosong(new String[]{
        "ID Bayar", "ID Booking", "Username", "Nama Hotel", "Total Tagihan", "Uang Bayar", "Kembalian", "Tgl Bayar"});
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("payment_id"),
                rs.getInt("booking_id"),
                rs.getString("username"),
                rs.getString("nama_hotel"),
                rp(rs.getBigDecimal("total_bayar")),
                rp(rs.getBigDecimal("uang_bayar")),
                rp(rs.getBigDecimal("kembalian")),
                rs.getTimestamp("tanggal_bayar").toLocalDateTime().format(FMT_WAKTU)
            });
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Gagal memuat laporan pembayaran:\n" + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }
    tblPembayaran.setModel(model);
    }
    
    private void loadTotalPendapatan() {
    // angka besar (pakai PaymentDAO yang sudah ada)
    java.math.BigDecimal total = new PaymentDAO().hitungTotalPendapatan();


    // rincian per hotel
    String sql = "SELECT h.nama AS nama_hotel, "
               + "COUNT(p.payment_id) AS jumlah_transaksi, "
               + "SUM(p.uang_bayar - p.kembalian) AS pendapatan "
               + "FROM payments p "
               + "JOIN bookings b ON p.booking_id = b.booking_id "
               + "JOIN hotels h   ON b.hotel_id   = h.hotel_id "
               + "GROUP BY h.hotel_id, h.nama "
               + "ORDER BY pendapatan DESC";
    DefaultTableModel model = modelKosong(new String[]{"Nama Hotel", "Jumlah Transaksi", "Pendapatan"});
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getString("nama_hotel"),
                rs.getInt("jumlah_transaksi"),
                rp(rs.getBigDecimal("pendapatan"))
            });
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Gagal memuat pendapatan:\n" + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }
    tblPendapatanHotel.setModel(model);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblBooking = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblPembayaran = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblPendapatanHotel = new javax.swing.JTable();
        btnRefresh = new javax.swing.JButton();
        btnKembali = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Laporan");

        tblBooking.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblBooking);

        jTabbedPane1.addTab("tab1", jScrollPane1);

        tblPembayaran.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tblPembayaran);

        jTabbedPane1.addTab("tab2", jScrollPane2);

        tblPendapatanHotel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(tblPendapatanHotel);

        jTabbedPane1.addTab("tab3", jScrollPane3);

        btnRefresh.setText("jButton1");
        btnRefresh.addActionListener(this::btnRefreshActionPerformed);

        btnKembali.setText("jButton2");
        btnKembali.addActionListener(this::btnKembaliActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 766, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(83, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnKembali)
                        .addGap(17, 17, 17))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnRefresh)
                .addGap(120, 120, 120))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(btnKembali)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addComponent(btnRefresh)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
    muatSemua();        // TODO add your handling code here:
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnKembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKembaliActionPerformed
    new AdminFrame().setVisible(true);
    this.dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_btnKembaliActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Laporan().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnKembali;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tblBooking;
    private javax.swing.JTable tblPembayaran;
    private javax.swing.JTable tblPendapatanHotel;
    // End of variables declaration//GEN-END:variables
}
