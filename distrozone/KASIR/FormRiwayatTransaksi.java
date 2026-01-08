package distrozone.KASIR;

import distrozone.SERVICEnew.RiwayatTransaksiService;
import distrozone.Model.RiwayatTransaksi;
import distrozone.Model.DetailTransaksi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.toedter.calendar.JDateChooser;

/**
 * VIEW - Form Riwayat Transaksi Kasir
 * 
 * FITUR:
 * ✅ Tampilkan riwayat transaksi kasir
 * ✅ Live search
 * ✅ Filter tanggal
 * ✅ Filter metode pembayaran
 * ✅ Lihat detail transaksi
 * ✅ Cetak ulang struk
 * ✅ Laporan penjualan
 */
public class FormRiwayatTransaksi extends JFrame {
    
    private RiwayatTransaksiService service;
    private int idKasir;
    private String namaKasir;
    
    // Components
    private JTable tableRiwayat;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JDateChooser dateFrom;
    private JDateChooser dateTo;
    private JComboBox<String> cmbMetode;
    private JLabel lblTotalTransaksi;
    private JLabel lblTotalOmzet;
    private JLabel lblTotalItem;
    
    public FormRiwayatTransaksi(int idKasir, String namaKasir) {
        this.idKasir = idKasir;
        this.namaKasir = namaKasir;
        this.service = new RiwayatTransaksiService();
        
        initComponents();
        loadRiwayat();
    }
    
    private void initComponents() {
        setTitle("Riwayat Transaksi - " + namaKasir);
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(25, 25, 30));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // ========== HEADER PANEL ==========
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // ========== CENTER PANEL (TABLE) ==========
        JPanel centerPanel = createTablePanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // ========== BOTTOM PANEL (STATISTIK) ==========
        JPanel bottomPanel = createStatistikPanel();
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    
    private static class StatCard {
    final JPanel panel;
    final JLabel valueLabel;
    
    StatCard(JPanel panel, JLabel valueLabel) {
        this.panel = panel;
        this.valueLabel = valueLabel;
        }
    }
    
