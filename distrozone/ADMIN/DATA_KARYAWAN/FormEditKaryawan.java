package distrozone.ADMIN.DATA_KARYAWAN;

import distrozone.ADMIN.DATA_KARYAWAN.FormDataKaryawan;
import distrozone.DataBaseConector;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class FormEditKaryawan extends JFrame {

    private FormDataKaryawan parent;
    private int id;

    private JTextField txtNama, txtUsername, txtPassword, txtAlamat, txtNoTelepon, txtNIK, txtFoto;
    private JLabel lblPreviewFoto;
    private String selectedFilePath = "";

    public FormEditKaryawan(FormDataKaryawan parent, int id, String nama, String username,
                            String password, String alamat, String noTelepon, String nik, String foto) {

        this.parent = parent;
        this.id = id;
        this.selectedFilePath = (foto != null) ? foto : "";

        setTitle("Edit Data Karyawan - DistroZone");
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
        
        JLabel lblTitle = new JLabel("✏️ EDIT DATA KARYAWAN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(Color.WHITE);
        titlePanel.add(lblTitle);
        
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Form Panel dengan Scroll
        JPanel formContainer = new JPanel();
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));
        formContainer.setBackground(new Color(40, 40, 45));
        formContainer.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));

        // ID Display (Read-only)
        JPanel idPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        idPanel.setBackground(new Color(40, 40, 45));
        idPanel.setMaximumSize(new Dimension(490, 25));
        
        JLabel lblIdInfo = new JLabel("🆔 ID Karyawan: " + id);
        lblIdInfo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblIdInfo.setForeground(new Color(52, 152, 219));
        idPanel.add(lblIdInfo);
        
        formContainer.add(idPanel);
        formContainer.add(Box.createRigidArea(new Dimension(0, 15)));

        // Nama
        formContainer.add(createLabel("Nama Lengkap"));
        txtNama = createTextField(nama);
        formContainer.add(txtNama);
        formContainer.add(Box.createRigidArea(new Dimension(0, 15)));

        // Username
        formContainer.add(createLabel("Username"));
        txtUsername = createTextField(username);
        formContainer.add(txtUsername);
        formContainer.add(Box.createRigidArea(new Dimension(0, 15)));

        // Password
        formContainer.add(createLabel("Password"));
        txtPassword = createTextField(password);
        formContainer.add(txtPassword);
        formContainer.add(Box.createRigidArea(new Dimension(0, 15)));

        // Alamat
        formContainer.add(createLabel("Alamat"));
        txtAlamat = createTextField(alamat);
        formContainer.add(txtAlamat);
        formContainer.add(Box.createRigidArea(new Dimension(0, 15)));

        // No Telepon
        formContainer.add(createLabel("No. Telepon"));
        txtNoTelepon = createTextField(noTelepon);
        formContainer.add(txtNoTelepon);
        formContainer.add(Box.createRigidArea(new Dimension(0, 15)));

        // NIK
        formContainer.add(createLabel("NIK (Nomor Induk Karyawan)"));
        txtNIK = createTextField(nik);
        formContainer.add(txtNIK);
        formContainer.add(Box.createRigidArea(new Dimension(0, 15)));

        // Foto
        formContainer.add(createLabel("Foto Karyawan (Opsional)"));
        
        JPanel fotoPanel = new JPanel(new BorderLayout(10, 0));
        fotoPanel.setBackground(new Color(40, 40, 45));
        fotoPanel.setMaximumSize(new Dimension(490, 35));
        
        txtFoto = createTextField(foto != null ? new File(foto).getName() : "");
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
        lblPreviewFoto = new JLabel("", SwingConstants.CENTER);
        lblPreviewFoto.setPreferredSize(new Dimension(200, 200));
        lblPreviewFoto.setMaximumSize(new Dimension(200, 200));
        lblPreviewFoto.setBackground(new Color(60, 60, 65));
        lblPreviewFoto.setForeground(new Color(150, 150, 150));
        lblPreviewFoto.setOpaque(true);
        lblPreviewFoto.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 105), 2));
        lblPreviewFoto.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Load existing photo if available
        if (foto != null && !foto.isEmpty()) {
            try {
                File fotoFile = new File(foto);
                if (fotoFile.exists()) {
                    ImageIcon icon = new ImageIcon(foto);
                    Image image = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                    lblPreviewFoto.setIcon(new ImageIcon(image));
                } else {
                    lblPreviewFoto.setText("File tidak ditemukan");
                }
            } catch (Exception e) {
                lblPreviewFoto.setText("Gagal load foto");
            }
        } else {
            lblPreviewFoto.setText("Belum ada foto");
        }
        
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

        JButton btnUpdate = createButton("💾 Update Data", new Color(46, 204, 113));
        btnUpdate.addActionListener(e -> verifikasiUpdate());

        JButton btnBatal = createButton("❌ Batal", new Color(231, 76, 60));
        btnBatal.addActionListener(e -> dispose());

        buttonPanel.add(btnUpdate);
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
        fileChooser.setDialogTitle("Pilih Foto Karyawan");
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

    private void verifikasiUpdate() {
        // Validasi input kosong
        if (txtNama.getText().trim().isEmpty() || 
            txtUsername.getText().trim().isEmpty() ||
            txtPassword.getText().trim().isEmpty() ||
            txtAlamat.getText().trim().isEmpty() ||
            txtNoTelepon.getText().trim().isEmpty() ||
            txtNIK.getText().trim().isEmpty()) {
            
            JOptionPane.showMessageDialog(this,
                "Semua field wajib diisi kecuali Foto!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validasi username minimal 4 karakter
        if (txtUsername.getText().trim().length() < 4) {
            JOptionPane.showMessageDialog(this,
                "Username minimal 4 karakter!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validasi password minimal 3 karakter
        if (txtPassword.getText().trim().length() < 3) {
            JOptionPane.showMessageDialog(this,
                "Password minimal 3 karakter!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Dialog konfirmasi
        int confirm = JOptionPane.showConfirmDialog(this,
            "Apakah Anda yakin ingin mengupdate data karyawan ini?\n\n" +
            "Nama: " + txtNama.getText() + "\n" +
            "Username: " + txtUsername.getText() + "\n" +
            "Alamat: " + txtAlamat.getText() + "\n" +
            "No. Telepon: " + txtNoTelepon.getText() + "\n" +
            "NIK: " + txtNIK.getText(),
            "Konfirmasi Update",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            updateData();
        }
    }

    private void updateData() {
        try {
            Connection conn = DataBaseConector.getConnection();
            String sql = "UPDATE tb_karyawan SET nama=?, username=?, password=?, alamat=?, no_telepon=?, NIK=?, foto_karyawan=? WHERE id_karyawan=?";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, txtNama.getText().trim());
            pst.setString(2, txtUsername.getText().trim());
            pst.setString(3, txtPassword.getText().trim());
            pst.setString(4, txtAlamat.getText().trim());
            pst.setString(5, txtNoTelepon.getText().trim());
            pst.setString(6, txtNIK.getText().trim());
            pst.setString(7, selectedFilePath.isEmpty() ? null : selectedFilePath);
            pst.setInt(8, id);

            int rowsAffected = pst.executeUpdate();
            pst.close();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this,
                    "✅ Data karyawan berhasil diupdate!",
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);
                parent.loadDataKaryawan();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Gagal update data. ID karyawan tidak ditemukan.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Gagal update data:\n" + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}