package distrozone.DAO;

import distrozone.DataBaseConector;
import distrozone.Model.JamOperasional;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO untuk Jam Operasional
 */
public class JamOperasionalDAO {

    /**
     * Ambil semua jam operasional
     */
    public List<JamOperasional> getAll() throws SQLException {
        List<JamOperasional> list = new ArrayList<>();
        String sql = "SELECT * FROM tb_jam_operasional";

        try (Connection conn = DataBaseConector.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                JamOperasional jam = new JamOperasional();
                jam.setId(rs.getInt("id"));
                jam.setTipe(rs.getString("tipe"));
                jam.setJamBuka(rs.getString("jam_buka"));
                jam.setJamTutup(rs.getString("jam_tutup"));
                jam.setSenin(rs.getBoolean("senin"));
                jam.setSelasa(rs.getBoolean("selasa"));
                jam.setRabu(rs.getBoolean("rabu"));
                jam.setKamis(rs.getBoolean("kamis"));
                jam.setJumat(rs.getBoolean("jumat"));
                jam.setSabtu(rs.getBoolean("sabtu"));
                jam.setMinggu(rs.getBoolean("minggu"));
                list.add(jam);
            }
        }
        return list;
    }

    /**
     * Ambil jam operasional by tipe (OFFLINE / ONLINE)
     */
    public JamOperasional getByTipe(String tipe) throws SQLException {
        String sql = "SELECT * FROM tb_jam_operasional WHERE tipe = ?";

        try (Connection conn = DataBaseConector.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tipe);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                JamOperasional jam = new JamOperasional();
                jam.setId(rs.getInt("id"));
                jam.setTipe(rs.getString("tipe"));
                jam.setJamBuka(rs.getString("jam_buka"));
                jam.setJamTutup(rs.getString("jam_tutup"));
                jam.setSenin(rs.getBoolean("senin"));
                jam.setSelasa(rs.getBoolean("selasa"));
                jam.setRabu(rs.getBoolean("rabu"));
                jam.setKamis(rs.getBoolean("kamis"));
                jam.setJumat(rs.getBoolean("jumat"));
                jam.setSabtu(rs.getBoolean("sabtu"));
                jam.setMinggu(rs.getBoolean("minggu"));
                return jam;
            }
        }
        return null;
    }

    /**
     * Update jam operasional
     */
    public boolean update(JamOperasional jam) throws SQLException {
        String sql = "UPDATE tb_jam_operasional SET jam_buka = ?, jam_tutup = ?, " +
                "senin = ?, selasa = ?, rabu = ?, kamis = ?, jumat = ?, sabtu = ?, minggu = ? " +
                "WHERE tipe = ?";

        try (Connection conn = DataBaseConector.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, jam.getJamBuka());
            ps.setString(2, jam.getJamTutup());
            ps.setBoolean(3, jam.isSenin());
            ps.setBoolean(4, jam.isSelasa());
            ps.setBoolean(5, jam.isRabu());
            ps.setBoolean(6, jam.isKamis());
            ps.setBoolean(7, jam.isJumat());
            ps.setBoolean(8, jam.isSabtu());
            ps.setBoolean(9, jam.isMinggu());
            ps.setString(10, jam.getTipe());

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Insert jam operasional baru
     */
    public boolean insert(JamOperasional jam) throws SQLException {
        String sql = "INSERT INTO tb_jam_operasional (tipe, jam_buka, jam_tutup, " +
                "senin, selasa, rabu, kamis, jumat, sabtu, minggu) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DataBaseConector.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, jam.getTipe());
            ps.setString(2, jam.getJamBuka());
            ps.setString(3, jam.getJamTutup());
            ps.setBoolean(4, jam.isSenin());
            ps.setBoolean(5, jam.isSelasa());
            ps.setBoolean(6, jam.isRabu());
            ps.setBoolean(7, jam.isKamis());
            ps.setBoolean(8, jam.isJumat());
            ps.setBoolean(9, jam.isSabtu());
            ps.setBoolean(10, jam.isMinggu());

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Check apakah tabel sudah ada datanya
     */
    public boolean hasData() throws SQLException {
        String sql = "SELECT COUNT(*) FROM tb_jam_operasional";

        try (Connection conn = DataBaseConector.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    /**
     * Initialize default data jika belum ada
     */
    public void initDefaultData() throws SQLException {
        if (!hasData()) {
            // Toko Offline: 10:00-20:00, Senin libur
            JamOperasional offline = new JamOperasional("OFFLINE", "10:00", "20:00");
            offline.setSenin(false); // Senin libur
            insert(offline);

            // Toko Online: 10:00-17:00, setiap hari
            JamOperasional online = new JamOperasional("ONLINE", "10:00", "17:00");
            insert(online);
        }
    }
}
