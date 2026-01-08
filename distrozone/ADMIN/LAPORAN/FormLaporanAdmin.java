package distrozone.ADMIN.LAPORAN;

import java.text.SimpleDateFormat;
import distrozone.SERVICEnew.LaporanService;
import distrozone.Model.RingkasanKeuangan;
import distrozone.Model.RiwayatTransaksi;
import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/**
 * Form Laporan Keuangan - Mirip dengan gambar referensi
 * Fitur:
 * - Tab Ringkasan dan Pending Refund
 * - Filter periode (Bulanan/Custom)
 * - Filter channel dan pembayaran
 * - Ringkasan keuangan di tengah
 * - Tabel transaksi di bawah
 * - Live search
 */
public class FormLaporanAdmin extends JFrame {
    
    private LaporanService service;
    
    // Tab Components
    private JTabbedPane tabbedPane;
    
    // Filter Components
    private JComboBox<String> cmbPeriode, cmbChannel, cmbPembayaran;
    private JComboBox<Integer> cmbBulan, cmbTahun;
    private JDateChooser dateFrom, dateTo;
    private JTextField txtSearch;
    private JButton btnGenerate;
    
    // Ringkasan Components (di tengah)
    private JLabel lblPendapatan, lblModal, lblLaba, lblTotalTransaksi;
    
    // Informasi Tambahan
    private JLabel lblInfoTambahan, lblTotalKesMasuk;
    
    // Table Components
    private JTable tableTransaksi;
    private DefaultTableModel tableModel;
    
    public FormLaporanAdmin() {
        service = new LaporanService();
        initComponents();
        setLocationRelativeTo(null);
        
        // Load data awal (bulan ini)
        loadDataBulanIni();
    }
    
    private void initComponents() {
        setTitle("Laporan Keuangan");
        setSize(1300, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Tabbed Pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 13));
        tabbedPane.setBackground(new Color(40, 40, 45));
        
