package distrozone.ADMIN.TRANSAKSI;

import distrozone.DAO.RiwayatTransaksiDAO;
import distrozone.DAO.DetailTransaksiDAO;
import distrozone.Model.RiwayatTransaksi;
import distrozone.Model.DetailTransaksi;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Form Detail Transaksi (Popup READ-ONLY)
 * 
 * FITUR:
 * ✅ Tampilkan header transaksi
 * ✅ Tampilkan list item yang dibeli
 * ✅ Harga dikunci (harga_saat_transaksi)
 * ✅ READ-ONLY (tidak bisa edit)
 * ✅ Tombol cetak struk
 */
public class FormDetailTransaksi extends JFrame {

    private String idTransaksi;
    private RiwayatTransaksi transaksi;
    private List<DetailTransaksi> detailList;

    private JLabel lblIdTransaksi, lblTanggal, lblKasir;
    private JLabel lblTotalItem, lblSubtotal, lblMetode, lblStatus;
    private JTable tableDetail;
    private DefaultTableModel tableModel;

    private RiwayatTransaksiDAO riwayatDAO;
    private DetailTransaksiDAO detailDAO;

    private String[] columnNames = {
            "No", "Nama Kaos", "Harga Satuan", "Qty", "Subtotal"
    };

    public FormDetailTransaksi(String idTransaksi) {
        this.idTransaksi = idTransaksi;
        this.riwayatDAO = new RiwayatTransaksiDAO();
        this.detailDAO = new DetailTransaksiDAO();

        initComponents();
        loadData();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Detail Transaksi - " + idTransaksi);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(40, 40, 45));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header Panel
        JPanel headerPanel = createHeaderPanel();

        // Info Panel
        JPanel infoPanel = createInfoPanel();

        // Table Panel
        JPanel tablePanel = createTablePanel();

        // Summary Panel
        JPanel summaryPanel = createSummaryPanel();

        // Button Panel
        JPanel buttonPanel = createButtonPanel();

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(new Color(40, 40, 45));
        centerPanel.add(infoPanel, BorderLayout.NORTH);
        centerPanel.add(tablePanel, BorderLayout.CENTER);
        centerPanel.add(summaryPanel, BorderLayout.SOUTH);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(40, 40, 45));

        JLabel lblTitle = new JLabel("🧾 DETAIL TRANSAKSI");
        lblTitle.setFont(new Font("Arial Black", Font.BOLD, 22));
        lblTitle.setForeground(new Color(255, 87, 51));

        JLabel lblReadOnly = new JLabel("(READ-ONLY - Data tidak dapat diubah)");
        lblReadOnly.setFont(new Font("Arial", Font.ITALIC, 12));
        lblReadOnly.setForeground(new Color(231, 76, 60));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setBackground(new Color(40, 40, 45));
        titlePanel.add(lblTitle);
        titlePanel.add(lblReadOnly);

        panel.add(titlePanel, BorderLayout.WEST);

