package stayease.view;

import stayease.dao.HotelDAO;
import stayease.model.Hotel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.*;
import java.math.BigDecimal;
import java.util.List;

public class KelolaHotel extends javax.swing.JFrame {

    private final HotelDAO hotelDAO = new HotelDAO();
    private DefaultTableModel tableModel;
    private int selectedHotelId = -1;

    private JTextField txtNama, txtLokasi, txtHarga, txtGambar, txtSearch;
    private JTextArea txtDeskripsi;
    private JScrollPane scrollTable;
    private JTable tblHotel;
    private JButton btnTambah, btnEdit, btnHapus, btnBatal, btnKembali, btnCari, btnReset;

    // Warna tema
    private static final Color BLUE_MAIN  = new Color(41, 171, 226);
    private static final Color BLUE_DARK  = new Color(26, 115, 180);
    private static final Color BLUE_LIGHT = new Color(220, 242, 255);
    private static final Color TEXT_DARK  = new Color(30, 30, 60);
    private static final Color TEXT_GRAY  = new Color(120, 130, 150);

    public KelolaHotel() {
        initComponents();
        loadTable("");
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 640);
        setResizable(false);

        // ===== ROOT PANEL =====
        JPanel root = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        setContentPane(root);

        // ===== PANEL KIRI (FORM) =====
        JPanel pnlKiri = new JPanel(null);
        pnlKiri.setBackground(Color.WHITE);
        pnlKiri.setPreferredSize(new Dimension(400, 640));

        // Logo + Judul
        ImageIcon rawIcon = new ImageIcon(getClass().getResource("/images/icon/icon.png"));
        Image scaled = rawIcon.getImage().getScaledInstance(36, 36, Image.SCALE_SMOOTH);
        JLabel lblIcon = new JLabel(new ImageIcon(scaled));
        lblIcon.setBounds(30, 28, 36, 36);
        pnlKiri.add(lblIcon);

        JLabel lblApp = new JLabel("StayEase");
        lblApp.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblApp.setForeground(TEXT_DARK);
        lblApp.setBounds(72, 32, 150, 30);
        pnlKiri.add(lblApp);

        JLabel lblTitle = new JLabel("Manage Hotel");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(TEXT_DARK);
        lblTitle.setBounds(30, 80, 300, 30);
        pnlKiri.add(lblTitle);

        // Fields
        int y = 145;
        int gap = 58;

        pnlKiri.add(buatLabel("Hotel Name"), labelBounds(30, y));
        txtNama = buatTextField();
        txtNama.setBounds(30, y + 22, 330, 34);
        pnlKiri.add(txtNama);

        y += gap;
        pnlKiri.add(buatLabel("Location"), labelBounds(30, y));
        txtLokasi = buatTextField();
        txtLokasi.setBounds(30, y + 22, 330, 34);
        pnlKiri.add(txtLokasi);

        y += gap;
        pnlKiri.add(buatLabel("Price / Night (Rp)"), labelBounds(30, y));
        txtHarga = buatTextField();
        txtHarga.setBounds(30, y + 22, 150, 34);
        pnlKiri.add(txtHarga);

        pnlKiri.add(buatLabel("Image Filename"), labelBounds(195, y));
        txtGambar = buatTextField();
        txtGambar.setBounds(195, y + 22, 165, 34);
        pnlKiri.add(txtGambar);

        y += gap;
        pnlKiri.add(buatLabel("Description"), labelBounds(30, y));
        txtDeskripsi = new JTextArea();
        txtDeskripsi.setLineWrap(true);
        txtDeskripsi.setWrapStyleWord(true);
        txtDeskripsi.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JScrollPane scrollDesc = new JScrollPane(txtDeskripsi);
        scrollDesc.setBounds(30, y + 22, 330, 60);
        scrollDesc.setBorder(BorderFactory.createLineBorder(new Color(200, 210, 220)));
        pnlKiri.add(scrollDesc);

        // Tombol aksi
        y += 100;
        btnTambah = buatTombolAksi("Add",       new Color(39, 174, 96));
        btnEdit   = buatTombolAksi("Save Edit", BLUE_DARK);
        btnHapus  = buatTombolAksi("Delete",    new Color(192, 57, 43));
        btnBatal  = buatTombolAksi("Cancel",    new Color(150, 150, 160));

        btnTambah.setBounds(30,  y, 155, 36);
        btnEdit.setBounds(205,   y, 155, 36);
        btnHapus.setBounds(30,   y + 44, 155, 36);
        btnBatal.setBounds(205,  y + 44, 155, 36);

        pnlKiri.add(btnTambah);
        pnlKiri.add(btnEdit);
        pnlKiri.add(btnHapus);
        pnlKiri.add(btnBatal);

        btnEdit.setEnabled(false);
        btnHapus.setEnabled(false);

