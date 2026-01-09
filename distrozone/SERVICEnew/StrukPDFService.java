package distrozone.SERVICEnew;

import distrozone.Model.StrukTransaksi;
import distrozone.Model.DetailTransaksi;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator; 
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.io.File;

/**
 * Service untuk generate PDF struk transaksi
 * Library: iTextPDF 5.5.13
 * 
 * CATATAN: Tambahkan ke pom.xml atau download JAR:
 * <dependency>
 *   <groupId>com.itextpdf</groupId>
 *   <artifactId>itextpdf</artifactId>
 *   <version>5.5.13</version>
 * </dependency>
 */
public class StrukPDFService {
    
    private static final Font FONT_TITLE = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
    private static final Font FONT_HEADER = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private static final Font FONT_NORMAL = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
    private static final Font FONT_SMALL = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
    
    /**
     * Generate PDF struk transaksi
     * @param struk Data struk
     * @return Path file PDF yang telah dibuat
     */
    public String generateStrukPDF(StrukTransaksi struk) {
        String fileName = "Struk_" + struk.getIdTransaksi() + ".pdf";
        String folderPath = "struk_pdf";
        
        // Buat folder jika belum ada
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        
        String filePath = folderPath + File.separator + fileName;
        
        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();
            
            // === HEADER ===
            addHeader(document);
            
            // === INFO TRANSAKSI ===
            addTransactionInfo(document, struk);
            
            // === TABEL ITEM ===
            addItemTable(document, struk);
            
            // === TOTAL & PAYMENT ===
            addTotalSection(document, struk);
            
            // === FOOTER ===
            addFooter(document);
            
            document.close();
            
            return filePath;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private void addHeader(Document document) throws DocumentException {
        Paragraph title = new Paragraph("DISTRO ZONE", FONT_TITLE);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        
        Paragraph address = new Paragraph("Jl. Contoh No. 123, Sidoarjo, Jawa Timur", FONT_SMALL);
        address.setAlignment(Element.ALIGN_CENTER);
        document.add(address);
        
        Paragraph contact = new Paragraph("Telp: 0812-3456-7890", FONT_SMALL);
        contact.setAlignment(Element.ALIGN_CENTER);
        document.add(contact);
        
        document.add(new Paragraph(" ")); // Space
        document.add(new LineSeparator());
        document.add(new Paragraph(" ")); // Space
    }
    
    private void addTransactionInfo(Document document, StrukTransaksi struk) throws DocumentException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1, 2});
        
        addInfoRow(table, "No. Transaksi", struk.getIdTransaksi());
        addInfoRow(table, "Tanggal", sdf.format(struk.getTanggalTransaksi()));
        addInfoRow(table, "Kasir", struk.getNamaKasir());
        addInfoRow(table, "Metode", struk.getMetodePembayaran());
        
        document.add(table);
        document.add(new Paragraph(" "));
    }

    
    private void addItemTable(Document document, StrukTransaksi struk) throws DocumentException {
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{3, 1, 2, 2});
        
        // Header
        addTableHeader(table, "Item");
        addTableHeader(table, "Qty");
        addTableHeader(table, "Harga");
        addTableHeader(table, "Subtotal");
        
        // Items
        for (DetailTransaksi item : struk.getItems()) {
            addTableCell(table, item.getNamaKaos());
            addTableCell(table, String.valueOf(item.getJumlah()));
            addTableCell(table, formatRupiah(item.getHargaSaatTransaksi()));
            addTableCell(table, formatRupiah(item.getSubtotal()));
        }
        
        document.add(table);
        document.add(new Paragraph(" "));
    }
    
    private void addTotalSection(Document document, StrukTransaksi struk) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(50);
        table.setHorizontalAlignment(Element.ALIGN_RIGHT);
        
        // Subtotal
        addTotalRow(table, "Subtotal:", formatRupiah(struk.getSubtotal()), false);
        
        // Total (bold)
        addTotalRow(table, "TOTAL:", formatRupiah(struk.getTotalBayar()), true);
        
        // Kembalian (jika CASH)
        if (struk.getMetodePembayaran().equals("CASH")) {
            addTotalRow(table, "Kembalian:", formatRupiah(struk.getKembalian()), false);
        }
        
        document.add(table);
    }
    
    private void addFooter(Document document) throws DocumentException {
        document.add(new Paragraph(" "));
        document.add(new LineSeparator());
        
        Paragraph footer = new Paragraph("Terima kasih atas kunjungan Anda!", FONT_NORMAL);
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);
        
        Paragraph footer2 = new Paragraph("Barang yang sudah dibeli tidak dapat ditukar/dikembalikan", FONT_SMALL);
        footer2.setAlignment(Element.ALIGN_CENTER);
        document.add(footer2);
    }
    
    // Helper methods
    
    private void addInfoRow(PdfPTable table, String label, String value) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, FONT_NORMAL));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPadding(5);
        table.addCell(labelCell);
        
        PdfPCell valueCell = new PdfPCell(new Phrase(": " + value, FONT_NORMAL));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPadding(5);
        table.addCell(valueCell);
    }
    
    private void addTableHeader(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FONT_HEADER));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setPadding(8);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }
    
    private void addTableCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FONT_NORMAL));
        cell.setPadding(8);
        table.addCell(cell);
    }
    
    private void addTotalRow(PdfPTable table, String label, String value, boolean bold) {
        Font font = bold ? FONT_HEADER : FONT_NORMAL;
        
        PdfPCell labelCell = new PdfPCell(new Phrase(label, font));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPadding(5);
        labelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(labelCell);
        
        PdfPCell valueCell = new PdfPCell(new Phrase(value, font));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPadding(5);
        valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(valueCell);
    }
    
    private String formatRupiah(int angka) {
        return "Rp " + String.format("%,d", angka).replace(',', '.');
    }
}