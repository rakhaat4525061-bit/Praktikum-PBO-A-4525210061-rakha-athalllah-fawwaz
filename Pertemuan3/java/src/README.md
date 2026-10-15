# Sesi 3 — Constructor Berdelegasi, Anggota Statis, dan Konstanta (RekeningBank)

Latihan ketiga PBO: kelas `RekeningBank` yang memakai **constructor berdelegasi**, **konstanta bernama** (tanpa angka ajaib), serta **anggota statis** (penghitung dan method utilitas). Diimplementasikan dalam **Java** dan **PHP**.

## Identitas

| Nama                  | NIM        |
|-----------------------|------------|
| Rakha Athallah Fawwaz | 4525210061 |

## Invariant

| No | Invariant                                         | Cara Menjaganya                                                   |
|----|---------------------------------------------------|-------------------------------------------------------------------|
| 1  | Saldo tidak pernah negatif                        | Validasi saldo awal, cek saldo di `tarik()`, `max(0, ...)` di `potongBiayaAdmin()` |
| 2  | Nomor rekening tidak berubah setelah dibuat       | `final` (Java) / `readonly` (PHP), tanpa `setNomor()`             |
| 3  | Setoran dan penarikan selalu bernilai positif     | Pemeriksaan `jumlah <= 0` di `setor()` dan `tarik()`              |
| 4  | Penarikan tidak melebihi batas sekali transaksi   | Pemeriksaan terhadap konstanta `BATAS_TARIK`                      |
| 5  | Nilai `NaN` tidak dianggap jumlah yang sah        | `isNaN` / `is_nan` dicek terpisah                                 |

## Konstanta (Pengganti Angka Ajaib)

| Konstanta        | Nilai     | Keterangan                    |
|------------------|-----------|-------------------------------|
| `BUNGA_TAHUNAN`  | 0.025     | Bunga tahunan (2,5%)          |
| `BIAYA_ADMIN`    | 5000      | Biaya administrasi            |
| `BATAS_TARIK`    | 5000000   | Batas penarikan sekali transaksi |

Setelah konstanta dibuat, tidak ada lagi angka literal di dalam badan method.

## Struktur Berkas

```
.
├── java/
│   └── RekeningBank.java
├── php/
│   └── RekeningBank.php
└── README.md
```

## Konsep Utama

### 1. Delegasi constructor (Java)

Constructor ringkas tidak menyalin validasi, melainkan menyerahkannya ke constructor lengkap:

```java
public RekeningBank(String nomor, String pemilik) {
    this(nomor, pemilik, 0);
}
```

Dengan begitu validasi hanya ada di **satu tempat**.

### 2. Pengganti overloading (PHP)

PHP tidak punya constructor overloading. Padanannya:
- **Default parameter**: `float $saldoAwal = 0`
- **Named constructor (static factory)**: `RekeningBank::rekeningPelajar(...)`

### 3. Penghitung statis

`jumlahRekening` adalah field statis privat yang dinaikkan **hanya di constructor lengkap**, setelah validasi lolos.
- Jika dinaikkan juga di constructor ringkas, rekening yang dibuat lewat constructor itu terhitung **dua kali**, karena constructor ringkas memanggil constructor lengkap.
- Karena dinaikkan setelah validasi, percobaan yang gagal (exception) tidak ikut terhitung.

### 4. Method statis utilitas

`bungaSetahun(pokok)` tidak membaca keadaan objek mana pun (hanya konstanta dan parameter), sehingga pantas menjadi `static` dan dapat dipanggil tanpa membuat objek.

### 5. `new static` vs `new self` (PHP)

`rekeningPelajar()` memakai `new static`, bukan `new self`. Dengan *late static binding*, jika dipanggil lewat subclass (misalnya `RekeningGold::rekeningPelajar(...)`), objek yang dibuat adalah `RekeningGold`. `new self` selalu membuat `RekeningBank`.

## Cara Menjalankan

### Java

Prasyarat: JDK 8 atau lebih baru.

```bash
cd java
javac -d out RekeningBank.java
java -cp out RekeningBank
```

### PHP

Prasyarat: PHP 8.3 atau lebih baru (konstanta bertipe `const float`).
Untuk PHP 8.0–8.2, hapus kata `float` pada deklarasi konstanta.

```bash
cd php
php RekeningBank.php
```

## Contoh Keluaran

**Java** (format angka mengikuti locale; contoh ini memakai locale en-US)

```
Rekening[001] Rakha          Rp200,000.00
Rekening[002] Budi           Rp745,000.00
Jumlah rekening : 2
Bunga 1 juta    : 25000.0
Ditolak: Penarikan 6000000.0 melebihi batas sekali transaksi 5000000.0
Ditolak: Nomor rekening tidak boleh null atau kosong
Jumlah rekening : 2 (tetap 2)
```

**PHP**

```
Rekening[001] Rakha          Rp200.000,00
Rekening[002] Budi           Rp745.000,00
Rekening[003] Siti           Rp0,00
Jumlah rekening : 3
Bunga 1 juta    : 25000
Ditolak: Penarikan 6000000 melebihi batas sekali transaksi 5000000
Ditolak: Nomor rekening tidak boleh kosong
Jumlah rekening : 3 (tetap 3)
```

Perhitungan saldo Budi: `1.000.000 − 250.000 (tarik) − 5.000 (biaya admin) = 745.000`.

## Perbandingan Java vs PHP

| Konsep                    | Java                                      | PHP                                              |
|---------------------------|-------------------------------------------|--------------------------------------------------|
| Konstanta                 | `public static final double`              | `public const float`                             |
| Akses konstanta           | `BATAS_TARIK`                             | `self::BATAS_TARIK`                              |
| Field statis              | `private static int jumlahRekening = 0;`  | `private static int $jumlahRekening = 0;`        |
| Akses field statis        | `jumlahRekening++`                        | `self::$jumlahRekening++`                        |
| Constructor ringkas       | Overloading + `this(...)`                 | Default parameter `$saldoAwal = 0`               |
| Cara membuat varian objek | Constructor tambahan                      | Named constructor (`rekeningPelajar`)            |
| Membuat instance di factory | `new RekeningBank(...)`                 | `new static(...)` (late static binding)          |
| Exception validasi        | `IllegalArgumentException`                | `InvalidArgumentException`                       |
| Cek NaN                   | `Double.isNaN(x)`                         | `is_nan($x)`                                     |
| Format uang               | `String.format("%,.2f")` (ikut locale)    | `number_format($x, 2, ',', '.')`                 |

## Catatan Desain

- **Validasi satu tempat**: semua pemeriksaan ada di constructor lengkap, sehingga objek yang tidak sah tidak pernah terbentuk.
- **Tanpa angka ajaib**: mengubah bunga, biaya admin, atau batas penarikan cukup dilakukan di satu baris konstanta.
- **Pesan error spesifik**: menyebut apa yang salah beserta nilai yang diterima.
- **`potongBiayaAdmin()` aman**: memakai `max(0, saldo - biaya)` sehingga saldo tidak pernah negatif.
- **Urutan pemeriksaan `tarik()`**: jumlah positif → batas sekali transaksi → kecukupan saldo.