        // Tombol kembali
        btnKembali = new JButton("← Back to Dashboard");
        btnKembali.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnKembali.setForeground(BLUE_MAIN);
        btnKembali.setBackground(Color.WHITE);
        btnKembali.setBorderPainted(false);
        btnKembali.setFocusPainted(false);
        btnKembali.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnKembali.setBounds(30, y + 90, 200, 28);
        pnlKiri.add(btnKembali);

        root.add(pnlKiri, BorderLayout.WEST);

        // ===== PANEL KANAN (TABEL + WAVE) =====
        JPanel pnlKanan = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background biru muda
                g2.setColor(BLUE_LIGHT);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Wave di kiri
                g2.setColor(Color.WHITE);
                Path2D wave = new Path2D.Double();
                wave.moveTo(0, 0);
                wave.curveTo(60, getHeight() * 0.25, -20, getHeight() * 0.5, 60, getHeight() * 0.75);
                wave.curveTo(100, getHeight() * 0.88, 40, getHeight(), 0, getHeight());
                wave.lineTo(0, 0);
                wave.closePath();
                g2.fill(wave);

                // Wave garis biru
                g2.setColor(new Color(BLUE_MAIN.getRed(), BLUE_MAIN.getGreen(), BLUE_MAIN.getBlue(), 120));
                g2.setStroke(new BasicStroke(3f));
                Path2D waveLine = new Path2D.Double();
                waveLine.moveTo(30, 0);
                waveLine.curveTo(90, getHeight() * 0.25, 10, getHeight() * 0.5, 90, getHeight() * 0.75);
                waveLine.curveTo(130, getHeight() * 0.88, 70, getHeight(), 30, getHeight());
                g2.draw(waveLine);
            }
        };
        pnlKanan.setOpaque(false);

        // Search bar
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 10));
        pnlSearch.setOpaque(false);

        JLabel lblSearch = new JLabel("Search:");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblSearch.setForeground(TEXT_DARK);
        pnlSearch.add(lblSearch);

        txtSearch = buatTextField();
        txtSearch.setPreferredSize(new Dimension(200, 32));
        pnlSearch.add(txtSearch);

        btnCari  = buatTombolAksi("Search", BLUE_DARK);
        btnReset = buatTombolAksi("Reset",  new Color(150, 150, 160));
        btnCari.setPreferredSize(new Dimension(85, 32));
        btnReset.setPreferredSize(new Dimension(75, 32));
        pnlSearch.add(btnCari);
        pnlSearch.add(btnReset);

        // Tabel
        String[] kolom = {"ID", "Hotel Name", "Location", "Price / Night", "Image File"};
        tableModel = new DefaultTableModel(kolom, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblHotel = new JTable(tableModel);
        tblHotel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblHotel.setRowHeight(28);
        tblHotel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblHotel.setGridColor(new Color(220, 232, 245));
        tblHotel.setShowVerticalLines(false);
        tblHotel.setBackground(Color.WHITE);
        tblHotel.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tblHotel.getTableHeader().setBackground(BLUE_DARK);
        tblHotel.getTableHeader().setForeground(Color.WHITE);
        tblHotel.getTableHeader().setPreferredSize(new Dimension(0, 36));
        tblHotel.getColumnModel().getColumn(0).setMaxWidth(40);
        tblHotel.getColumnModel().getColumn(1).setPreferredWidth(160);
        tblHotel.getColumnModel().getColumn(2).setPreferredWidth(100);
        tblHotel.getColumnModel().getColumn(3).setPreferredWidth(120);
        tblHotel.getColumnModel().getColumn(4).setPreferredWidth(150);

        // Renderer warna selang-seling
        tblHotel.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int r, int c) {
                super.getTableCellRendererComponent(t, val, sel, foc, r, c);
                if (sel) {
                    setBackground(new Color(190, 225, 255));
                    setForeground(TEXT_DARK);
                } else {
                    setBackground(r % 2 == 0 ? Color.WHITE : new Color(240, 248, 255));
                    setForeground(TEXT_DARK);
                }
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return this;
            }
        });

        scrollTable = new JScrollPane(tblHotel);
        scrollTable.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        scrollTable.setOpaque(false);
        scrollTable.getViewport().setOpaque(false);

        JPanel pnlTabelWrapper = new JPanel(new BorderLayout());
        pnlTabelWrapper.setOpaque(false);
        pnlTabelWrapper.setBorder(BorderFactory.createEmptyBorder(0, 50, 10, 15));
        pnlTabelWrapper.add(pnlSearch, BorderLayout.NORTH);
        pnlTabelWrapper.add(scrollTable, BorderLayout.CENTER);

        pnlKanan.add(pnlTabelWrapper, BorderLayout.CENTER);
        root.add(pnlKanan, BorderLayout.CENTER);

        // ===== EVENT LISTENERS =====
        tblHotel.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) isiFormDariTabel();
        });
        btnTambah.addActionListener(e -> tambahHotel());
        btnEdit.addActionListener(e -> editHotel());
        btnHapus.addActionListener(e -> hapusHotel());
        btnBatal.addActionListener(e -> resetForm());
        btnCari.addActionListener(e -> loadTable(txtSearch.getText().trim()));
        btnReset.addActionListener(e -> { txtSearch.setText(""); loadTable(""); });
        btnKembali.addActionListener(e -> { new AdminFrames().setVisible(true); dispose(); });
    }

    // ===================== LOAD TABEL =====================
    private void loadTable(String keyword) {
        tableModel.setRowCount(0);
        List<Hotel> list = keyword.isEmpty()
                ? hotelDAO.getAllHotels()
                : hotelDAO.searchHotels(keyword);
        for (Hotel h : list) {
            tableModel.addRow(new Object[]{
                h.getHotelId(), h.getNama(), h.getLokasi(),
                "Rp " + h.getHarga().toPlainString(), h.getGambar()
            });
        }
    }

    // ===================== ISI FORM DARI TABEL =====================
    private void isiFormDariTabel() {
        int row = tblHotel.getSelectedRow();
        if (row < 0) return;
        selectedHotelId = (int) tableModel.getValueAt(row, 0);
        Hotel h = hotelDAO.getHotelById(selectedHotelId);
        if (h == null) return;
        txtNama.setText(h.getNama());
        txtLokasi.setText(h.getLokasi());
        txtHarga.setText(h.getHarga().toPlainString());
        txtGambar.setText(h.getGambar());
        txtDeskripsi.setText(h.getDeskripsi());
        btnEdit.setEnabled(true);
        btnHapus.setEnabled(true);
        btnTambah.setEnabled(false);
    }

    // ===================== TAMBAH =====================
    private void tambahHotel() {
        if (!validasiForm()) return;
        Hotel h = new Hotel(0,
                txtNama.getText().trim(), txtLokasi.getText().trim(),
                txtDeskripsi.getText().trim(),
                new BigDecimal(txtHarga.getText().trim()),
                txtGambar.getText().trim());
        int id = hotelDAO.insert(h);
        if (id > 0) {
            JOptionPane.showMessageDialog(this, "Hotel berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            resetForm(); loadTable("");
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menambahkan hotel.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ===================== EDIT =====================
    private void editHotel() {
        if (selectedHotelId < 0) return;
        if (!validasiForm()) return;
        Hotel h = new Hotel(selectedHotelId,
                txtNama.getText().trim(), txtLokasi.getText().trim(),
                txtDeskripsi.getText().trim(),
                new BigDecimal(txtHarga.getText().trim()),
                txtGambar.getText().trim());
        boolean ok = hotelDAO.update(h);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Hotel berhasil diperbarui!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            resetForm(); loadTable("");
        } else {
            JOptionPane.showMessageDialog(this, "Gagal memperbarui hotel.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ===================== HAPUS =====================
    private void hapusHotel() {
        if (selectedHotelId < 0) return;
        int konfirm = JOptionPane.showConfirmDialog(this,
                "Yakin ingin menghapus hotel ini?", "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (konfirm != JOptionPane.YES_OPTION) return;
        boolean ok = hotelDAO.delete(selectedHotelId);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Hotel berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            resetForm(); loadTable("");
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menghapus hotel.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ===================== VALIDASI =====================
    private boolean validasiForm() {
        if (txtNama.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama hotel tidak boleh kosong!", "Validasi", JOptionPane.WARNING_MESSAGE);
            txtNama.requestFocus(); return false;
        }
        if (txtLokasi.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lokasi tidak boleh kosong!", "Validasi", JOptionPane.WARNING_MESSAGE);
            txtLokasi.requestFocus(); return false;
        }
        try {
            new BigDecimal(txtHarga.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Harga harus berupa angka! Contoh: 850000", "Validasi", JOptionPane.WARNING_MESSAGE);
            txtHarga.requestFocus(); return false;
        }
        return true;
    }

    // ===================== RESET FORM =====================
    private void resetForm() {
        selectedHotelId = -1;
        txtNama.setText(""); txtLokasi.setText("");
        txtHarga.setText(""); txtGambar.setText("");
        txtDeskripsi.setText("");
        tblHotel.clearSelection();
        btnTambah.setEnabled(true);
        btnEdit.setEnabled(false);
        btnHapus.setEnabled(false);
    }

    // ===================== HELPERS =====================
    private Rectangle labelBounds(int x, int y) {
        return new Rectangle(x, y, 320, 18);
    }

    private JLabel buatLabel(String teks) {
        JLabel lbl = new JLabel(teks);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(TEXT_GRAY);
        return lbl;
    }

    private void pnlKiri_add(JPanel p, JLabel lbl, Rectangle r) {
        lbl.setBounds(r);
        p.add(lbl);
    }

    private JTextField buatTextField() {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 215, 230)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return tf;
    }

    private JButton buatTombolAksi(String teks, Color warna) {
        JButton btn = new JButton(teks);
        btn.setBackground(warna);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new KelolaHotel().setVisible(true));
    }
}