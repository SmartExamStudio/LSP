package distrozone.DAO;

import distrozone.DataBaseConector;
import distrozone.Model.Kaos;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) untuk tb_kaos
 * 
 * PERBAIKAN MASALAH #1: ATOMIC TRANSACTION
 * - Method kurangiStok sekarang MENERIMA Connection yang sama
 * 
 * PERBAIKAN MASALAH #3: UPDATE STOK TIDAK "BUTA"
 * - Sekarang cek stok >= jumlah di SQL
 * - Mencegah stok minus
 * - Anti race condition (2 kasir bersamaan)
 */
public class KaosDAO {
    
    /**
     * Mengambil semua data kaos dari database
     */
    public List<Kaos> getAllKaos() throws SQLException {
        List<Kaos> listKaos = new ArrayList<>();
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        
        try {
            conn = DataBaseConector.getConnection();
            st = conn.createStatement();
            rs = st.executeQuery("SELECT * FROM tb_kaos ORDER BY id_kaos DESC");
            
            while (rs.next()) {
                Kaos kaos = new Kaos();
                kaos.setIdKaos(rs.getInt("id_kaos"));
                kaos.setMerekKaos(rs.getString("merek_kaos"));
                kaos.setTypeKaos(rs.getString("type_kaos"));
                kaos.setWarnaKaos(rs.getString("warna_kaos"));
                kaos.setSize(rs.getString("size"));
                kaos.setHargaPokok(rs.getInt("harga_pokok"));
                kaos.setHargaJual(rs.getInt("harga_jual"));
                kaos.setStokKaos(rs.getInt("stok_kaos"));
                kaos.setFotoKaos(rs.getString("foto_kaos"));
                
                listKaos.add(kaos);
            }
            
        } finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
            if (st != null) {
                try { st.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        
        return listKaos;
    }
    
    /**
     * Cari kaos berdasarkan keyword
     */
    public List<Kaos> searchKaos(String keyword) throws SQLException {
        List<Kaos> listKaos = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        try {
            conn = DataBaseConector.getConnection();
            
            String sql = "SELECT * FROM tb_kaos WHERE " +
                         "merek_kaos LIKE ? OR " +
                         "type_kaos LIKE ? OR " +
                         "warna_kaos LIKE ? OR " +
                         "size LIKE ? " +
                         "ORDER BY id_kaos DESC";
            
            pst = conn.prepareStatement(sql);
            
            String searchPattern = "%" + keyword + "%";
            pst.setString(1, searchPattern);
            pst.setString(2, searchPattern);
            pst.setString(3, searchPattern);
            pst.setString(4, searchPattern);
            
            rs = pst.executeQuery();
            
            while (rs.next()) {
                Kaos kaos = new Kaos();
                kaos.setIdKaos(rs.getInt("id_kaos"));
                kaos.setMerekKaos(rs.getString("merek_kaos"));
                kaos.setTypeKaos(rs.getString("type_kaos"));
                kaos.setWarnaKaos(rs.getString("warna_kaos"));
                kaos.setSize(rs.getString("size"));
                kaos.setHargaPokok(rs.getInt("harga_pokok"));
                kaos.setHargaJual(rs.getInt("harga_jual"));
                kaos.setStokKaos(rs.getInt("stok_kaos"));
                kaos.setFotoKaos(rs.getString("foto_kaos"));
                
                listKaos.add(kaos);
            }
            
        } finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
            if (pst != null) {
                try { pst.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        
        return listKaos;
    }
    
    /**
     * Mendapatkan kaos berdasarkan ID
     */
    public Kaos getKaosById(int idKaos) throws SQLException {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        Kaos kaos = null;
        
        try {
            conn = DataBaseConector.getConnection();
            String sql = "SELECT * FROM tb_kaos WHERE id_kaos = ?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, idKaos);
            rs = pst.executeQuery();
            
            if (rs.next()) {
                kaos = new Kaos();
                kaos.setIdKaos(rs.getInt("id_kaos"));
                kaos.setMerekKaos(rs.getString("merek_kaos"));
                kaos.setTypeKaos(rs.getString("type_kaos"));
                kaos.setWarnaKaos(rs.getString("warna_kaos"));
                kaos.setSize(rs.getString("size"));
                kaos.setHargaPokok(rs.getInt("harga_pokok"));
                kaos.setHargaJual(rs.getInt("harga_jual"));
                kaos.setStokKaos(rs.getInt("stok_kaos"));
                kaos.setFotoKaos(rs.getString("foto_kaos"));
            }
            
        } finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
            if (pst != null) {
                try { pst.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        
        return kaos;
    }
    
    /**
     * Insert kaos baru ke database
     */
    public int insertKaos(Kaos kaos) throws SQLException {
        Connection conn = null;
        PreparedStatement pst = null;
        int result = 0;
        
        try {
            conn = DataBaseConector.getConnection();
            
            String sql = "INSERT INTO tb_kaos (merek_kaos, type_kaos, warna_kaos, size, " +
                         "harga_pokok, harga_jual, stok_kaos, foto_kaos) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            
            pst = conn.prepareStatement(sql);
            pst.setString(1, kaos.getMerekKaos());
            pst.setString(2, kaos.getTypeKaos());
            pst.setString(3, kaos.getWarnaKaos());
            pst.setString(4, kaos.getSize());
            pst.setInt(5, kaos.getHargaPokok());
            pst.setInt(6, kaos.getHargaJual());
            pst.setInt(7, kaos.getStokKaos());
            pst.setString(8, kaos.getFotoKaos());
            
            result = pst.executeUpdate();
            
        } finally {
            if (pst != null) {
                try { pst.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        
        return result;
    }
    
    /**
     * Update data kaos
     */
    public int updateKaos(Kaos kaos) throws SQLException {
        Connection conn = null;
        PreparedStatement pst = null;
        int result = 0;
        
        try {
            conn = DataBaseConector.getConnection();
            
            String sql = "UPDATE tb_kaos SET merek_kaos=?, type_kaos=?, warna_kaos=?, " +
                         "size=?, harga_pokok=?, harga_jual=?, stok_kaos=?, foto_kaos=? " +
                         "WHERE id_kaos=?";
            
            pst = conn.prepareStatement(sql);
            pst.setString(1, kaos.getMerekKaos());
            pst.setString(2, kaos.getTypeKaos());
            pst.setString(3, kaos.getWarnaKaos());
            pst.setString(4, kaos.getSize());
            pst.setInt(5, kaos.getHargaPokok());
            pst.setInt(6, kaos.getHargaJual());
            pst.setInt(7, kaos.getStokKaos());
            pst.setString(8, kaos.getFotoKaos());
            pst.setInt(9, kaos.getIdKaos());
            
            result = pst.executeUpdate();
            
        } finally {
            if (pst != null) {
                try { pst.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        
        return result;
    }
    
    /**
     * Hapus kaos dari database
     */
    public int deleteKaos(int idKaos) throws SQLException {
        Connection conn = null;
        PreparedStatement pst = null;
        int result = 0;
        
        try {
            conn = DataBaseConector.getConnection();
            
            String sql = "DELETE FROM tb_kaos WHERE id_kaos = ?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, idKaos);
            
            result = pst.executeUpdate();
            
        } finally {
            if (pst != null) {
                try { pst.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        
        return result;
    }
    
    /**
     * Update stok kaos (untuk transaksi) - METHOD LAMA
     * Deprecated: Gunakan kurangiStok() dengan Connection
     */
    @Deprecated
    public int updateStok(int idKaos, int jumlah) throws SQLException {
        Connection conn = null;
        PreparedStatement pst = null;
        int result = 0;
        
        try {
            conn = DataBaseConector.getConnection();
            
            String sql = "UPDATE tb_kaos SET stok_kaos = stok_kaos - ? " +
                         "WHERE id_kaos = ? AND stok_kaos >= ?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, jumlah);
            pst.setInt(2, idKaos);
            pst.setInt(3, jumlah);
            
            result = pst.executeUpdate();
            
        } finally {
            if (pst != null) {
                try { pst.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        
        return result;
    }
    
    // ========== METHOD UNTUK TRANSAKSI SERVICE (ATOMIC & AMAN) ==========
    
    /**
     * GET stok kaos saat ini
     * Method ini boleh pakai connection sendiri karena hanya READ
     */
    public int getStokKaos(Connection conn, int idKaos) throws SQLException {
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        try {
            String sql = "SELECT stok_kaos FROM tb_kaos WHERE id_kaos = ?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, idKaos);
            
            rs = pst.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("stok_kaos");
            }
            
            return -1; // Produk tidak ditemukan
            
        } finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
            if (pst != null) {
                try { pst.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }
    
    /**
     * Kurangi stok kaos (AMAN dari race condition & stok minus)
     * 
     * PERBAIKAN MASALAH #3: UPDATE STOK TIDAK "BUTA"
     * - Sekarang cek stok >= jumlah di SQL
     * - Return false jika stok tidak cukup
     * - Anti race condition (2 kasir beli bersamaan)
     * 
     * @param conn Connection yang sama dari transaksi (WAJIB!)
     * @param idKaos ID kaos yang stoknya akan dikurangi
     * @param jumlah Jumlah yang akan dikurangi dari stok
     * @return true jika berhasil, false jika gagal (stok tidak cukup)
     */
    public boolean kurangiStok(Connection conn, int idKaos, int jumlah) throws SQLException {
        PreparedStatement ps = null;
        
        // SQL yang AMAN: hanya update jika stok cukup!
            String sql = "UPDATE tb_kaos " +
                         "SET stok_kaos = stok_kaos - ? " +
                         "WHERE id_kaos = ? " +
                         "AND stok_kaos >= ?";  // ← INI KUNCI KEAMANANNYA!
       
        try {           
            ps = conn.prepareStatement(sql);
            ps.setInt(1, jumlah);
            ps.setInt(2, idKaos);
            ps.setInt(3, jumlah); // Cek stok >= jumlah
            
            int affected = ps.executeUpdate();

            // Jika 0 row terupdate → stok tidak cukup atau produk tidak ada
            if (affected == 0) {
                return false; // GAGAL - stok tidak cukup!
            }

            return true; // BERHASIL
            
        } finally {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // TIDAK menutup connection! Dikelola oleh SERVICE
    }
}
    
    /**
     * Kembalikan stok kaos (untuk rollback transaksi)
     * 
     * @param conn Connection yang sama dari transaksi (WAJIB!)
     * @param idKaos ID kaos yang stoknya akan dikembalikan
     * @param jumlah Jumlah yang akan ditambahkan ke stok
     * @return true jika berhasil, false jika gagal
     */
    public boolean kembalikanStok(Connection conn, int idKaos, int jumlah) throws SQLException {
        PreparedStatement pst = null;
        
        try {
            String sql = "UPDATE tb_kaos SET stok_kaos = stok_kaos + ? WHERE id_kaos = ?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, jumlah);
            pst.setInt(2, idKaos);
            
            int result = pst.executeUpdate();
            return result > 0;
            
        } finally {
            if (pst != null) {
                try { pst.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }
}