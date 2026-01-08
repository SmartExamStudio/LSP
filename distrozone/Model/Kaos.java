package distrozone.Model;

/**
 * Model class untuk Kaos
 * Merepresentasikan 1 row di table tb_kaos
 */
public class Kaos {
    private int idKaos;
    private String merekKaos;
    private String typeKaos;
    private String warnaKaos;
    private String size;
    private int hargaPokok;
    private int hargaJual;
    private int stokKaos;
    private String fotoKaos;
    
    // Constructor kosong
    public Kaos() {
    }
    
    // Constructor dengan parameter
    public Kaos(int idKaos, String merekKaos, String typeKaos, String warnaKaos, 
                String size, int hargaPokok, int hargaJual, int stokKaos, String fotoKaos) {
        this.idKaos = idKaos;
        this.merekKaos = merekKaos;
        this.typeKaos = typeKaos;
        this.warnaKaos = warnaKaos;
        this.size = size;
        this.hargaPokok = hargaPokok;
        this.hargaJual = hargaJual;
        this.stokKaos = stokKaos;
        this.fotoKaos = fotoKaos;
    }
    
    // Getters and Setters
    public int getIdKaos() {
        return idKaos;
    }
    
    public void setIdKaos(int idKaos) {
        this.idKaos = idKaos;
    }
    
    public String getMerekKaos() {
        return merekKaos;
    }
    
    public void setMerekKaos(String merekKaos) {
        this.merekKaos = merekKaos;
    }
    
    public String getTypeKaos() {
        return typeKaos;
    }
    
    public void setTypeKaos(String typeKaos) {
        this.typeKaos = typeKaos;
    }
    
    public String getWarnaKaos() {
        return warnaKaos;
    }
    
    public void setWarnaKaos(String warnaKaos) {
        this.warnaKaos = warnaKaos;
    }
    
    public String getSize() {
        return size;
    }
    
    public void setSize(String size) {
        this.size = size;
    }
    
    public int getHargaPokok() {
        return hargaPokok;
    }
    
    public void setHargaPokok(int hargaPokok) {
        this.hargaPokok = hargaPokok;
    }
    
    public int getHargaJual() {
        return hargaJual;
    }
    
    public void setHargaJual(int hargaJual) {
        this.hargaJual = hargaJual;
    }
    
    public int getStokKaos() {
        return stokKaos;
    }
    
    public void setStokKaos(int stokKaos) {
        this.stokKaos = stokKaos;
    }
    
    public String getFotoKaos() {
        return fotoKaos;
    }
    
    public void setFotoKaos(String fotoKaos) {
        this.fotoKaos = fotoKaos;
    }
    
    /**
     * Method untuk menghitung profit
     * Profit = Harga Jual - Harga Pokok
     */
}