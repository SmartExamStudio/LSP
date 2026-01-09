package distrozone.SERVICEnew;

import distrozone.DAO.RiwayatTransaksiDAO;
import distrozone.DAO.DetailTransaksiDAO;
import distrozone.Model.RiwayatTransaksi;
import distrozone.Model.DetailTransaksi;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * SERVICE - Logika bisnis riwayat transaksi
 * 
 * TUGAS:
 * 1. Validasi hak akses (kasir hanya lihat miliknya)
 * 2. Filter data (tanggal, metode pembayaran)
 * 3. Laporan penjualan
 * 4. Generate data untuk cetak struk
 */
public class RiwayatTransaksiService {
    
    private RiwayatTransaksiDAO riwayatDAO;
    private DetailTransaksiDAO detailDAO;
    
    public RiwayatTransaksiService() {
        this.riwayatDAO = new RiwayatTransaksiDAO();
        this.detailDAO = new DetailTransaksiDAO();
    }
    
    /**
     * ✅ KASIR - Ambil riwayat transaksi kasir
     */
    public List<RiwayatTransaksi> getRiwayatKasir(int idKasir) {
        try {
            return riwayatDAO.getRiwayatByKasir(idKasir);
        } catch (SQLException e) {
            System.err.println("Error get riwayat kasir: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * ✅ ADMIN - Ambil semua riwayat (untuk admin)
     */
    public List<RiwayatTransaksi> getAllRiwayat() {
        try {
            return riwayatDAO.getAllRiwayat();
        } catch (SQLException e) {
            System.err.println("Error get all riwayat: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * ✅ FILTER - Riwayat dengan filter tanggal
     */
    public List<RiwayatTransaksi> getRiwayatWithFilter(
            int idKasir, Date tanggalFrom, Date tanggalTo) {
        try {
            // Validasi tanggal
            if (tanggalFrom == null || tanggalTo == null) {
                System.err.println("Tanggal filter tidak boleh kosong!");
                return null;
            }
            
            if (tanggalFrom.after(tanggalTo)) {
                System.err.println("Tanggal mulai tidak boleh lebih besar dari tanggal akhir!");
                return null;
            }
            
            return riwayatDAO.getRiwayatByKasirWithFilter(idKasir, tanggalFrom, tanggalTo);
            
        } catch (SQLException e) {
            System.err.println("Error filter riwayat: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * ✅ FILTER - Riwayat dengan filter metode pembayaran
     */
    public List<RiwayatTransaksi> getRiwayatWithMetode(
            int idKasir, String metodePembayaran) {
        try {
            // Validasi metode pembayaran
            if (!isValidMetodePembayaran(metodePembayaran)) {
                System.err.println("Metode pembayaran tidak valid!");
                return null;
            }
            
            return riwayatDAO.getRiwayatByKasirWithMetode(idKasir, metodePembayaran);
            
        } catch (SQLException e) {
            System.err.println("Error filter metode: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * ✅ FILTER LENGKAP - Tanggal + Metode Pembayaran
     */
    public List<RiwayatTransaksi> getRiwayatWithFullFilter(
            int idKasir, Date tanggalFrom, Date tanggalTo, String metodePembayaran) {
        try {
            // Validasi
            if (tanggalFrom == null || tanggalTo == null) {
                return null;
            }
            
            if (tanggalFrom.after(tanggalTo)) {
                return null;
            }
            
            if (!isValidMetodePembayaran(metodePembayaran)) {
                return null;
            }
            
            return riwayatDAO.getRiwayatByKasirWithFullFilter(
                idKasir, tanggalFrom, tanggalTo, metodePembayaran);
            
        } catch (SQLException e) {
            System.err.println("Error full filter: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * ✅ SEARCH - Live search berdasarkan keyword
     */
    public List<RiwayatTransaksi> searchRiwayat(int idKasir, String keyword) {
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                return getRiwayatKasir(idKasir);
            }
            
            return riwayatDAO.searchRiwayatByKasir(idKasir, keyword.trim());
            
        } catch (SQLException e) {
            System.err.println("Error search: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * ✅ DETAIL - Ambil detail transaksi untuk popup/modal
     */
    public List<DetailTransaksi> getDetailTransaksi(String idTransaksi) {
        try {
            return detailDAO.getByIdTransaksi(idTransaksi);
        } catch (SQLException e) {
            System.err.println("Error get detail: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * ✅ LAPORAN - Statistik penjualan kasir
     * Return: Map dengan key: total_transaksi, total_omzet, total_item
     */
    public Map<String, Object> getLaporanPenjualan(
            int idKasir, Date tanggalFrom, Date tanggalTo) {
        
        Map<String, Object> laporan = new HashMap<>();
        
        try {
            List<RiwayatTransaksi> list = riwayatDAO.getRiwayatByKasirWithFilter(
                idKasir, tanggalFrom, tanggalTo);
            
            if (list == null || list.isEmpty()) {
                laporan.put("total_transaksi", 0);
                laporan.put("total_omzet", 0);
                laporan.put("total_item", 0);
                return laporan;
            }
            
            int totalTransaksi = list.size();
            int totalOmzet = 0;
            int totalItem = 0;
            
            for (RiwayatTransaksi r : list) {
                totalOmzet += r.getTotalBayar();
                totalItem += r.getTotalItem();
            }
            
            laporan.put("total_transaksi", totalTransaksi);
            laporan.put("total_omzet", totalOmzet);
            laporan.put("total_item", totalItem);
            
            return laporan;
            
        } catch (SQLException e) {
            System.err.println("Error laporan: " + e.getMessage());
            laporan.put("total_transaksi", 0);
            laporan.put("total_omzet", 0);
            laporan.put("total_item", 0);
            return laporan;
        }
    }
    
    /**
     * ✅ CETAK STRUK - Ambil data lengkap untuk cetak ulang struk
     * Return: Map dengan key: transaksi, detail
     */
    public Map<String, Object> getDataStruk(String idTransaksi) {
        Map<String, Object> data = new HashMap<>();
        
        try {
            // Ambil header transaksi
            RiwayatTransaksi transaksi = riwayatDAO.getById(idTransaksi);
            if (transaksi == null) {
                System.err.println("Transaksi tidak ditemukan!");
                return null;
            }
            
            // Ambil detail transaksi
            List<DetailTransaksi> detail = detailDAO.getByIdTransaksi(idTransaksi);
            if (detail == null || detail.isEmpty()) {
                System.err.println("Detail transaksi tidak ditemukan!");
                return null;
            }
            
            data.put("transaksi", transaksi);
            data.put("detail", detail);
            
            return data;
            
        } catch (SQLException e) {
            System.err.println("Error get data struk: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * ✅ VALIDASI - Cek metode pembayaran valid
     */
    private boolean isValidMetodePembayaran(String metode) {
        if (metode == null) return false;
        return metode.equals("CASH") || metode.equals("QRIS") || metode.equals("TRANSFER");
    }
    
    /**
     * ✅ FORMAT - Format rupiah
     */
    public String formatRupiah(int nominal) {
        return String.format("Rp %,d", nominal);
    }
    
    /**
     * ✅ VALIDASI - Cek hak akses kasir ke transaksi
     */
    public boolean isKasirOwner(int idKasir, String idTransaksi) {
        try {
            RiwayatTransaksi transaksi = riwayatDAO.getById(idTransaksi);
            if (transaksi == null) return false;
            
            return transaksi.getIdKasir() == idKasir;
            
        } catch (SQLException e) {
            System.err.println("Error validasi owner: " + e.getMessage());
            return false;
        }
    }
}