package distrozone.ADMIN.PENGATURAN;

import distrozone.DAO.JamOperasionalDAO;
import distrozone.Model.JamOperasional;
import javax.swing.*;
import java.awt.*;

public class FormJamOperasional extends JFrame {

    private JamOperasionalDAO dao;
    private JamOperasional jamOffline;
    private JamOperasional jamOnline;

    // Offline components
    private JComboBox<String> cmbOfflineBukaJam, cmbOfflineBukaMenit;
    private JComboBox<String> cmbOfflineTutupJam, cmbOfflineTutupMenit;
    private JToggleButton[] btnOfflineHari;

    // Online components
    private JComboBox<String> cmbOnlineBukaJam, cmbOnlineBukaMenit;
    private JComboBox<String> cmbOnlineTutupJam, cmbOnlineTutupMenit;
    private JToggleButton[] btnOnlineHari;

    private String[] namaHari = { "Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min" };

    // Status labels
    private JLabel lblStatusOffline;
    private JLabel lblStatusOnline;

    public FormJamOperasional() {
        dao = new JamOperasionalDAO();

        setTitle("Jam Operasional - DistroZone");
        setSize(1050, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(30, 30, 35));
        mainPanel.setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(30, 30, 35));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        JLabel lblTitle = new JLabel("Jam Operasional");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle, BorderLayout.WEST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Content dengan 2 panel
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        contentPanel.setBackground(new Color(30, 30, 35));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));

        // Panel Toko Offline
        contentPanel.add(createOfflinePanel());

        // Panel Toko Online
        contentPanel.add(createOnlinePanel());

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        buttonPanel.setBackground(new Color(30, 30, 35));

        JButton btnSimpan = new JButton("Simpan Pengaturan");
        btnSimpan.setFont(new Font("Arial", Font.BOLD, 14));
        btnSimpan.setBackground(new Color(46, 204, 113));
        btnSimpan.setForeground(Color.BLACK);
        btnSimpan.setFocusPainted(false);
        btnSimpan.setOpaque(true);
        btnSimpan.setBorderPainted(false);
        btnSimpan.setPreferredSize(new Dimension(200, 45));
        btnSimpan.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSimpan.addActionListener(e -> simpanData());

        buttonPanel.add(btnSimpan);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Load data
        loadData();
    }

    private JPanel createOfflinePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(45, 45, 50));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 75), 2),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Title
        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setOpaque(false);
        titleRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleRow.setMaximumSize(new Dimension(500, 30));

        JLabel lblTitle = new JLabel("Toko Offline (Fisik)");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setForeground(Color.WHITE);
        titleRow.add(lblTitle, BorderLayout.WEST);

        lblStatusOffline = new JLabel("BUKA");
        lblStatusOffline.setFont(new Font("Arial", Font.BOLD, 12));
        lblStatusOffline.setForeground(new Color(46, 204, 113));
        lblStatusOffline.setOpaque(true);
        lblStatusOffline.setBackground(new Color(46, 204, 113, 50));
        lblStatusOffline.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
        titleRow.add(lblStatusOffline, BorderLayout.EAST);

        panel.add(titleRow);
        panel.add(Box.createVerticalStrut(15));

        // Jam Buka
        panel.add(createLabel("Jam Buka"));
        panel.add(Box.createVerticalStrut(5));

        JPanel jamBukaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        jamBukaPanel.setOpaque(false);
        jamBukaPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        jamBukaPanel.setMaximumSize(new Dimension(500, 40));

        cmbOfflineBukaJam = createHourCombo();
        cmbOfflineBukaMenit = createMinuteCombo();

        jamBukaPanel.add(cmbOfflineBukaJam);
        jamBukaPanel.add(new JLabel(":") {
            {
                setForeground(Color.WHITE);
                setFont(new Font("Arial", Font.BOLD, 16));
            }
        });
        jamBukaPanel.add(cmbOfflineBukaMenit);

        panel.add(jamBukaPanel);
        panel.add(Box.createVerticalStrut(10));

        // Jam Tutup
        panel.add(createLabel("Jam Tutup"));
        panel.add(Box.createVerticalStrut(5));

        JPanel jamTutupPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        jamTutupPanel.setOpaque(false);
        jamTutupPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        jamTutupPanel.setMaximumSize(new Dimension(500, 40));

        cmbOfflineTutupJam = createHourCombo();
        cmbOfflineTutupMenit = createMinuteCombo();

        jamTutupPanel.add(cmbOfflineTutupJam);
        jamTutupPanel.add(new JLabel(":") {
            {
                setForeground(Color.WHITE);
                setFont(new Font("Arial", Font.BOLD, 16));
            }
        });
        jamTutupPanel.add(cmbOfflineTutupMenit);

        panel.add(jamTutupPanel);
        panel.add(Box.createVerticalStrut(15));

        // Hari Libur
        panel.add(createLabel("Hari Libur (Klik untuk toggle)"));
        panel.add(Box.createVerticalStrut(5));

        JPanel hariPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        hariPanel.setOpaque(false);
        hariPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        hariPanel.setMaximumSize(new Dimension(500, 45));

        btnOfflineHari = new JToggleButton[7];
        for (int i = 0; i < 7; i++) {
            btnOfflineHari[i] = createDayButton(namaHari[i]);
            hariPanel.add(btnOfflineHari[i]);
        }

        panel.add(hariPanel);

        return panel;
    }

    private JPanel createOnlinePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(45, 45, 50));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 75), 2),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Title
        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setOpaque(false);
        titleRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleRow.setMaximumSize(new Dimension(500, 30));

        JLabel lblTitle = new JLabel("Toko Online (Website)");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setForeground(Color.WHITE);
        titleRow.add(lblTitle, BorderLayout.WEST);

        lblStatusOnline = new JLabel("BUKA");
        lblStatusOnline.setFont(new Font("Arial", Font.BOLD, 12));
        lblStatusOnline.setForeground(new Color(46, 204, 113));
        lblStatusOnline.setOpaque(true);
        lblStatusOnline.setBackground(new Color(46, 204, 113, 50));
        lblStatusOnline.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
        titleRow.add(lblStatusOnline, BorderLayout.EAST);

        panel.add(titleRow);
        panel.add(Box.createVerticalStrut(15));

        // Jam Buka
        panel.add(createLabel("Jam Buka"));
        panel.add(Box.createVerticalStrut(5));

        JPanel jamBukaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        jamBukaPanel.setOpaque(false);
        jamBukaPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        jamBukaPanel.setMaximumSize(new Dimension(500, 40));

        cmbOnlineBukaJam = createHourCombo();
        cmbOnlineBukaMenit = createMinuteCombo();

        jamBukaPanel.add(cmbOnlineBukaJam);
        jamBukaPanel.add(new JLabel(":") {
            {
                setForeground(Color.WHITE);
                setFont(new Font("Arial", Font.BOLD, 16));
            }
        });
        jamBukaPanel.add(cmbOnlineBukaMenit);

        panel.add(jamBukaPanel);
        panel.add(Box.createVerticalStrut(10));

        // Jam Tutup
        panel.add(createLabel("Jam Tutup"));
        panel.add(Box.createVerticalStrut(5));

        JPanel jamTutupPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        jamTutupPanel.setOpaque(false);
        jamTutupPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        jamTutupPanel.setMaximumSize(new Dimension(500, 40));

        cmbOnlineTutupJam = createHourCombo();
        cmbOnlineTutupMenit = createMinuteCombo();

        jamTutupPanel.add(cmbOnlineTutupJam);
        jamTutupPanel.add(new JLabel(":") {
            {
                setForeground(Color.WHITE);
                setFont(new Font("Arial", Font.BOLD, 16));
            }
        });
        jamTutupPanel.add(cmbOnlineTutupMenit);

        panel.add(jamTutupPanel);
        panel.add(Box.createVerticalStrut(15));

        // Hari Libur
        panel.add(createLabel("Hari Libur (Klik untuk toggle)"));
        panel.add(Box.createVerticalStrut(5));

        JPanel hariPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        hariPanel.setOpaque(false);
        hariPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        hariPanel.setMaximumSize(new Dimension(500, 45));

        btnOnlineHari = new JToggleButton[7];
        for (int i = 0; i < 7; i++) {
            btnOnlineHari[i] = createDayButton(namaHari[i]);
            hariPanel.add(btnOnlineHari[i]);
        }

        panel.add(hariPanel);

        return panel;
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.PLAIN, 12));
        lbl.setForeground(new Color(180, 180, 180));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JComboBox<String> createHourCombo() {
        String[] hours = new String[24];
        for (int i = 0; i < 24; i++) {
            hours[i] = String.format("%02d", i);
        }
        JComboBox<String> cmb = new JComboBox<>(hours);
        cmb.setPreferredSize(new Dimension(60, 30));
        cmb.setFont(new Font("Arial", Font.PLAIN, 14));
        return cmb;
    }

    private JComboBox<String> createMinuteCombo() {
        String[] minutes = { "00", "15", "30", "45" };
        JComboBox<String> cmb = new JComboBox<>(minutes);
        cmb.setPreferredSize(new Dimension(60, 30));
        cmb.setFont(new Font("Arial", Font.PLAIN, 14));
        return cmb;
    }

    private JToggleButton createDayButton(String day) {
        JToggleButton btn = new JToggleButton(day);
        btn.setPreferredSize(new Dimension(52, 35));
        btn.setFont(new Font("Arial", Font.BOLD, 11));
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(true);
        btn.setBackground(new Color(70, 70, 75));
        btn.setForeground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Toggle: selected = LIBUR (orange), unselected = BUKA (grey)
        btn.addActionListener(e -> {
            if (btn.isSelected()) {
                btn.setBackground(new Color(255, 152, 0)); // Orange = libur
                btn.setForeground(Color.BLACK);
            } else {
                btn.setBackground(new Color(60, 60, 65)); // Grey = buka
                btn.setForeground(Color.WHITE);
            }
        });

        return btn;
    }

    private void loadData() {
        try {
            // Init default data jika belum ada
            dao.initDefaultData();

            // Load Offline
            jamOffline = dao.getByTipe("OFFLINE");
            if (jamOffline != null) {
                setJamToCombo(jamOffline.getJamBuka(), cmbOfflineBukaJam, cmbOfflineBukaMenit);
                setJamToCombo(jamOffline.getJamTutup(), cmbOfflineTutupJam, cmbOfflineTutupMenit);

                // Set hari libur (selected = libur, jadi invert dari isBuka)
                setHariButtons(btnOfflineHari, jamOffline);
            }

            // Load Online
            jamOnline = dao.getByTipe("ONLINE");
            if (jamOnline != null) {
                setJamToCombo(jamOnline.getJamBuka(), cmbOnlineBukaJam, cmbOnlineBukaMenit);
                setJamToCombo(jamOnline.getJamTutup(), cmbOnlineTutupJam, cmbOnlineTutupMenit);

                setHariButtons(btnOnlineHari, jamOnline);
            }

            // Update status labels based on current time
            updateStatusLabels();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error load data: " + e.getMessage() +
                            "\n\nPastikan tabel tb_jam_operasional sudah dibuat di database!",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStatusLabels() {
        // Get current time
        java.util.Calendar cal = java.util.Calendar.getInstance();
        int currentHour = cal.get(java.util.Calendar.HOUR_OF_DAY);
        int currentMinute = cal.get(java.util.Calendar.MINUTE);
        int currentDay = cal.get(java.util.Calendar.DAY_OF_WEEK); // 1=Sunday, 2=Monday...

        // Convert to our index (0=Senin, 1=Selasa, ..., 6=Minggu)
        int dayIndex = (currentDay == 1) ? 6 : currentDay - 2;

        // Update Offline status
        if (jamOffline != null) {
            boolean isOpen = isStoreOpen(jamOffline, currentHour, currentMinute, dayIndex);
            updateLabel(lblStatusOffline, isOpen);
        }

        // Update Online status
        if (jamOnline != null) {
            boolean isOpen = isStoreOpen(jamOnline, currentHour, currentMinute, dayIndex);
            updateLabel(lblStatusOnline, isOpen);
        }
    }

    private boolean isStoreOpen(JamOperasional jam, int hour, int minute, int dayIndex) {
        // Check if day is open
        boolean dayOpen = false;
        switch (dayIndex) {
            case 0:
                dayOpen = jam.isSenin();
                break;
            case 1:
                dayOpen = jam.isSelasa();
                break;
            case 2:
                dayOpen = jam.isRabu();
                break;
            case 3:
                dayOpen = jam.isKamis();
                break;
            case 4:
                dayOpen = jam.isJumat();
                break;
            case 5:
                dayOpen = jam.isSabtu();
                break;
            case 6:
                dayOpen = jam.isMinggu();
                break;
        }

        if (!dayOpen)
            return false;

        // Parse jam buka dan tutup
        try {
            String[] bukaParts = jam.getJamBuka().split(":");
            String[] tutupParts = jam.getJamTutup().split(":");

            int bukaHour = Integer.parseInt(bukaParts[0]);
            int bukaMinute = Integer.parseInt(bukaParts[1]);
            int tutupHour = Integer.parseInt(tutupParts[0]);
            int tutupMinute = Integer.parseInt(tutupParts[1]);

            int currentTotal = hour * 60 + minute;
            int bukaTotal = bukaHour * 60 + bukaMinute;
            int tutupTotal = tutupHour * 60 + tutupMinute;

            return currentTotal >= bukaTotal && currentTotal < tutupTotal;
        } catch (Exception e) {
            return false;
        }
    }

    private void updateLabel(JLabel label, boolean isOpen) {
        if (isOpen) {
            label.setText("BUKA");
            label.setForeground(new Color(46, 204, 113));
            label.setBackground(new Color(46, 204, 113, 50));
        } else {
            label.setText("TUTUP");
            label.setForeground(new Color(220, 53, 69));
            label.setBackground(new Color(220, 53, 69, 50));
        }
    }

    private void setJamToCombo(String jam, JComboBox<String> cmbJam, JComboBox<String> cmbMenit) {
        if (jam != null && jam.contains(":")) {
            String[] parts = jam.split(":");
            cmbJam.setSelectedItem(parts[0]);
            cmbMenit.setSelectedItem(parts[1]);
        }
    }

    private void setHariButtons(JToggleButton[] buttons, JamOperasional jam) {
        // Index: 0=Senin, 1=Selasa, ..., 6=Minggu
        // Selected = LIBUR, jadi invert dari isBuka
        buttons[0].setSelected(!jam.isSenin());
        buttons[1].setSelected(!jam.isSelasa());
        buttons[2].setSelected(!jam.isRabu());
        buttons[3].setSelected(!jam.isKamis());
        buttons[4].setSelected(!jam.isJumat());
        buttons[5].setSelected(!jam.isSabtu());
        buttons[6].setSelected(!jam.isMinggu());

        // Update visual
        for (JToggleButton btn : buttons) {
            if (btn.isSelected()) {
                btn.setBackground(new Color(255, 152, 0));
                btn.setForeground(Color.BLACK);
            }
        }
    }

    private String getJamFromCombo(JComboBox<String> cmbJam, JComboBox<String> cmbMenit) {
        return cmbJam.getSelectedItem() + ":" + cmbMenit.getSelectedItem();
    }

    private void simpanData() {
        try {
            // Update Offline
            if (jamOffline == null) {
                jamOffline = new JamOperasional();
                jamOffline.setTipe("OFFLINE");
            }

            jamOffline.setJamBuka(getJamFromCombo(cmbOfflineBukaJam, cmbOfflineBukaMenit));
            jamOffline.setJamTutup(getJamFromCombo(cmbOfflineTutupJam, cmbOfflineTutupMenit));
            // Selected = LIBUR, jadi isBuka = NOT selected
            jamOffline.setSenin(!btnOfflineHari[0].isSelected());
            jamOffline.setSelasa(!btnOfflineHari[1].isSelected());
            jamOffline.setRabu(!btnOfflineHari[2].isSelected());
            jamOffline.setKamis(!btnOfflineHari[3].isSelected());
            jamOffline.setJumat(!btnOfflineHari[4].isSelected());
            jamOffline.setSabtu(!btnOfflineHari[5].isSelected());
            jamOffline.setMinggu(!btnOfflineHari[6].isSelected());

            dao.update(jamOffline);

            // Update Online
            if (jamOnline == null) {
                jamOnline = new JamOperasional();
                jamOnline.setTipe("ONLINE");
            }

            jamOnline.setJamBuka(getJamFromCombo(cmbOnlineBukaJam, cmbOnlineBukaMenit));
            jamOnline.setJamTutup(getJamFromCombo(cmbOnlineTutupJam, cmbOnlineTutupMenit));
            jamOnline.setSenin(!btnOnlineHari[0].isSelected());
            jamOnline.setSelasa(!btnOnlineHari[1].isSelected());
            jamOnline.setRabu(!btnOnlineHari[2].isSelected());
            jamOnline.setKamis(!btnOnlineHari[3].isSelected());
            jamOnline.setJumat(!btnOnlineHari[4].isSelected());
            jamOnline.setSabtu(!btnOnlineHari[5].isSelected());
            jamOnline.setMinggu(!btnOnlineHari[6].isSelected());

            dao.update(jamOnline);

            JOptionPane.showMessageDialog(this,
                    "Jam operasional berhasil disimpan!",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error simpan data: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
