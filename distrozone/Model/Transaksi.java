package distrozone.Model;

import java.util.Date;

/**
 * MODEL - Representasi data transaksi
 * - Tidak ada SQL
 * - Tidak ada UI
 * - Tidak ada logika berat
 */
public class Transaksi {
    private String idTransaksi;
    private int idKaryawan;
    private Date tanggal;
    private int totalHarga;
    private int totalItem; 
    private String metodePembayaran;
     private String statusTransaksi;// CASH, QRIS, TRANSFER

    // Constructor
    public Transaksi() {
    }

    public Transaksi(String idTransaksi, int idKaryawan, Date tanggal, 
                     int totalHarga, int totalItem, String metodePembayaran, String statusTransaksi) {
        this.idTransaksi = idTransaksi;
        this.idKaryawan = idKaryawan;
        this.tanggal = tanggal;
        this.totalHarga = totalHarga;
        this.totalItem = totalItem;
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

    public int getIdKaryawan() {
        return idKaryawan;
    }

    public void setIdKaryawan(int idKaryawan) {
        this.idKaryawan = idKaryawan;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    public int getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(int totalHarga) {
        this.totalHarga = totalHarga;
    }
    
    public int getTotalItem() {
        return totalItem;
    }
    
    public void setTotalItem(int totalItem) {
        this.totalItem = totalItem;
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

    @Override
    public String toString() {
        return "Transaksi{" +
                "idTransaksi='" + idTransaksi + '\'' +
                ", idKaryawan=" + idKaryawan +
                ", tanggal=" + tanggal +
                ", totalHarga=" + totalHarga +
                ", metodePembayaran='" + metodePembayaran + '\'' +
                '}';
    }
}