package distrozone.DAO;

import distrozone.DataBaseConector;
import distrozone.Model.Kaos;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO khusus untuk POS - Load produk dengan live search
 */
public class ProdukPOSDAO {
    
    /**
     * Load semua produk aktif (stok > 0)
     */
    public List<Kaos> getAllProdukAktif() throws SQLException {
        String sql = "SELECT * FROM tb_kaos WHERE stok_kaos > 0 ORDER BY merek_kaos, type_kaos";
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Kaos> list = new ArrayList<>();
        
        try {
            conn = DataBaseConector.getConnection();
            ps = conn.prepareStatement(sql);
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
     * Search produk dengan keyword (live search)
     * Cari di: merek, type, warna, size
     */
    public List<Kaos> searchProduk(String keyword) throws SQLException {
        String sql = "SELECT * FROM tb_kaos " +
                    "WHERE stok_kaos > 0 " +
                    "AND (merek_kaos LIKE ? " +
                    "OR type_kaos LIKE ? " +
                    "OR warna_kaos LIKE ? " +
                    "OR size LIKE ?) " +
                    "ORDER BY merek_kaos, type_kaos";
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Kaos> list = new ArrayList<>();
        
        try {
            conn = DataBaseConector.getConnection();
            ps = conn.prepareStatement(sql);
            
            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);
            ps.setString(4, searchPattern);
            
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
     * Filter produk berdasarkan kriteria spesifik
     */
    public List<Kaos> filterProduk(String merek, String type, String warna, String size) 
            throws SQLException {
        
        StringBuilder sql = new StringBuilder("SELECT * FROM tb_kaos WHERE stok_kaos > 0");
        List<String> params = new ArrayList<>();
        
        if (merek != null && !merek.trim().isEmpty()) {
            sql.append(" AND merek_kaos LIKE ?");
            params.add("%" + merek + "%");
        }
        
        if (type != null && !type.equals("Semua")) {
            sql.append(" AND type_kaos = ?");
            params.add(type);
        }
        
        if (warna != null && !warna.trim().isEmpty()) {
            sql.append(" AND warna_kaos LIKE ?");
            params.add("%" + warna + "%");
        }
        
        if (size != null && !size.equals("Semua")) {
            sql.append(" AND size = ?");
            params.add(size);
        }
        
        sql.append(" ORDER BY merek_kaos, type_kaos");
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Kaos> list = new ArrayList<>();
        
        try {
            conn = DataBaseConector.getConnection();
            ps = conn.prepareStatement(sql.toString());
            
            for (int i = 0; i < params.size(); i++) {
                ps.setString(i + 1, params.get(i));
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
    
    /**
     * Get produk by ID
     */
    public Kaos getProdukById(int idKaos) throws SQLException {
        String sql = "SELECT * FROM tb_kaos WHERE id_kaos = ?";
        
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = DataBaseConector.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idKaos);
            
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
    
    private Kaos mapResultSet(ResultSet rs) throws SQLException {
        Kaos kaos = new Kaos();
        kaos.setIdKaos(rs.getInt("id_kaos"));
        kaos.setMerekKaos(rs.getString("merek_kaos"));
        kaos.setTypeKaos(rs.getString("type_kaos"));
        kaos.setWarnaKaos(rs.getString("warna_kaos"));
        kaos.setSize(rs.getString("size"));
        kaos.setStokKaos(rs.getInt("stok_kaos"));
        kaos.setHargaPokok(rs.getInt("harga_pokok"));
        kaos.setHargaJual(rs.getInt("harga_jual"));
        return kaos;
    }
}