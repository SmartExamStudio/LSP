package distrozone.DAO;

import distrozone.DataBaseConector;
import distrozone.Model.RiwayatTransaksi;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 * DAO - Khusus untuk mengambil data RIWAYAT transaksi
 * READ-ONLY - Tidak ada insert, update, delete
 */
public class RiwayatTransaksiDAO {

    /**
     * ✅ KASIR - Ambil riwayat transaksi milik kasir saja
     * @param idKasir ID kasir yang login
     * @return List riwayat transaksi
     */
    public List<RiwayatTransaksi> getRiwayatByKasir(int idKasir) throws SQLException {
        String sql = "SELECT t.id_transaksi, t.id_transaksi as kode_transaksi, " +
                     "t.tanggal, DATE_FORMAT(t.tanggal, '%H:%i:%s') as jam, " +
                     "t.id_karyawan, k.nama as nama_kasir, " +
                     "t.total_item, t.total_harga, t.total_harga as total_bayar, " +
                     "t.metode_pembayaran, t.status_transaksi, t.tanggal as created_at " +
                     "FROM tb_transaksi t " +
                     "LEFT JOIN tb_karyawan k ON t.id_karyawan = k.id_karyawan " +
                     "WHERE t.id_karyawan = ? " +
                     "ORDER BY t.tanggal DESC";
        
        return executeQuery(sql, idKasir);
    }

    /**
     * ✅ ADMIN - Ambil SEMUA riwayat transaksi
     * @return List semua riwayat transaksi
     */
    public List<RiwayatTransaksi> getAllRiwayat() throws SQLException {
        String sql = "SELECT t.id_transaksi, t.id_transaksi as kode_transaksi, " +
                     "t.tanggal, DATE_FORMAT(t.tanggal, '%H:%i:%s') as jam, " +
                     "t.id_karyawan, k.nama as nama_kasir, " +
                     "t.total_item, t.total_harga, t.total_harga as total_bayar, " +
                     "t.metode_pembayaran, t.status_transaksi, t.tanggal as created_at " +
                     "FROM tb_transaksi t " +
                     "LEFT JOIN tb_karyawan k ON t.id_karyawan = k.id_karyawan " +
                     "ORDER BY t.tanggal DESC";
        
        return executeQuery(sql);
    }

