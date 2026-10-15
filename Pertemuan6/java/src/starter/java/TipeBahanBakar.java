/**
 * Enum: hanya nilai yang terdaftar di sini yang mungkin ada.
 * Bandingkan dengan `public static final int BENSIN = 1;`
 * yang membiarkan angka 99 lolos begitu saja.
 */
public enum TipeBahanBakar {

    // TODO 1: konstanta enum beserta label dan harga per satuannya.
    BENSIN("Bensin", 12000),
    SOLAR("Solar", 10500),
    // TODO 2 (Langkah 2): LISTRIK sudah ditambahkan (harga per kWh).
    LISTRIK("Listrik", 2500);

    private final String label;
    private final double hargaPerSatuan;

    TipeBahanBakar(String label, double hargaPerSatuan) {
        this.label = label;
        this.hargaPerSatuan = hargaPerSatuan;
    }

    public String getLabel() { return label; }

    /** TODO 3: enum boleh punya method — ini yang tidak bisa dilakukan konstanta int. */
    public double biayaPengisian(double jumlah) {
        if (Double.isNaN(jumlah) || jumlah < 0) {
            throw new IllegalArgumentException(
                    "Jumlah pengisian tidak boleh negatif, tetapi diterima: " + jumlah);
        }
        return jumlah * hargaPerSatuan;
    }

    /** TODO 4: true hanya untuk LISTRIK. */
    public boolean ramahLingkungan() {
        return this == LISTRIK;
    }
}