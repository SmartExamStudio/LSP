package distrozone.SERVICEnew;

import distrozone.DAO.KaosDAO;
import distrozone.DAO.TransaksiDAO;
import distrozone.DAO.DetailTransaksiDAO;
import distrozone.DataBaseConector;
import distrozone.Model.Kaos;
import distrozone.Model.Transaksi;
import distrozone.Model.DetailTransaksi;
import distrozone.Model.KeranjangItem;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * SERVICE untuk Point of Sale (POS)
 * 
 * SEMUA LOGIC BISNIS ADA DI SINI:
 * - Load & search produk
 * - Kelola keranjang
 * - Validasi stok & pembayaran
 * - Proses transaksi ATOMIC
 * 
 * UI tidak boleh tahu SQL!
 */
public class POSService {

    private KaosDAO kaosDAO;
    private TransaksiDAO transaksiDAO;
    private DetailTransaksiDAO detailDAO;

    public POSService() {
        this.kaosDAO = new KaosDAO();
        this.transaksiDAO = new TransaksiDAO();
        this.detailDAO = new DetailTransaksiDAO();
    }

    // ================= FORMAT HELPERS =================

    /**
     * Format angka ke format rupiah (dengan titik)
     * Contoh: 150000 -> "150.000"
     */
    public String formatRupiah(int angka) {
        return String.format("%,d", angka).replace(',', '.');
    }

