/**
 * Satu kelas boleh mewarisi SATU class, tetapi mengimplementasikan BANYAK interface.
 * Tuliskan di keputusan.md: mengapa Java membuat aturan seperti itu?
 */
public class Mobil extends Kendaraan implements Movable, Fuelable {

    private final double kapasitasTangki;
    private double isiTangki = 0;

    public Mobil(String merek, int tahun, double kapasitasTangki) {
        super(merek, tahun);
        if (Double.isNaN(kapasitasTangki) || kapasitasTangki <= 0) {
            throw new IllegalArgumentException(
                    "Kapasitas tangki harus lebih dari 0, tetapi diterima: " + kapasitasTangki);
        }
        this.kapasitasTangki = kapasitasTangki;
    }

    @Override public int jumlahRoda() { return 4; }

    // TODO 1: lengkapi kontrak Movable.
    @Override public void bergerak() {
        System.out.println("  " + getMerek() + " melaju di jalan raya");
    }

    @Override public double kecepatanMaksimum() { return 180; }

    // TODO 2: lengkapi kontrak Fuelable.
    @Override public void isiBahanBakar(double jumlah) {
        if (Double.isNaN(jumlah) || jumlah <= 0) {
            throw new IllegalArgumentException(
                    "Jumlah bahan bakar harus lebih dari 0, tetapi diterima: " + jumlah);
        }
        if (isiTangki + jumlah > kapasitasTangki) {
            throw new IllegalArgumentException(
                    "Melebihi kapasitas tangki: isi " + isiTangki + " + " + jumlah
                            + " > kapasitas " + kapasitasTangki);
        }
        isiTangki += jumlah;
    }

    @Override public double kapasitasTangki() { return kapasitasTangki; }

    @Override public TipeBahanBakar tipeBahanBakar() { return TipeBahanBakar.BENSIN; }

    public double getIsiTangki() { return isiTangki; }
}