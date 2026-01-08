package distrozone.KASIR;

import distrozone.Model.*;
import distrozone.SERVICEnew.POSService;
import distrozone.SERVICEnew.StrukPDFService;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.io.File;

/**
 * FORM POS - PURE UI (NO BUSINESS LOGIC)
 * 
 * ISINYA:
 * - Menampilkan komponen UI
 * - Handle event (klik, ketik, dll)
 * - Panggil SERVICE untuk logic
 * - Tampilkan hasil/error dari SERVICE
 * 
 * TIDAK BOLEH:
 * - Tidak ada SQL
 * - Tidak ada validasi bisnis
 * - Tidak ada perhitungan kompleks
 * - Semua logic di SERVICE
 */
public class FormTransaksi extends JFrame {

    private int idKaryawan;
    private String namaKasir;

    // Services (All logic here)
    private POSService posService;
    private StrukPDFService strukService;

    // Data (Managed by SERVICE)
    private List<Kaos> allProduk;
    private List<Kaos> filteredProduk;
    private List<KeranjangItem> keranjang;

    // UI Components - Left Panel
    private JTextField txtSearch;
    private JPanel panelProdukGrid;
    private JLabel lblProdukCount;

    // UI Components - Right Panel
    private JPanel panelKeranjangList;
    private JLabel lblTotalItem, lblSubtotal, lblTotal;
    private JTextField txtCustomerName, txtCustomerPhone;
    private JComboBox<String> cmbMetodePembayaran;
    private JButton btnBayar;

    public FormTransaksi(int idKaryawan, String namaKasir) {
        this.idKaryawan = idKaryawan;
        this.namaKasir = namaKasir;

        // Initialize services
        this.posService = new POSService();
        this.strukService = new StrukPDFService();

        // Initialize data
        this.allProduk = new ArrayList<>();
        this.filteredProduk = new ArrayList<>();
        this.keranjang = new ArrayList<>();

        initComponents();
        loadProdukData();
    }

    private void initComponents() {
        setTitle("Point of Sale - DistroZone");
        setSize(1400, 850);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Auto full screen saat dibuka
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        add(createHeader(), BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(900);
        splitPane.setDividerSize(2);
        splitPane.setBackground(new Color(240, 240, 240));

        splitPane.setLeftComponent(createProdukPanel());
        splitPane.setRightComponent(createKeranjangPanel());

        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(52, 152, 219));
        panel.setPreferredSize(new Dimension(1400, 80));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        JPanel leftPanel = new JPanel(new GridLayout(2, 1));
        leftPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("🛒 Point of Sale");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblDate = new JLabel("Kamis, 08 Januari 2026 • " +
                new java.text.SimpleDateFormat("HH:mm").format(new Date()) + " WIB");
        lblDate.setFont(new Font("Arial", Font.PLAIN, 13));
        lblDate.setForeground(new Color(255, 255, 255, 200));

        leftPanel.add(lblTitle);
        leftPanel.add(lblDate);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);

        JLabel lblKasir = new JLabel("Kasir: " + namaKasir);
        lblKasir.setFont(new Font("Arial", Font.BOLD, 16));
        lblKasir.setForeground(Color.WHITE);
        rightPanel.add(lblKasir);

        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createProdukPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBackground(Color.WHITE);

