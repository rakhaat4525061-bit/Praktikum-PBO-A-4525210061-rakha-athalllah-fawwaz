/**
 * Sesi 3 — constructor berdelegasi, anggota statis, dan konstanta.
 *
 * Invariant:
 *   1. saldo tidak pernah negatif
 *   2. nomor rekening tidak berubah setelah objek dibuat
 *   3. setoran dan penarikan selalu bernilai positif
 */
public class RekeningBank {

    // TODO 1: konstanta bernama pengganti angka ajaib
    public static final double BUNGA_TAHUNAN = 0.025;
    public static final double BIAYA_ADMIN   = 5000;
    public static final double BATAS_TARIK   = 5000000;

    // TODO 2: penghitung jumlah rekening (static, privat, awal 0)
    private static int jumlahRekening = 0;

    private final String nomor;
    private final String pemilik;
    private double saldo;

    /**
     * Constructor ringkas.
     * TODO 3: delegasi ke constructor lengkap, tanpa menyalin validasi.
     */
    public RekeningBank(String nomor, String pemilik) {
        this(nomor, pemilik, 0);
    }

    /** Constructor lengkap — SATU-SATUNYA tempat validasi berada. */
    public RekeningBank(String nomor, String pemilik, double saldoAwal) {
        // TODO 4: tolak nomor kosong dan saldo awal negatif
        if (nomor == null || nomor.trim().isEmpty()) {
            throw new IllegalArgumentException("Nomor rekening tidak boleh null atau kosong");
        }
        if (Double.isNaN(saldoAwal) || saldoAwal < 0) {
            throw new IllegalArgumentException(
                    "Saldo awal tidak boleh negatif, tetapi diterima: " + saldoAwal);
        }

        this.nomor = nomor;
        this.pemilik = pemilik;
        this.saldo = saldoAwal;

        // TODO 5: naikkan penghitung DI SINI SAJA.
        // Constructor ringkas sudah memanggil constructor ini lewat this(...), jadi kalau
        // dinaikkan di keduanya, rekening yang dibuat lewat constructor ringkas terhitung 2x.
        // Letaknya juga setelah validasi: jika exception dilempar, tidak ada objek yang
        // terbentuk, sehingga penghitung tidak ikut naik.
        jumlahRekening++;
    }

    public void setor(double jumlah) {
        // TODO 6: tolak jumlah <= 0, lalu tambahkan ke saldo
        if (Double.isNaN(jumlah) || jumlah <= 0) {
            throw new IllegalArgumentException(
                    "Jumlah setoran harus lebih dari 0, tetapi diterima: " + jumlah);
        }
        saldo += jumlah;
    }

    public void tarik(double jumlah) {
        // TODO 7: tolak jumlah <= 0, melebihi batas sekali transaksi, dan melebihi saldo
        if (Double.isNaN(jumlah) || jumlah <= 0) {
            throw new IllegalArgumentException(
                    "Jumlah penarikan harus lebih dari 0, tetapi diterima: " + jumlah);
        }
        if (jumlah > BATAS_TARIK) {
            throw new IllegalArgumentException(
                    "Penarikan " + jumlah + " melebihi batas sekali transaksi " + BATAS_TARIK);
        }
        if (jumlah > saldo) {
            throw new IllegalArgumentException(
                    "Saldo tidak cukup: saldo " + saldo + ", penarikan " + jumlah);
        }
        saldo -= jumlah;
    }

    /** TODO 8: kurangi saldo sebesar biaya admin, tetapi jangan sampai negatif. */
    public void potongBiayaAdmin() {
        saldo = Math.max(0, saldo - BIAYA_ADMIN);
    }

    /** TODO 9: kembalikan jumlah rekening yang pernah dibuat. */
    public static int getJumlahRekening() {
        return jumlahRekening;
    }

    /**
     * TODO 10: hitung bunga setahun dari pokok.
     *          Tidak membaca keadaan objek mana pun, maka pantas menjadi static.
     */
    public static double bungaSetahun(double pokok) {
        return pokok * BUNGA_TAHUNAN;
    }

    public double getSaldo()  { return saldo; }
    public String getNomor()  { return nomor; }

    @Override
    public String toString() {
        return String.format("Rekening[%s] %-14s Rp%,.2f", nomor, pemilik, saldo);
    }

    // Demo singkat (opsional, boleh dihapus)
    public static void main(String[] args) {
        RekeningBank a = new RekeningBank("001", "Rakha");
        RekeningBank b = new RekeningBank("002", "Budi", 1000000);

        a.setor(200000);
        b.tarik(250000);
        b.potongBiayaAdmin();

        System.out.println(a);
        System.out.println(b);
        System.out.println("Jumlah rekening : " + getJumlahRekening());
        System.out.println("Bunga 1 juta    : " + bungaSetahun(1000000));

        try {
            b.tarik(6000000);
        } catch (IllegalArgumentException e) {
            System.out.println("Ditolak: " + e.getMessage());
        }
        try {
            new RekeningBank("", "Tanpa Nomor");
        } catch (IllegalArgumentException e) {
            System.out.println("Ditolak: " + e.getMessage());
        }
        System.out.println("Jumlah rekening : " + getJumlahRekening() + " (tetap 2)");
    }
}