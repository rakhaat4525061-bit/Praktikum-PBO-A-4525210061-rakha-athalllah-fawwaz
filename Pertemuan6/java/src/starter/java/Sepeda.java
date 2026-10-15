/**
 * Sepeda bisa bergerak, tetapi tidak memakai bahan bakar.
 * Karena itu ia hanya implements Movable, BUKAN Fuelable.
 */
public class Sepeda implements Movable {

    private final String nama;
    private final int jumlahGir;

    public Sepeda(String nama, int jumlahGir) {
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama sepeda tidak boleh null atau kosong");
        }
        if (jumlahGir <= 0) {
            throw new IllegalArgumentException(
                    "Jumlah gir harus lebih dari 0, tetapi diterima: " + jumlahGir);
        }
        this.nama = nama;
        this.jumlahGir = jumlahGir;
    }

    @Override
    public void bergerak() {
        System.out.println("  " + nama + " dikayuh (" + jumlahGir + " gir)");
    }

    @Override
    public double kecepatanMaksimum() {
        return 30;
    }

    public String getNama() { return nama; }
}