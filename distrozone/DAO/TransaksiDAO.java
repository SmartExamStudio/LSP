package distrozone.DAO;

import distrozone.Model.Transaksi;
import java.sql.*;

/**
 * DAO - Koneksi ke database untuk tb_transaksi
 * 
 * PERBAIKAN MASALAH #1: ATOMIC TRANSACTION
 * - Semua method sekarang MENERIMA Connection yang sama
 * - Tidak membuat connection baru sendiri
 * - Transaksi dikelola oleh SERVICE
 */
public class TransaksiDAO {

    /**
     * INSERT transaksi ke database
     * @param conn Connection yang sama dari transaksi (WAJIB!)
     * @param transaksi Object transaksi yang akan disimpan
     * @return true jika berhasil, false jika gagal
     */
    public boolean insert(Connection conn, Transaksi transaksi) throws SQLException {
    String sql = "INSERT INTO tb_transaksi " +
                 "(id_transaksi, id_karyawan, tanggal, total_harga, total_item, metode_pembayaran, status_transaksi) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        PreparedStatement ps = null;
        
        try {
            ps = conn.prepareStatement(sql);
            
            ps.setString(1, transaksi.getIdTransaksi());
            ps.setInt(2, transaksi.getIdKaryawan());
            ps.setTimestamp(3, new Timestamp(transaksi.getTanggal().getTime()));
            ps.setInt(4, transaksi.getTotalHarga());
            ps.setInt(5, transaksi.getTotalItem());
            ps.setString(6, transaksi.getMetodePembayaran());
            ps.setString(7, transaksi.getStatusTransaksi());
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } finally {
            if (ps != null) ps.close();
            // TIDAK menutup connection! Dikelola oleh SERVICE
        }
    }

    /**
     * SELECT transaksi berdasarkan ID
     * Method ini boleh pakai connection sendiri karena hanya READ
     */
    public Transaksi getById(String idTransaksi) throws SQLException {
        String sql = "SELECT * FROM tb_transaksi WHERE id_transaksi = ?";
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = distrozone.DataBaseConector.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, idTransaksi);
            
            rs = ps.executeQuery();
            
            if (rs.next()) {
                Transaksi transaksi = new Transaksi();
                transaksi.setIdTransaksi(rs.getString("id_transaksi"));
                transaksi.setIdKaryawan(rs.getInt("id_karyawan"));
                transaksi.setTanggal(rs.getTimestamp("tanggal"));
                transaksi.setTotalHarga(rs.getInt("total_harga"));
                transaksi.setMetodePembayaran(rs.getString("metode_pembayaran"));
                
                return transaksi;
            }
            
            return null;
            
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        }
    }

    /**
     * UPDATE transaksi
     */
    public boolean update(Connection conn, Transaksi transaksi) throws SQLException {
        String sql = "UPDATE tb_transaksi SET id_karyawan = ?, tanggal = ?, " +
                     "total_harga = ?, metode_pembayaran = ? WHERE id_transaksi = ?";
        
        PreparedStatement ps = null;
        
        try {
            ps = conn.prepareStatement(sql);
            
            ps.setInt(1, transaksi.getIdKaryawan());
            ps.setTimestamp(2, new Timestamp(transaksi.getTanggal().getTime()));
            ps.setInt(3, transaksi.getTotalHarga());
            ps.setString(4, transaksi.getMetodePembayaran());
            ps.setString(5, transaksi.getIdTransaksi());
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } finally {
            if (ps != null) ps.close();
        }
    }

    /**
     * DELETE transaksi
     */
    public boolean delete(Connection conn, String idTransaksi) throws SQLException {
        String sql = "DELETE FROM tb_transaksi WHERE id_transaksi = ?";
        
        PreparedStatement ps = null;
        
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, idTransaksi);
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } finally {
            if (ps != null) ps.close();
        }
    }

    /**
     * Cek apakah ID transaksi sudah ada
     * Method ini boleh pakai connection sendiri karena hanya READ
     */
    public boolean isIdExists(String idTransaksi) throws SQLException {
        String sql = "SELECT COUNT(*) FROM tb_transaksi WHERE id_transaksi = ?";
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = distrozone.DataBaseConector.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, idTransaksi);
            
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
            return false;
            
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        }
    }
}