        return panel;
    }

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(50, 50, 55));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(255, 87, 51), 2),
                "Informasi Transaksi",
                0, 0,
                new Font("Arial", Font.BOLD, 14),
                Color.WHITE));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 1
        gbc.gridx = 0;
        gbc.gridy = 0;
        addLabel(panel, "ID Transaksi:", gbc);

        gbc.gridx = 1;
        lblIdTransaksi = addValueLabel(panel, "-", gbc);

        gbc.gridx = 2;
        addLabel(panel, "Tanggal:", gbc);

        gbc.gridx = 3;
        lblTanggal = addValueLabel(panel, "-", gbc);

        // Row 2
        gbc.gridx = 0;
        gbc.gridy = 1;
        addLabel(panel, "Nama Kasir:", gbc);

        gbc.gridx = 1;
        lblKasir = addValueLabel(panel, "-", gbc);

        gbc.gridx = 2;
        addLabel(panel, "Metode Bayar:", gbc);

        gbc.gridx = 3;
        lblMetode = addValueLabel(panel, "-", gbc);

        // Row 3
        gbc.gridx = 0;
        gbc.gridy = 2;
        addLabel(panel, "Status:", gbc);

        gbc.gridx = 1;
        lblStatus = addValueLabel(panel, "-", gbc);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(40, 40, 45));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(46, 204, 113), 2),
                "Detail Produk",
                0, 0,
                new Font("Arial", Font.BOLD, 14),
                Color.WHITE));

        // Table Model
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // READ-ONLY
            }
        };

        tableDetail = new JTable(tableModel);
        tableDetail.setFont(new Font("Arial", Font.PLAIN, 12));
        tableDetail.setRowHeight(30);
        tableDetail.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tableDetail.getTableHeader().setBackground(new Color(46, 204, 113));
        tableDetail.getTableHeader().setForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(tableDetail);
        scrollPane.setPreferredSize(new Dimension(750, 200));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(50, 50, 55));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
                "Ringkasan",
                0, 0,
                new Font("Arial", Font.BOLD, 14),
                Color.WHITE));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblTotalItemLabel = new JLabel("Total Item:");
        lblTotalItemLabel.setFont(new Font("Arial", Font.BOLD, 16));
        lblTotalItemLabel.setForeground(Color.WHITE);
        panel.add(lblTotalItemLabel, gbc);

        gbc.gridx = 1;
        lblTotalItem = new JLabel("-");
        lblTotalItem.setFont(new Font("Arial", Font.BOLD, 16));
        lblTotalItem.setForeground(new Color(52, 152, 219));
        panel.add(lblTotalItem, gbc);

        gbc.gridx = 2;
        JLabel lblSubtotalLabel = new JLabel("TOTAL BAYAR:");
        lblSubtotalLabel.setFont(new Font("Arial", Font.BOLD, 18));
        lblSubtotalLabel.setForeground(Color.WHITE);
        panel.add(lblSubtotalLabel, gbc);

        gbc.gridx = 3;
        lblSubtotal = new JLabel("-");
        lblSubtotal.setFont(new Font("Arial", Font.BOLD, 20));
        lblSubtotal.setForeground(new Color(46, 204, 113));
        panel.add(lblSubtotal, gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(new Color(40, 40, 45));

        JButton btnCetak = createStyledButton("🖨️ Cetak Struk", new Color(46, 204, 113));
        btnCetak.addActionListener(e -> cetakStruk());
        panel.add(btnCetak);

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

    private void addLabel(JPanel panel, String text, GridBagConstraints gbc) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        label.setForeground(new Color(200, 200, 200));
        panel.add(label, gbc);
    }

    private JLabel addValueLabel(JPanel panel, String text, GridBagConstraints gbc) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 13));
        label.setForeground(Color.WHITE);
        panel.add(label, gbc);
        return label;
    }

    /**
     * Load data transaksi dan detail
     */
    private void loadData() {
        try {
            // Load header transaksi
            transaksi = riwayatDAO.getById(idTransaksi);

            if (transaksi == null) {
                JOptionPane.showMessageDialog(this,
                        "Transaksi tidak ditemukan!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                dispose();
                return;
            }

            // Load detail transaksi
            detailList = detailDAO.getByIdTransaksi(idTransaksi);

            // Display data
            displayHeader();
            displayDetail();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error load data: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Display header transaksi
     */
    private void displayHeader() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        lblIdTransaksi.setText(transaksi.getIdTransaksi());
        lblTanggal.setText(sdf.format(transaksi.getTanggalTransaksi()));
        lblKasir.setText(transaksi.getNamaKasir());
        lblMetode.setText(transaksi.getMetodePembayaran());

        // Status dengan warna
        lblStatus.setText(transaksi.getStatusTransaksi());
        if (transaksi.getStatusTransaksi().equals("SELESAI")) {
            lblStatus.setForeground(new Color(46, 204, 113));
        } else {
            lblStatus.setForeground(new Color(231, 76, 60));
        }

        lblTotalItem.setText(transaksi.getTotalItem() + " item");
        lblSubtotal.setText(formatRupiah(transaksi.getTotalBayar()));
    }

    /**
     * Display detail transaksi ke table
     */
    private void displayDetail() {
        tableModel.setRowCount(0);

        int no = 1;
        for (DetailTransaksi detail : detailList) {
            Object[] row = {
                    no++,
                    detail.getNamaKaos(),
                    formatRupiah(detail.getHargaSaatTransaksi()),
                    detail.getJumlah(),
                    formatRupiah(detail.getSubtotal())
            };
            tableModel.addRow(row);
        }
    }

    /**
     * Cetak struk dalam bentuk PDF
     */
    private void cetakStruk() {
        try {
            // Buat model StrukTransaksi
            distrozone.Model.StrukTransaksi struk = new distrozone.Model.StrukTransaksi();
            struk.setIdTransaksi(transaksi.getIdTransaksi());
            struk.setTanggalTransaksi(transaksi.getTanggalTransaksi());
            struk.setNamaKasir(transaksi.getNamaKasir());
            struk.setMetodePembayaran(transaksi.getMetodePembayaran());
            struk.setTotalBayar(transaksi.getTotalBayar());
            struk.setSubtotal(transaksi.getTotalBayar());
            struk.setItems(detailList);
            struk.setKembalian(0); // Tidak ada data kembalian di riwayat

            // Generate PDF
            distrozone.SERVICEnew.StrukPDFService pdfService = new distrozone.SERVICEnew.StrukPDFService();
            String filePath = pdfService.generateStrukPDF(struk);

            if (filePath != null) {
                int option = JOptionPane.showConfirmDialog(this,
                        "Struk PDF berhasil dibuat!\n\nFile: " + filePath + "\n\nBuka file sekarang?",
                        "Sukses", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

                if (option == JOptionPane.YES_OPTION) {
                    // Buka file PDF
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

    private String formatRupiah(int angka) {
        return "Rp " + String.format("%,d", angka).replace(',', '.');
    }
}