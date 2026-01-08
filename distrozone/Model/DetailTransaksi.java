package distrozone.Model;

/**
 * MODEL - Detail Transaksi (Updated)
 * Ditambahkan field: namaKaos, hargaSatuan untuk riwayat
 */
public class DetailTransaksi {
    private int idDetail;
    private String idTransaksi;
    private int idKaos;
    private String namaKaos; // ✅ TAMBAHAN untuk riwayat
    private int jumlah;
    private int hargaSatuan; // ✅ TAMBAHAN (alias untuk harga_saat_transaksi)
    private int hargaSaatTransaksi; // ✅ Harga dikunci saat transaksi
    private int subtotal;

    // Constructor kosong
    public DetailTransaksi() {
    }

    // Constructor untuk transaksi baru (dengan nama kaos)
    public DetailTransaksi(String idTransaksi, int idKaos, String namaKaos, 
                          int jumlah, int hargaSaatTransaksi, int subtotal) {
        this.idTransaksi = idTransaksi;
        this.idKaos = idKaos;
        this.namaKaos = namaKaos;
        this.jumlah = jumlah;
        this.hargaSatuan = hargaSaatTransaksi;
        this.hargaSaatTransaksi = hargaSaatTransaksi;
        this.subtotal = subtotal;
    }

    // Constructor lengkap
    public DetailTransaksi(int idDetail, String idTransaksi, int idKaos, 
                          String namaKaos, int jumlah, int hargaSaatTransaksi, int subtotal) {
        this.idDetail = idDetail;
        this.idTransaksi = idTransaksi;
        this.idKaos = idKaos;
        this.namaKaos = namaKaos;
        this.jumlah = jumlah;
        this.hargaSatuan = hargaSaatTransaksi;
        this.hargaSaatTransaksi = hargaSaatTransaksi;
        this.subtotal = subtotal;
    }

    // Getters and Setters
    public int getIdDetail() {
        return idDetail;
    }

    public void setIdDetail(int idDetail) {
        this.idDetail = idDetail;
    }

    public String getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(String idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public int getIdKaos() {
        return idKaos;
    }

    public void setIdKaos(int idKaos) {
        this.idKaos = idKaos;
    }

    public String getNamaKaos() {
        return namaKaos;
    }

    public void setNamaKaos(String namaKaos) {
        this.namaKaos = namaKaos;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public int getHargaSatuan() {
        return hargaSatuan;
    }

    public void setHargaSatuan(int hargaSatuan) {
        this.hargaSatuan = hargaSatuan;
        this.hargaSaatTransaksi = hargaSatuan; // Sync
    }

    public int getHargaSaatTransaksi() {
        return hargaSaatTransaksi;
    }

    public void setHargaSaatTransaksi(int hargaSaatTransaksi) {
        this.hargaSaatTransaksi = hargaSaatTransaksi;
        this.hargaSatuan = hargaSaatTransaksi; // Sync
    }

    public int getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
    }
}