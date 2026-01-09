package distrozone.ADMIN.DATA_KAOS;

import distrozone.Model.Kaos;
import distrozone.SERVICEnew.KaosService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.File;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * FormDataKaos - UI LAYER
 * 
 * HANYA UNTUK:
 * - Menampilkan data di tabel
 * - Ambil input user (klik tombol)
 * - Tampilkan pesan error/sukses
 * 
 * TIDAK BOLEH:
 * - Ada SQL
 * - Hitung profit
 * - Update stok
 * - Validasi bisnis
 */
public class FormDataKaos extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private JTextField txtSearch;
    private KaosService kaosService; // Service untuk logika bisnis

    public FormDataKaos() {
        // Inisialisasi service
        this.kaosService = new KaosService();
        
        setTitle("Data Kaos - DistroZone");
        setSize(1300, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(40, 40, 45));

        // ===== TOP PANEL =====
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(40, 40, 45));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Title Section
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(new Color(40, 40, 45));
        
        JLabel lblTitle = new JLabel("📦 DATA KAOS");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        titlePanel.add(lblTitle);
        
        topPanel.add(titlePanel, BorderLayout.WEST);
        
        // Button Section
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(new Color(40, 40, 45));

        JButton btnTambah = createButton("+ Tambah Kaos", new Color(46, 204, 113));
        btnTambah.addActionListener(e -> new FormTambahKaos(this).setVisible(true));

        JButton btnEdit = createButton("✏️ Edit Kaos", new Color(52, 152, 219));
        btnEdit.addActionListener(e -> editKaos());

        JButton btnHapus = createButton("🗑️ Hapus Kaos", new Color(231, 76, 60));
        btnHapus.addActionListener(e -> hapusKaos());

        JButton btnRefresh = createButton("🔄 Refresh", new Color(149, 165, 166));
        btnRefresh.addActionListener(e -> loadDataKaos());

        buttonPanel.add(btnTambah);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnRefresh);

        topPanel.add(buttonPanel, BorderLayout.EAST);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // ===== SEARCH PANEL =====
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        searchPanel.setBackground(new Color(50, 50, 55));
        searchPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(70, 70, 75)));

        JLabel lblSearch = new JLabel("🔍 Cari:");
        lblSearch.setForeground(Color.WHITE);
        lblSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        txtSearch = new JTextField(30);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSearch.setBackground(new Color(60, 60, 65));
        txtSearch.setForeground(Color.WHITE);
        txtSearch.setCaretColor(Color.WHITE);
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 105), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
        // Search dilakukan saat user berhenti mengetik (tidak setiap keystroke)
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchData();
            }
        });

        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(40, 40, 45));
        centerPanel.add(searchPanel, BorderLayout.NORTH);

        // ===== TABLE =====
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 9) return ImageIcon.class; // Kolom foto
                return Object.class;
            }
        };
        
        String[] columnNames = {
            "ID", "Merek", "Tipe", "Warna", "Size", "Harga Pokok", "Harga Jual", "Profit", "Stok", "Foto"
        };
        
        for (String col : columnNames) {
            model.addColumn(col);
        }

        table = new JTable(model);
        table.setRowHeight(80);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setBackground(new Color(50, 50, 55));
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(70, 70, 75));
        table.setSelectionBackground(new Color(52, 152, 219));
        table.setSelectionForeground(Color.BLACK);
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);

        // Table Header Styling
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(60, 60, 65));
        header.setForeground(Color.BLACK);
        header.setPreferredSize(new Dimension(100, 40));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(52, 152, 219)));

        // Set column width
        table.getColumnModel().getColumn(0).setPreferredWidth(50);   // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(120);  // Merek
        table.getColumnModel().getColumn(2).setPreferredWidth(110);  // Tipe
        table.getColumnModel().getColumn(3).setPreferredWidth(90);   // Warna
        table.getColumnModel().getColumn(4).setPreferredWidth(50);   // Size
        table.getColumnModel().getColumn(5).setPreferredWidth(110);  // Harga Pokok
        table.getColumnModel().getColumn(6).setPreferredWidth(110);  // Harga Jual
        table.getColumnModel().getColumn(7).setPreferredWidth(110);  // Profit
        table.getColumnModel().getColumn(8).setPreferredWidth(60);   // Stok
        table.getColumnModel().getColumn(9).setPreferredWidth(90);   // Foto

        // Set alignment untuk kolom tertentu
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(8).setCellRenderer(centerRenderer);

        // Custom renderer untuk kolom foto
        table.getColumnModel().getColumn(9).setCellRenderer(new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
                
                JLabel label = new JLabel();
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setVerticalAlignment(JLabel.CENTER);
                label.setOpaque(true);
                
                if (isSelected) {
                    label.setBackground(new Color(52, 152, 219));
                } else {
                    label.setBackground(new Color(50, 50, 55));
                }
                
                if (value instanceof ImageIcon) {
                    label.setIcon((ImageIcon) value);
                } else {
                    label.setText(value != null ? value.toString() : "");
                    label.setForeground(new Color(150, 150, 150));
                    label.setFont(new Font("Segoe UI", Font.ITALIC, 11));
                }
                
                label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                return label;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(new Color(40, 40, 45));
        scrollPane.getViewport().setBackground(new Color(50, 50, 55));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(40, 40, 45));
        tablePanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        centerPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);

        // Load data saat form dibuka
        loadDataKaos();
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(150, 40));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
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

    private ImageIcon createThumbnail(String fotoPath) {
        try {
            if (fotoPath == null || fotoPath.isEmpty()) {
                return null;
            }
            
            File file = new File(fotoPath);
            if (file.exists()) {
                ImageIcon icon = new ImageIcon(fotoPath);
                Image image = icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                return new ImageIcon(image);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Load semua data kaos dari SERVICE
     * Form hanya tampilkan data, tidak ada SQL di sini!
     */
    public void loadDataKaos() {
        model.setRowCount(0); // Clear table
        
        // Formatter untuk mata uang Indonesia
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

        try {
            // Ambil data dari SERVICE (bukan langsung ke database!)
            List<Kaos> listKaos = kaosService.getAllKaos();
            
            // Loop dan tampilkan ke table
            for (Kaos kaos : listKaos) {
                // Hitung profit pakai service
                int profit = kaosService.hitungProfit(kaos);
                
                // Handle foto
                Object fotoDisplay;
                String fotoPath = kaos.getFotoKaos();
                
                if (fotoPath == null || fotoPath.isEmpty()) {
                    fotoDisplay = "Tidak ada foto";
                } else {
                    ImageIcon thumbnail = createThumbnail(fotoPath);
                    if (thumbnail != null) {
                        fotoDisplay = thumbnail;
                    } else {
                        fotoDisplay = "File hilang";
                    }
                }

                // Tambahkan row ke table
                Object[] row = {
                    kaos.getIdKaos(),
                    kaos.getMerekKaos(),
                    kaos.getTypeKaos(),
                    kaos.getWarnaKaos(),
                    kaos.getSize(),
                    currencyFormat.format(kaos.getHargaPokok()),
                    currencyFormat.format(kaos.getHargaJual()),
                    currencyFormat.format(profit),
                    kaos.getStokKaos(),
                    fotoDisplay
                };
                model.addRow(row);
            }

        } catch (Exception e) {
            // Tampilkan error ke user
            JOptionPane.showMessageDialog(this,
                e.getMessage(),
                "Error Load Data",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Search data dari SERVICE (bukan filter di Java!)
     */
    private void searchData() {
        String keyword = txtSearch.getText().trim();
        model.setRowCount(0); // Clear table

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

        try {
            // Search menggunakan SERVICE (yang akan query ke SQL)
            List<Kaos> listKaos = kaosService.searchKaos(keyword);
            
            // Tampilkan hasil search
            for (Kaos kaos : listKaos) {
                int profit = kaosService.hitungProfit(kaos);
                
                Object fotoDisplay;
                String fotoPath = kaos.getFotoKaos();
                
                if (fotoPath == null || fotoPath.isEmpty()) {
                    fotoDisplay = "Tidak ada foto";
                } else {
                    ImageIcon thumbnail = createThumbnail(fotoPath);
                    if (thumbnail != null) {
                        fotoDisplay = thumbnail;
                    } else {
                        fotoDisplay = "File hilang";
                    }
                }

                Object[] row = {
                    kaos.getIdKaos(),
                    kaos.getMerekKaos(),
                    kaos.getTypeKaos(),
                    kaos.getWarnaKaos(),
                    kaos.getSize(),
                    currencyFormat.format(kaos.getHargaPokok()),
                    currencyFormat.format(kaos.getHargaJual()),
                    currencyFormat.format(profit),
                    kaos.getStokKaos(),
                    fotoDisplay
                };
                model.addRow(row);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                e.getMessage(),
                "Error Search",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Edit kaos - hanya buka form edit
     */
    private void editKaos() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                "Pilih data kaos yang ingin diedit!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Ambil ID dari table
            int id = (Integer) table.getValueAt(row, 0);
            
            // Ambil data lengkap dari SERVICE
            Kaos kaos = kaosService.getKaosById(id);
            
            // Buka form edit dengan data kaos
            new FormEditKaos(this, kaos).setVisible(true);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Hapus kaos - panggil SERVICE untuk delete
     */
    private void hapusKaos() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                "Pilih data kaos yang ingin dihapus!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Ambil data dari table untuk konfirmasi
        int id = (Integer) table.getValueAt(row, 0);
        String merek = table.getValueAt(row, 1).toString();

        // Konfirmasi hapus
        int confirm = JOptionPane.showConfirmDialog(this,
            "Apakah Anda yakin ingin menghapus kaos:\n" +
            "Merek: " + merek + " (ID: " + id + ")?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Hapus melalui SERVICE
                kaosService.hapusKaos(id);
                
                // Tampilkan pesan sukses
                JOptionPane.showMessageDialog(this,
                    "Data kaos berhasil dihapus!",
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Refresh table
                loadDataKaos();
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}