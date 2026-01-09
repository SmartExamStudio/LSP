package distrozone.KASIR;

import distrozone.DAO.KaosDAO;
import distrozone.Model.Kaos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

/**
 * Form Lihat Stok Produk (READ-ONLY untuk Kasir)
 * Kasir hanya bisa melihat stok, tidak bisa edit/hapus
 */
public class FormLihatStok extends JFrame {

    private JTable tableStok;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private KaosDAO kaosDAO;

    private String[] columnNames = {
            "ID", "Merek", "Jenis", "Warna", "Ukuran", "Harga Jual", "Stok"
    };

    public FormLihatStok() {
        kaosDAO = new KaosDAO();
        initComponents();
        loadData();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Cek Stok Produk - Kasir");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(40, 40, 45));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Table Panel
        JPanel tablePanel = createTablePanel();
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(40, 40, 45));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Title
        JLabel lblTitle = new JLabel("📦 CEK STOK PRODUK");
        lblTitle.setFont(new Font("Arial Black", Font.BOLD, 24));
        lblTitle.setForeground(new Color(52, 152, 219));

        JLabel lblSubtitle = new JLabel("(READ-ONLY - Hanya untuk melihat stok)");
        lblSubtitle.setFont(new Font("Arial", Font.ITALIC, 12));
        lblSubtitle.setForeground(new Color(150, 150, 150));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setBackground(new Color(40, 40, 45));
        titlePanel.add(lblTitle);
        titlePanel.add(lblSubtitle);

        panel.add(titlePanel, BorderLayout.WEST);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchPanel.setBackground(new Color(40, 40, 45));

        JLabel lblSearch = new JLabel("🔍 Cari:");
        lblSearch.setForeground(Color.WHITE);
        lblSearch.setFont(new Font("Arial", Font.BOLD, 14));
        searchPanel.add(lblSearch);

        txtSearch = new JTextField(25);
        txtSearch.setFont(new Font("Arial", Font.PLAIN, 14));
        txtSearch.setPreferredSize(new Dimension(250, 35));
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchData();
            }
        });
        searchPanel.add(txtSearch);

        JButton btnRefresh = new JButton("🔄 Refresh");
        btnRefresh.setFont(new Font("Arial", Font.BOLD, 12));
        btnRefresh.setBackground(new Color(155, 89, 182));
        btnRefresh.setForeground(Color.BLACK);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setPreferredSize(new Dimension(100, 35));
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(e -> {
            txtSearch.setText("");
            loadData();
        });
        searchPanel.add(btnRefresh);

        panel.add(searchPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(40, 40, 45));

        // Table Model (READ-ONLY)
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // READ-ONLY ✅
            }
        };

        tableStok = new JTable(tableModel);
        tableStok.setFont(new Font("Arial", Font.PLAIN, 13));
        tableStok.setRowHeight(35);
        tableStok.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tableStok.getTableHeader().setBackground(new Color(52, 152, 219));
        tableStok.getTableHeader().setForeground(Color.BLACK);
        tableStok.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Set column widths
        tableStok.getColumnModel().getColumn(0).setPreferredWidth(50);
        tableStok.getColumnModel().getColumn(1).setPreferredWidth(150);
        tableStok.getColumnModel().getColumn(2).setPreferredWidth(120);
        tableStok.getColumnModel().getColumn(3).setPreferredWidth(80);
        tableStok.getColumnModel().getColumn(4).setPreferredWidth(60);
        tableStok.getColumnModel().getColumn(5).setPreferredWidth(120);
        tableStok.getColumnModel().getColumn(6).setPreferredWidth(80);

        // Color code stock levels
        tableStok.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(CENTER);

                try {
                    int stok = Integer.parseInt(value.toString());
                    if (stok == 0) {
                        setBackground(new Color(220, 53, 69)); // Red
                        setForeground(Color.WHITE);
                    } else if (stok <= 5) {
                        setBackground(new Color(255, 193, 7)); // Yellow
                        setForeground(Color.BLACK);
                    } else {
                        setBackground(new Color(46, 204, 113)); // Green
                        setForeground(Color.WHITE);
                    }
                } catch (Exception e) {
                    setBackground(Color.WHITE);
                    setForeground(Color.BLACK);
                }

                if (isSelected) {
                    setBackground(new Color(52, 152, 219));
                    setForeground(Color.WHITE);
                }

                return this;
            }
        });

        JScrollPane scrollPane = new JScrollPane(tableStok);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 65), 2));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panel.setBackground(new Color(40, 40, 45));

        JButton btnClose = new JButton("❌ Tutup");
        btnClose.setFont(new Font("Arial", Font.BOLD, 14));
        btnClose.setBackground(new Color(149, 165, 166));
        btnClose.setForeground(Color.BLACK);
        btnClose.setFocusPainted(false);
        btnClose.setPreferredSize(new Dimension(150, 45));
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClose.addActionListener(e -> dispose());
        panel.add(btnClose);

        return panel;
    }

    private void loadData() {
        tableModel.setRowCount(0);

        try {
            List<Kaos> listKaos = kaosDAO.getAllKaos();

            for (Kaos kaos : listKaos) {
                Object[] row = {
                        kaos.getIdKaos(),
                        kaos.getMerekKaos(),
                        kaos.getTypeKaos(),
                        kaos.getWarnaKaos(),
                        kaos.getSize(),
                        formatRupiah(kaos.getHargaJual()),
                        kaos.getStokKaos()
                };
                tableModel.addRow(row);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error load data: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchData() {
        String keyword = txtSearch.getText().trim().toLowerCase();

        if (keyword.isEmpty()) {
            loadData();
            return;
        }

        tableModel.setRowCount(0);

        try {
            // Get all data and filter manually since search method may not exist
            List<Kaos> listKaos = kaosDAO.getAllKaos();

            for (Kaos kaos : listKaos) {
                // Manual filter by keyword
                String merek = kaos.getMerekKaos() != null ? kaos.getMerekKaos().toLowerCase() : "";
                String tipe = kaos.getTypeKaos() != null ? kaos.getTypeKaos().toLowerCase() : "";
                String warna = kaos.getWarnaKaos() != null ? kaos.getWarnaKaos().toLowerCase() : "";
                String size = kaos.getSize() != null ? kaos.getSize().toLowerCase() : "";

                if (merek.contains(keyword) || tipe.contains(keyword) ||
                        warna.contains(keyword) || size.contains(keyword)) {
                    Object[] row = {
                            kaos.getIdKaos(),
                            kaos.getMerekKaos(),
                            kaos.getTypeKaos(),
                            kaos.getWarnaKaos(),
                            kaos.getSize(),
                            formatRupiah(kaos.getHargaJual()),
                            kaos.getStokKaos()
                    };
                    tableModel.addRow(row);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error search: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String formatRupiah(int angka) {
        return "Rp " + String.format("%,d", angka).replace(',', '.');
    }
}