    /**
     * ✅ FILTER - Riwayat kasir dengan filter tanggal
     */
    public List<RiwayatTransaksi> getRiwayatByKasirWithFilter(
            int idKasir, Date tanggalFrom, Date tanggalTo) throws SQLException {
        
        String sql = "SELECT t.id_transaksi, t.id_transaksi as kode_transaksi, " +
                     "t.tanggal, DATE_FORMAT(t.tanggal, '%H:%i:%s') as jam, " +
                     "t.id_karyawan, k.nama as nama_kasir, " +
                     "t.total_item, t.total_harga, t.total_harga as total_bayar, " +
                     "t.metode_pembayaran, t.status_transaksi, t.tanggal as created_at " +
                     "FROM tb_transaksi t " +
                     "LEFT JOIN tb_karyawan k ON t.id_karyawan = k.id_karyawan " +
                     "WHERE t.id_karyawan = ? " +
                     "AND DATE(t.tanggal) BETWEEN ? AND ? " +
                     "ORDER BY t.tanggal DESC";
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<RiwayatTransaksi> list = new ArrayList<>();
        
        try {
            conn = DataBaseConector.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idKasir);
            ps.setDate(2, new java.sql.Date(tanggalFrom.getTime()));
            ps.setDate(3, new java.sql.Date(tanggalTo.getTime()));
            
            rs = ps.executeQuery();
            
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
            
            return list;
            
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        }
    }

    /**
     * ✅ FILTER - Riwayat kasir dengan filter metode pembayaran
     */
    public List<RiwayatTransaksi> getRiwayatByKasirWithMetode(
            int idKasir, String metodePembayaran) throws SQLException {
        
        String sql = "SELECT t.id_transaksi, t.id_transaksi as kode_transaksi, " +
                     "t.tanggal, DATE_FORMAT(t.tanggal, '%H:%i:%s') as jam, " +
                     "t.id_karyawan, k.nama as nama_kasir, " +
                     "t.total_item, t.total_harga, t.total_harga as total_bayar, " +
                     "t.metode_pembayaran, t.status_transaksi, t.tanggal as created_at " +
                     "FROM tb_transaksi t " +
                     "LEFT JOIN tb_karyawan k ON t.id_karyawan = k.id_karyawan " +
                     "WHERE t.id_karyawan = ? AND t.metode_pembayaran = ? " +
                     "ORDER BY t.tanggal DESC";
        
        return executeQueryWithMetode(sql, idKasir, metodePembayaran);
    }

    /**
     * ✅ FILTER - Riwayat kasir LENGKAP (tanggal + metode)
     */
    public List<RiwayatTransaksi> getRiwayatByKasirWithFullFilter(
            int idKasir, Date tanggalFrom, Date tanggalTo, String metodePembayaran) throws SQLException {
        
        String sql = "SELECT t.id_transaksi, t.id_transaksi as kode_transaksi, " +
                     "t.tanggal, DATE_FORMAT(t.tanggal, '%H:%i:%s') as jam, " +
                     "t.id_karyawan, k.nama as nama_kasir, " +
                     "t.total_item, t.total_harga, t.total_harga as total_bayar, " +
                     "t.metode_pembayaran, t.status_transaksi, t.tanggal as created_at " +
                     "FROM tb_transaksi t " +
                     "LEFT JOIN tb_karyawan k ON t.id_karyawan = k.id_karyawan " +
                     "WHERE t.id_karyawan = ? " +
                     "AND DATE(t.tanggal) BETWEEN ? AND ? " +
                     "AND t.metode_pembayaran = ? " +
                     "ORDER BY t.tanggal DESC";
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<RiwayatTransaksi> list = new ArrayList<>();
        
        try {
            conn = DataBaseConector.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idKasir);
            ps.setDate(2, new java.sql.Date(tanggalFrom.getTime()));
            ps.setDate(3, new java.sql.Date(tanggalTo.getTime()));
            ps.setString(4, metodePembayaran);
            
            rs = ps.executeQuery();
            
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
            
            return list;
            
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        }
    }

    /**
     * ✅ SEARCH - Cari berdasarkan kode transaksi (untuk live search)
     */
    public List<RiwayatTransaksi> searchRiwayatByKasir(int idKasir, String keyword) throws SQLException {
        String sql = "SELECT t.id_transaksi, t.id_transaksi as kode_transaksi, " +
                     "t.tanggal, DATE_FORMAT(t.tanggal, '%H:%i:%s') as jam, " +
                     "t.id_karyawan, k.nama as nama_kasir, " +
                     "t.total_item, t.total_harga, t.total_harga as total_bayar, " +
                     "t.metode_pembayaran, t.status_transaksi, t.tanggal as created_at " +
                     "FROM tb_transaksi t " +
                     "LEFT JOIN tb_karyawan k ON t.id_karyawan = k.id_karyawan " +
                     "WHERE t.id_karyawan = ? " +
                     "AND (t.id_transaksi LIKE ? OR t.metode_pembayaran LIKE ?) " +
                     "ORDER BY t.tanggal DESC";
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<RiwayatTransaksi> list = new ArrayList<>();
        
        try {
            conn = DataBaseConector.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idKasir);
            ps.setString(2, "%" + keyword + "%");
            ps.setString(3, "%" + keyword + "%");
            
            rs = ps.executeQuery();
            
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
            
            return list;
            
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        }
    }

    /**
     * ✅ GET BY ID - Ambil 1 transaksi untuk detail/cetak struk
     */
    public RiwayatTransaksi getById(String idTransaksi) throws SQLException {
        String sql = "SELECT t.id_transaksi, t.id_transaksi as kode_transaksi, " +
                     "t.tanggal, DATE_FORMAT(t.tanggal, '%H:%i:%s') as jam, " +
                     "t.id_karyawan, k.nama as nama_kasir, " +
                     "t.total_item, t.total_harga, t.total_harga as total_bayar, " +
                     "t.metode_pembayaran, t.status_transaksi, t.tanggal as created_at " +
                     "FROM tb_transaksi t " +
                     "LEFT JOIN tb_karyawan k ON t.id_karyawan = k.id_karyawan " +
                     "WHERE t.id_transaksi = ?";
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DataBaseConector.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, idTransaksi);
            
            rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSet(rs);
            }
            
            return null;
            
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        }
    }

    // ========== HELPER METHODS ==========

    private List<RiwayatTransaksi> executeQuery(String sql, Object... params) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<RiwayatTransaksi> list = new ArrayList<>();
        
        try {
            conn = DataBaseConector.getConnection();
            ps = conn.prepareStatement(sql);
            
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            
            rs = ps.executeQuery();
            
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
            
            return list;
            
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        }
    }

    private List<RiwayatTransaksi> executeQueryWithMetode(String sql, int idKasir, String metode) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<RiwayatTransaksi> list = new ArrayList<>();
        
        try {
            conn = DataBaseConector.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idKasir);
            ps.setString(2, metode);
            
            rs = ps.executeQuery();
            
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
            
            return list;
            
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        }
    }

    private RiwayatTransaksi mapResultSet(ResultSet rs) throws SQLException {
        RiwayatTransaksi r = new RiwayatTransaksi();
        r.setIdTransaksi(rs.getString("id_transaksi"));
        r.setKodeTransaksi(rs.getString("kode_transaksi"));
        r.setTanggalTransaksi(rs.getTimestamp("tanggal"));
        r.setJamTransaksi(rs.getString("jam"));
        r.setIdKasir(rs.getInt("id_karyawan"));
        r.setNamaKasir(rs.getString("nama_kasir"));
        r.setTotalItem(rs.getInt("total_item"));
        r.setSubtotal(rs.getInt("total_harga"));
        r.setTotalBayar(rs.getInt("total_bayar"));
        r.setMetodePembayaran(rs.getString("metode_pembayaran"));
        r.setStatusTransaksi(rs.getString("status_transaksi"));
        r.setCreatedAt(rs.getTimestamp("created_at"));
        return r;
    }
}