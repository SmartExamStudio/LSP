package distrozone.DAO;

import distrozone.DataBaseConector;
import distrozone.Model.RingkasanKeuangan;
import distrozone.Model.RiwayatTransaksi;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DAO untuk laporan keuangan lengkap
 */
public class LaporanKeuanganDAO {

    /**
     * Ambil ringkasan keuangan berdasarkan periode
     */
    public RingkasanKeuangan getRingkasanKeuangan(Date tanggalFrom, Date tanggalTo,
            String channel, String pembayaran) throws SQLException {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("SUM(dt.subtotal) as pendapatan_produk, ");
        sql.append("SUM(k.harga_pokok * dt.jumlah) as total_modal, ");
        sql.append("SUM((dt.harga_saat_transaksi - k.harga_pokok) * dt.jumlah) as laba_bersih ");
        sql.append("FROM tb_transaksi t ");
        sql.append("JOIN tb_detail_transaksi dt ON t.id_transaksi = dt.id_transaksi ");
        sql.append("JOIN tb_kaos k ON dt.id_kaos = k.id_kaos ");
        sql.append("WHERE DATE(t.tanggal) BETWEEN ? AND ? ");
        sql.append("AND t.status_transaksi = 'SELESAI' ");

        // Filter channel - skip untuk sementara karena tidak ada field channel
        // if (channel != null && !channel.equals("Semua")) {
        // sql.append("AND t.channel = ? ");
        // }

        // Filter pembayaran - case insensitive
        if (pembayaran != null && !pembayaran.equals("Semua")) {
            sql.append("AND UPPER(t.metode_pembayaran) = UPPER(?) ");
        }

        System.out.println("SQL Ringkasan: " + sql.toString());
        System.out.println("Pembayaran param: " + pembayaran);

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DataBaseConector.getConnection();
            ps = conn.prepareStatement(sql.toString());

            int paramIndex = 1;
            ps.setDate(paramIndex++, new java.sql.Date(tanggalFrom.getTime()));
            ps.setDate(paramIndex++, new java.sql.Date(tanggalTo.getTime()));

            if (pembayaran != null && !pembayaran.equals("Semua")) {
                ps.setString(paramIndex++, pembayaran);
            }

            rs = ps.executeQuery();

            RingkasanKeuangan ringkasan = new RingkasanKeuangan();

            if (rs.next()) {
                int pendapatan = rs.getInt("pendapatan_produk");
                int modal = rs.getInt("total_modal");
                int laba = rs.getInt("laba_bersih");

                ringkasan.setPendapatanProduk(pendapatan);
                ringkasan.setTotalHargaModal(modal);
                ringkasan.setLabaBersih(laba);

                // Hitung margin persen
                if (modal > 0) {
                    double margin = ((double) laba / modal) * 100;
                    ringkasan.setMarginPersen(margin);
                } else {
                    ringkasan.setMarginPersen(0);
                }

                // Ongkir dan total kas masuk (bisa disesuaikan dengan kebutuhan)
                ringkasan.setTotalOngkir(0);
                ringkasan.setTotalKesMasuk(pendapatan);
            }

            return ringkasan;

        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
            if (conn != null)
                conn.close();
        }
    }

    /**
     * Ambil total transaksi dalam periode
     */
    public int getTotalTransaksi(Date tanggalFrom, Date tanggalTo,
            String channel, String pembayaran) throws SQLException {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) as total ");
        sql.append("FROM tb_transaksi t ");
        sql.append("WHERE DATE(t.tanggal) BETWEEN ? AND ? ");
        sql.append("AND t.status_transaksi = 'SELESAI' ");

        // Filter pembayaran - case insensitive
        if (pembayaran != null && !pembayaran.equals("Semua")) {
            sql.append("AND UPPER(t.metode_pembayaran) = UPPER(?) ");
        }

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DataBaseConector.getConnection();
            ps = conn.prepareStatement(sql.toString());

            int paramIndex = 1;
            ps.setDate(paramIndex++, new java.sql.Date(tanggalFrom.getTime()));
            ps.setDate(paramIndex++, new java.sql.Date(tanggalTo.getTime()));

            if (pembayaran != null && !pembayaran.equals("Semua")) {
                ps.setString(paramIndex++, pembayaran);
            }

            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("total");
            }

            return 0;

        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
            if (conn != null)
                conn.close();
        }
    }

    /**
     * Ambil daftar transaksi dalam periode dengan filter
     */
    public List<RiwayatTransaksi> getTransaksiList(Date tanggalFrom, Date tanggalTo,
            String channel, String pembayaran, String searchKeyword) throws SQLException {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT t.id_transaksi, t.id_transaksi as kode_transaksi, ");
        sql.append("t.tanggal, DATE_FORMAT(t.tanggal, '%H:%i:%s') as jam, ");
        sql.append("t.id_karyawan, k.nama as nama_kasir, ");
        sql.append("t.total_item, t.total_harga, t.total_harga as total_bayar, ");
        sql.append("t.metode_pembayaran, t.status_transaksi, t.tanggal as created_at ");
        sql.append("FROM tb_transaksi t ");
        sql.append("LEFT JOIN tb_karyawan k ON t.id_karyawan = k.id_karyawan ");
        sql.append("WHERE DATE(t.tanggal) BETWEEN ? AND ? ");
        sql.append("AND t.status_transaksi = 'SELESAI' ");

        // Filter pembayaran - case insensitive
        if (pembayaran != null && !pembayaran.equals("Semua")) {
            sql.append("AND UPPER(t.metode_pembayaran) = UPPER(?) ");
        }

        // Filter search
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            sql.append("AND (t.id_transaksi LIKE ? OR k.nama LIKE ?) ");
        }

        sql.append("ORDER BY t.tanggal DESC");

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<RiwayatTransaksi> list = new ArrayList<>();

        try {
            conn = DataBaseConector.getConnection();
            ps = conn.prepareStatement(sql.toString());

            int paramIndex = 1;
            ps.setDate(paramIndex++, new java.sql.Date(tanggalFrom.getTime()));
            ps.setDate(paramIndex++, new java.sql.Date(tanggalTo.getTime()));

            if (pembayaran != null && !pembayaran.equals("Semua")) {
                ps.setString(paramIndex++, pembayaran);
            }

            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                String keyword = "%" + searchKeyword + "%";
                ps.setString(paramIndex++, keyword);
                ps.setString(paramIndex++, keyword);
            }

            rs = ps.executeQuery();

            while (rs.next()) {
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

                list.add(r);
            }

            return list;

        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
            if (conn != null)
                conn.close();
        }
    }
}