    /**
     * Parse string rupiah ke integer
     * Contoh: "150.000" atau "150000" -> 150000
     * Return -1 jika format tidak valid
     */
    public int parseRupiah(String input) {
        try {
            String cleaned = input.replace(".", "").replace(",", "").replace("Rp", "").trim();
            return Integer.parseInt(cleaned);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // ================= PRODUK =================

    /**
     * Load semua produk dari database
     */
    public List<Kaos> loadAllProduk() throws SQLException {
        return kaosDAO.getAllKaos();
    }

    /**
     * Search produk berdasarkan keyword
     * Cari di: merek, type, warna
     */
    public List<Kaos> searchProduk(String keyword) throws SQLException {
        if (keyword == null || keyword.trim().isEmpty()) {
            return kaosDAO.getAllKaos();
        }
        return kaosDAO.searchKaos(keyword.trim());
    }

    // ================= KERANJANG =================

    /**
     * Tambah produk ke keranjang
     * Return: null jika sukses, string error jika gagal
     */
    public String tambahKeKeranjang(List<KeranjangItem> keranjang, Kaos kaos) {
        // Validasi stok
        if (kaos.getStokKaos() <= 0) {
            return "Stok " + kaos.getMerekKaos() + " habis!";
        }

        // Cek apakah sudah ada di keranjang
        for (KeranjangItem item : keranjang) {
            if (item.getIdKaos() == kaos.getIdKaos()) {
                // Cek stok sebelum tambah qty
                if (item.getJumlah() >= kaos.getStokKaos()) {
                    return "Stok tidak cukup! Maksimal " + kaos.getStokKaos() + " item.";
                }
                // Update qty existing item
                item.setJumlah(item.getJumlah() + 1);
                item.setSubtotal(item.getHarga() * item.getJumlah());
                return null; // Sukses
            }
        }

        // Tambah item baru
        KeranjangItem newItem = new KeranjangItem(
                kaos.getIdKaos(),
                kaos.getMerekKaos(),
                kaos.getTypeKaos(),
                kaos.getWarnaKaos(),
                kaos.getSize(),
                kaos.getHargaJual(),
                1, // qty = 1
                kaos.getHargaJual() // subtotal = harga x 1
        );
        keranjang.add(newItem);

        return null; // Sukses
    }

    /**
     * Update qty item di keranjang
     * Return: null jika sukses, "QTY_ZERO" jika qty jadi 0, string error jika gagal
     */
    public String updateQtyKeranjang(List<KeranjangItem> keranjang, int index, int delta) throws SQLException {
        if (index < 0 || index >= keranjang.size()) {
            return "Index tidak valid!";
        }

        KeranjangItem item = keranjang.get(index);
        int newQty = item.getJumlah() + delta;

        if (newQty <= 0) {
            return "QTY_ZERO"; // Signal untuk hapus item
        }

        // Cek stok untuk penambahan
        if (delta > 0) {
            Kaos kaos = kaosDAO.getKaosById(item.getIdKaos());
            if (kaos != null && newQty > kaos.getStokKaos()) {
                return "Stok tidak cukup! Maksimal " + kaos.getStokKaos() + " item.";
            }
        }

        item.setJumlah(newQty);
        item.setSubtotal(item.getHarga() * newQty);

        return null; // Sukses
    }

    /**
     * Hapus item dari keranjang
     */
    public void hapusItemKeranjang(List<KeranjangItem> keranjang, int index) {
        if (index >= 0 && index < keranjang.size()) {
            keranjang.remove(index);
        }
    }

    /**
     * Hitung total harga keranjang
     */
    public int hitungTotalKeranjang(List<KeranjangItem> keranjang) {
        int total = 0;
        for (KeranjangItem item : keranjang) {
            total += item.getSubtotal();
        }
        return total;
    }

    /**
     * Validasi keranjang sebelum checkout
     * Return: null jika valid, string error jika tidak valid
     */
    public String validasiKeranjang(List<KeranjangItem> keranjang) {
        if (keranjang == null || keranjang.isEmpty()) {
            return "Keranjang masih kosong!";
        }
        return null;
    }

    // ================= PEMBAYARAN =================

    /**
     * Validasi pembayaran CASH
     * Return: null jika valid, string error jika tidak valid
     */
    public String validasiPembayaranCash(int totalHarga, int jumlahBayar) {
        if (jumlahBayar < totalHarga) {
            return "Uang kurang! Kurang Rp " + formatRupiah(totalHarga - jumlahBayar);
        }
        return null;
    }

    /**
     * Hitung kembalian
     */
    public int hitungKembalian(int totalHarga, int jumlahBayar) {
        return jumlahBayar - totalHarga;
    }

    // ================= TRANSAKSI =================

    /**
     * Generate ID Transaksi otomatis
     * Format: TRX-YYYYMMDD-XXX
     */
    private String generateIdTransaksi() throws SQLException {
        String datePrefix = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String baseId = "TRX-" + datePrefix + "-";

        // Cari nomor urut
        int counter = 1;
        while (transaksiDAO.isIdExists(baseId + String.format("%03d", counter))) {
            counter++;
        }

        return baseId + String.format("%03d", counter);
    }

    /**
     * PROSES TRANSAKSI - ATOMIC (semua atau gagal semua)
     * 
     * Return: ID transaksi jika sukses, null jika gagal
     */
    public String prosesTransaksi(int idKaryawan, List<KeranjangItem> keranjang,
            int totalHarga, String metodePembayaran) {

        Connection conn = null;
        String idTransaksi = null;

        try {
            // 1. Buat connection baru & matikan auto-commit
            conn = DataBaseConector.getConnection();
            conn.setAutoCommit(false);

            // 2. Generate ID Transaksi
            idTransaksi = generateIdTransaksi();

            // 3. Validasi stok SEMUA item dulu (sebelum update apapun)
            for (KeranjangItem item : keranjang) {
                int stokSkrg = kaosDAO.getStokKaos(conn, item.getIdKaos());
                if (stokSkrg < item.getJumlah()) {
                    throw new SQLException("Stok " + item.getMerek() + " tidak cukup! " +
                            "Tersedia: " + stokSkrg + ", Diminta: " + item.getJumlah());
                }
            }

            // 4. Hitung total item
            int totalItem = 0;
            for (KeranjangItem item : keranjang) {
                totalItem += item.getJumlah();
            }

            // 5. Insert transaksi header
            Transaksi transaksi = new Transaksi(
                    idTransaksi,
                    idKaryawan,
                    new Date(),
                    totalHarga,
                    totalItem,
                    metodePembayaran,
                    "SELESAI");

            boolean insertOk = transaksiDAO.insert(conn, transaksi);
            if (!insertOk) {
                throw new SQLException("Gagal insert transaksi header");
            }

            // 6. Insert detail transaksi & kurangi stok
            List<DetailTransaksi> detailList = new ArrayList<>();

            for (KeranjangItem item : keranjang) {
                // Buat nama lengkap produk
                String namaKaos = item.getMerek() + " " + item.getType() +
                        " - " + item.getWarna() + " (" + item.getSize() + ")";

                DetailTransaksi detail = new DetailTransaksi(
                        idTransaksi,
                        item.getIdKaos(),
                        namaKaos,
                        item.getJumlah(),
                        item.getHarga(), // harga dikunci saat transaksi
                        item.getSubtotal());
                detailList.add(detail);

                // Kurangi stok
                boolean stokOk = kaosDAO.kurangiStok(conn, item.getIdKaos(), item.getJumlah());
                if (!stokOk) {
                    throw new SQLException("Gagal update stok: " + item.getMerek());
                }
            }

            // Insert batch detail
            int inserted = detailDAO.insertBatch(conn, detailList);
            if (inserted != detailList.size()) {
                throw new SQLException("Gagal insert detail transaksi");
            }

            // 7. COMMIT - semua sukses!
            conn.commit();
            System.out.println("✅ Transaksi " + idTransaksi + " berhasil COMMIT");

            return idTransaksi;

        } catch (SQLException e) {
            // ROLLBACK - ada yang gagal
            System.err.println("❌ Transaksi ROLLBACK: " + e.getMessage());
            e.printStackTrace();

            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("🔄 ROLLBACK berhasil");
                } catch (SQLException ex) {
                    System.err.println("❌ ROLLBACK gagal: " + ex.getMessage());
                }
            }

            return null;

        } finally {
            // Tutup connection
            DataBaseConector.closeConnection(conn);
        }
    }
}
