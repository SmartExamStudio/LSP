package distrozone.SERVICEnew;

import distrozone.DAO.TransaksiDAO;
import distrozone.DAO.DetailTransaksiDAO;
import distrozone.DAO.KaosDAO;
import distrozone.Model.Transaksi;
import distrozone.Model.DetailTransaksi;
import distrozone.Model.KeranjangItem;
import distrozone.DataBaseConector;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * SERVICE - Mengatur sistem transaksi
 * 
 * SEMUA PERBAIKAN DITERAPKAN:
 * ✅ MASALAH #1: Transaksi benar-benar ATOMIC (1 connection untuk semua)
 * ✅ MASALAH #2: Validasi stok SEBELUM update (urutan benar)
 * ✅ MASALAH #3: Update stok TIDAK buta (cek di SQL)
 * ✅ MASALAH #4: Harga dikunci saat transaksi
 * ✅ MASALAH #5: Keranjang 100% di memory (List)
 */
public class TransaksiService {
    
    private TransaksiDAO transaksiDAO;
    private DetailTransaksiDAO detailDAO;
    private KaosDAO kaosDAO;
    
    public TransaksiService() {
        this.transaksiDAO = new TransaksiDAO();
        this.detailDAO = new DetailTransaksiDAO();
        this.kaosDAO = new KaosDAO();
    }
    
    /**
     * Generate ID Transaksi otomatis
     * Format: TRX-YYYYMMDD-XXX
     */
    public String generateIdTransaksi() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String tanggal = sdf.format(new Date());
        String idTransaksi = "TRX-" + tanggal + "-001";
        
        try {
            int counter = 1;
            while (transaksiDAO.isIdExists(idTransaksi)) {
                counter++;
                idTransaksi = "TRX-" + tanggal + "-" + String.format("%03d", counter);
            }
            
        } catch (SQLException e) {
            System.err.println("Error generate ID: " + e.getMessage());
        }
        
