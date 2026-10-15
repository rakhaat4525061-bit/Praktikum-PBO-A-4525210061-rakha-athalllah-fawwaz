package service;

import model.Buku;
import java.util.ArrayList;
import java.util.List;

public class PerpustakaanService {


    private final List<Buku> daftarKoleksi = new ArrayList<>();
    

    public void tambahKoleksi(Buku buku) {
        if (buku != null && cariKoleksi(buku.getNomorInduk()) == null) {
            daftarKoleksi.add(buku);
        }
    }

    public Buku cariKoleksi(String nomorInduk) {
        for(Buku b : daftarKoleksi) {
            if (b.getNomorInduk().equalsIgnoreCase(nomorInduk)) {
                return b;
            }
        }
        return null;
    }

    public void pinjam(String nomorAnggota, String nomorInduk) {
        Buku buku = cariKoleksi(nomorInduk);
        if (buku != null && buku.tersedia()) {
            buku.kurangiStok();
        }
    }

    public void kembalikan(String nomorAnggota, String nomorInduk) {
        Buku buku = cariKoleksi(nomorInduk);
        if (buku != null) {
            buku.tambahStok();
        }
    }
}