package distrozone.ADMIN.DATA_KARYAWAN;

import distrozone.DataBaseConector;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class FormDataKaryawan extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private JTextField txtSearch;

    public FormDataKaryawan() {
        setTitle("Data Karyawan - DistroZone");
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
        
        JLabel lblTitle = new JLabel("👥 DATA KARYAWAN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        titlePanel.add(lblTitle);
        
        topPanel.add(titlePanel, BorderLayout.WEST);
        
        // Button Section
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(new Color(40, 40, 45));

        JButton btnTambah = createButton("+ Tambah Karyawan", new Color(46, 204, 113));
        btnTambah.addActionListener(e -> new FormTambahKaryawan(this).setVisible(true));

        JButton btnEdit = createButton("✏️ Edit Karyawan", new Color(52, 152, 219));
        btnEdit.addActionListener(e -> editKaryawan());

        JButton btnHapus = createButton("🗑️ Hapus Karyawan", new Color(231, 76, 60));
        btnHapus.addActionListener(e -> hapusKaryawan());

        JButton btnRefresh = createButton("🔄 Refresh", new Color(149, 165, 166));
        btnRefresh.addActionListener(e -> loadDataKaryawan());

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
                if (column == 7) return ImageIcon.class; // Kolom foto
                return Object.class;
            }
        };
        
        model.addColumn("ID");
        model.addColumn("Nama");
        model.addColumn("Username");
        model.addColumn("Alamat");
        model.addColumn("No. Telepon");
        model.addColumn("NIK");
        model.addColumn("Password");
        model.addColumn("Foto");

        table = new JTable(model);
        table.setRowHeight(80);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setBackground(new Color(50, 50, 55));
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(70, 70, 75));
        table.setSelectionBackground(new Color(52, 152, 219));
        table.setSelectionForeground(Color.WHITE);
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
        table.getColumnModel().getColumn(1).setPreferredWidth(150);  // Nama
        table.getColumnModel().getColumn(2).setPreferredWidth(120);  // Username
        table.getColumnModel().getColumn(3).setPreferredWidth(200);  // Alamat
        table.getColumnModel().getColumn(4).setPreferredWidth(120);  // No Telepon
        table.getColumnModel().getColumn(5).setPreferredWidth(120);  // NIK
        table.getColumnModel().getColumn(6).setPreferredWidth(100);  // Password
        table.getColumnModel().getColumn(7).setPreferredWidth(90);   // Foto

        // Set alignment
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);

        // Custom renderer untuk kolom foto
        table.getColumnModel().getColumn(7).setCellRenderer(new TableCellRenderer() {
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

        loadDataKaryawan();
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(170, 40));
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

    public void loadDataKaryawan() {
        model.setRowCount(0);

        try {
            Connection conn = DataBaseConector.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(
                "SELECT id_karyawan, nama, username, alamat, no_telepon, NIK, password, foto_karyawan FROM tb_karyawan ORDER BY id_karyawan DESC"
            );

            while (rs.next()) {
                String foto = rs.getString("foto_karyawan");
                Object fotoDisplay;
                
                if (foto == null || foto.isEmpty()) {
                    fotoDisplay = "Tidak ada foto";
                } else {
                    ImageIcon thumbnail = createThumbnail(foto);
                    if (thumbnail != null) {
                        fotoDisplay = thumbnail;
                    } else {
                        fotoDisplay = "File hilang";
                    }
                }

                Object[] row = {
                    rs.getInt("id_karyawan"),
                    rs.getString("nama"),
                    rs.getString("username"),
                    rs.getString("alamat"),
                    rs.getString("no_telepon"),
                    rs.getString("NIK"),
                    "••••••", // Mask password
                    fotoDisplay
                };
                model.addRow(row);
            }

            rs.close();
            st.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Gagal load data karyawan:\n" + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void searchData() {
        String keyword = txtSearch.getText().toLowerCase();
        model.setRowCount(0);

        try {
            Connection conn = DataBaseConector.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(
                "SELECT id_karyawan, nama, username, alamat, no_telepon, NIK, password, foto_karyawan FROM tb_karyawan ORDER BY id_karyawan DESC"
            );

            while (rs.next()) {
                String nama = rs.getString("nama").toLowerCase();
                String username = rs.getString("username").toLowerCase();
                String alamat = rs.getString("alamat") != null ? rs.getString("alamat").toLowerCase() : "";
                String nik = rs.getString("NIK") != null ? rs.getString("NIK").toLowerCase() : "";

                if (nama.contains(keyword) || username.contains(keyword) || 
                    alamat.contains(keyword) || nik.contains(keyword)) {
                    
                    String foto = rs.getString("foto_karyawan");
                    Object fotoDisplay;
                    
                    if (foto == null || foto.isEmpty()) {
                        fotoDisplay = "Tidak ada foto";
                    } else {
                        ImageIcon thumbnail = createThumbnail(foto);
                        if (thumbnail != null) {
                            fotoDisplay = thumbnail;
                        } else {
                            fotoDisplay = "File hilang";
                        }
                    }

                    Object[] row = {
                        rs.getInt("id_karyawan"),
                        rs.getString("nama"),
                        rs.getString("username"),
                        rs.getString("alamat"),
                        rs.getString("no_telepon"),
                        rs.getString("NIK"),
                        "••••••",
                        fotoDisplay
                    };
                    model.addRow(row);
                }
            }

            rs.close();
            st.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Gagal search data:\n" + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editKaryawan() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                "Pilih data karyawan yang ingin diedit!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(table.getValueAt(row, 0).toString());
            
            Connection conn = DataBaseConector.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM tb_karyawan WHERE id_karyawan=" + id);
            
            if (rs.next()) {
                new FormEditKaryawan(this, 
                    rs.getInt("id_karyawan"),
                    rs.getString("nama"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("alamat"),
                    rs.getString("no_telepon"),
                    rs.getString("NIK"),
                    rs.getString("foto_karyawan")
                ).setVisible(true);
            }
            
            rs.close();
            st.close();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Gagal membuka form edit:\n" + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapusKaryawan() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                "Pilih data karyawan yang ingin dihapus!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = Integer.parseInt(table.getValueAt(row, 0).toString());
        String nama = table.getValueAt(row, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
            "Apakah Anda yakin ingin menghapus karyawan:\n" +
            "Nama: " + nama + " (ID: " + id + ")?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Connection conn = DataBaseConector.getConnection();
                String sql = "DELETE FROM tb_karyawan WHERE id_karyawan=?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setInt(1, id);
                
                int result = pst.executeUpdate();
                
                if (result > 0) {
                    JOptionPane.showMessageDialog(this,
                        "✅ Data karyawan berhasil dihapus!",
                        "Sukses",
                        JOptionPane.INFORMATION_MESSAGE);
                    loadDataKaryawan();
                }
                
                pst.close();
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Gagal menghapus data:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}