        return idTransaksi;
    }
    
    /**
     * PERBAIKAN MASALAH #2: Validasi stok SEMUA item dulu
     * Loop semua keranjang → cek stok
     * Jika ada 1 yang gagal → STOP, jangan lanjut
     * 
     * TIDAK update stok di sini! Hanya CEK!
     */
    public String validasiKeranjang(Connection conn, List<KeranjangItem> keranjang) {
        if (keranjang == null || keranjang.isEmpty()) {
            return "Keranjang masih kosong!";
        }
        
        // CEK stok SEMUA item dulu sebelum lanjut
        for (KeranjangItem item : keranjang) {
            try {
                int stokSekarang = kaosDAO.getStokKaos(conn, item.getIdKaos());
                
                if (stokSekarang < 0) {
                    return "Produk ID " + item.getIdKaos() + " tidak ditemukan!";
                }
                
                if (stokSekarang < item.getJumlah()) {
                    return "Stok tidak cukup untuk " + item.getMerek() + 
                           "!\nStok tersedia: " + stokSekarang + 
                           ", diminta: " + item.getJumlah();
                }
                
            } catch (SQLException e) {
                return "Error validasi stok: " + e.getMessage();
            }
        }
        
        return null; // Semua valid ✅
    }
    
    /**
     * Validasi pembayaran
     */
    public String validasiPembayaran(int totalHarga, int jumlahBayar) {
        if (jumlahBayar < totalHarga) {
            int kurang = totalHarga - jumlahBayar;
            return "Uang tidak cukup! Kurang: Rp " + String.format("%,d", kurang);
        }
        return null;
    }
    
    /**
     * Hitung kembalian
     */
    public int hitungKembalian(int totalHarga, int jumlahBayar) {
        return jumlahBayar - totalHarga;
    }
    
    /**
     * PROSES TRANSAKSI LENGKAP - 100% ATOMIC & AMAN!
     * 
     * PERBAIKAN MASALAH #1: ATOMIC TRANSACTION
     * - 1 Connection untuk SEMUA operasi
     * - setAutoCommit(false)
     * - Commit jika sukses, Rollback jika gagal
     * 
     * PERBAIKAN MASALAH #2: VALIDASI URUTAN BENAR
     * - Validasi SEMUA item dulu
     * - Baru insert & update stok
     * 
     * PERBAIKAN MASALAH #3: UPDATE STOK AMAN
     * - Cek rowsAffected
     * - Rollback jika stok tidak cukup
     * 
     * PERBAIKAN MASALAH #4: HARGA DIKUNCI
     * - Simpan harga_saat_transaksi
     * 
     * Flow:
     * 1. Validasi keranjang GLOBAL (semua item)
     * 2. BEGIN TRANSACTION (setAutoCommit(false))
     * 3. Simpan transaksi header
     * 4. Simpan detail transaksi (dengan harga dikunci!)
     * 5. Kurangi stok (CEK rowsAffected!)
     * 6. COMMIT (semua sukses) atau ROLLBACK (ada yang gagal)
     */
    public String prosesTransaksi(int idKaryawan, List<KeranjangItem> keranjang, 
                              int totalHarga, String metodePembayaran) {
    
    Connection conn = null;
    String idTransaksi = generateIdTransaksi();      
    
    try {
        System.out.println("🔄 Step 1: Membuat connection...");
        conn = DataBaseConector.getConnection();
        System.out.println("✅ Connection created: " + conn);
        
        System.out.println("🔄 Step 2: Set autoCommit(false)...");
        conn.setAutoCommit(false);
        System.out.println("✅ AutoCommit set to false");
        
        System.out.println("🔄 Step 3: Validasi keranjang...");
        String validasiError = validasiKeranjang(conn, keranjang);
        if (validasiError != null) {
            System.err.println("❌ Validasi gagal: " + validasiError);
            conn.rollback();
            return null;
        }           
            System.out.println("✅ Validasi berhasil");
            
            int totalItem = 0;
            for (KeranjangItem item : keranjang) {
            totalItem += item.getJumlah();
        }
        
        System.out.println("🔄 Step 4: Simpan header transaksi...");
        Transaksi transaksi = new Transaksi(
            idTransaksi,
            idKaryawan,
            new Date(),
            totalHarga,
            totalItem,  
            metodePembayaran,
            "SELESAI"
        );
        
        boolean transaksiSaved = transaksiDAO.insert(conn, transaksi);
        if (!transaksiSaved) {
            throw new SQLException("Gagal menyimpan header transaksi!");
        }
        System.out.println("✅ Header transaksi tersimpan");
        
        System.out.println("🔄 Step 5: Simpan detail transaksi...");
        List<DetailTransaksi> detailList = new ArrayList<>();
        for (KeranjangItem item : keranjang) {
            
            String namaKaos = item.getMerek() + " " + item.getType() + 
                      " - " + item.getWarna() + " (" + item.getSize() + ")";
            
            DetailTransaksi detail = new DetailTransaksi(
                idTransaksi,
                item.getIdKaos(),
                namaKaos,                    // ← TAMBAH INI
                item.getJumlah(),
                item.getHarga(),
                item.getSubtotal()
            );  
            detailList.add(detail);
        }
            
            int detailSaved = detailDAO.insertBatch(conn, detailList);
        System.out.println("✅ Detail tersimpan: " + detailSaved + " items");
        
        if (detailSaved != keranjang.size()) {
            throw new SQLException("Gagal menyimpan detail transaksi!");
        }
        
        System.out.println("🔄 Step 6: Update stok...");
        for (KeranjangItem item : keranjang) {
            boolean stokUpdated = kaosDAO.kurangiStok(conn, item.getIdKaos(), item.getJumlah());
            
            if (!stokUpdated) {
                throw new SQLException(
                    "Gagal mengurangi stok untuk " + item.getMerek() + "!"
                );
            }
            System.out.println("✅ Stok ID " + item.getIdKaos() + " dikurangi: " + item.getJumlah());
        }
        
        System.out.println("🔄 Step 7: Commit transaksi...");
        conn.commit();
        System.out.println("🎉 Transaksi berhasil! ID: " + idTransaksi);
        
        return idTransaksi;
        
    } catch (SQLException e) {
        System.err.println("❌ ERROR di step: " + e.getMessage());
        e.printStackTrace();
        
        if (conn != null) {
            try {
                System.out.println("🔄 Mencoba rollback...");
                System.out.println("Connection status: " + (conn.isClosed() ? "CLOSED" : "OPEN"));
                conn.rollback();
                System.err.println("✅ Rollback berhasil");
            } catch (SQLException ex) {
                System.err.println("❌ Error saat rollback: " + ex.getMessage());
            }
        }
        
        return null; // Gagal ❌
            
        } finally {
        if (conn != null) {
            try {
                System.out.println("🔄 Closing connection...");
                if (!conn.isClosed()) {
                    conn.setAutoCommit(true);
                    conn.close();
                    System.out.println("✅ Connection closed");
                } else {
                    System.out.println("⚠️ Connection sudah closed sebelumnya!");
                }
            } catch (SQLException e) {
                System.err.println("❌ Error closing connection: " + e.getMessage());
            }
        }
    }
}
    
    /**
     * Validasi metode pembayaran
     */
    public boolean validasiMetodePembayaran(String metode) {
        return metode != null && 
               (metode.equals("CASH") || metode.equals("QRIS") || metode.equals("TRANSFER"));
    }
    
    /**
     * Hitung total dari keranjang
     */
    public int hitungTotal(List<KeranjangItem> keranjang) {
        int total = 0;
        for (KeranjangItem item : keranjang) {
            total += item.getSubtotal();
        }
        return total;
    }
}