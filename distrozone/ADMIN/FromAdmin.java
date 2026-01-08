package distrozone.ADMIN;

import distrozone.ADMIN.DATA_KARYAWAN.FormDataKaryawan;
import distrozone.ADMIN.DATA_KAOS.FormDataKaos;
import distrozone.ADMIN.TRANSAKSI.FormRiwayatTransaksi;
import distrozone.ADMIN.LAPORAN.FormLaporanAdmin; // 🔥 NEW
import distrozone.DataBaseConector;
import javax.swing.*;
import java.awt.*;

/**
 * Dashboard Admin - Updated with New Laporan Keuangan
 */
public class FromAdmin extends JFrame {

    private JLabel lblNama, lblLevel;

    public FromAdmin(String nama, String level) {

        setTitle("DistroZone - Dashboard Admin");
        setSize(1150, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // --- MAIN CONTAINER dengan Gradient ---
        GradientPanel mainPanel = new GradientPanel(
                new Color(17, 24, 39),
                new Color(31, 41, 55));
        mainPanel.setLayout(new BorderLayout());

        // --- SIDEBAR dengan Gradient ---
        GradientPanel sidebar = new GradientPanel(
                new Color(30, 41, 59),
                new Color(15, 23, 42));
        sidebar.setPreferredSize(new Dimension(260, 750));
        sidebar.setLayout(null);
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 3, new Color(100, 116, 139)));

        // Logo/Brand Section
        JPanel brandPanel = new JPanel();
        brandPanel.setBounds(20, 25, 220, 80);
        brandPanel.setOpaque(false);
        brandPanel.setLayout(new BorderLayout(10, 5));

        JLabel lblApp = new JLabel("DISTRO ZONE");
        lblApp.setFont(new Font("Arial Black", Font.BOLD, 24));
        lblApp.setForeground(new Color(251, 146, 60));

        JLabel lblTagline = new JLabel("Admin Dashboard");
        lblTagline.setFont(new Font("Arial", Font.PLAIN, 12));
        lblTagline.setForeground(new Color(148, 163, 184));

        brandPanel.add(lblApp, BorderLayout.CENTER);
        brandPanel.add(lblTagline, BorderLayout.SOUTH);
        sidebar.add(brandPanel);

