package distrozone.SERVICEnew;

import distrozone.DAO.KaosDAO;
import distrozone.Model.Kaos;
import java.sql.SQLException;
import java.util.List;

/**
 * SERVICE untuk logika bisnis Kaos
 * - Validasi data
 * - Aturan bisnis
 * - Kontrol alur
 * 
 * TIDAK tahu UI (warna, tombol, JOptionPane)
 * TIDAK tahu SQL (itu urusan DAO)
 */
public class KaosService {
    private KaosDAO kaosDAO;
    
    public KaosService() {
        this.kaosDAO = new KaosDAO();
    }
    
    /**
     * Mendapatkan semua data kaos
     * @return List semua kaos
     * @throws Exception jika ada error
     */
    public List<Kaos> getAllKaos() throws Exception {
        try {
            return kaosDAO.getAllKaos();
        } catch (SQLException e) {
            throw new Exception("Gagal mengambil data kaos: " + e.getMessage());
        }
    }
    
    /**
     * Cari kaos berdasarkan keyword
     * @param keyword kata kunci pencarian
     * @return List kaos yang sesuai
     * @throws Exception jika ada error
     */
    public List<Kaos> searchKaos(String keyword) throws Exception {
        // Validasi: keyword tidak boleh null
        if (keyword == null) {
            keyword = "";
        }
        
        try {
            return kaosDAO.searchKaos(keyword.trim());
        } catch (SQLException e) {
            throw new Exception("Gagal mencari data kaos: " + e.getMessage());
        }
    }
    
    /**
     * Mendapatkan kaos berdasarkan ID
     * @param idKaos ID kaos
     * @return object Kaos
     * @throws Exception jika tidak ditemukan atau error
     */
    public Kaos getKaosById(int idKaos) throws Exception {
        try {
            Kaos kaos = kaosDAO.getKaosById(idKaos);
            if (kaos == null) {
                throw new Exception("Kaos dengan ID " + idKaos + " tidak ditemukan!");
            }
            return kaos;
        } catch (SQLException e) {
            throw new Exception("Gagal mengambil data kaos: " + e.getMessage());
        }
    }
    
    /**
     * Tambah kaos baru dengan VALIDASI LENGKAP
     * @param kaos object Kaos yang akan ditambahkan
     * @throws Exception jika validasi gagal atau error database
     */
    public void tambahKaos(Kaos kaos) throws Exception {
        // VALIDASI 1: Field tidak boleh kosong/null
        if (kaos.getMerekKaos() == null || kaos.getMerekKaos().trim().isEmpty()) {
            throw new Exception("Merek kaos harus diisi!");
        }
        
        if (kaos.getTypeKaos() == null || kaos.getTypeKaos().trim().isEmpty()) {
            throw new Exception("Tipe kaos harus diisi!");
        }
        
        if (kaos.getWarnaKaos() == null || kaos.getWarnaKaos().trim().isEmpty()) {
            throw new Exception("Warna kaos harus diisi!");
        }
        
        if (kaos.getSize() == null || kaos.getSize().trim().isEmpty()) {
            throw new Exception("Size harus diisi!");
        }
        
        // VALIDASI 2: Harga harus lebih dari 0
        if (kaos.getHargaPokok() <= 0) {
            throw new Exception("Harga pokok harus lebih dari 0!");
        }
        
        if (kaos.getHargaJual() <= 0) {
            throw new Exception("Harga jual harus lebih dari 0!");
        }
        
        // VALIDASI 3: Stok tidak boleh negatif
        if (kaos.getStokKaos() < 0) {
            throw new Exception("Stok tidak boleh negatif!");
        }
        
        // VALIDASI 4: Peringatan jika profit negatif (boleh simpan tapi kasih warning)
        int profit = hitungProfit(kaos);       
        if (profit < 0) {
            System.out.println("PERINGATAN: Profit negatif (Rugi Rp " + Math.abs(profit) + ")");
        }
        
        // ATURAN BISNIS: Trim semua string input
        kaos.setMerekKaos(kaos.getMerekKaos().trim());
        kaos.setTypeKaos(kaos.getTypeKaos().trim());
        kaos.setWarnaKaos(kaos.getWarnaKaos().trim());
        kaos.setSize(kaos.getSize().trim());
        
        // Simpan ke database
        try {
            int result = kaosDAO.insertKaos(kaos);
            if (result == 0) {
                throw new Exception("Gagal menyimpan data kaos!");
            }
        } catch (SQLException e) {
            throw new Exception("Gagal menyimpan data kaos: " + e.getMessage());
        }
    }
    
