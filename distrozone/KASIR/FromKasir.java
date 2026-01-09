package distrozone.KASIR;

import distrozone.Login;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FromKasir extends JFrame {

    private JLabel lblNama, lblLevel;
    private JPanel contentPanel;
    private String namaKasir;
    private int idKaryawan;

    public FromKasir(int idKaryawan, String nama, String level) {
        this.namaKasir = nama;
        this.idKaryawan = idKaryawan;

        setTitle("DistroZone - Kasir Dashboard");
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Main Container
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(25, 25, 30));

        // === SIDEBAR ===
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(280, 750));
        sidebar.setBackground(new Color(30, 30, 35));
        sidebar.setLayout(null);

        // Brand Logo
        JLabel lblApp = new JLabel("DISTRO");
        lblApp.setBounds(30, 30, 220, 35);
        lblApp.setFont(new Font("Arial Black", Font.BOLD, 28));
        lblApp.setForeground(Color.WHITE);
        sidebar.add(lblApp);

        JLabel lblAppZone = new JLabel("ZONE");
        lblAppZone.setBounds(30, 65, 220, 30);
        lblAppZone.setFont(new Font("Arial Black", Font.BOLD, 24));
        lblAppZone.setForeground(new Color(255, 87, 51));
        sidebar.add(lblAppZone);

        // User Info Panel
        JPanel userPanel = new JPanel();
        userPanel.setBounds(20, 130, 240, 80);
        userPanel.setBackground(new Color(40, 40, 45));
        userPanel.setLayout(null);
        userPanel.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 65), 1));

        lblNama = new JLabel("Hi, " + nama);
        lblNama.setBounds(15, 15, 210, 25);
        lblNama.setForeground(Color.WHITE);
        lblNama.setFont(new Font("Arial", Font.BOLD, 16));
        userPanel.add(lblNama);

        lblLevel = new JLabel("Role: " + level);
        lblLevel.setBounds(15, 42, 210, 20);
        lblLevel.setForeground(new Color(52, 152, 219));
        lblLevel.setFont(new Font("Arial", Font.BOLD, 13));
        userPanel.add(lblLevel);

        sidebar.add(userPanel);

        // Menu Label
        JLabel lblMenu = new JLabel("MENU KASIR");
        lblMenu.setBounds(30, 235, 220, 20);
        lblMenu.setFont(new Font("Arial", Font.BOLD, 12));
        lblMenu.setForeground(new Color(150, 150, 150));
        sidebar.add(lblMenu);

        // Menu Buttons - SEMUA HANYA BUKA FORM TRANSAKSI
        JButton btnDashboard = makeMenuButton("📊 Dashboard", 270);
        btnDashboard.addActionListener(e -> {
            // Dashboard tidak melakukan apa-apa, sudah tampil default
        });
        sidebar.add(btnDashboard);

        JButton btnTransaksi = makeMenuButton("🛒 Transaksi Baru", 320);
        btnTransaksi.addActionListener(e -> {
            FormTransaksi form = new FormTransaksi(this.idKaryawan, this.namaKasir);
            form.setVisible(true);
            form.toFront();
        });
        sidebar.add(btnTransaksi);

        JButton btnRiwayat = makeMenuButton("📜 Riwayat Transaksi", 370);
        btnRiwayat.addActionListener(e -> {
            FormRiwayatTransaksi formRiwayat = new FormRiwayatTransaksi(this.idKaryawan, this.namaKasir);
            formRiwayat.setVisible(true);
        });
        sidebar.add(btnRiwayat);

        JButton btnProduk = makeMenuButton("📦 Cek Stok Produk", 420);
        btnProduk.addActionListener(e -> {
            new FormLihatStok().setVisible(true);
        });
        sidebar.add(btnProduk);

        // Logout Button
        JButton btnLogout = new JButton("🚪 Logout");
        btnLogout.setBounds(30, 650, 220, 45);
        btnLogout.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogout.setFocusPainted(false);
        btnLogout.setForeground(Color.BLACK);
        btnLogout.setBackground(new Color(220, 53, 69));
        btnLogout.setBorder(BorderFactory.createEmptyBorder());
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnLogout.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnLogout.setBackground(new Color(200, 35, 51));
            }

            public void mouseExited(MouseEvent evt) {
                btnLogout.setBackground(new Color(220, 53, 69));
            }
        });

        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Apakah Anda yakin ingin logout?",
                    "Konfirmasi Logout",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose();
                new Login().setVisible(true);
            }
        });

        sidebar.add(btnLogout);

        // === CONTENT PANEL ===
        contentPanel = new JPanel();
        contentPanel.setBackground(new Color(25, 25, 30));
        contentPanel.setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setPreferredSize(new Dimension(920, 80));
        headerPanel.setBackground(new Color(30, 30, 35));
        headerPanel.setLayout(null);

        JLabel lblHeader = new JLabel("Kasir Dashboard");
        lblHeader.setBounds(30, 20, 400, 40);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 28));
        lblHeader.setForeground(Color.WHITE);
        headerPanel.add(lblHeader);

        // Clock/Date Display - Dynamic
        String[] days = { "Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu" };
        java.util.Calendar cal = java.util.Calendar.getInstance();
        String dayName = days[cal.get(java.util.Calendar.DAY_OF_WEEK) - 1];
        String dateStr = new java.text.SimpleDateFormat("dd MMMM yyyy | HH:mm").format(new java.util.Date());

        JLabel lblDate = new JLabel(dayName + ", " + dateStr + " WIB");
        lblDate.setBounds(30, 60, 400, 20);
        lblDate.setFont(new Font("Arial", Font.PLAIN, 13));
        lblDate.setForeground(new Color(150, 150, 150));
        headerPanel.add(lblDate);

        contentPanel.add(headerPanel, BorderLayout.NORTH);

        // Dashboard Content
        JPanel dashboardContent = createKasirDashboard(nama);
        contentPanel.add(dashboardContent, BorderLayout.CENTER);

        // Add to main
        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JButton makeMenuButton(String title, int y) {
        JButton btn = new JButton(title);
        btn.setBounds(30, y, 220, 45);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setForeground(Color.BLACK);
        btn.setBackground(new Color(40, 40, 45));
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMargin(new Insets(0, 15, 0, 0));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btn.setBackground(new Color(52, 152, 219));
            }

            public void mouseExited(MouseEvent evt) {
                btn.setBackground(new Color(40, 40, 45));
            }
        });

        return btn;
    }

    private JPanel createKasirDashboard(String nama) {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(25, 25, 30));
        panel.setLayout(null);

        // Welcome Message
        JLabel lblWelcome = new JLabel("Selamat Bekerja, " + nama + "!");
        lblWelcome.setBounds(30, 30, 600, 40);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 28));
        lblWelcome.setForeground(Color.WHITE);
        panel.add(lblWelcome);

        JLabel lblSubtext = new JLabel("Layani pelanggan dengan senyuman terbaik");
        lblSubtext.setBounds(30, 75, 600, 20);
        lblSubtext.setFont(new Font("Arial", Font.PLAIN, 14));
        lblSubtext.setForeground(new Color(150, 150, 150));
        panel.add(lblSubtext);

        // Quick Transaction Panel
        JPanel transactionPanel = new JPanel();
        transactionPanel.setBounds(30, 130, 860, 200);
        transactionPanel.setBackground(new Color(40, 40, 45));
        transactionPanel.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 65), 2));
        transactionPanel.setLayout(null);

        JLabel lblTransaction = new JLabel("Mulai Transaksi");
        lblTransaction.setBounds(30, 20, 300, 30);
        lblTransaction.setFont(new Font("Arial", Font.BOLD, 20));
        lblTransaction.setForeground(Color.WHITE);
        transactionPanel.add(lblTransaction);

        JLabel lblDesc = new JLabel("Klik tombol di bawah untuk memulai transaksi penjualan baru");
        lblDesc.setBounds(30, 50, 600, 20);
        lblDesc.setFont(new Font("Arial", Font.PLAIN, 13));
        lblDesc.setForeground(new Color(150, 150, 150));
        transactionPanel.add(lblDesc);

        // Button Transaksi Baru
        JButton btnNewTransaction = new JButton("MULAI TRANSAKSI BARU");
        btnNewTransaction.setBounds(30, 100, 800, 70);
        btnNewTransaction.setFont(new Font("Arial", Font.BOLD, 20));
        btnNewTransaction.setBackground(new Color(46, 204, 113));
        btnNewTransaction.setForeground(Color.BLACK);
        btnNewTransaction.setBorder(BorderFactory.createEmptyBorder());
        btnNewTransaction.setFocusPainted(false);
        btnNewTransaction.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnNewTransaction.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnNewTransaction.setBackground(new Color(39, 174, 96));
            }

            public void mouseExited(MouseEvent e) {
                btnNewTransaction.setBackground(new Color(46, 204, 113));
            }
        });

        btnNewTransaction.addActionListener(e -> {
            FormTransaksi form = new FormTransaksi(this.idKaryawan, this.namaKasir);
            form.setVisible(true);
            form.toFront();
        });

        transactionPanel.add(btnNewTransaction);
        panel.add(transactionPanel);

        // Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setBounds(30, 360, 860, 100);
        infoPanel.setBackground(new Color(40, 40, 45));
        infoPanel.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 65), 2));
        infoPanel.setLayout(null);

        JLabel lblInfo = new JLabel("Menu Lainnya");
        lblInfo.setBounds(30, 20, 300, 25);
        lblInfo.setFont(new Font("Arial", Font.BOLD, 16));
        lblInfo.setForeground(Color.WHITE);
        infoPanel.add(lblInfo);

        JLabel lblInfoDesc = new JLabel("Gunakan menu di sidebar untuk mengakses Riwayat Transaksi dan fitur lainnya");
        lblInfoDesc.setBounds(30, 50, 800, 20);
        lblInfoDesc.setFont(new Font("Arial", Font.PLAIN, 13));
        lblInfoDesc.setForeground(new Color(150, 150, 150));
        infoPanel.add(lblInfoDesc);

        panel.add(infoPanel);

        return panel;
    }
}
