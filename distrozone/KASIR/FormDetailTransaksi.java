package distrozone.KASIR;

import distrozone.SERVICEnew.RiwayatTransaksiService;
import distrozone.Model.DetailTransaksi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * VIEW - Form Detail Transaksi (Popup)
 * Menampilkan detail item yang dibeli dalam 1 transaksi
 * READ-ONLY ✅
 */
public class FormDetailTransaksi extends JDialog {
    
    private RiwayatTransaksiService service;
    private String idTransaksi;
    
    private JTable tableDetail;
    private DefaultTableModel tableModel;
    private JLabel lblTotal;
    
    public FormDetailTransaksi(String idTransaksi) {
        this.idTransaksi = idTransaksi;
        this.service = new RiwayatTransaksiService();
        
        initComponents();
        loadDetail();
    }
    
    private void initComponents() {
        setTitle("Detail Transaksi - " + idTransaksi);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setModal(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(25, 25, 30));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // ========== HEADER ==========
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(30, 30, 35));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel lblTitle = new JLabel("📋 DETAIL TRANSAKSI");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle, BorderLayout.WEST);
        
        JLabel lblKode = new JLabel(idTransaksi);
        lblKode.setFont(new Font("Arial", Font.BOLD, 18));
        lblKode.setForeground(new Color(52, 152, 219));
        headerPanel.add(lblKode, BorderLayout.EAST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // ========== TABLE ==========
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(25, 25, 30));
        
        String[] columns = {
            "No", "Nama Produk", "Harga Satuan", "Qty", "Subtotal"
        };
        
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // READ-ONLY ✅
            }
        };
        
        tableDetail = new JTable(tableModel);
        tableDetail.setFont(new Font("Arial", Font.PLAIN, 14));
        tableDetail.setRowHeight(35);
        tableDetail.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableDetail.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        tableDetail.getTableHeader().setBackground(new Color(52, 152, 219));
        tableDetail.getTableHeader().setForeground(Color.BLACK);
        
        // Set column width
        tableDetail.getColumnModel().getColumn(0).setPreferredWidth(50);
        tableDetail.getColumnModel().getColumn(1).setPreferredWidth(300);
        tableDetail.getColumnModel().getColumn(2).setPreferredWidth(120);
        tableDetail.getColumnModel().getColumn(3).setPreferredWidth(80);
        tableDetail.getColumnModel().getColumn(4).setPreferredWidth(120);
        
        JScrollPane scrollPane = new JScrollPane(tableDetail);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 65), 2));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        
        // ========== FOOTER (TOTAL) ==========
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        footerPanel.setBackground(new Color(30, 30, 35));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel lblTotalText = new JLabel("TOTAL:");
        lblTotalText.setFont(new Font("Arial", Font.BOLD, 18));
        lblTotalText.setForeground(Color.WHITE);
        footerPanel.add(lblTotalText);
        
        lblTotal = new JLabel("Rp 0");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 24));
        lblTotal.setForeground(new Color(46, 204, 113));
        footerPanel.add(lblTotal);
        
        JButton btnClose = new JButton("❌ TUTUP");
        btnClose.setPreferredSize(new Dimension(120, 40));
        btnClose.setBackground(new Color(220, 53, 69));
        btnClose.setForeground(Color.BLACK);
        btnClose.setFont(new Font("Arial", Font.BOLD, 14));
        btnClose.setFocusPainted(false);
        btnClose.setBorder(BorderFactory.createEmptyBorder());
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClose.addActionListener(e -> dispose());
        footerPanel.add(btnClose);
        
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    /**
     * ========== LOAD DETAIL ==========
     */
    private void loadDetail() {
        tableModel.setRowCount(0);
        
        List<DetailTransaksi> list = service.getDetailTransaksi(idTransaksi);
        
        if (list == null || list.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Detail transaksi tidak ditemukan!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int no = 1;
        int totalBayar = 0;
        
        for (DetailTransaksi detail : list) {
            Object[] row = {
                no++,
                detail.getNamaKaos(),
                service.formatRupiah(detail.getHargaSaatTransaksi()),
                detail.getJumlah(),
                service.formatRupiah(detail.getSubtotal())
            };
            tableModel.addRow(row);
            
            totalBayar += detail.getSubtotal();
        }
        
        lblTotal.setText(service.formatRupiah(totalBayar));
    }
}