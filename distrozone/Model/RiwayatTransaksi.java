package distrozone.Model;

import java.util.Date;

/**
 * Model untuk Riwayat Transaksi
 * Digunakan untuk menampilkan data transaksi di UI
 */
public class RiwayatTransaksi {
    
    private String idTransaksi;
    private String kodeTransaksi;
    private Date tanggalTransaksi;
    private String jamTransaksi;
    private int idKasir;
    private String namaKasir;
    private int totalItem;
    private int subtotal;
    private int totalBayar;
    private String metodePembayaran;
    private String statusTransaksi;
    private Date createdAt;
    
    // Constructor
    public RiwayatTransaksi() {
    }
    
    public RiwayatTransaksi(String idTransaksi, Date tanggalTransaksi, String namaKasir, 
                           int totalItem, int totalBayar, String metodePembayaran, 
                           String statusTransaksi) {
        this.idTransaksi = idTransaksi;
        this.tanggalTransaksi = tanggalTransaksi;
        this.namaKasir = namaKasir;
        this.totalItem = totalItem;
        this.totalBayar = totalBayar;
        this.metodePembayaran = metodePembayaran;
        this.statusTransaksi = statusTransaksi;
    }
    
    // Getters and Setters
    public String getIdTransaksi() {
        return idTransaksi;
    }
    
    public void setIdTransaksi(String idTransaksi) {
        this.idTransaksi = idTransaksi;
    }
    
    public String getKodeTransaksi() {
        return kodeTransaksi;
    }
    
    public void setKodeTransaksi(String kodeTransaksi) {
        this.kodeTransaksi = kodeTransaksi;
    }
    
    public Date getTanggalTransaksi() {
        return tanggalTransaksi;
    }
    
    public void setTanggalTransaksi(Date tanggalTransaksi) {
        this.tanggalTransaksi = tanggalTransaksi;
    }
    
    public String getJamTransaksi() {
        return jamTransaksi;
    }
    
    public void setJamTransaksi(String jamTransaksi) {
        this.jamTransaksi = jamTransaksi;
    }
    
    public int getIdKasir() {
        return idKasir;
    }
    
    public void setIdKasir(int idKasir) {
        this.idKasir = idKasir;
    }
    
    public String getNamaKasir() {
        return namaKasir;
    }
    
    public void setNamaKasir(String namaKasir) {
        this.namaKasir = namaKasir;
    }
    
    public int getTotalItem() {
        return totalItem;
    }
    
    public void setTotalItem(int totalItem) {
        this.totalItem = totalItem;
    }
    
    public int getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
    }
    
    public int getTotalBayar() {
        return totalBayar;
    }
    
    public void setTotalBayar(int totalBayar) {
        this.totalBayar = totalBayar;
    }
    
    public String getMetodePembayaran() {
        return metodePembayaran;
    }
    
    public void setMetodePembayaran(String metodePembayaran) {
        this.metodePembayaran = metodePembayaran;
    }
    
    public String getStatusTransaksi() {
        return statusTransaksi;
    }
    
    public void setStatusTransaksi(String statusTransaksi) {
        this.statusTransaksi = statusTransaksi;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "RiwayatTransaksi{" +
                "idTransaksi='" + idTransaksi + '\'' +
                ", tanggalTransaksi=" + tanggalTransaksi +
                ", namaKasir='" + namaKasir + '\'' +
                ", totalItem=" + totalItem +
                ", totalBayar=" + totalBayar +
                ", metodePembayaran='" + metodePembayaran + '\'' +
                ", statusTransaksi='" + statusTransaksi + '\'' +
                '}';
    }
}