package distrozone.ADMIN.DATA_KAOS;

import distrozone.Model.Kaos;
import distrozone.SERVICEnew.KaosService;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

/**
 * FormTambahKaos - UI LAYER
 * 
 * HANYA UNTUK:
 * - Ambil input dari user
 * - Tampilkan preview
 * - Kirim data ke SERVICE
 * - Tampilkan pesan sukses/error
 * 
 * TIDAK BOLEH:
 * - Ada SQL
 * - Ada validasi bisnis (service yang urus)
 * - Hitung profit (service yang urus)
 */
public class FormTambahKaos extends JFrame {

    private JTextField txtMerek, txtWarna, txtHargaPokok, txtHargaJual, txtStok, txtFoto;
    private JComboBox<String> cmbTipe, cmbSize;
    private JLabel lblPreviewFoto, lblProfit;
    private String selectedFilePath = "";
    private FormDataKaos parent;
    private KaosService kaosService; // SERVICE untuk logika bisnis

    public FormTambahKaos(FormDataKaos parent) {
        this.parent = parent;
        this.kaosService = new KaosService();
        
        setTitle("Tambah Data Kaos - DistroZone");
        setSize(550, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(40, 40, 45));

        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(40, 40, 45));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
        
        JLabel lblTitle = new JLabel("➕ TAMBAH DATA KAOS");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(Color.WHITE);
        titlePanel.add(lblTitle);
        
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Form Panel dengan Scroll
        JPanel formContainer = new JPanel();
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));
        formContainer.setBackground(new Color(40, 40, 45));
        formContainer.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));

        // Merek
        formContainer.add(createLabel("Merek Kaos"));
        txtMerek = createTextField("");
        formContainer.add(txtMerek);
        formContainer.add(Box.createRigidArea(new Dimension(0, 15)));

        // Tipe
        formContainer.add(createLabel("Tipe Kaos"));
        String[] tipeOptions = {"Lengan Panjang", "Lengan Pendek"};
        cmbTipe = createComboBox(tipeOptions);
        formContainer.add(cmbTipe);
        formContainer.add(Box.createRigidArea(new Dimension(0, 15)));

        // Warna
        formContainer.add(createLabel("Warna"));
        txtWarna = createTextField("");
        formContainer.add(txtWarna);
        formContainer.add(Box.createRigidArea(new Dimension(0, 15)));

        // Size
        formContainer.add(createLabel("Size"));
        String[] sizeOptions = {"XS", "S", "M", "L", "XL", "2XL", "3XL", "4XL", "5XL"};
        cmbSize = createComboBox(sizeOptions);
        formContainer.add(cmbSize);
        formContainer.add(Box.createRigidArea(new Dimension(0, 15)));

        // Harga Pokok
        formContainer.add(createLabel("Harga Pokok (Modal)"));
        txtHargaPokok = createTextField("");
        txtHargaPokok.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                hitungProfitPreview(); // Hanya untuk preview di UI
            }
        });
        formContainer.add(txtHargaPokok);
        formContainer.add(Box.createRigidArea(new Dimension(0, 15)));

        // Harga Jual
        formContainer.add(createLabel("Harga Jual"));
        txtHargaJual = createTextField("");
        txtHargaJual.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                hitungProfitPreview(); // Hanya untuk preview di UI
            }
        });
        formContainer.add(txtHargaJual);
        formContainer.add(Box.createRigidArea(new Dimension(0, 15)));

        // Profit Display (hanya preview, bukan kalkulasi final)
        JPanel profitPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        profitPanel.setBackground(new Color(40, 40, 45));
        profitPanel.setMaximumSize(new Dimension(490, 35));
        
        lblProfit = new JLabel("💰 Profit: Rp 0");
        lblProfit.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblProfit.setForeground(new Color(46, 204, 113));
        
        profitPanel.add(lblProfit);
        formContainer.add(profitPanel);
        formContainer.add(Box.createRigidArea(new Dimension(0, 15)));

        // Stok
        formContainer.add(createLabel("Stok"));
        txtStok = createTextField("");
        formContainer.add(txtStok);
        formContainer.add(Box.createRigidArea(new Dimension(0, 15)));

        // Foto
        formContainer.add(createLabel("Foto Kaos (Opsional)"));
        
        JPanel fotoPanel = new JPanel(new BorderLayout(10, 0));
        fotoPanel.setBackground(new Color(40, 40, 45));
        fotoPanel.setMaximumSize(new Dimension(490, 35));
        
        txtFoto = createTextField("");
        txtFoto.setEditable(false);
        txtFoto.setMaximumSize(new Dimension(360, 35));
        
        JButton btnBrowse = new JButton("📁 Browse");
        btnBrowse.setPreferredSize(new Dimension(120, 35));
        btnBrowse.setBackground(new Color(52, 152, 219));
        btnBrowse.setForeground(Color.WHITE);
        btnBrowse.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBrowse.setFocusPainted(false);
        btnBrowse.setBorderPainted(false);
        btnBrowse.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBrowse.addActionListener(e -> pilihFoto());
        
        fotoPanel.add(txtFoto, BorderLayout.CENTER);
        fotoPanel.add(btnBrowse, BorderLayout.EAST);
        
        formContainer.add(fotoPanel);
        formContainer.add(Box.createRigidArea(new Dimension(0, 15)));

        // Preview Foto
        lblPreviewFoto = new JLabel("Belum ada foto", SwingConstants.CENTER);
        lblPreviewFoto.setPreferredSize(new Dimension(200, 200));
        lblPreviewFoto.setMaximumSize(new Dimension(200, 200));
        lblPreviewFoto.setBackground(new Color(60, 60, 65));
        lblPreviewFoto.setForeground(new Color(150, 150, 150));
        lblPreviewFoto.setOpaque(true);
        lblPreviewFoto.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 105), 2));
        lblPreviewFoto.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        formContainer.add(lblPreviewFoto);
        formContainer.add(Box.createRigidArea(new Dimension(0, 20)));

        JScrollPane scrollPane = new JScrollPane(formContainer);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(new Color(40, 40, 45));
        buttonPanel.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, new Color(70, 70, 75)));

        JButton btnSimpan = createButton("💾 Simpan Data", new Color(46, 204, 113));
        btnSimpan.addActionListener(e -> simpanData());

        JButton btnBatal = createButton("❌ Batal", new Color(231, 76, 60));
        btnBatal.addActionListener(e -> dispose());

        buttonPanel.add(btnSimpan);
        buttonPanel.add(btnBatal);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JTextField createTextField(String value) {
        JTextField txt = new JTextField(value);
        txt.setMaximumSize(new Dimension(490, 35));
        txt.setPreferredSize(new Dimension(490, 35));
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBackground(new Color(60, 60, 65));
        txt.setForeground(Color.WHITE);
        txt.setCaretColor(Color.WHITE);
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 105), 1),
            BorderFactory.createEmptyBorder(5, 12, 5, 12)
        ));
        return txt;
    }

    private JComboBox<String> createComboBox(String[] options) {
        JComboBox<String> cmb = new JComboBox<>(options);
        cmb.setMaximumSize(new Dimension(490, 35));
        cmb.setPreferredSize(new Dimension(490, 35));
        cmb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmb.setBackground(new Color(60, 60, 65));
        cmb.setForeground(Color.BLACK);
        cmb.setFocusable(false);
        
        cmb.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);
                
                if (isSelected) {
                    label.setBackground(new Color(52, 152, 219));
                    label.setForeground(Color.WHITE);
                } else {
                    label.setBackground(new Color(60, 60, 65));
                    label.setForeground(Color.WHITE);
                }
                label.setOpaque(true);
                return label;
            }
        });
        
        return cmb;
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(180, 45));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
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

    private void pilihFoto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Pilih Foto Kaos");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Image Files", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            selectedFilePath = selectedFile.getAbsolutePath();
            txtFoto.setText(selectedFile.getName());

            // Preview foto
            try {
                ImageIcon icon = new ImageIcon(selectedFilePath);
                Image image = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                lblPreviewFoto.setIcon(new ImageIcon(image));
                lblPreviewFoto.setText("");
            } catch (Exception e) {
                lblPreviewFoto.setText("Gagal load preview");
            }
        }
    }

    /**
     * Hitung profit untuk PREVIEW saja (bukan validasi final)
     * Kalkulasi final dilakukan di SERVICE
     */
    private void hitungProfitPreview() {
        try {
            String hargaPokokStr = txtHargaPokok.getText().trim();
            String hargaJualStr = txtHargaJual.getText().trim();
            
            if (!hargaPokokStr.isEmpty() && !hargaJualStr.isEmpty()) {
                int hargaPokok = Integer.parseInt(hargaPokokStr);
                int hargaJual = Integer.parseInt(hargaJualStr);
                int profit = hargaJual - hargaPokok;
                
                lblProfit.setText("💰 Profit: Rp " + String.format("%,d", profit));
                
                if (profit < 0) {
                    lblProfit.setForeground(new Color(231, 76, 60)); // Merah
                } else if (profit == 0) {
                    lblProfit.setForeground(new Color(243, 156, 18)); // Orange
                } else {
                    lblProfit.setForeground(new Color(46, 204, 113)); // Hijau
                }
            }
        } catch (NumberFormatException e) {
            lblProfit.setText("💰 Profit: Rp 0");
            lblProfit.setForeground(new Color(46, 204, 113));
        }
    }

    /**
     * Simpan data - SEMUA VALIDASI DI SERVICE
     * Form hanya:
     * 1. Ambil input
     * 2. Buat object Kaos
     * 3. Kirim ke service
     * 4. Tampilkan hasil
     */
    private void simpanData() {
        try {
            // 1. AMBIL INPUT dari form (tidak ada validasi di sini!)
            String merek = txtMerek.getText().trim();
            String tipe = (String) cmbTipe.getSelectedItem();
            String warna = txtWarna.getText().trim();
            String size = (String) cmbSize.getSelectedItem();
            
            // Parse harga dan stok - jika error, biarkan exception terjadi
            int hargaPokok = Integer.parseInt(txtHargaPokok.getText().trim());
            int hargaJual = Integer.parseInt(txtHargaJual.getText().trim());
            int stok = Integer.parseInt(txtStok.getText().trim());
            
            // 2. BUAT OBJECT KAOS
            Kaos kaos = new Kaos();
            kaos.setMerekKaos(merek);
            kaos.setTypeKaos(tipe);
            kaos.setWarnaKaos(warna);
            kaos.setSize(size);
            kaos.setHargaPokok(hargaPokok);
            kaos.setHargaJual(hargaJual);
            kaos.setStokKaos(stok);
            kaos.setFotoKaos(selectedFilePath.isEmpty() ? null : selectedFilePath);
            
            // 3. KONFIRMASI ke user sebelum simpan
            int profit = kaosService.hitungProfit(kaos);
            int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin menyimpan data kaos ini?\n\n" +
                "Merek: " + merek + "\n" +
                "Tipe: " + tipe + "\n" +
                "Warna: " + warna + "\n" +
                "Size: " + size + "\n" +
                "Harga Pokok: Rp " + String.format("%,d", hargaPokok) + "\n" +
                "Harga Jual: Rp " + String.format("%,d", hargaJual) + "\n" +
                "Profit: Rp " + String.format("%,d", profit) + "\n" +
                "Stok: " + stok,
                "Konfirmasi Simpan",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

            if (confirm != JOptionPane.YES_OPTION) {
                return; // User cancel
            }
            
            // 4. KIRIM KE SERVICE (validasi ada di sini!)
            kaosService.tambahKaos(kaos);
            
            // 5. TAMPILKAN SUKSES
            JOptionPane.showMessageDialog(this,
                "✅ Data kaos berhasil disimpan!",
                "Sukses",
                JOptionPane.INFORMATION_MESSAGE);
            
            // 6. REFRESH parent table dan tutup form
            parent.loadDataKaos();
            dispose();
            
        } catch (NumberFormatException e) {
            // Error parsing angka
            JOptionPane.showMessageDialog(this,
                "Harga Pokok, Harga Jual, dan Stok harus berupa angka!",
                "Error Input",
                JOptionPane.ERROR_MESSAGE);
                
        } catch (Exception e) {
            // Error dari SERVICE (validasi bisnis atau database)
            JOptionPane.showMessageDialog(this,
                e.getMessage(), // Pesan error dari service
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}