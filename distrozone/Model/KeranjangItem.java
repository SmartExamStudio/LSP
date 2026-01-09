package distrozone.Model;

/**
 * MODEL - Representasi item di keranjang (belum masuk DB)
 * - Tidak ada SQL
 * - Tidak ada UI
 * - Tidak ada logika berat
 * 
 * PENTING: Data ini hanya di memory (List), BELUM masuk database!
 * Baru masuk DB saat transaksi di-COMMIT
 */
public class KeranjangItem {
    private int idKaos;
    private String merek;
    private String type;
    private String warna;
    private String size;
    private int harga;
    private int jumlah;
    private int subtotal;

    // Constructor kosong
    public KeranjangItem() {
    }

    // Constructor lengkap
    public KeranjangItem(int idKaos, String merek, String type, String warna, String size, 
                         int harga, int jumlah, int subtotal) {
        this.idKaos = idKaos;
        this.merek = merek;
        this.type = type;
        this.warna = warna;
        this.size = size;
        this.harga = harga;
        this.jumlah = jumlah;
        this.subtotal = subtotal;
    }

    // Getters and Setters
    public int getIdKaos() {
        return idKaos;
    }

    public void setIdKaos(int idKaos) {
        this.idKaos = idKaos;
    }

    public String getMerek() {
        return merek;
    }

    public void setMerek(String merek) {
        this.merek = merek;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWarna() {
        return warna;
    }

    public void setWarna(String warna) {
        this.warna = warna;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public int getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
    }

    @Override
    public String toString() {
        return merek + " - " + type + " - " + warna + " - " + size;
    }
}