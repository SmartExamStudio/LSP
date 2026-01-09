package distrozone;

import distrozone.ADMIN.FromAdmin;
import distrozone.KASIR.FromKasir;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Login extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    
    public Login() {
        initComponents();
    }
    
    private void initComponents() {
        setTitle("DistroZone - Login");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(20, 20, 25));
        
        // Panel Kiri - Branding
        JPanel leftPanel = new JPanel();
        leftPanel.setBounds(0, 0, 450, 600);
        leftPanel.setBackground(new Color(30, 30, 35));
        leftPanel.setLayout(null);
        
        // Logo/Brand Name
        JLabel lblBrand = new JLabel("DISTRO");
        lblBrand.setBounds(50, 180, 350, 80);
        lblBrand.setFont(new Font("Arial Black", Font.BOLD, 64));
        lblBrand.setForeground(Color.WHITE);
        lblBrand.setHorizontalAlignment(SwingConstants.CENTER);
        leftPanel.add(lblBrand);
        
        JLabel lblZone = new JLabel("ZONE");
        lblZone.setBounds(50, 250, 350, 60);
        lblZone.setFont(new Font("Arial Black", Font.BOLD, 48));
        lblZone.setForeground(new Color(255, 87, 51));
        lblZone.setHorizontalAlignment(SwingConstants.CENTER);
        leftPanel.add(lblZone);
        
        JLabel lblTagline = new JLabel("Urban Style, Modern Vibe");
        lblTagline.setBounds(50, 320, 350, 30);
        lblTagline.setFont(new Font("Arial", Font.ITALIC, 16));
        lblTagline.setForeground(new Color(150, 150, 150));
        lblTagline.setHorizontalAlignment(SwingConstants.CENTER);
        leftPanel.add(lblTagline);
        
        JPanel line1 = new JPanel();
        line1.setBounds(100, 380, 250, 3);
        line1.setBackground(new Color(255, 87, 51));
        leftPanel.add(line1);
        
        // Panel Kanan - Form Login
        JPanel rightPanel = new JPanel();
        rightPanel.setBounds(450, 0, 450, 600);
        rightPanel.setBackground(new Color(20, 20, 25));
        rightPanel.setLayout(null);
        
        JLabel lblLogin = new JLabel("LOGIN");
        lblLogin.setBounds(50, 120, 350, 40);
        lblLogin.setFont(new Font("Arial", Font.BOLD, 32));
        lblLogin.setForeground(Color.WHITE);
        rightPanel.add(lblLogin);
        
        JLabel lblWelcome = new JLabel("Welcome back! Please login to continue");
        lblWelcome.setBounds(50, 165, 350, 20);
        lblWelcome.setFont(new Font("Arial", Font.PLAIN, 13));
        lblWelcome.setForeground(new Color(150, 150, 150));
        rightPanel.add(lblWelcome);
        
        JLabel lblUsername = new JLabel("USERNAME");
        lblUsername.setBounds(50, 220, 350, 20);
        lblUsername.setFont(new Font("Arial", Font.BOLD, 12));
        lblUsername.setForeground(new Color(200, 200, 200));
        rightPanel.add(lblUsername);
        
        txtUsername = new JTextField();
        txtUsername.setBounds(50, 245, 350, 45);
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 16));
        txtUsername.setBackground(new Color(40, 40, 45));
        txtUsername.setForeground(Color.WHITE);
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 65), 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        txtUsername.setCaretColor(Color.WHITE);
        rightPanel.add(txtUsername);
        
        JLabel lblPassword = new JLabel("PASSWORD");
        lblPassword.setBounds(50, 310, 350, 20);
        lblPassword.setFont(new Font("Arial", Font.BOLD, 12));
        lblPassword.setForeground(new Color(200, 200, 200));
        rightPanel.add(lblPassword);
        
        txtPassword = new JPasswordField();
        txtPassword.setBounds(50, 335, 350, 45);
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 16));
        txtPassword.setBackground(new Color(40, 40, 45));
        txtPassword.setForeground(Color.WHITE);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 65), 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        txtPassword.setCaretColor(Color.WHITE);
        rightPanel.add(txtPassword);
        
        btnLogin = new JButton("LOGIN");
        btnLogin.setBounds(50, 410, 350, 50);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 16));
        btnLogin.setBackground(new Color(255, 87, 51));
        btnLogin.setForeground(Color.BLACK);
        btnLogin.setBorder(BorderFactory.createEmptyBorder());
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnLogin.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnLogin.setBackground(new Color(255, 107, 71));
            }
            public void mouseExited(MouseEvent e) {
                btnLogin.setBackground(new Color(255, 87, 51));
            }
        });
        
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginAction();
            }
        });
        rightPanel.add(btnLogin);
        
        txtPassword.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginAction();
                }
            }
        });
        
        JLabel lblFooter = new JLabel("© 2024 DistroZone. All rights reserved.");
        lblFooter.setBounds(50, 520, 350, 20);
        lblFooter.setFont(new Font("Arial", Font.PLAIN, 11));
        lblFooter.setForeground(new Color(100, 100, 100));
        lblFooter.setHorizontalAlignment(SwingConstants.CENTER);
        rightPanel.add(lblFooter);
        
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
        
        add(mainPanel);
    }
    
    private void loginAction() {
    String username = txtUsername.getText().trim();
    String password = new String(txtPassword.getPassword()).trim();

    if (username.isEmpty() || password.isEmpty()) {
        JOptionPane.showMessageDialog(this,
            "Username dan Password tidak boleh kosong!",
            "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    
    System.out.println("\n===== DEBUG LOGIN =====");
    System.out.println("🔹 Username input      : [" + username + "]");
    System.out.println("🔹 Password input      : [" + password + "]");
    
    String hashed = hashPassword(password);
    System.out.println("🔹 Hashed password    : [" + hashed + "]");
    System.out.println("🔹 Panjang hash       : " + hashed.length());
    System.out.println("=======================\n");

    try {
        Connection conn = DataBaseConector.getConnection();
        
        // 🔑 🔑 🔑 INI YANG BARU: hash password input user
        String hashedPassword = hashPassword(password);
        // 🔑 🔑 🔑

        // Coba login sebagai Kasir (bandingkan HASH vs HASH)
        String sqlKasir = "SELECT nama, id_karyawan FROM tb_karyawan WHERE username = ? AND password = ?";
        PreparedStatement pstKasir = conn.prepareStatement(sqlKasir);
        pstKasir.setString(1, username);
        pstKasir.setString(2, hashedPassword); // ← pakai hashedPassword, bukan password mentah!

        ResultSet rsKasir = pstKasir.executeQuery();
        if (rsKasir.next()) {
            String nama = rsKasir.getString("nama");
            int idKaryawan = rsKasir.getInt("id_karyawan");
            
            if (idKaryawan <= 0) {
                JOptionPane.showMessageDialog(this, "ID karyawan tidak valid.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            JOptionPane.showMessageDialog(this, "Login Berhasil!\nSelamat datang, " + nama, "Success", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            new FromKasir(idKaryawan, nama, "Kasir").setVisible(true);
            return;
        }

        // Coba login sebagai Admin
        String sqlAdmin = "SELECT nama FROM tb_admin WHERE username = ? AND password = ?";
        PreparedStatement pstAdmin = conn.prepareStatement(sqlAdmin);
        pstAdmin.setString(1, username);
        pstAdmin.setString(2, hashedPassword); // ← sama di sini juga!

        ResultSet rsAdmin = pstAdmin.executeQuery();
        if (rsAdmin.next()) {
            String nama = rsAdmin.getString("nama");
            JOptionPane.showMessageDialog(this, "Login Berhasil!\nSelamat datang, " + nama, "Success", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            new FromAdmin(nama, "Admin").setVisible(true);
            return;
        }

        // Gagal
        JOptionPane.showMessageDialog(this,
            "Login Gagal!\nUsername atau Password salah.",
            "Error", JOptionPane.ERROR_MESSAGE);
        txtPassword.setText("");
        txtUsername.requestFocus();

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this,
            "Error: " + e.getMessage(),
            "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}
    
    private String hashPassword(String password) {
    try {
        java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes("UTF-8"));
        
        // Konversi byte[] ke hex string
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    } catch (Exception e) {
        throw new RuntimeException("Gagal hashing password", e);
    }
}
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }
}