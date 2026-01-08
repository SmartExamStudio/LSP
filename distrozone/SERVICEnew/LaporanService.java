package distrozone.SERVICEnew;

import distrozone.DAO.LaporanKeuanganDAO;
import distrozone.Model.RingkasanKeuangan;
import distrozone.Model.RiwayatTransaksi;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Service untuk laporan keuangan
 * Mengelola logika bisnis laporan
 */
public class LaporanService {
    
    private LaporanKeuanganDAO dao;
    
    public LaporanService() {
        this.dao = new LaporanKeuanganDAO();
    }
    
    /**
     * Dapatkan periode berdasarkan tipe
     * @param tipePeriode "Harian", "Bulanan", "Custom"
     * @param tanggalHarian untuk harian
     * @param bulan untuk bulanan (1-12)
     * @param tahun untuk bulanan
     * @param tanggalFrom untuk custom
     * @param tanggalTo untuk custom
     * @return Map dengan key "from" dan "to"
     */
    public Map<String, Date> hitungPeriode(String tipePeriode, Date tanggalHarian, 
            int bulan, int tahun, Date tanggalFrom, Date tanggalTo) {
        
        Map<String, Date> periode = new HashMap<>();
        Calendar cal = Calendar.getInstance();
        
        switch (tipePeriode) {
            case "Harian":
                // Set jam 00:00:00
                cal.setTime(tanggalHarian);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                periode.put("from", cal.getTime());
                
                // Set jam 23:59:59
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                periode.put("to", cal.getTime());
                break;
                
            case "Bulanan":
                // Set tanggal 1
                cal.set(Calendar.YEAR, tahun);
                cal.set(Calendar.MONTH, bulan - 1);
                cal.set(Calendar.DAY_OF_MONTH, 1);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                periode.put("from", cal.getTime());
                
                // Set tanggal terakhir bulan
                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                periode.put("to", cal.getTime());
                break;
                
            case "Custom":
                periode.put("from", tanggalFrom);
                periode.put("to", tanggalTo);
                break;
        }
        
        return periode;
    }
    
    /**
     * Dapatkan ringkasan keuangan lengkap
     */
    public Map<String, Object> getLaporanKeuangan(Date tanggalFrom, Date tanggalTo, 
            String channel, String pembayaran, String searchKeyword) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 1. Ambil ringkasan keuangan
            RingkasanKeuangan ringkasan = dao.getRingkasanKeuangan(
                    tanggalFrom, tanggalTo, channel, pembayaran);
            
            // 2. Ambil total transaksi
            int totalTransaksi = dao.getTotalTransaksi(
                    tanggalFrom, tanggalTo, channel, pembayaran);
            
            // 3. Ambil daftar transaksi
            List<RiwayatTransaksi> transaksiList = dao.getTransaksiList(
                    tanggalFrom, tanggalTo, channel, pembayaran, searchKeyword);
            
            // 4. Susun hasil
            result.put("ringkasan", ringkasan);
            result.put("total_transaksi", totalTransaksi);
            result.put("transaksi_list", transaksiList);
            result.put("periode_from", new SimpleDateFormat("dd/MM/yyyy").format(tanggalFrom));
            result.put("periode_to", new SimpleDateFormat("dd/MM/yyyy").format(tanggalTo));
            result.put("success", true);
            
        } catch (SQLException e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Format rupiah
     */
    public String formatRupiah(int angka) {
        return "Rp " + String.format("%,d", angka).replace(',', '.');
    }
    
    /**
     * Format persen
     */
    public String formatPersen(double persen) {
        return String.format("%.2f%%", persen);
    }
    
    /**
     * Validasi periode
     */
    public String validatePeriode(String tipePeriode, Date tanggalHarian, 
            Integer bulan, Integer tahun, Date tanggalFrom, Date tanggalTo) {
        
        switch (tipePeriode) {
            case "Harian":
                if (tanggalHarian == null) {
                    return "Pilih tanggal terlebih dahulu!";
                }
                break;
                
            case "Bulanan":
                if (bulan == null || tahun == null) {
                    return "Pilih bulan dan tahun terlebih dahulu!";
                }
                break;
                
            case "Custom":
                if (tanggalFrom == null || tanggalTo == null) {
                    return "Pilih range tanggal terlebih dahulu!";
                }
                if (tanggalFrom.after(tanggalTo)) {
                    return "Tanggal mulai tidak boleh lebih besar dari tanggal akhir!";
                }
                break;
        }
        
        return null; // Valid
    }
}