package distrozone.DAO;

import distrozone.DataBaseConector;
import distrozone.Model.DetailTransaksi;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO - Koneksi ke database untuk tb_detail_transaksi
 * ISINYA:
 * - Hanya tau SQL
 * - Mengatur CRUD
 * - Mengatur transaksi DB
 * 
 * TIDAK BOLEH:
 * - Tidak tau UI
 * - Tidak tau SERVICE
 * - Tidak tau validasi bisnis
 */
public class DetailTransaksiDAO {

    /**
     * INSERT detail transaksi ke database
     * @param detail Object detail transaksi yang akan disimpan
     * @return true jika berhasil, false jika gagal
     */
    public boolean insert(DetailTransaksi detail) throws SQLException {
        String sql = "INSERT INTO tb_detail_transaksi (id_transaksi, id_kaos, jumlah, subtotal) " +
                     "VALUES (?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = DataBaseConector.getConnection();
            ps = conn.prepareStatement(sql);
            
            ps.setString(1, detail.getIdTransaksi());
            ps.setInt(2, detail.getIdKaos());
            ps.setInt(3, detail.getJumlah());
            ps.setInt(4, detail.getSubtotal());
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } finally {
            if (ps != null) ps.close();
        }
    }

    /**
     * INSERT multiple detail transaksi sekaligus (batch)
     * @param detailList List detail transaksi yang akan disimpan
     * @return jumlah row yang berhasil diinsert
     */
     public int insertBatch(Connection conn, List<DetailTransaksi> list) throws SQLException {
    String sql = "INSERT INTO tb_detail_transaksi " +
                 "(id_transaksi, id_kaos, nama_kaos, jumlah, harga_saat_transaksi, subtotal) " +
                 "VALUES (?, ?, ?, ?, ?, ?)";
              
        PreparedStatement ps = null;
        
        try {
            ps = conn.prepareStatement(sql);
            
            for (DetailTransaksi detail : list) {
                ps.setString(1, detail.getIdTransaksi());
                ps.setInt(2, detail.getIdKaos());
                ps.setString(3, detail.getNamaKaos());
                ps.setInt(4, detail.getJumlah());
                ps.setInt(5, detail.getHargaSaatTransaksi());
                ps.setInt(6, detail.getSubtotal());
                ps.addBatch();
            }
            
            int[] results = ps.executeBatch();
            
            // Hitung jumlah yang berhasil
            int successCount = 0;
            for (int result : results) {
                if (result > 0) successCount++;
            }
            
            return successCount;
            
        } finally {
            if (ps != null) ps.close();
        }
    }

    /**
     * ✅ UPDATED: SELECT semua detail transaksi berdasarkan ID transaksi
     * Mengambil SEMUA field termasuk nama_kaos dan harga_saat_transaksi
     * @param idTransaksi ID transaksi yang dicari
     * @return List detail transaksi
     */
    public List<DetailTransaksi> getByIdTransaksi(String idTransaksi) throws SQLException {
        String sql = "SELECT * FROM tb_detail_transaksi WHERE id_transaksi = ?";
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<DetailTransaksi> detailList = new ArrayList<>();
        
        try {
            conn = DataBaseConector.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, idTransaksi);
            
            rs = ps.executeQuery();
            
            while (rs.next()) {
                DetailTransaksi detail = new DetailTransaksi();
                detail.setIdDetail(rs.getInt("id_detail"));
                detail.setIdTransaksi(rs.getString("id_transaksi"));
                detail.setIdKaos(rs.getInt("id_kaos"));
                detail.setNamaKaos(rs.getString("nama_kaos")); // ✅ TAMBAH
                detail.setJumlah(rs.getInt("jumlah"));
                detail.setHargaSatuan(rs.getInt("harga_saat_transaksi")); // ✅ TAMBAH
                detail.setHargaSaatTransaksi(rs.getInt("harga_saat_transaksi")); // ✅ TAMBAH
                detail.setSubtotal(rs.getInt("subtotal"));
                
                detailList.add(detail);
            }
            
            return detailList;
            
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close(); // ✅ TAMBAH close connection
        }
    }

    /**
     * SELECT detail transaksi berdasarkan ID detail
     * @param idDetail ID detail yang dicari
     * @return Object DetailTransaksi jika ditemukan, null jika tidak
     */
    public DetailTransaksi getById(int idDetail) throws SQLException {
        String sql = "SELECT * FROM tb_detail_transaksi WHERE id_detail = ?";
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DataBaseConector.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idDetail);
            
            rs = ps.executeQuery();
            
            if (rs.next()) {
                DetailTransaksi detail = new DetailTransaksi();
                detail.setIdDetail(rs.getInt("id_detail"));
                detail.setIdTransaksi(rs.getString("id_transaksi"));
                detail.setIdKaos(rs.getInt("id_kaos"));
                detail.setJumlah(rs.getInt("jumlah"));
                detail.setSubtotal(rs.getInt("subtotal"));
                
                return detail;
            }
            
            return null;
            
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        }
    }

    /**
     * DELETE semua detail transaksi berdasarkan ID transaksi
     * @param idTransaksi ID transaksi yang detail-nya akan dihapus
     * @return jumlah row yang dihapus
     */
    public int deleteByIdTransaksi(String idTransaksi) throws SQLException {
        String sql = "DELETE FROM tb_detail_transaksi WHERE id_transaksi = ?";
        
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = DataBaseConector.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, idTransaksi);
            
            return ps.executeUpdate();
            
        } finally {
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        }
    }

    /**
     * DELETE detail transaksi berdasarkan ID detail
     * @param idDetail ID detail yang akan dihapus
     * @return true jika berhasil, false jika gagal
     */
    public boolean delete(int idDetail) throws SQLException {
        String sql = "DELETE FROM tb_detail_transaksi WHERE id_detail = ?";
        
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = DataBaseConector.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idDetail);
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } finally {
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        }
    }
}