        txtSearch = new JTextField();
        txtSearch.setFont(new Font("Arial", Font.PLAIN, 14));
        txtSearch.setPreferredSize(new Dimension(400, 40));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                handleLiveSearch();
            }
        });

        lblProdukCount = new JLabel("0 produk tersedia");
        lblProdukCount.setFont(new Font("Arial", Font.PLAIN, 12));
        lblProdukCount.setForeground(new Color(100, 100, 100));

        searchPanel.add(txtSearch, BorderLayout.CENTER);
        searchPanel.add(lblProdukCount, BorderLayout.EAST);

        panelProdukGrid = new JPanel();
        panelProdukGrid.setLayout(new GridLayout(0, 3, 15, 15));
        panelProdukGrid.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(panelProdukGrid);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createKeranjangPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JPanel headerLeft = new JPanel(new GridLayout(2, 1));
        headerLeft.setOpaque(false);

        JLabel lblKeranjangTitle = new JLabel("🛒 Keranjang");
        lblKeranjangTitle.setFont(new Font("Arial", Font.BOLD, 18));

        lblTotalItem = new JLabel("0 item");
        lblTotalItem.setFont(new Font("Arial", Font.PLAIN, 13));
        lblTotalItem.setForeground(new Color(100, 100, 100));

        headerLeft.add(lblKeranjangTitle);
        headerLeft.add(lblTotalItem);

        JButton btnClearAll = new JButton("Hapus Semua");
        btnClearAll.setFont(new Font("Arial", Font.PLAIN, 11));
        btnClearAll.setForeground(new Color(220, 53, 69));
        btnClearAll.setBackground(Color.WHITE);
        btnClearAll.setBorder(BorderFactory.createLineBorder(new Color(220, 53, 69), 1));
        btnClearAll.setFocusPainted(false);
        btnClearAll.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClearAll.addActionListener(e -> handleResetKeranjang());

        headerPanel.add(headerLeft, BorderLayout.WEST);
        headerPanel.add(btnClearAll, BorderLayout.EAST);

        panelKeranjangList = new JPanel();
        panelKeranjangList.setLayout(new BoxLayout(panelKeranjangList, BoxLayout.Y_AXIS));
        panelKeranjangList.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(panelKeranjangList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(createBottomKeranjangPanel(), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createBottomKeranjangPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(15, 0, 0, 0)));

        // Customer Info
        JLabel lblCustomerTitle = new JLabel("Customer (optional)");
        lblCustomerTitle.setFont(new Font("Arial", Font.PLAIN, 12));
        lblCustomerTitle.setForeground(new Color(100, 100, 100));
        lblCustomerTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtCustomerName = createStyledTextField("Nama customer");
        txtCustomerPhone = createStyledTextField("No. Telepon");

        // Subtotal
        JPanel subtotalPanel = createTotalRow("Subtotal", "Rp 0", false);
        lblSubtotal = (JLabel) ((JPanel) subtotalPanel.getComponent(1)).getComponent(0);

        // Total
        JPanel totalPanel = createTotalRow("TOTAL", "Rp 0", true);
        lblTotal = (JLabel) ((JPanel) totalPanel.getComponent(1)).getComponent(0);

        // Metode Pembayaran
        JPanel metodePanel = new JPanel(new BorderLayout(10, 0));
        metodePanel.setOpaque(false);
        metodePanel.setMaximumSize(new Dimension(400, 40));
        metodePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JLabel lblMetode = new JLabel("Pembayaran:");
        lblMetode.setFont(new Font("Arial", Font.PLAIN, 13));

        cmbMetodePembayaran = new JComboBox<>(new String[] { "CASH", "QRIS", "TRANSFER" });
        cmbMetodePembayaran.setFont(new Font("Arial", Font.PLAIN, 13));
        cmbMetodePembayaran.setPreferredSize(new Dimension(150, 35));

        metodePanel.add(lblMetode, BorderLayout.WEST);
        metodePanel.add(cmbMetodePembayaran, BorderLayout.EAST);

        // Button Bayar
        btnBayar = new JButton("💳 Proses Pembayaran");
        btnBayar.setFont(new Font("Arial", Font.BOLD, 15));
        btnBayar.setBackground(new Color(46, 204, 113));
        btnBayar.setForeground(Color.WHITE);
        btnBayar.setFocusPainted(false);
        btnBayar.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        btnBayar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBayar.setMaximumSize(new Dimension(400, 50));
        btnBayar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnBayar.addActionListener(e -> handleProsesPembayaran());

        btnBayar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnBayar.setBackground(new Color(39, 174, 96));
            }

            public void mouseExited(MouseEvent e) {
                btnBayar.setBackground(new Color(46, 204, 113));
            }
        });

        panel.add(Box.createVerticalStrut(10));
        panel.add(lblCustomerTitle);
        panel.add(Box.createVerticalStrut(5));
        panel.add(txtCustomerName);
        panel.add(Box.createVerticalStrut(5));
        panel.add(txtCustomerPhone);
        panel.add(subtotalPanel);
        panel.add(totalPanel);
        panel.add(metodePanel);
        panel.add(btnBayar);

        return panel;
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField txt = new JTextField();
        txt.setFont(new Font("Arial", Font.PLAIN, 13));
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        txt.setMaximumSize(new Dimension(400, 35));
        txt.setAlignmentX(Component.LEFT_ALIGNMENT);
        return txt;
    }

    private JPanel createTotalRow(String label, String value, boolean bold) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(400, bold ? 40 : 30));
        panel.setBorder(BorderFactory.createEmptyBorder(bold ? 5 : 5, 0, bold ? 15 : 5, 0));

        Font font = bold ? new Font("Arial", Font.BOLD, 18) : new Font("Arial", Font.PLAIN, 13);

        JLabel lblLeft = new JLabel(label);
        lblLeft.setFont(font);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setOpaque(false);

        JLabel lblRight = new JLabel(value);
        lblRight.setFont(bold ? new Font("Arial", Font.BOLD, 24) : new Font("Arial", Font.BOLD, 14));
        lblRight.setForeground(bold ? new Color(46, 204, 113) : Color.BLACK);

        rightPanel.add(lblRight);

        panel.add(lblLeft, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }

    // ===== CARD COMPONENTS =====

    private JPanel createProdukCard(Kaos kaos) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setPreferredSize(new Dimension(250, 220));

        // Image Panel
        JPanel imagePanel = new JPanel(new GridBagLayout());
        imagePanel.setBackground(new Color(245, 245, 245));
        imagePanel.setPreferredSize(new Dimension(230, 130));

        JLabel lblImage = new JLabel();
        lblImage.setHorizontalAlignment(SwingConstants.CENTER);

        // Load foto produk dari database
        String fotoPath = kaos.getFotoKaos();
        if (fotoPath != null && !fotoPath.isEmpty()) {
            try {
                File imgFile = new File(fotoPath);
                if (imgFile.exists()) {
                    ImageIcon originalIcon = new ImageIcon(fotoPath);
                    // Resize image agar muat di card
                    Image scaledImage = originalIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                    lblImage.setIcon(new ImageIcon(scaledImage));
                } else {
                    // File tidak ditemukan - tampilkan placeholder
                    lblImage.setText("[No Image]");
                    lblImage.setFont(new Font("Arial", Font.PLAIN, 12));
                    lblImage.setForeground(new Color(150, 150, 150));
                }
            } catch (Exception e) {
                // Gagal load image - tampilkan placeholder
                lblImage.setText("[Error]");
                lblImage.setFont(new Font("Arial", Font.PLAIN, 12));
                lblImage.setForeground(new Color(150, 150, 150));
            }
        } else {
            // Tidak ada foto - tampilkan placeholder dengan initial merek
            String initial = kaos.getMerekKaos().substring(0, Math.min(2, kaos.getMerekKaos().length())).toUpperCase();
            lblImage.setText(initial);
            lblImage.setFont(new Font("Arial", Font.BOLD, 36));
            lblImage.setForeground(new Color(52, 152, 219));
        }

        imagePanel.add(lblImage);

        // Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        JLabel lblNama = new JLabel(kaos.getMerekKaos().toUpperCase());
        lblNama.setFont(new Font("Arial", Font.BOLD, 13));
        lblNama.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDetail = new JLabel(kaos.getTypeKaos() + " - " + kaos.getWarnaKaos() + " (" + kaos.getSize() + ")");
        lblDetail.setFont(new Font("Arial", Font.PLAIN, 11));
        lblDetail.setForeground(new Color(100, 100, 100));
        lblDetail.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Bottom row dengan harga dan stok
        JPanel bottomRow = new JPanel(new BorderLayout());
        bottomRow.setOpaque(false);
        bottomRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        bottomRow.setMaximumSize(new Dimension(230, 25));

        JLabel lblHarga = new JLabel("Rp " + posService.formatRupiah(kaos.getHargaJual()));
        lblHarga.setFont(new Font("Arial", Font.BOLD, 14));
        lblHarga.setForeground(new Color(46, 204, 113));

        JLabel lblStok = new JLabel("Stok: " + kaos.getStokKaos());
        lblStok.setFont(new Font("Arial", Font.PLAIN, 11));
        lblStok.setForeground(kaos.getStokKaos() > 0 ? new Color(100, 100, 100) : new Color(220, 53, 69));

        bottomRow.add(lblHarga, BorderLayout.WEST);
        bottomRow.add(lblStok, BorderLayout.EAST);

        infoPanel.add(lblNama);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(lblDetail);
        infoPanel.add(Box.createVerticalStrut(6));
        infoPanel.add(bottomRow);

        card.add(imagePanel, BorderLayout.NORTH);
        card.add(infoPanel, BorderLayout.CENTER);

        // Mouse Listener untuk klik dan hover
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleTambahKeKeranjang(kaos);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)));
            }
        });

        return card;
    }

    private JPanel createKeranjangItemPanel(KeranjangItem item, int index) {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(10, 0, 10, 0)));
        panel.setMaximumSize(new Dimension(400, 80));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);

        JLabel lblNama = new JLabel(item.getMerek() + " - " + item.getWarna());
        lblNama.setFont(new Font("Arial", Font.BOLD, 13));

        JLabel lblDetail = new JLabel(item.getType() + " (" + item.getSize() + ")");
        lblDetail.setFont(new Font("Arial", Font.PLAIN, 11));
        lblDetail.setForeground(new Color(100, 100, 100));

        JLabel lblHarga = new JLabel("@ Rp " + posService.formatRupiah(item.getHarga()));
        lblHarga.setFont(new Font("Arial", Font.PLAIN, 11));
        lblHarga.setForeground(new Color(100, 100, 100));

        leftPanel.add(lblNama);
        leftPanel.add(Box.createVerticalStrut(3));
        leftPanel.add(lblDetail);
        leftPanel.add(Box.createVerticalStrut(3));
        leftPanel.add(lblHarga);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setOpaque(false);

        JPanel qtyPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        qtyPanel.setOpaque(false);

        JButton btnMinus = new JButton("-");
        btnMinus.setPreferredSize(new Dimension(32, 28));
        btnMinus.setFont(new Font("Arial", Font.BOLD, 16));
        btnMinus.setFocusPainted(false);
        btnMinus.setBackground(new Color(240, 240, 240));
        btnMinus.addActionListener(e -> handleUpdateQty(index, -1));

        JLabel lblQty = new JLabel(String.valueOf(item.getJumlah()));
        lblQty.setFont(new Font("Arial", Font.BOLD, 14));
        lblQty.setPreferredSize(new Dimension(30, 28));
        lblQty.setHorizontalAlignment(SwingConstants.CENTER);

        JButton btnPlus = new JButton("+");
        btnPlus.setPreferredSize(new Dimension(32, 28));
        btnPlus.setFont(new Font("Arial", Font.BOLD, 16));
        btnPlus.setFocusPainted(false);
        btnPlus.setBackground(new Color(240, 240, 240));
        btnPlus.addActionListener(e -> handleUpdateQty(index, 1));

        JButton btnDelete = new JButton("X");
        btnDelete.setPreferredSize(new Dimension(32, 28));
        btnDelete.setFont(new Font("Arial", Font.BOLD, 14));
        btnDelete.setFocusPainted(false);
        btnDelete.setBackground(new Color(255, 230, 230));
        btnDelete.setForeground(new Color(220, 53, 69));
        btnDelete.addActionListener(e -> handleHapusItem(index));

        qtyPanel.add(btnMinus);
        qtyPanel.add(lblQty);
        qtyPanel.add(btnPlus);
        qtyPanel.add(btnDelete);

        JLabel lblSubtotal = new JLabel("Rp " + posService.formatRupiah(item.getSubtotal()));
        lblSubtotal.setFont(new Font("Arial", Font.BOLD, 14));
        lblSubtotal.setAlignmentX(Component.RIGHT_ALIGNMENT);

        rightPanel.add(qtyPanel);
        rightPanel.add(Box.createVerticalStrut(5));
        rightPanel.add(lblSubtotal);

        panel.add(leftPanel, BorderLayout.CENTER);
        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }

    private void loadProdukData() {
        try {
            allProduk = posService.loadAllProduk();
            filteredProduk = new ArrayList<>(allProduk);
            displayProduk();
        } catch (Exception e) {
            showError("Error load produk: " + e.getMessage());
        }
    }

    private void handleLiveSearch() {
        String keyword = txtSearch.getText().trim();
        try {
            filteredProduk = posService.searchProduk(keyword);
            displayProduk();
        } catch (Exception e) {
            showError("Error search: " + e.getMessage());
        }
    }

    private void displayProduk() {
        panelProdukGrid.removeAll();

        for (Kaos kaos : filteredProduk) {
            panelProdukGrid.add(createProdukCard(kaos));
        }

        lblProdukCount.setText(filteredProduk.size() + " produk tersedia");

        panelProdukGrid.revalidate();
        panelProdukGrid.repaint();
    }

    private void handleTambahKeKeranjang(Kaos kaos) {
        String error = posService.tambahKeKeranjang(keranjang, kaos);

        if (error != null) {
            showWarning(error);
        } else {
            updateKeranjangDisplay();
        }
    }

    private void handleUpdateQty(int index, int delta) {
        try {
            String result = posService.updateQtyKeranjang(keranjang, index, delta);

            if ("QTY_ZERO".equals(result)) {
                handleHapusItem(index);
            } else if (result != null) {
                showWarning(result);
            } else {
                updateKeranjangDisplay();
            }
        } catch (Exception e) {
            showError("Error update qty: " + e.getMessage());
        }
    }

    private void handleHapusItem(int index) {
        posService.hapusItemKeranjang(keranjang, index);
        updateKeranjangDisplay();
    }

    private void handleResetKeranjang() {
        if (keranjang.isEmpty()) {
            showInfo("Keranjang sudah kosong!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Hapus semua item dari keranjang?",
                "Konfirmasi Reset",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            keranjang.clear();
            updateKeranjangDisplay();
        }
    }

    private void updateKeranjangDisplay() {
        panelKeranjangList.removeAll();

        if (keranjang.isEmpty()) {
            JLabel lblEmpty = new JLabel("Keranjang kosong");
            lblEmpty.setFont(new Font("Arial", Font.PLAIN, 13));
            lblEmpty.setForeground(new Color(150, 150, 150));
            lblEmpty.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelKeranjangList.add(Box.createVerticalStrut(50));
            panelKeranjangList.add(lblEmpty);
        } else {
            for (int i = 0; i < keranjang.size(); i++) {
                panelKeranjangList.add(createKeranjangItemPanel(keranjang.get(i), i));
            }
        }

        int total = posService.hitungTotalKeranjang(keranjang);
        lblSubtotal.setText("Rp " + posService.formatRupiah(total));
        lblTotal.setText("Rp " + posService.formatRupiah(total));
        lblTotalItem.setText(keranjang.size() + " item");

        panelKeranjangList.revalidate();
        panelKeranjangList.repaint();
    }

    private void handleProsesPembayaran() {
        // Validasi keranjang
        String validasiError = posService.validasiKeranjang(keranjang);
        if (validasiError != null) {
            showWarning(validasiError);
            return;
        }

        int totalHarga = posService.hitungTotalKeranjang(keranjang);
        String metodePembayaran = (String) cmbMetodePembayaran.getSelectedItem();
        String customerName = txtCustomerName.getText().trim();
        String customerPhone = txtCustomerPhone.getText().trim();

        int jumlahBayar = totalHarga;
        int kembalian = 0;

        // CASH: minta input bayar
        if (metodePembayaran.equals("CASH")) {
            String inputBayar = JOptionPane.showInputDialog(this,
                    "Total: Rp " + posService.formatRupiah(totalHarga) + "\n\nMasukkan jumlah bayar:",
                    "Pembayaran CASH",
                    JOptionPane.QUESTION_MESSAGE);

            if (inputBayar == null || inputBayar.trim().isEmpty()) {
                return; // Cancel
            }

            jumlahBayar = posService.parseRupiah(inputBayar);
            if (jumlahBayar == -1) {
                showError("Format uang tidak valid!");
                return;
            }

            String validasiCash = posService.validasiPembayaranCash(totalHarga, jumlahBayar);
            if (validasiCash != null) {
                showError(validasiCash);
                return;
            }

            kembalian = posService.hitungKembalian(totalHarga, jumlahBayar);
        }

        // Proses transaksi via SERVICE
        String idTransaksi = posService.prosesTransaksi(
                idKaryawan,
                keranjang,
                totalHarga,
                metodePembayaran);

        if (idTransaksi != null) {
            generateAndShowStruk(idTransaksi, customerName, customerPhone,
                    totalHarga, jumlahBayar, metodePembayaran);
        } else {
            showError("❌ Transaksi gagal!\nSilakan coba lagi atau hubungi admin.");
        }
    }

    /**
     * Generate dan tampilkan struk transaksi
     */
    private void generateAndShowStruk(String idTransaksi, String customerName,
            String customerPhone, int totalHarga,
            int jumlahBayar, String metodePembayaran) {
        // Build struk model
        StrukTransaksi struk = new StrukTransaksi();
        struk.setIdTransaksi(idTransaksi);
        struk.setTanggalTransaksi(new Date());
        struk.setNamaKasir(namaKasir);
        struk.setMetodePembayaran(metodePembayaran);
        struk.setCustomerName(customerName.isEmpty() ? "Guest" : customerName);
        struk.setCustomerPhone(customerPhone);

        // Convert keranjang ke detail transaksi
        List<DetailTransaksi> items = new ArrayList<>();
        for (KeranjangItem item : keranjang) {
            String namaKaos = item.getMerek() + " " + item.getType() +
                    " - " + item.getWarna() + " (" + item.getSize() + ")";
            DetailTransaksi detail = new DetailTransaksi();
            detail.setNamaKaos(namaKaos);
            detail.setJumlah(item.getJumlah());
            detail.setHargaSaatTransaksi(item.getHarga());
            detail.setSubtotal(item.getSubtotal());
            items.add(detail);
        }
        struk.setItems(items);
        struk.setSubtotal(totalHarga);
        struk.setTotalBayar(jumlahBayar);
        struk.setKembalian(posService.hitungKembalian(totalHarga, jumlahBayar));

        // Generate PDF
        String pdfPath = strukService.generateStrukPDF(struk);

        // Tampilkan pesan sukses
        int kembalian = posService.hitungKembalian(totalHarga, jumlahBayar);

        StringBuilder message = new StringBuilder();
        message.append("✅ TRANSAKSI BERHASIL!\n\n");
        message.append("ID Transaksi : ").append(idTransaksi).append("\n");
        message.append("Metode       : ").append(metodePembayaran).append("\n");
        message.append("Total        : Rp ").append(posService.formatRupiah(totalHarga)).append("\n");

        if (metodePembayaran.equals("CASH")) {
            message.append("Bayar        : Rp ").append(posService.formatRupiah(jumlahBayar)).append("\n");
            message.append("Kembalian    : Rp ").append(posService.formatRupiah(kembalian)).append("\n");
        }

        message.append("\nStruk telah disimpan di:\n").append(pdfPath);

        JOptionPane.showMessageDialog(this, message.toString(),
                "Transaksi Sukses", JOptionPane.INFORMATION_MESSAGE);

        // Tanya apakah mau buka PDF
        int openPdf = JOptionPane.showConfirmDialog(this,
                "Buka struk PDF?",
                "Buka Struk",
                JOptionPane.YES_NO_OPTION);

        if (openPdf == JOptionPane.YES_OPTION && pdfPath != null) {
            try {
                java.awt.Desktop.getDesktop().open(new File(pdfPath));
            } catch (Exception e) {
                showError("Tidak dapat membuka PDF: " + e.getMessage());
            }
        }

        // Reset keranjang dan refresh produk
        keranjang.clear();
        updateKeranjangDisplay();
        txtCustomerName.setText("");
        txtCustomerPhone.setText("");
        loadProdukData(); // Refresh stok
    }

    // ================= DIALOG HELPERS =================

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Peringatan", JOptionPane.WARNING_MESSAGE);
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Informasi", JOptionPane.INFORMATION_MESSAGE);
    }
}