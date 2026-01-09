package distrozone.KASIR;

import distrozone.Model.RiwayatTransaksi;
import distrozone.Model.DetailTransaksi;

import javax.swing.*;
import java.awt.*;
import java.awt.print.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * VIEW - Form Cetak Struk (Print Preview + Print)
 * Format struk thermal 58mm
 */
public class FormCetakStruk extends JDialog implements Printable {

    private RiwayatTransaksi transaksi;
    private List<DetailTransaksi> detailList;
    private JTextArea txtStruk;

    public FormCetakStruk(JFrame owner, Map<String, Object> dataStruk) {
        super(owner, true);
        this.transaksi = (RiwayatTransaksi) dataStruk.get("transaksi");
        this.detailList = (List<DetailTransaksi>) dataStruk.get("detail");

        initComponents();
        generateStruk();
    }

    private void initComponents() {
        setTitle("Cetak Struk - " + transaksi.getKodeTransaksi());
        setSize(450, 700);
        setLocationRelativeTo(null);
        setModal(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(25, 25, 30));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(30, 30, 35));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTitle = new JLabel("🖨️ PREVIEW STRUK");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Text Area untuk preview struk
        txtStruk = new JTextArea();
        txtStruk.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtStruk.setEditable(false);
        txtStruk.setBackground(Color.WHITE);
        txtStruk.setForeground(Color.BLACK);
        txtStruk.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JScrollPane scrollPane = new JScrollPane(txtStruk);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 65), 2));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(30, 30, 35));

        JButton btnPrint = new JButton("🖨️ CETAK");
        btnPrint.setPreferredSize(new Dimension(130, 45));
        btnPrint.setBackground(new Color(46, 204, 113));
        btnPrint.setForeground(Color.BLACK);
        btnPrint.setFont(new Font("Arial", Font.BOLD, 14));
        btnPrint.setFocusPainted(false);
        btnPrint.setBorder(BorderFactory.createEmptyBorder());
        btnPrint.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPrint.addActionListener(e -> printStruk());
        buttonPanel.add(btnPrint);

        JButton btnPDF = new JButton("📄 SIMPAN PDF");
        btnPDF.setPreferredSize(new Dimension(150, 45));
        btnPDF.setBackground(new Color(52, 152, 219));
        btnPDF.setForeground(Color.BLACK);
        btnPDF.setFont(new Font("Arial", Font.BOLD, 14));
        btnPDF.setFocusPainted(false);
        btnPDF.setBorder(BorderFactory.createEmptyBorder());
        btnPDF.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPDF.addActionListener(e -> exportToPDF());
        buttonPanel.add(btnPDF);

        JButton btnClose = new JButton("❌ TUTUP");
        btnClose.setPreferredSize(new Dimension(130, 45));
        btnClose.setBackground(new Color(220, 53, 69));
        btnClose.setForeground(Color.BLACK);
        btnClose.setFont(new Font("Arial", Font.BOLD, 14));
        btnClose.setFocusPainted(false);
        btnClose.setBorder(BorderFactory.createEmptyBorder());
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClose.addActionListener(e -> dispose());
        buttonPanel.add(btnClose);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    /**
     * ========== GENERATE STRUK ==========
     * Format thermal printer 58mm (32 karakter per baris)
     */
    private void generateStruk() {
        StringBuilder struk = new StringBuilder();
        SimpleDateFormat sdfTanggal = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfWaktu = new SimpleDateFormat("HH:mm:ss");

        // Header Toko
        struk.append(center("================================", 32)).append("\n");
        struk.append(center("DISTRO ZONE", 32)).append("\n");
        struk.append(center("Jl. Raya Distro No. 123", 32)).append("\n");
        struk.append(center("Telp: 0812-3456-7890", 32)).append("\n");
        struk.append(center("================================", 32)).append("\n\n");

        // Info Transaksi
        struk.append(String.format("%-15s: %s\n", "No. Transaksi", transaksi.getKodeTransaksi()));
        struk.append(String.format("%-15s: %s\n", "Tanggal", sdfTanggal.format(transaksi.getTanggalTransaksi())));
        struk.append(String.format("%-15s: %s\n", "Jam", transaksi.getJamTransaksi()));
        struk.append(String.format("%-15s: %s\n", "Kasir", transaksi.getNamaKasir()));
        struk.append(center("--------------------------------", 32)).append("\n\n");

        // Detail Items
        for (DetailTransaksi detail : detailList) {
            // Nama produk (bisa multi-line jika panjang)
            String namaKaos = detail.getNamaKaos();
            if (namaKaos.length() > 32) {
                namaKaos = namaKaos.substring(0, 29) + "...";
            }
            struk.append(namaKaos).append("\n");

            // Qty x Harga = Subtotal
            String line = String.format("  %d x %s = %s\n",
                    detail.getJumlah(),
                    formatRupiah(detail.getHargaSaatTransaksi()),
                    formatRupiah(detail.getSubtotal()));
            struk.append(line);
        }

        struk.append(center("--------------------------------", 32)).append("\n");

        // Total
        struk.append(String.format("%-20s: %s\n", "TOTAL", formatRupiah(transaksi.getTotalBayar())));
        struk.append(String.format("%-20s: %s\n", "Metode Pembayaran", transaksi.getMetodePembayaran()));

        struk.append("\n").append(center("================================", 32)).append("\n");
        struk.append(center("TERIMA KASIH", 32)).append("\n");
        struk.append(center("BARANG YANG SUDAH DIBELI", 32)).append("\n");
        struk.append(center("TIDAK DAPAT DITUKAR/DIKEMBALIKAN", 32)).append("\n");
        struk.append(center("================================", 32)).append("\n");

        txtStruk.setText(struk.toString());
    }

    /**
     * ========== EXPORT TO PDF ==========
     */
    private void exportToPDF() {
        try {
            // Buat model StrukTransaksi
            distrozone.Model.StrukTransaksi struk = new distrozone.Model.StrukTransaksi();
            struk.setIdTransaksi(
                    transaksi.getIdTransaksi() != null ? transaksi.getIdTransaksi() : transaksi.getKodeTransaksi());
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
                        "Struk PDF berhasil disimpan!\n\nFile: " + filePath + "\n\nBuka file sekarang?",
                        "Sukses", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

                if (option == JOptionPane.YES_OPTION) {
                    java.awt.Desktop.getDesktop().open(new java.io.File(filePath));
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Gagal membuat struk PDF!",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error export PDF: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * ========== PRINT STRUK ==========
     */
    private void printStruk() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);

        // Custom page format (58mm thermal)
        PageFormat pageFormat = job.defaultPage();
        Paper paper = new Paper();

        // 58mm = 165 points, tinggi dinamis
        double width = 165;
        double height = 842; // A4 height sebagai default

        paper.setSize(width, height);
        paper.setImageableArea(0, 0, width, height);
        pageFormat.setPaper(paper);

        job.setPrintable(this, pageFormat);

        boolean doPrint = job.printDialog();

        if (doPrint) {
            try {
                job.print();
                JOptionPane.showMessageDialog(this,
                        "Struk berhasil dicetak!",
                        "Sukses",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (PrinterException e) {
                JOptionPane.showMessageDialog(this,
                        "Gagal mencetak struk: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * ========== PRINTABLE IMPLEMENTATION ==========
     */
    @Override
    public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());

        // Set font
        Font font = new Font("Monospaced", Font.PLAIN, 9);
        g2d.setFont(font);
        g2d.setColor(Color.BLACK);

        // Print text
        String[] lines = txtStruk.getText().split("\n");
        int y = 20;

        for (String line : lines) {
            g2d.drawString(line, 10, y);
            y += 12; // Line spacing
        }

        return PAGE_EXISTS;
    }

    /**
     * ========== HELPER METHODS ==========
     */
    private String formatRupiah(int nominal) {
        return String.format("Rp %,d", nominal);
    }

    private String center(String text, int width) {
        if (text.length() >= width)
            return text;

        int spaces = (width - text.length()) / 2;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < spaces; i++) {
            sb.append(" ");
        }

        sb.append(text);
        return sb.toString();
    }
}