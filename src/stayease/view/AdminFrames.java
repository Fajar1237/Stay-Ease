package stayease.view;

import stayease.util.DBConnection;
import stayease.util.Session;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminFrames extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
        java.util.logging.Logger.getLogger(AdminFrames.class.getName());

    private static final Color BLUE_MAIN  = new Color(41, 171, 226);
    private static final Color BLUE_DARK  = new Color(26, 115, 180);
    private static final Color BLUE_LIGHT = new Color(235, 246, 255);
    private static final Color BG         = new Color(245, 247, 250);
    private static final Color TEXT_DARK  = new Color(30,  30,  60);
    private static final Color TEXT_GRAY  = new Color(120, 130, 150);
    private static final Color WHITE      = Color.WHITE;

    private JLabel lblTotalHotel, lblTotalBooking, lblTotalRevenue, lblTotalUser;

    public AdminFrames() {
        initComponents();
        // ── [SESSION GUARD] hanya admin yang boleh ──
        if (!stayease.util.Session.isLoggedIn() || !stayease.util.Session.isAdmin()) {
            javax.swing.JOptionPane.showMessageDialog(this, "Access denied. Admins only.",
                    "Session", javax.swing.JOptionPane.ERROR_MESSAGE);
            new LoginFrame().setVisible(true);
            javax.swing.SwingUtilities.invokeLater(this::dispose);
            return;
        }
        refreshStats();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(985, 580);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);
        setContentPane(root);

        root.add(buatSidebar(), BorderLayout.WEST);
        root.add(buatMainPanel(), BorderLayout.CENTER);
    }

    private ImageIcon loadIcon(String path, int w, int h) {
        try {
            ImageIcon raw = new ImageIcon(getClass().getResource(path));
            return new ImageIcon(raw.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
        } catch (Exception e) {
            return null;
        }
    }

    private JPanel buatSidebar() {
        JPanel sidebar = new JPanel(null);
        sidebar.setBackground(WHITE);
        sidebar.setPreferredSize(new Dimension(210, 580));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(225, 230, 240)));

        ImageIcon appIcon = loadIcon("/images/Icon/icon.png", 30, 30);
        JLabel lblIcon = new JLabel(appIcon);
        lblIcon.setBounds(16, 20, 30, 30);
        sidebar.add(lblIcon);

        JLabel lblApp = new JLabel("StayEase");
        lblApp.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblApp.setForeground(TEXT_DARK);
        lblApp.setBounds(52, 24, 120, 24);
        sidebar.add(lblApp);

        JSeparator sep = new JSeparator();
        sep.setBounds(0, 65, 210, 1);
        sep.setForeground(new Color(230, 235, 245));
        sidebar.add(sep);

        JLabel lblMenu = new JLabel("MENU");
        lblMenu.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblMenu.setForeground(TEXT_GRAY);
        lblMenu.setBounds(16, 82, 160, 18);
        sidebar.add(lblMenu);

        String[] labels = {"Manage Hotel", "User Data", "Report"};
        String[] icons  = {
            "/images/Icon/home_icon.png",
            "/images/Icon/user_icon.png",
            "/images/Icon/chart_icon.png"
        };

        int y = 108;
        for (int i = 0; i < labels.length; i++) {
            ImageIcon ic = loadIcon(icons[i], 18, 18);
            JButton btn = buatMenuBtn(labels[i], ic);
            btn.setBounds(0, y, 210, 44);
            sidebar.add(btn);

            String label = labels[i];
            btn.addActionListener(e -> {
                switch (label) {
                    case "Manage Hotel" -> { new KelolaHotel().setVisible(true); dispose(); }
                    case "User Data"  -> { new DataUser().setVisible(true);    dispose(); }
                    case "Report"       -> { new Laporan().setVisible(true);     dispose(); }
                }
            });
            y += 48;
        }

        JSeparator sep2 = new JSeparator();
        sep2.setBounds(0, 490, 210, 1);
        sep2.setForeground(new Color(230, 235, 245));
        sidebar.add(sep2);

        ImageIcon logoutIc = loadIcon("/images/Icon/logout_icon.png", 18, 18);
        JButton btnLogout = buatMenuBtn("Logout", logoutIc);
        btnLogout.setBounds(0, 498, 210, 44);
        btnLogout.setForeground(new Color(192, 57, 43));
        btnLogout.addActionListener(e -> {
            int ok = JOptionPane.showConfirmDialog(this,
                "Are you sure want to logout?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (ok == JOptionPane.YES_OPTION) {
                Session.logout();
                new LoginFrame().setVisible(true);
                dispose();
            }
        });
        sidebar.add(btnLogout);

        return sidebar;
    }

    private JPanel buatMainPanel() {
        JPanel main = new JPanel(null);
        main.setBackground(BG);

        JLabel lblTitle = new JLabel("Dashboard Overview");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(TEXT_DARK);
        lblTitle.setBounds(24, 24, 400, 30);
        main.add(lblTitle);

        JLabel lblSub = new JLabel("Welcome back, let's see how your properties are performing.");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(TEXT_GRAY);
        lblSub.setBounds(24, 54, 500, 20);
        main.add(lblSub);

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRefresh.setBackground(BLUE_DARK);
        btnRefresh.setForeground(WHITE);
        btnRefresh.setBorderPainted(false);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.setBounds(570, 30, 130, 34);
        btnRefresh.addActionListener(e -> refreshStats());
        main.add(btnRefresh);

        String[] cardTitles = {"TOTAL HOTELS", "ACTIVE BOOKINGS", "TOTAL REVENUE", "TOTAL USERS"};
        String[] cardIcons  = {
            "/images/Icon/home_icon.png",
            "/images/Icon/chart_icon.png",
            "/images/Icon/chart_icon.png",
            "/images/Icon/user_icon.png"
        };
        Color[] cardColors = {
            new Color(41, 171, 226),
            new Color(52, 152, 219),
            new Color(39, 174, 96),
            new Color(155, 89, 182)
        };

        JLabel[] valLabels = new JLabel[4];
        int cx = 18;
        for (int i = 0; i < 4; i++) {
            ImageIcon ic = loadIcon(cardIcons[i], 28, 28);
            JPanel card = buatCard(ic, cardTitles[i], cardColors[i]);
            card.setBounds(cx, 96, 172, 105);
            main.add(card);
            valLabels[i] = (JLabel) card.getClientProperty("valLabel");
            cx += 182;
        }

        lblTotalHotel   = valLabels[0];
        lblTotalBooking = valLabels[1];
        lblTotalRevenue = valLabels[2];
        lblTotalUser    = valLabels[3];

        JPanel pnlSummary = new JPanel(new BorderLayout());
        pnlSummary.setBackground(WHITE);
        pnlSummary.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(225, 232, 245)),
            BorderFactory.createEmptyBorder(16, 20, 16, 20)
        ));
        pnlSummary.setBounds(18, 218, 706, 300);

        JLabel lblTrend = new JLabel("Booking & Payment Summary");
        lblTrend.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTrend.setForeground(TEXT_DARK);
        pnlSummary.add(lblTrend, BorderLayout.NORTH);

        JTextArea txtInfo = new JTextArea();
        txtInfo.setEditable(false);
        txtInfo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtInfo.setForeground(TEXT_GRAY);
        txtInfo.setBackground(WHITE);
        txtInfo.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        txtInfo.setText(getSummaryText());

        JScrollPane scroll = new JScrollPane(txtInfo);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        pnlSummary.add(scroll, BorderLayout.CENTER);

        main.add(pnlSummary);
        return main;
    }

    private JPanel buatCard(ImageIcon icon, String title, Color valueColor) {
        JPanel card = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.setColor(new Color(225, 232, 245));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
            }
        };
        card.setOpaque(false);

        JLabel lblIcon = new JLabel(icon);
        lblIcon.setBounds(12, 12, 32, 32);
        card.add(lblIcon);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblTitle.setForeground(TEXT_GRAY);
        lblTitle.setBounds(12, 50, 155, 16);
        card.add(lblTitle);

        JLabel lblVal = new JLabel("...");
        lblVal.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblVal.setForeground(valueColor);
        lblVal.setBounds(12, 66, 155, 30);
        card.add(lblVal);

        card.putClientProperty("valLabel", lblVal);
        return card;
    }

    private JButton buatMenuBtn(String teks, ImageIcon icon) {
        JButton btn = new JButton(teks, icon);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(TEXT_DARK);
        btn.setBackground(WHITE);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setIconTextGap(10);
        btn.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 0));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(BLUE_LIGHT);
                btn.setForeground(BLUE_DARK);
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(WHITE);
                btn.setForeground(TEXT_DARK);
            }
        });
        return btn;
    }

    private void refreshStats() {
        try (Connection conn = DBConnection.getConnection()) {

            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM hotels");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) lblTotalHotel.setText(String.valueOf(rs.getInt(1)));
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM bookings");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) lblTotalBooking.setText(String.valueOf(rs.getInt(1)));
            }

            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT COALESCE(SUM(total_bayar), 0) FROM bookings");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    BigDecimal total = rs.getBigDecimal(1);
                    lblTotalRevenue.setText("Rp " + String.format("%,.0f", total));
                }
            }

            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM users");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) lblTotalUser.setText(String.valueOf(rs.getInt(1)));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat statistik:\n" + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getSummaryText() {
        StringBuilder sb = new StringBuilder();
        try (Connection conn = DBConnection.getConnection()) {

            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT status, COUNT(*) AS jml FROM bookings GROUP BY status");
                 ResultSet rs = ps.executeQuery()) {
                sb.append("Booking by Status:\n");
                boolean ada = false;
                while (rs.next()) {
                    sb.append("   • ").append(rs.getString("status"))
                      .append(": ").append(rs.getInt("jml")).append(" booking\n");
                    ada = true;
                }
                if (!ada) sb.append("   (No booking data yet)\n");
            }

            sb.append("\n");

            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT h.nama, COUNT(b.booking_id) AS jml FROM hotels h " +
                    "LEFT JOIN bookings b ON h.hotel_id = b.hotel_id " +
                    "GROUP BY h.hotel_id, h.nama ORDER BY jml DESC LIMIT 5");
                 ResultSet rs = ps.executeQuery()) {
                sb.append("Top Hotels by Booking:\n");
                while (rs.next()) {
                    sb.append("   • ").append(rs.getString("nama"))
                      .append(": ").append(rs.getInt("jml")).append(" booking\n");
                }
            }

            sb.append("\n");

            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT username, role FROM users ORDER BY user_id DESC LIMIT 5");
                 ResultSet rs = ps.executeQuery()) {
                sb.append("Recent Users:\n");
                while (rs.next()) {
                    sb.append("   • ").append(rs.getString("username"))
                      .append(" (").append(rs.getString("role")).append(")\n");
                }
            }

        } catch (SQLException e) {
            sb.append("Error loading summary: ").append(e.getMessage());
        }
        return sb.toString();
    }

    public static void main(String[] args) {
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
        java.awt.EventQueue.invokeLater(() -> new AdminFrames().setVisible(true));
    }
}