        // User Info Section dengan gradient box
        JPanel userPanel = new JPanel();
        userPanel.setBounds(20, 120, 220, 65);
        userPanel.setBackground(new Color(51, 65, 85));
        userPanel.setLayout(new GridLayout(2, 1, 5, 3));
        userPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 116, 139), 2),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        lblNama = new JLabel("👤 " + nama);
        lblNama.setForeground(Color.WHITE);
        lblNama.setFont(new Font("Arial", Font.BOLD, 15));

        lblLevel = new JLabel("⭐ " + level);
        lblLevel.setForeground(new Color(251, 146, 60));
        lblLevel.setFont(new Font("Arial", Font.BOLD, 13));

        userPanel.add(lblNama);
        userPanel.add(lblLevel);
        sidebar.add(userPanel);

        // --- MENU BUTTONS ---
        int menuStartY = 210;
        int menuSpacing = 52;

        sidebar.add(makeMenuButton("📊 Dashboard", menuStartY, true));

        JButton btnDataKaryawan = makeMenuButton("👥 Data Karyawan", menuStartY + menuSpacing, false);
        sidebar.add(btnDataKaryawan);
        btnDataKaryawan.addActionListener(e -> {
            new FormDataKaryawan().setVisible(true);
        });

        JButton btnDataKaos = makeMenuButton("👕 Data Kaos", menuStartY + menuSpacing * 2, false);
        btnDataKaos.addActionListener(e -> {
            new FormDataKaos().setVisible(true);
        });
        sidebar.add(btnDataKaos);

        // Riwayat Transaksi
        JButton btnRiwayatTransaksi = makeMenuButton("🧾 Riwayat Transaksi", menuStartY + menuSpacing * 3, false);
        btnRiwayatTransaksi.addActionListener(e -> {
            new FormRiwayatTransaksi().setVisible(true);
        });
        sidebar.add(btnRiwayatTransaksi);

        // 🔥 MENU BARU: Laporan Keuangan (Menggantikan Laporan lama)
        JButton btnLaporan = makeMenuButton("📈 Laporan Keuangan", menuStartY + menuSpacing * 4, false);
        btnLaporan.addActionListener(e -> {
            new FormLaporanAdmin().setVisible(true);
        });
        sidebar.add(btnLaporan);

        // 🕐 MENU BARU: Jam Operasional
        JButton btnJamOperasional = makeMenuButton("🕐 Jam Operasional", menuStartY + menuSpacing * 5, false);
        btnJamOperasional.addActionListener(e -> {
            new distrozone.ADMIN.PENGATURAN.FormJamOperasional().setVisible(true);
        });
        sidebar.add(btnJamOperasional);

        JButton btnLogout = makeMenuButton("🚪 Logout", menuStartY + menuSpacing * 6, false);
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Apakah Anda yakin ingin logout?",
                    "Konfirmasi Logout",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                // TODO: Kembali ke login form
                // new FormLogin().setVisible(true);
            }
        });
        sidebar.add(btnLogout);

        // --- CONTENT PANEL dengan Gradient ---
        GradientPanel content = new GradientPanel(
                new Color(31, 41, 55),
                new Color(55, 65, 81));
        content.setLayout(null);

        // Title
        JLabel lblTitle = new JLabel("Dashboard Admin");
        lblTitle.setBounds(30, 20, 400, 40);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        content.add(lblTitle);

        // Subtitle
        JLabel lblSubtitle = new JLabel("Selamat datang, " + nama + "!");
        lblSubtitle.setBounds(30, 65, 400, 25);
        lblSubtitle.setFont(new Font("Arial", Font.PLAIN, 16));
        lblSubtitle.setForeground(new Color(200, 200, 200));
        content.add(lblSubtitle);

        // Get statistics from database
        int totalKaos = getCount("tb_kaos");
        int totalKaryawan = getCount("tb_karyawan");
        int totalTransaksi = getCount("tb_transaksi");

        // Dashboard Cards
        content.add(createCard("Total Kaos", totalKaos, "👕", new Color(52, 152, 219), 30, 120));
        content.add(createCard("Total Karyawan", totalKaryawan, "👥", new Color(155, 89, 182), 300, 120));
        content.add(createCard("Total Transaksi", totalTransaksi, "🧾", new Color(46, 204, 113), 570, 120));

        // Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setBounds(30, 270, 780, 150);
        infoPanel.setBackground(new Color(50, 50, 55));
        infoPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(255, 87, 51), 2),
                "ℹ️ Informasi Sistem",
                0, 0,
                new Font("Arial", Font.BOLD, 14),
                Color.WHITE));
        infoPanel.setLayout(new GridLayout(3, 1, 10, 10));

        JLabel lblInfo1 = new JLabel("✅ Sistem Transaksi: Riwayat transaksi tersimpan permanen (READ-ONLY)");
        lblInfo1.setForeground(Color.WHITE);
        lblInfo1.setFont(new Font("Arial", Font.PLAIN, 13));
        infoPanel.add(lblInfo1);

        JLabel lblInfo2 = new JLabel("✅ Laporan Keuangan: Ringkasan laba rugi dan daftar transaksi lengkap");
        lblInfo2.setForeground(Color.WHITE);
        lblInfo2.setFont(new Font("Arial", Font.PLAIN, 13));
        infoPanel.add(lblInfo2);

        JLabel lblInfo3 = new JLabel("✅ Keamanan: Harga dikunci saat transaksi, tidak dapat diubah");
        lblInfo3.setForeground(Color.WHITE);
        lblInfo3.setFont(new Font("Arial", Font.PLAIN, 13));
        infoPanel.add(lblInfo3);

        content.add(infoPanel);

        // Quick Access Panel
        JPanel quickPanel = new JPanel();
        quickPanel.setBounds(30, 450, 780, 150);
        quickPanel.setBackground(new Color(50, 50, 55));
        quickPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
                "⚡ Quick Access",
                0, 0,
                new Font("Arial", Font.BOLD, 14),
                Color.WHITE));
        quickPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        JButton btnQuickRiwayat = createQuickButton("🧾 Riwayat", new Color(52, 152, 219));
        btnQuickRiwayat.addActionListener(e -> new FormRiwayatTransaksi().setVisible(true));
        quickPanel.add(btnQuickRiwayat);

        JButton btnQuickLaporan = createQuickButton("📈 Laporan", new Color(46, 204, 113));
        btnQuickLaporan.addActionListener(e -> new FormLaporanAdmin().setVisible(true));
        quickPanel.add(btnQuickLaporan);

        JButton btnQuickKaos = createQuickButton("👕 Data Kaos", new Color(155, 89, 182));
        btnQuickKaos.addActionListener(e -> new FormDataKaos().setVisible(true));
        quickPanel.add(btnQuickKaos);

        JButton btnQuickKaryawan = createQuickButton("👥 Karyawan", new Color(241, 196, 15));
        btnQuickKaryawan.addActionListener(e -> new FormDataKaryawan().setVisible(true));
        quickPanel.add(btnQuickKaryawan);

        content.add(quickPanel);

        // Add to main container
        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(content, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JButton makeMenuButton(String title, int y, boolean isActive) {
        JButton btn = new JButton(title);
        btn.setBounds(15, y, 230, 46);

        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setForeground(Color.BLACK);

        if (isActive) {
            btn.setBackground(new Color(59, 130, 246));
        } else {
            btn.setBackground(new Color(51, 65, 85));
        }

        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 116, 139), 1),
                BorderFactory.createEmptyBorder(0, 15, 0, 0)));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!isActive) {
                    btn.setBackground(new Color(71, 85, 105));
                    btn.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(148, 163, 184), 2),
                            BorderFactory.createEmptyBorder(0, 15, 0, 0)));
                }
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!isActive) {
                    btn.setBackground(new Color(51, 65, 85));
                    btn.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(100, 116, 139), 1),
                            BorderFactory.createEmptyBorder(0, 15, 0, 0)));
                }
            }
        });

        return btn;
    }

    private JButton createQuickButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setBackground(color);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(150, 50));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(color.brighter());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(color);
            }
        });

        return btn;
    }

    private int getCount(String tableName) {
        int total = 0;
        try {
            java.sql.Connection conn = DataBaseConector.getConnection();
            String sql = "SELECT COUNT(*) FROM " + tableName;
            java.sql.PreparedStatement pst = conn.prepareStatement(sql);
            java.sql.ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error ambil data: " + e.getMessage());
        }
        return total;
    }

    private JPanel createCard(String title, int value, String emoji, Color color, int x, int y) {
        Color color1 = color;
        Color color2 = color.darker();

        GradientPanel card = new GradientPanel(color1, color2);
        card.setBounds(x, y, 260, 145);
        card.setLayout(null);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color.brighter(), 3),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        JLabel lblEmoji = new JLabel(emoji);
        lblEmoji.setBounds(15, 15, 60, 60);
        lblEmoji.setFont(new Font("Arial", Font.PLAIN, 48));
        card.add(lblEmoji);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setBounds(15, 90, 230, 22);
        lblTitle.setForeground(new Color(255, 255, 255, 230));
        lblTitle.setFont(new Font("Arial", Font.BOLD, 14));
        card.add(lblTitle);

        JLabel lblValue = new JLabel(String.valueOf(value));
        lblValue.setBounds(95, 15, 150, 60);
        lblValue.setForeground(Color.WHITE);
        lblValue.setFont(new Font("Arial Black", Font.BOLD, 42));
        lblValue.setHorizontalAlignment(SwingConstants.RIGHT);
        card.add(lblValue);

        return card;
    }
}

/**
 * Custom Gradient Panel Component
 */
class GradientPanel extends JPanel {
    private Color color1;
    private Color color2;

    public GradientPanel(Color color1, Color color2) {
        this.color1 = color1;
        this.color2 = color2;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        int w = getWidth();
        int h = getHeight();

        GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
    }
}