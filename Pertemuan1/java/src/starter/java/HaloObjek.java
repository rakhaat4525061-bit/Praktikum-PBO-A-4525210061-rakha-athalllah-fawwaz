/**
 * Sesi 1 — objek pertama Anda.
 *
 * Kerangka ini SUDAH BISA DIKOMPILASI apa adanya, tetapi keluarannya belum
 * benar sampai seluruh TODO dilengkapi.
 *
 * Kompilasi dan jalankan:
 *      javac -d out HaloObjek.java
 *      java -cp out HaloObjek
 */
public class HaloObjek {

    // TODO 1: Tambahkan kata kunci `private` pada kedua atribut.
    private String nama;
    private String nim;

    public HaloObjek(String nama, String nim) {
        // TODO 2: Gunakan kata kunci `this` untuk membedakan parameter dan atribut.
        this.nama = nama;
        this.nim = nim;
    }

    /**
     * TODO 3: Kembalikan string berisi nama dan NIM menggunakan atribut objek.
     */
    public String sapa() {
        return "Halo, saya " + this.nama + " (" + this.nim + ")";
    }

    public static void main(String[] args) {
        // Ganti dua nilai di bawah dengan nama dan NIM Anda sendiri.
        HaloObjek saya = new HaloObjek("Rakha Athallah Fawwaz", "4525210061");
        System.out.println(saya.sapa());

        // Bukti bahwa objek menyimpan datanya masing-masing:
        HaloObjek temanSekelas = new HaloObjek("Ghatfan heru", "4525210025");
        System.out.println(temanSekelas.sapa());

        System.out.println();
        System.out.println("Pertanyaan untuk direnungkan:");
        System.out.println("  Baris mana di atas yang MEMBUAT objek?");
        System.out.println("  Baris mana yang hanya MENDEKLARASIKAN kelas?");
        System.out.println("  Mengapa kedua objek di atas bisa menyapa dengan nama berbeda,");
        System.out.println("  padahal method sapa() hanya ditulis satu kali?");
    }
}