    /**
     * ========== HEADER PANEL ==========
     * Berisi: Title, Search, Filter
     */
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(30, 30, 35));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Title
        JLabel lblTitle = new JLabel("📜 RIWAYAT TRANSAKSI");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        panel.add(lblTitle, BorderLayout.NORTH);
        
        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filterPanel.setBackground(new Color(30, 30, 35));
        
        // Search
        JLabel lblSearch = new JLabel("🔍 Cari:");
        lblSearch.setForeground(Color.WHITE);
        filterPanel.add(lblSearch);
        
        txtSearch = new JTextField(20);
        txtSearch.setFont(new Font("Arial", Font.PLAIN, 14));
        txtSearch.setPreferredSize(new Dimension(250, 35));
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { doSearch(); }
            public void removeUpdate(DocumentEvent e) { doSearch(); }
            public void changedUpdate(DocumentEvent e) { doSearch(); }
        });
        filterPanel.add(txtSearch);
        
        // Date From
        JLabel lblFrom = new JLabel("📅 Dari:");
        lblFrom.setForeground(Color.WHITE);
        filterPanel.add(lblFrom);
        
        dateFrom = new JDateChooser();
        dateFrom.setPreferredSize(new Dimension(150, 35));
        dateFrom.setDateFormatString("dd/MM/yyyy");
        filterPanel.add(dateFrom);
        
        // Date To
        JLabel lblTo = new JLabel("Sampai:");
        lblTo.setForeground(Color.WHITE);
        filterPanel.add(lblTo);
        
        dateTo = new JDateChooser();
        dateTo.setPreferredSize(new Dimension(150, 35));
        dateTo.setDateFormatString("dd/MM/yyyy");
        filterPanel.add(dateTo);
        
        // Metode Pembayaran
        JLabel lblMetode = new JLabel("💳 Metode:");
        lblMetode.setForeground(Color.WHITE);
        filterPanel.add(lblMetode);
        
        cmbMetode = new JComboBox<>(new String[]{"SEMUA", "CASH", "QRIS", "TRANSFER"});
        cmbMetode.setPreferredSize(new Dimension(120, 35));
        cmbMetode.setFont(new Font("Arial", Font.PLAIN, 14));
        filterPanel.add(cmbMetode);
        
        // Button Filter
        JButton btnFilter = new JButton("🔍 FILTER");
        btnFilter.setPreferredSize(new Dimension(120, 35));
        btnFilter.setBackground(new Color(52, 152, 219));
        btnFilter.setForeground(Color.BLACK);
        btnFilter.setFont(new Font("Arial", Font.BOLD, 14));
        btnFilter.setFocusPainted(false);
        btnFilter.setBorder(BorderFactory.createEmptyBorder());
        btnFilter.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnFilter.addActionListener(e -> doFilter());
        filterPanel.add(btnFilter);
        
        // Button Reset
        JButton btnReset = new JButton("🔄 RESET");
        btnReset.setPreferredSize(new Dimension(120, 35));
        btnReset.setBackground(new Color(220, 53, 69));
        btnReset.setForeground(Color.BLACK);
        btnReset.setFont(new Font("Arial", Font.BOLD, 14));
        btnReset.setFocusPainted(false);
        btnReset.setBorder(BorderFactory.createEmptyBorder());
        btnReset.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReset.addActionListener(e -> doReset());
        filterPanel.add(btnReset);
        
        panel.add(filterPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * ========== TABLE PANEL ==========
     */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(new Color(25, 25, 30));
        
        // Table Model
        String[] columns = {
            "Kode Transaksi", "Tanggal", "Jam", 
            "Total Item", "Total Bayar", "Metode", "Status"
        };
        
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // READ-ONLY ✅
            }
        };
        
        tableRiwayat = new JTable(tableModel);
        tableRiwayat.setFont(new Font("Arial", Font.PLAIN, 13));
        tableRiwayat.setRowHeight(30);
        tableRiwayat.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableRiwayat.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tableRiwayat.getTableHeader().setBackground(new Color(52, 152, 219));
        tableRiwayat.getTableHeader().setForeground(Color.BLACK);
        
        // Set column width
        tableRiwayat.getColumnModel().getColumn(0).setPreferredWidth(150);
        tableRiwayat.getColumnModel().getColumn(1).setPreferredWidth(100);
        tableRiwayat.getColumnModel().getColumn(2).setPreferredWidth(80);
        tableRiwayat.getColumnModel().getColumn(3).setPreferredWidth(80);
        tableRiwayat.getColumnModel().getColumn(4).setPreferredWidth(120);
        tableRiwayat.getColumnModel().getColumn(5).setPreferredWidth(100);
        tableRiwayat.getColumnModel().getColumn(6).setPreferredWidth(100);
        
        JScrollPane scrollPane = new JScrollPane(tableRiwayat);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 65), 2));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Button Panel (kanan tabel)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(new Color(25, 25, 30));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        
        JButton btnDetail = createActionButton("📋 DETAIL", new Color(52, 152, 219));
        btnDetail.setForeground(Color.BLACK);
        btnDetail.setFont(new Font("Arial", Font.BOLD, 14));
        btnDetail.addActionListener(e -> showDetail());
        buttonPanel.add(btnDetail);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JButton btnCetak = createActionButton("🖨️ CETAK STRUK", new Color(46, 204, 113));
        btnCetak.setForeground(Color.BLACK);
        btnCetak.setFont(new Font("Arial", Font.BOLD, 14));
        btnCetak.addActionListener(e -> cetakStruk());
        buttonPanel.add(btnCetak);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JButton btnRefresh = createActionButton("🔄 REFRESH", new Color(155, 89, 182));
        btnRefresh.setForeground(Color.BLACK);
        btnRefresh.setFont(new Font("Arial", Font.BOLD, 14));
        btnRefresh.addActionListener(e -> loadRiwayat());
        buttonPanel.add(btnRefresh);
        
        panel.add(buttonPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * ========== STATISTIK PANEL ==========
     */
    private JPanel createStatistikPanel() {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
    panel.setBackground(new Color(30, 30, 35));
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
    // Card 1
    StatCard card1 = createStatCard("Total Transaksi", "0", new Color(52, 152, 219));
    lblTotalTransaksi = card1.valueLabel;
    panel.add(card1.panel);
    
    // Card 2
    StatCard card2 = createStatCard("Total Omzet", "Rp 0", new Color(46, 204, 113));
    lblTotalOmzet = card2.valueLabel;
    panel.add(card2.panel);
    
    // Card 3
    StatCard card3 = createStatCard("Total Item Terjual", "0", new Color(155, 89, 182));
    lblTotalItem = card3.valueLabel;
    panel.add(card3.panel);
    
    return panel;
}     
    
    private StatCard createStatCard(String title, String value, Color color) {
    JPanel card = new JPanel();
    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
    card.setBackground(new Color(40, 40, 45));
    card.setPreferredSize(new Dimension(250, 80));
    card.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(color, 2),
        BorderFactory.createEmptyBorder(10, 15, 10, 15)
    ));
    
    JLabel lblTitle = new JLabel(title);
    lblTitle.setFont(new Font("Arial", Font.PLAIN, 12));
    lblTitle.setForeground(new Color(150, 150, 150));
    lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    JLabel lblValue = new JLabel(value);
    lblValue.setFont(new Font("Arial", Font.BOLD, 20));
    lblValue.setForeground(color);
    lblValue.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    card.add(lblTitle);
    card.add(Box.createRigidArea(new Dimension(0, 5)));
    card.add(lblValue);
    
    return new StatCard(card, lblValue); // ✅ Kembalikan keduanya
}
    
    private JButton createActionButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(180, 45));
        btn.setPreferredSize(new Dimension(180, 45));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(color.darker());
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(color);
            }
        });
        
        return btn;
    }
    
    /**
     * ========== LOAD DATA ==========
     */
    private void loadRiwayat() {
        tableModel.setRowCount(0);
        
        List<RiwayatTransaksi> list = service.getRiwayatKasir(idKasir);
        
        if (list == null || list.isEmpty()) {
            updateStatistik(0, 0, 0);
            return;
        }
        
        SimpleDateFormat sdfTanggal = new SimpleDateFormat("dd/MM/yyyy");
        int totalTransaksi = 0;
        int totalOmzet = 0;
        int totalItem = 0;
        
        for (RiwayatTransaksi r : list) {
            Object[] row = {
                r.getKodeTransaksi(),
                sdfTanggal.format(r.getTanggalTransaksi()),
                r.getJamTransaksi(),
                r.getTotalItem(),
                service.formatRupiah(r.getTotalBayar()),
                r.getMetodePembayaran(),
                r.getStatusTransaksi()
            };
            tableModel.addRow(row);
            
            totalTransaksi++;
            totalOmzet += r.getTotalBayar();
            totalItem += r.getTotalItem();
        }
        
        updateStatistik(totalTransaksi, totalOmzet, totalItem);
    }
    
    /**
     * ========== SEARCH ==========
     */
    private void doSearch() {
        String keyword = txtSearch.getText().trim();
        
        tableModel.setRowCount(0);
        List<RiwayatTransaksi> list = service.searchRiwayat(idKasir, keyword);
        
        if (list == null || list.isEmpty()) {
            return;
        }
        
        SimpleDateFormat sdfTanggal = new SimpleDateFormat("dd/MM/yyyy");
        
        for (RiwayatTransaksi r : list) {
            Object[] row = {
                r.getKodeTransaksi(),
                sdfTanggal.format(r.getTanggalTransaksi()),
                r.getJamTransaksi(),
                r.getTotalItem(),
                service.formatRupiah(r.getTotalBayar()),
                r.getMetodePembayaran(),
                r.getStatusTransaksi()
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * ========== FILTER ==========
     */
    private void doFilter() {
        Date from = dateFrom.getDate();
        Date to = dateTo.getDate();
        String metode = (String) cmbMetode.getSelectedItem();
        
        List<RiwayatTransaksi> list = null;
        
        // Logika filter
        if (from != null && to != null && !metode.equals("SEMUA")) {
            // Filter lengkap: tanggal + metode
            list = service.getRiwayatWithFullFilter(idKasir, from, to, metode);
        } else if (from != null && to != null) {
            // Filter tanggal saja
            list = service.getRiwayatWithFilter(idKasir, from, to);
        } else if (!metode.equals("SEMUA")) {
            // Filter metode saja
            list = service.getRiwayatWithMetode(idKasir, metode);
        } else {
            // Tidak ada filter
            loadRiwayat();
            return;
        }
        
        // Tampilkan hasil filter
        tableModel.setRowCount(0);
        
        if (list == null || list.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Tidak ada data sesuai filter!", 
                "Info", 
                JOptionPane.INFORMATION_MESSAGE);
            updateStatistik(0, 0, 0);
            return;
        }
        
        SimpleDateFormat sdfTanggal = new SimpleDateFormat("dd/MM/yyyy");
        int totalTransaksi = 0;
        int totalOmzet = 0;
        int totalItem = 0;
        
        for (RiwayatTransaksi r : list) {
            Object[] row = {
                r.getKodeTransaksi(),
                sdfTanggal.format(r.getTanggalTransaksi()),
                r.getJamTransaksi(),
                r.getTotalItem(),
                service.formatRupiah(r.getTotalBayar()),
                r.getMetodePembayaran(),
                r.getStatusTransaksi()
            };
            tableModel.addRow(row);
            
            totalTransaksi++;
            totalOmzet += r.getTotalBayar();
            totalItem += r.getTotalItem();
        }
        
        updateStatistik(totalTransaksi, totalOmzet, totalItem);
    }
    
    /**
     * ========== RESET FILTER ==========
     */
    private void doReset() {
        txtSearch.setText("");
        dateFrom.setDate(null);
        dateTo.setDate(null);
        cmbMetode.setSelectedIndex(0);
        loadRiwayat();
    }
    
    /**
     * ========== SHOW DETAIL ==========
     */
    private void showDetail() {
        int selectedRow = tableRiwayat.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Pilih transaksi terlebih dahulu!", 
                "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String idTransaksi = (String) tableModel.getValueAt(selectedRow, 0);
        
        // Buka form detail
        FormDetailTransaksi formDetail = new FormDetailTransaksi(idTransaksi);
        formDetail.setVisible(true);
    }
    
    /**
     * ========== CETAK STRUK ==========
     */
    private void cetakStruk() {
        int selectedRow = tableRiwayat.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Pilih transaksi terlebih dahulu!", 
                "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String idTransaksi = (String) tableModel.getValueAt(selectedRow, 0);
        
        // Ambil data untuk cetak struk
        Map<String, Object> dataStruk = service.getDataStruk(idTransaksi);
        
        if (dataStruk == null) {
            JOptionPane.showMessageDialog(this, 
                "Gagal mengambil data transaksi!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Buka dialog cetak struk
        FormCetakStruk formCetak = new FormCetakStruk(this,dataStruk);
        formCetak.setVisible(true);
    }
    
    /**
     * ========== UPDATE STATISTIK ==========
     */
    private void updateStatistik(int totalTransaksi, int totalOmzet, int totalItem) {
        lblTotalTransaksi.setText(String.valueOf(totalTransaksi));
        lblTotalOmzet.setText(service.formatRupiah(totalOmzet));
        lblTotalItem.setText(String.valueOf(totalItem));
    }
}