    /**
     * Update kaos dengan VALIDASI LENGKAP
     * @param kaos object Kaos dengan data baru
     * @throws Exception jika validasi gagal atau error database
     */
    public void updateKaos(Kaos kaos) throws Exception {
        // VALIDASI 1: ID harus ada
        if (kaos.getIdKaos() <= 0) {
            throw new Exception("ID kaos tidak valid!");
        }
        
        // VALIDASI 2: Field tidak boleh kosong/null
        if (kaos.getMerekKaos() == null || kaos.getMerekKaos().trim().isEmpty()) {
            throw new Exception("Merek kaos harus diisi!");
        }
        
        if (kaos.getTypeKaos() == null || kaos.getTypeKaos().trim().isEmpty()) {
            throw new Exception("Tipe kaos harus diisi!");
        }
        
        if (kaos.getWarnaKaos() == null || kaos.getWarnaKaos().trim().isEmpty()) {
            throw new Exception("Warna kaos harus diisi!");
        }
        
        if (kaos.getSize() == null || kaos.getSize().trim().isEmpty()) {
            throw new Exception("Size harus diisi!");
        }
        
        // VALIDASI 3: Harga harus lebih dari 0
        if (kaos.getHargaPokok() <= 0) {
            throw new Exception("Harga pokok harus lebih dari 0!");
        }
        
        if (kaos.getHargaJual() <= 0) {
            throw new Exception("Harga jual harus lebih dari 0!");
        }
        
        // VALIDASI 4: Stok tidak boleh negatif
        if (kaos.getStokKaos() < 0) {
            throw new Exception("Stok tidak boleh negatif!");
        }
        
        // VALIDASI 5: Cek apakah kaos dengan ID tersebut ada
        Kaos existing = kaosDAO.getKaosById(kaos.getIdKaos());
        if (existing == null) {
            throw new Exception("Kaos dengan ID " + kaos.getIdKaos() + " tidak ditemukan!");
        }
        
        // ATURAN BISNIS: Trim semua string input
        kaos.setMerekKaos(kaos.getMerekKaos().trim());
        kaos.setTypeKaos(kaos.getTypeKaos().trim());
        kaos.setWarnaKaos(kaos.getWarnaKaos().trim());
        kaos.setSize(kaos.getSize().trim());
        
        // Update ke database
        try {
            int result = kaosDAO.updateKaos(kaos);
            if (result == 0) {
                throw new Exception("Gagal mengupdate data kaos!");
            }
        } catch (SQLException e) {
            throw new Exception("Gagal mengupdate data kaos: " + e.getMessage());
        }
    }
    
    /**
     * Hapus kaos dengan validasi
     * @param idKaos ID kaos yang akan dihapus
     * @return object Kaos yang dihapus (untuk konfirmasi di UI)
     * @throws Exception jika tidak ditemukan atau error
     */
    public Kaos hapusKaos(int idKaos) throws Exception {
        // VALIDASI: Cek apakah kaos ada
        Kaos kaos = kaosDAO.getKaosById(idKaos);
        if (kaos == null) {
            throw new Exception("Kaos dengan ID " + idKaos + " tidak ditemukan!");
        }
        
        // ATURAN BISNIS: Bisa tambah validasi lain, misal:
        // - Cek apakah kaos sedang ada di keranjang user
        // - Cek apakah kaos pernah ditransaksikan (untuk history)
        // Tapi untuk saat ini kita izinkan hapus langsung
        
        try {
            int result = kaosDAO.deleteKaos(idKaos);
            if (result == 0) {
                throw new Exception("Gagal menghapus data kaos!");
            }
            return kaos; // Return data kaos yang dihapus
        } catch (SQLException e) {
            throw new Exception("Gagal menghapus data kaos: " + e.getMessage());
        }
    }
    
    /**
     * Validasi apakah stok cukup untuk transaksi
     * @param idKaos ID kaos
     * @param jumlah jumlah yang diminta
     * @return true jika stok cukup
     * @throws Exception jika stok tidak cukup atau kaos tidak ada
     */
    public boolean cekStokTersedia(int idKaos, int jumlah) throws Exception {
        Kaos kaos = kaosDAO.getKaosById(idKaos);
        if (kaos == null) {
            throw new Exception("Kaos tidak ditemukan!");
        }
        
        if (kaos.getStokKaos() < jumlah) {
            throw new Exception("Stok tidak cukup! Stok tersedia: " + kaos.getStokKaos());
        }
        
        return true;
    }
    
    /**
     * Hitung profit dari sebuah kaos
     * @param kaos object Kaos
     * @return nilai profit (bisa negatif jika rugi)
     */  
    public int hitungProfit(Kaos kaos) throws IllegalArgumentException {
    if (kaos == null) {
        throw new IllegalArgumentException("Kaos tidak boleh null!");
    }
    return kaos.getHargaJual() - kaos.getHargaPokok();
}
    
    /**
     * Validasi profit - return status profit
     * @param profit nilai profit
     * @return "UNTUNG", "IMPAS", atau "RUGI"
     */
    public String getStatusProfit(int profit) {
        if (profit > 0) {
            return "UNTUNG";
        } else if (profit == 0) {
            return "IMPAS";
        } else {
            return "RUGI";
        }
    }
}