        tabbedPane.addTab("Ringkasan", createRingkasanTab());
        tabbedPane.addTab("Pending Refund", createPendingRefundTab());
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(40, 40, 45));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        // Title Section
        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setBackground(new Color(40, 40, 45));
        
        JLabel lblTitle = new JLabel("Laporan Keuangan");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        
        JLabel lblSubtitle = new JLabel("Ringkasan keuangan dan daftar transaksi");
        lblSubtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        lblSubtitle.setForeground(new Color(180, 180, 180));
        
        titlePanel.add(lblTitle);
        titlePanel.add(lblSubtitle);
        
        // Date Info
        JLabel lblDate = new JLabel(new SimpleDateFormat("EEEE, dd MMMM yyyy - HH:mm", 
                new Locale("id", "ID")).format(new Date()) + " WIB");
        lblDate.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDate.setForeground(new Color(150, 150, 150));
        
        panel.add(titlePanel, BorderLayout.WEST);
        panel.add(lblDate, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createRingkasanTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(new Color(40, 40, 45));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        // Filter Panel (Top)
        JPanel filterPanel = createFilterPanel();
        panel.add(filterPanel, BorderLayout.NORTH);
        
        // Content Panel (Ringkasan + Table)
        JPanel contentPanel = new JPanel(new BorderLayout(0, 10));
        contentPanel.setBackground(new Color(40, 40, 45));
        
        // Ringkasan Panel (Center Top)
        JPanel ringkasanPanel = createRingkasanPanel();
        contentPanel.add(ringkasanPanel, BorderLayout.NORTH);
        
        // Table Panel (Center Bottom)
        JPanel tablePanel = createTablePanel();
        contentPanel.add(tablePanel, BorderLayout.CENTER);
        
        panel.add(contentPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createFilterPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(50, 50, 55));
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(80, 80, 85)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Periode
        JLabel lblPeriode = new JLabel("Periode:");
        lblPeriode.setForeground(Color.WHITE);
        lblPeriode.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblPeriode);
        
        cmbPeriode = new JComboBox<>(new String[]{"Bulanan", "Custom"});
        cmbPeriode.setPreferredSize(new Dimension(120, 30));
        cmbPeriode.addActionListener(e -> updatePeriodeInput());
        panel.add(cmbPeriode);
        
        // Bulan
        cmbBulan = new JComboBox<>();
        String[] namaBulan = {"Januari", "Februari", "Maret", "April", "Mei", "Juni",
                              "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
        for (int i = 0; i < namaBulan.length; i++) {
            cmbBulan.addItem(i + 1);
        }
        cmbBulan.setSelectedIndex(Calendar.getInstance().get(Calendar.MONTH));
        cmbBulan.setPreferredSize(new Dimension(100, 30));
        cmbBulan.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value != null) {
                    int bulan = (Integer) value;
                    setText(namaBulan[bulan - 1]);
                }
                return this;
            }
        });
        panel.add(cmbBulan);
        
        // Tahun
        cmbTahun = new JComboBox<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear - 5; i <= currentYear; i++) {
            cmbTahun.addItem(i);
        }
        cmbTahun.setSelectedItem(currentYear);
        cmbTahun.setPreferredSize(new Dimension(100, 30));
        panel.add(cmbTahun);
        
        // Date From (Hidden)
        dateFrom = new JDateChooser();
        dateFrom.setDateFormatString("dd/MM/yyyy");
        dateFrom.setPreferredSize(new Dimension(130, 30));
        dateFrom.setVisible(false);
        panel.add(dateFrom);
        
        // Date To (Hidden)
        dateTo = new JDateChooser();
        dateTo.setDateFormatString("dd/MM/yyyy");
        dateTo.setPreferredSize(new Dimension(130, 30));
        dateTo.setVisible(false);
        panel.add(dateTo);
        
        // Channel
        JLabel lblChannel = new JLabel("Channel:");
        lblChannel.setForeground(Color.WHITE);
        lblChannel.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblChannel);
        
        cmbChannel = new JComboBox<>(new String[]{"Semua", "Online", "Offline"});
        cmbChannel.setPreferredSize(new Dimension(120, 30));
        panel.add(cmbChannel);
        
        // Pembayaran
        JLabel lblPembayaran = new JLabel("Pembayaran:");
        lblPembayaran.setForeground(Color.WHITE);
        lblPembayaran.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblPembayaran);
        
        cmbPembayaran = new JComboBox<>(new String[]{"Semua", "CASH", "QRIS", "TRANSFER"});
        cmbPembayaran.setPreferredSize(new Dimension(120, 30));
        panel.add(cmbPembayaran);
        
        // Button Generate
        btnGenerate = createStyledButton("🔍 Tampilkan", new Color(52, 152, 219));
        btnGenerate.addActionListener(e -> loadLaporanData());
        panel.add(btnGenerate);
        
        return panel;
    }
    
    private JPanel createRingkasanPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(50, 50, 55));
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 85), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Row 1: 3 Cards utama
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(createCardRingkasan("Pendapatan Produk", "Rp 0", 
            new Color(241, 196, 15), lblPendapatan = new JLabel("Rp 0")), gbc);
        
        gbc.gridx = 1;
        panel.add(createCardRingkasan("Total Harga Modal", "Rp 0", 
            new Color(231, 76, 60), lblModal = new JLabel("Rp 0")), gbc);
        
        gbc.gridx = 2;
        panel.add(createCardRingkasan("Laba Bersih", "Rp 0", 
            new Color(46, 204, 113), lblLaba = new JLabel("Rp 0")), gbc);
        
        // Row 2: Total Transaksi (centered)
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 3;
        panel.add(createCardRingkasan("Total Transaksi", "0", 
            new Color(52, 152, 219), lblTotalTransaksi = new JLabel("0")), gbc);
        
        // Row 3: Info Tambahan
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBackground(new Color(50, 50, 55));
        
        JLabel lblInfo = new JLabel("🚚 Info Tambahan  Total Ongkir: Rp 360.000   |   ");
        lblInfo.setForeground(new Color(180, 180, 180));
        lblInfo.setFont(new Font("Arial", Font.PLAIN, 12));
        infoPanel.add(lblInfo);
        
        lblInfoTambahan = new JLabel("(ditanggung ke kurir)");
        lblInfoTambahan.setForeground(new Color(150, 150, 150));
        lblInfoTambahan.setFont(new Font("Arial", Font.ITALIC, 11));
        infoPanel.add(lblInfoTambahan);
        
        panel.add(infoPanel, gbc);
        
        // Row 3: Total Kas Masuk
        gbc.gridx = 2; gbc.gridwidth = 1;
        JPanel kasPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        kasPanel.setBackground(new Color(50, 50, 55));
        
        JLabel lblKasLabel = new JLabel("Total Kas Masuk: ");
        lblKasLabel.setForeground(new Color(180, 180, 180));
        lblKasLabel.setFont(new Font("Arial", Font.BOLD, 12));
        kasPanel.add(lblKasLabel);
        
        lblTotalKesMasuk = new JLabel("Rp 12.298.800");
        lblTotalKesMasuk.setForeground(new Color(46, 204, 113));
        lblTotalKesMasuk.setFont(new Font("Arial", Font.BOLD, 14));
        kasPanel.add(lblTotalKesMasuk);
        
        panel.add(kasPanel, gbc);
        
        return panel;
    }
    
    private JPanel createCardRingkasan(String title, String value, Color color, JLabel valueLabel) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(new Color(60, 60, 65));
        card.setPreferredSize(new Dimension(280, 100));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial", Font.PLAIN, 13));
        lblTitle.setForeground(new Color(200, 200, 200));
        
        valueLabel.setText(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(color);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        card.add(lblTitle, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(new Color(50, 50, 55));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 85), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Header dengan search
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(50, 50, 55));
        
        JLabel lblTableTitle = new JLabel("Transaksi Terbaru");
        lblTableTitle.setFont(new Font("Arial", Font.BOLD, 14));
        lblTableTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTableTitle, BorderLayout.WEST);
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(new Color(50, 50, 55));
        
        txtSearch = new JTextField(20);
        txtSearch.setFont(new Font("Arial", Font.PLAIN, 12));
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterTable();
            }
        });
        searchPanel.add(new JLabel("🔍"));
        searchPanel.add(txtSearch);
        
        headerPanel.add(searchPanel, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"No. Transaksi", "Tgl. Bayar", "Kasir", "Tipe", 
                           "Pembayaran", "Status", "Subtotal"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableTransaksi = new JTable(tableModel);
        tableTransaksi.setBackground(new Color(45, 45, 50));
        tableTransaksi.setForeground(Color.WHITE);
        tableTransaksi.setFont(new Font("Arial", Font.PLAIN, 12));
        tableTransaksi.setRowHeight(35);
        tableTransaksi.setSelectionBackground(new Color(52, 152, 219));
        tableTransaksi.getTableHeader().setBackground(new Color(35, 35, 40));
        tableTransaksi.getTableHeader().setForeground(Color.WHITE);
        tableTransaksi.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        // Column widths
        tableTransaksi.getColumnModel().getColumn(0).setPreferredWidth(150);
        tableTransaksi.getColumnModel().getColumn(1).setPreferredWidth(120);
        tableTransaksi.getColumnModel().getColumn(2).setPreferredWidth(120);
        tableTransaksi.getColumnModel().getColumn(3).setPreferredWidth(80);
        tableTransaksi.getColumnModel().getColumn(4).setPreferredWidth(100);
        tableTransaksi.getColumnModel().getColumn(5).setPreferredWidth(100);
        tableTransaksi.getColumnModel().getColumn(6).setPreferredWidth(120);
        
        JScrollPane scrollPane = new JScrollPane(tableTransaksi);
        scrollPane.getViewport().setBackground(new Color(45, 45, 50));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createPendingRefundTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(40, 40, 45));
        
        JLabel lblInfo = new JLabel("Pending Refund - Coming Soon", SwingConstants.CENTER);
        lblInfo.setFont(new Font("Arial", Font.BOLD, 18));
        lblInfo.setForeground(Color.WHITE);
        
        panel.add(lblInfo, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btn.setBackground(bgColor.brighter());
            }
            public void mouseExited(MouseEvent evt) {
                btn.setBackground(bgColor);
            }
        });
        
        return btn;
    }
    
    private void updatePeriodeInput() {
        String periode = cmbPeriode.getSelectedItem().toString();
        
        boolean isBulanan = periode.equals("Bulanan");
        cmbBulan.setVisible(isBulanan);
        cmbTahun.setVisible(isBulanan);
        dateFrom.setVisible(!isBulanan);
        dateTo.setVisible(!isBulanan);
    }
    
    private void loadDataBulanIni() {
        Calendar cal = Calendar.getInstance();
        int bulan = cal.get(Calendar.MONTH) + 1;
        int tahun = cal.get(Calendar.YEAR);
        
        cmbBulan.setSelectedItem(bulan);
        cmbTahun.setSelectedItem(tahun);
        
        loadLaporanData();
    }
    
    private void loadLaporanData() {
        String tipePeriode = cmbPeriode.getSelectedItem().toString();
        
        // Validasi
        String error = null;
        if (tipePeriode.equals("Bulanan")) {
            error = service.validatePeriode(tipePeriode, null, 
                (Integer) cmbBulan.getSelectedItem(), 
                (Integer) cmbTahun.getSelectedItem(), null, null);
        } else {
            error = service.validatePeriode(tipePeriode, null, null, null, 
                dateFrom.getDate(), dateTo.getDate());
        }
        
        if (error != null) {
            JOptionPane.showMessageDialog(this, error, "Validasi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Hitung periode
        Map<String, Date> periode;
        if (tipePeriode.equals("Bulanan")) {
            periode = service.hitungPeriode(tipePeriode, null, 
                (Integer) cmbBulan.getSelectedItem(), 
                (Integer) cmbTahun.getSelectedItem(), null, null);
        } else {
            periode = service.hitungPeriode(tipePeriode, null, 0, 0, 
                dateFrom.getDate(), dateTo.getDate());
        }
        
        Date from = periode.get("from");
        Date to = periode.get("to");
        String channel = cmbChannel.getSelectedItem().toString();
        String pembayaran = cmbPembayaran.getSelectedItem().toString();
        String search = txtSearch.getText().trim();
        
        // Load data
        Map<String, Object> result = service.getLaporanKeuangan(from, to, channel, pembayaran, search);
        
        if ((Boolean) result.get("success")) {
            // Update ringkasan
            RingkasanKeuangan ringkasan = (RingkasanKeuangan) result.get("ringkasan");
            lblPendapatan.setText(service.formatRupiah(ringkasan.getPendapatanProduk()));
            lblModal.setText(service.formatRupiah(ringkasan.getTotalHargaModal()));
            lblLaba.setText(service.formatRupiah(ringkasan.getLabaBersih()));
            lblTotalTransaksi.setText(String.valueOf(result.get("total_transaksi")));
            lblTotalKesMasuk.setText(service.formatRupiah(ringkasan.getTotalKesMasuk()));
            
            // Update table
            @SuppressWarnings("unchecked")
            List<RiwayatTransaksi> transaksiList = (List<RiwayatTransaksi>) result.get("transaksi_list");
            updateTable(transaksiList);
            
        } else {
            JOptionPane.showMessageDialog(this, "Error: " + result.get("error"), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateTable(List<RiwayatTransaksi> list) {
        tableModel.setRowCount(0);
        
        for (RiwayatTransaksi t : list) {
            Object[] row = {
                t.getKodeTransaksi(),
                new SimpleDateFormat("dd/MM/yy HH:mm").format(t.getTanggalTransaksi()),
                t.getNamaKasir(),
                "Online", // Bisa disesuaikan
                t.getMetodePembayaran(),
                t.getStatusTransaksi(),
                service.formatRupiah(t.getTotalBayar())
            };
            tableModel.addRow(row);
        }
    }
    
    private void filterTable() {
        loadLaporanData(); // Reload dengan keyword search
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FormLaporanAdmin().setVisible(true);
        });
    }
}