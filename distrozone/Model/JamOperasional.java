package distrozone.Model;

/**
 * Model untuk Jam Operasional
 * 2 tipe: OFFLINE (toko fisik) dan ONLINE (website)
 */
public class JamOperasional {

    private int id;
    private String tipe; // "OFFLINE" atau "ONLINE"
    private String jamBuka;
    private String jamTutup;
    private boolean senin;
    private boolean selasa;
    private boolean rabu;
    private boolean kamis;
    private boolean jumat;
    private boolean sabtu;
    private boolean minggu;

    // Constructor kosong
    public JamOperasional() {
    }

    // Constructor dengan parameter
    public JamOperasional(String tipe, String jamBuka, String jamTutup) {
        this.tipe = tipe;
        this.jamBuka = jamBuka;
        this.jamTutup = jamTutup;
        // Default semua hari buka
        this.senin = true;
        this.selasa = true;
        this.rabu = true;
        this.kamis = true;
        this.jumat = true;
        this.sabtu = true;
        this.minggu = true;
    }

    // Getters dan Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public String getJamBuka() {
        return jamBuka;
    }

    public void setJamBuka(String jamBuka) {
        this.jamBuka = jamBuka;
    }

    public String getJamTutup() {
        return jamTutup;
    }

    public void setJamTutup(String jamTutup) {
        this.jamTutup = jamTutup;
    }

    public boolean isSenin() {
        return senin;
    }

    public void setSenin(boolean senin) {
        this.senin = senin;
    }

    public boolean isSelasa() {
        return selasa;
    }

    public void setSelasa(boolean selasa) {
        this.selasa = selasa;
    }

    public boolean isRabu() {
        return rabu;
    }

    public void setRabu(boolean rabu) {
        this.rabu = rabu;
    }

    public boolean isKamis() {
        return kamis;
    }

    public void setKamis(boolean kamis) {
        this.kamis = kamis;
    }

    public boolean isJumat() {
        return jumat;
    }

    public void setJumat(boolean jumat) {
        this.jumat = jumat;
    }

    public boolean isSabtu() {
        return sabtu;
    }

    public void setSabtu(boolean sabtu) {
        this.sabtu = sabtu;
    }

    public boolean isMinggu() {
        return minggu;
    }

    public void setMinggu(boolean minggu) {
        this.minggu = minggu;
    }

    // Helper method untuk check apakah hari tertentu buka
    public boolean isHariBuka(String hari) {
        switch (hari.toLowerCase()) {
            case "senin":
                return senin;
            case "selasa":
                return selasa;
            case "rabu":
                return rabu;
            case "kamis":
                return kamis;
            case "jumat":
                return jumat;
            case "sabtu":
                return sabtu;
            case "minggu":
                return minggu;
            default:
                return false;
        }
    }
}
