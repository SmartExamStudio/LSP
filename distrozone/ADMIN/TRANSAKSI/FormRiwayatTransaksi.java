package distrozone.ADMIN.TRANSAKSI;

import distrozone.DAO.RiwayatTransaksiDAO;
import distrozone.Model.RiwayatTransaksi;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.toedter.calendar.JDateChooser;

/**
 * Form Riwayat Transaksi untuk ADMIN
 * 
 * FITUR:
 * ✅ Tampilkan SEMUA transaksi dari semua kasir
 * ✅ Filter tanggal (From - To)
 * ✅ Filter metode pembayaran
 * ✅ Search ID transaksi
 * ✅ Klik row → buka detail transaksi
 * ✅ Cetak struk transaksi
 * ✅ READ-ONLY (tidak bisa edit/delete)
 */
public class FormRiwayatTransaksi extends JFrame {

    private JTable tableRiwayat;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JDateChooser dateFrom, dateTo;
    private JComboBox<String> cmbMetode;
    private JButton btnFilter, btnReset, btnCetakStruk;

    private RiwayatTransaksiDAO riwayatDAO;

    private String[] columnNames = {
            "ID Transaksi", "Tanggal", "Jam", "Nama Kasir",
            "Total Item", "Total Harga", "Metode", "Status"
    };

    public FormRiwayatTransaksi() {
        riwayatDAO = new RiwayatTransaksiDAO();

        initComponents();
        loadAllRiwayat();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Riwayat Transaksi - Admin Dashboard");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Panel Utama
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(40, 40, 45));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header Panel
        JPanel headerPanel = createHeaderPanel();

        // Filter Panel
        JPanel filterPanel = createFilterPanel();

        // Table Panel
        JPanel tablePanel = createTablePanel();

        // Button Panel
        JPanel buttonPanel = createButtonPanel();

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(filterPanel, BorderLayout.CENTER);
        mainPanel.add(tablePanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(40, 40, 45));

        JLabel lblTitle = new JLabel("📋 RIWAYAT TRANSAKSI");
        lblTitle.setFont(new Font("Arial Black", Font.BOLD, 24));
        lblTitle.setForeground(new Color(255, 87, 51));

        JLabel lblSubtitle = new JLabel("Semua transaksi dari semua kasir");
        lblSubtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        lblSubtitle.setForeground(new Color(200, 200, 200));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setBackground(new Color(40, 40, 45));
        titlePanel.add(lblTitle);
        titlePanel.add(lblSubtitle);

        panel.add(titlePanel, BorderLayout.WEST);

