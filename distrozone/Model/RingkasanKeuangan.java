package distrozone.Model;

/**
 * Model untuk ringkasan keuangan
 */
public class RingkasanKeuangan {
    private int pendapatanProduk;
    private int totalHargaModal;
    private int labaBersih;
    private double marginPersen;
    private int totalOngkir;
    private int totalKesMasuk;
    
    // Getters and Setters
    public int getPendapatanProduk() {
        return pendapatanProduk;
    }
    
    public void setPendapatanProduk(int pendapatanProduk) {
        this.pendapatanProduk = pendapatanProduk;
    }
    
    public int getTotalHargaModal() {
        return totalHargaModal;
    }
    
    public void setTotalHargaModal(int totalHargaModal) {
        this.totalHargaModal = totalHargaModal;
    }
    
    public int getLabaBersih() {
        return labaBersih;
    }
    
    public void setLabaBersih(int labaBersih) {
        this.labaBersih = labaBersih;
    }
    
    public double getMarginPersen() {
        return marginPersen;
    }
    
    public void setMarginPersen(double marginPersen) {
        this.marginPersen = marginPersen;
    }
    
    public int getTotalOngkir() {
        return totalOngkir;
    }
    
    public void setTotalOngkir(int totalOngkir) {
        this.totalOngkir = totalOngkir;
    }
    
    public int getTotalKesMasuk() {
        return totalKesMasuk;
    }
    
    public void setTotalKesMasuk(int totalKesMasuk) {
        this.totalKesMasuk = totalKesMasuk;
    }
}