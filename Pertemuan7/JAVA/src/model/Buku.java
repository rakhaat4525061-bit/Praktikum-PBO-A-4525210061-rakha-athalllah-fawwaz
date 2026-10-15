package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Sesi 7 — KOMPOSISI.
 *
 * Tiga hal yang membuat sebuah relasi benar-benar komposisi:
 *   1. tidak ada constructor atau method yang MENERIMA objek bagian dari luar
 *   2. objek bagian dibuat DI DALAM induk
 *   3. getter mengembalikan salinan yang tidak bisa diubah
 *
 * Lengkapi TODO sehingga ketiganya terpenuhi.
 */
public class Buku {

    private final String nomorInduk;
    private final String judul;
    private final String penulis;
    private int jumlahEksemplar;
    private final List<Bab> bab = new ArrayList<>();

    public Buku(String nomorInduk, String judul, String penulis, int jumlahEksemplar) {
        if (jumlahEksemplar < 0) {
            throw new IllegalArgumentException("jumlah eksemplar tidak boleh negatif");
        }
        this.nomorInduk = nomorInduk;
        this.judul = judul;
        this.penulis = penulis;
        this.jumlahEksemplar = jumlahEksemplar;
    }

    public void tambahBab(int nomor, String judulBab) {
        bab.add(new Bab(nomor, judulBab));
    }

    public List<Bab> getBab() {
        return List.copyOf(bab);   
    }

    public boolean tersedia() { return jumlahEksemplar > 0; }

    public void kurangiStok() {
        if (!tersedia()) {
            throw new IllegalStateException("Stok buku habis.");
        }
        this.jumlahEksemplar--;
    }

    public void tambahStok() { jumlahEksemplar++; }

    public String getNomorInduk()  { return nomorInduk; }
    public String getJudul()       { return judul; }
    public int getJumlahEksemplar() { return jumlahEksemplar; }

    @Override
    public String toString() {
        return String.format("%-32s %-16s [%d eks, %d bab]",
                judul, penulis, jumlahEksemplar, bab.size());
    }

    /**
     * Bab adalah BAGIAN dari Buku — kelasnya bersarang untuk menegaskan itu,
     * dan constructor-nya privat agar hanya Buku yang boleh membuatnya.
     */
    public static final class Bab {
        private final int nomor;
        private final String judul;

        private Bab(int nomor, String judul) {
            this.nomor = nomor;
            this.judul = judul;
        }

        public int getNomor()    { return nomor; }
        public String getJudul() { return judul; }

        @Override public String toString() { return "Bab " + nomor + ": " + judul; }
    }
}