        return panel;
    }

    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(50, 50, 55));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(255, 87, 51), 2),
                "Filter & Pencarian",
                0, 0,
                new Font("Arial", Font.BOLD, 14),
                Color.WHITE));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 1: Search
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblSearch = new JLabel("🔍 Cari ID Transaksi:");
        lblSearch.setForeground(Color.WHITE);
        panel.add(lblSearch, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        txtSearch = new JTextField(20);
        txtSearch.setFont(new Font("Arial", Font.PLAIN, 14));
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchRiwayat();
            }
        });
        panel.add(txtSearch, gbc);

        // Row 2: Tanggal From
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel lblFrom = new JLabel("📅 Dari Tanggal:");
        lblFrom.setForeground(Color.WHITE);
        panel.add(lblFrom, gbc);

        gbc.gridx = 1;
        dateFrom = new JDateChooser();
        dateFrom.setDateFormatString("dd/MM/yyyy");
        dateFrom.setPreferredSize(new Dimension(150, 30));
        panel.add(dateFrom, gbc);

        // Tanggal To
        gbc.gridx = 2;
        JLabel lblTo = new JLabel("📅 Sampai:");
        lblTo.setForeground(Color.WHITE);
        panel.add(lblTo, gbc);

        gbc.gridx = 3;
        dateTo = new JDateChooser();
        dateTo.setDateFormatString("dd/MM/yyyy");
        dateTo.setPreferredSize(new Dimension(150, 30));
        panel.add(dateTo, gbc);

        // Row 3: Metode Pembayaran
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblMetode = new JLabel("💳 Metode:");
        lblMetode.setForeground(Color.WHITE);
        panel.add(lblMetode, gbc);

        gbc.gridx = 1;
        cmbMetode = new JComboBox<>(new String[] { "SEMUA", "CASH", "QRIS", "TRANSFER" });
        cmbMetode.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(cmbMetode, gbc);

        // Button Filter
        gbc.gridx = 2;
        btnFilter = createStyledButton("🔍 Filter", new Color(52, 152, 219));
        btnFilter.addActionListener(e -> applyFilter());
        panel.add(btnFilter, gbc);

        // Button Reset
        gbc.gridx = 3;
        btnReset = createStyledButton("🔄 Reset", new Color(231, 76, 60));
        btnReset.addActionListener(e -> resetFilter());
        panel.add(btnReset, gbc);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(40, 40, 45));

        // Table Model
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // READ-ONLY
            }
        };

        tableRiwayat = new JTable(tableModel);
        tableRiwayat.setFont(new Font("Arial", Font.PLAIN, 12));
        tableRiwayat.setRowHeight(30);
        tableRiwayat.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tableRiwayat.getTableHeader().setBackground(new Color(255, 87, 51));
        tableRiwayat.getTableHeader().setForeground(Color.BLACK);
        tableRiwayat.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Double click untuk detail
        tableRiwayat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    showDetailTransaksi();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tableRiwayat);
        scrollPane.setPreferredSize(new Dimension(1150, 300));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(new Color(40, 40, 45));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        btnCetakStruk = createStyledButton("🖨️ Cetak Struk", new Color(46, 204, 113));
        btnCetakStruk.addActionListener(e -> cetakStruk());
        panel.add(btnCetakStruk);

        JButton btnClose = createStyledButton("❌ Tutup", new Color(149, 165, 166));
        btnClose.addActionListener(e -> dispose());
        panel.add(btnClose);

        return panel;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setBackground(bgColor);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor.brighter());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor);
            }
        });

        return btn;
    }

    /**
     * Load semua riwayat transaksi
     */
    private void loadAllRiwayat() {
        try {
            List<RiwayatTransaksi> listRiwayat = riwayatDAO.getAllRiwayat();
            displayRiwayat(listRiwayat);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error load riwayat: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Apply filter berdasarkan input user
     */
    private void applyFilter() {
        try {
            Date from = dateFrom.getDate();
            Date to = dateTo.getDate();
            String metode = cmbMetode.getSelectedItem().toString();

            List<RiwayatTransaksi> listRiwayat;

            // Filter berdasarkan kondisi
            if (from != null && to != null && !metode.equals("SEMUA")) {
                // Filter lengkap (tanggal + metode) - untuk Admin
                // Karena admin tidak punya id kasir, kita perlu modifikasi query
                // Untuk sementara gunakan getAllRiwayat lalu filter manual
                listRiwayat = riwayatDAO.getAllRiwayat();
                listRiwayat = filterManual(listRiwayat, from, to, metode);
            } else if (from != null && to != null) {
                // Filter tanggal saja
                listRiwayat = riwayatDAO.getAllRiwayat();
                listRiwayat = filterByDate(listRiwayat, from, to);
            } else if (!metode.equals("SEMUA")) {
                // Filter metode saja
                listRiwayat = riwayatDAO.getAllRiwayat();
                listRiwayat = filterByMetode(listRiwayat, metode);
            } else {
                // Tidak ada filter
                listRiwayat = riwayatDAO.getAllRiwayat();
            }

            displayRiwayat(listRiwayat);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error filter: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Reset filter
     */
    private void resetFilter() {
        txtSearch.setText("");
        dateFrom.setDate(null);
        dateTo.setDate(null);
        cmbMetode.setSelectedIndex(0);
        loadAllRiwayat();
    }

    /**
     * Search real-time berdasarkan ID transaksi
     */
    private void searchRiwayat() {
        String keyword = txtSearch.getText().trim();

        if (keyword.isEmpty()) {
            loadAllRiwayat();
            return;
        }

        try {
            List<RiwayatTransaksi> listRiwayat = riwayatDAO.getAllRiwayat();
            List<RiwayatTransaksi> filtered = new java.util.ArrayList<>();

            for (RiwayatTransaksi r : listRiwayat) {
                if (r.getIdTransaksi().toLowerCase().contains(keyword.toLowerCase())) {
                    filtered.add(r);
                }
            }

            displayRiwayat(filtered);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error search: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Tampilkan list riwayat ke table
     */
    private void displayRiwayat(List<RiwayatTransaksi> listRiwayat) {
        tableModel.setRowCount(0);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (RiwayatTransaksi r : listRiwayat) {
            Object[] row = {
                    r.getIdTransaksi(),
                    sdf.format(r.getTanggalTransaksi()),
                    r.getJamTransaksi(),
                    r.getNamaKasir(),
                    r.getTotalItem(),
                    formatRupiah(r.getTotalBayar()),
                    r.getMetodePembayaran(),
                    r.getStatusTransaksi()
            };
            tableModel.addRow(row);
        }
    }

    /**
     * Show detail transaksi (popup)
     */
    private void showDetailTransaksi() {
        int selectedRow = tableRiwayat.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Pilih transaksi terlebih dahulu!",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String idTransaksi = tableModel.getValueAt(selectedRow, 0).toString();

        // Buka form detail
        new FormDetailTransaksi(idTransaksi).setVisible(true);
    }

    /**
     * Cetak struk transaksi dalam bentuk PDF
     */
    private void cetakStruk() {
        int selectedRow = tableRiwayat.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Pilih transaksi terlebih dahulu!",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String idTransaksi = tableModel.getValueAt(selectedRow, 0).toString();

        try {
            // Load data transaksi dan detail
            RiwayatTransaksi transaksi = riwayatDAO.getById(idTransaksi);
            distrozone.DAO.DetailTransaksiDAO detailDAO = new distrozone.DAO.DetailTransaksiDAO();
            java.util.List<distrozone.Model.DetailTransaksi> detailList = detailDAO.getByIdTransaksi(idTransaksi);

            if (transaksi == null) {
                JOptionPane.showMessageDialog(this,
                        "Transaksi tidak ditemukan!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Buat model StrukTransaksi
            distrozone.Model.StrukTransaksi struk = new distrozone.Model.StrukTransaksi();
            struk.setIdTransaksi(transaksi.getIdTransaksi());
            struk.setTanggalTransaksi(transaksi.getTanggalTransaksi());
            struk.setNamaKasir(transaksi.getNamaKasir());
            struk.setMetodePembayaran(transaksi.getMetodePembayaran());
            struk.setTotalBayar(transaksi.getTotalBayar());
            struk.setSubtotal(transaksi.getTotalBayar());
            struk.setItems(detailList);
            struk.setKembalian(0);

            // Generate PDF
            distrozone.SERVICEnew.StrukPDFService pdfService = new distrozone.SERVICEnew.StrukPDFService();
            String filePath = pdfService.generateStrukPDF(struk);

            if (filePath != null) {
                int option = JOptionPane.showConfirmDialog(this,
                        "Struk PDF berhasil dibuat!\n\nFile: " + filePath + "\n\nBuka file sekarang?",
                        "Sukses", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

                if (option == JOptionPane.YES_OPTION) {
                    java.awt.Desktop.getDesktop().open(new java.io.File(filePath));
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Gagal membuat struk PDF!",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error cetak struk: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Helper methods untuk filter manual
    private List<RiwayatTransaksi> filterManual(List<RiwayatTransaksi> list, Date from, Date to, String metode) {
        List<RiwayatTransaksi> filtered = new java.util.ArrayList<>();

        for (RiwayatTransaksi r : list) {
            Date transaksiDate = new Date(r.getTanggalTransaksi().getTime());

            if (isDateInRange(transaksiDate, from, to) &&
                    r.getMetodePembayaran().equals(metode)) {
                filtered.add(r);
            }
        }

        return filtered;
    }

    private List<RiwayatTransaksi> filterByDate(List<RiwayatTransaksi> list, Date from, Date to) {
        List<RiwayatTransaksi> filtered = new java.util.ArrayList<>();

        for (RiwayatTransaksi r : list) {
            Date transaksiDate = new Date(r.getTanggalTransaksi().getTime());

            if (isDateInRange(transaksiDate, from, to)) {
                filtered.add(r);
            }
        }

        return filtered;
    }

    private List<RiwayatTransaksi> filterByMetode(List<RiwayatTransaksi> list, String metode) {
        List<RiwayatTransaksi> filtered = new java.util.ArrayList<>();

        for (RiwayatTransaksi r : list) {
            if (r.getMetodePembayaran().equals(metode)) {
                filtered.add(r);
            }
        }

        return filtered;
    }

    private boolean isDateInRange(Date date, Date from, Date to) {
        return !date.before(from) && !date.after(to);
    }

    private String formatRupiah(int angka) {
        return "Rp " + String.format("%,d", angka).replace(',', '.');
    }
}