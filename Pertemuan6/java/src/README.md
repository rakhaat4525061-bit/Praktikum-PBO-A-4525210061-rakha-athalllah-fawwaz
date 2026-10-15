# Sesi 6 — Abstraksi: Interface, Abstract Class, Enum, dan Trait

Latihan keenam PBO: memisahkan **apa yang bisa dilakukan** (interface) dari **apa benda itu** (abstract class), memakai **enum** yang punya perilaku, dan (khusus PHP) **trait** untuk penggunaan ulang kode secara horizontal. Diimplementasikan dalam **Java** dan **PHP**.

## Identitas

| Nama                  | NIM        |
|-----------------------|------------|
| Rakha Athallah Fawwaz | 4525210061 |

## Gambaran Rancangan

```
            «interface»        «interface»
             Movable            Fuelable
             ▲     ▲               ▲
             │     │               │
   Sepeda ───┘     └─── Mobil ─────┘
      │                   │
      └──── Kendaraan ────┘        (abstract class)

   TipeBahanBakar (enum)  ◄── dipakai oleh Fuelable
   Loggable (trait, PHP)  ◄── dipakai Mobil dan Pesanan
```

| Tipe              | Peran                                                                   |
|-------------------|-------------------------------------------------------------------------|
| `Movable`         | Kontrak "bisa bergerak": `bergerak()`, `kecepatanMaksimum()`            |
| `Fuelable`        | Kontrak "memakai bahan bakar": isi, kapasitas, tipe bahan bakar         |
| `Kendaraan`       | Abstract class: merek, tahun, `umur()`, `jumlahRoda()` abstract         |
| `Mobil`           | Turunan `Kendaraan`, mengimplementasikan `Movable` dan `Fuelable`       |
| `Sepeda`          | Turunan `Kendaraan`, hanya `Movable` (**bukan** `Fuelable`)             |
| `TipeBahanBakar`  | Enum: Bensin, Solar, Listrik, lengkap dengan label, harga, dan perilaku |
| `Loggable`        | Trait (PHP): method `log()` yang bisa disisipkan ke kelas mana pun      |
| `Pesanan`         | Kelas PHP yang tidak berkerabat dengan `Kendaraan`, tetapi memakai `Loggable` |

## Data Enum

| Tipe    | Label   | Harga per Satuan | Ramah Lingkungan |
|---------|---------|------------------|------------------|
| Bensin  | Bensin  | Rp12.000         | Tidak            |
| Solar   | Solar   | Rp10.500         | Tidak            |
| Listrik | Listrik | Rp2.500 (per kWh)| Ya               |

## Invariant

| No | Invariant                                                  | Cara Menjaganya                                                |
|----|------------------------------------------------------------|----------------------------------------------------------------|
| 1  | Umur kendaraan tidak pernah negatif                        | `Math.max(0, ...)` / `max(0, ...)` di `umur()`                 |
| 2  | Kapasitas tangki harus lebih dari 0                        | Validasi di constructor `Mobil`                                |
| 3  | Jumlah pengisian harus lebih dari 0                        | Validasi di `isiBahanBakar()`                                  |
| 4  | Isi tangki tidak melebihi kapasitas                        | Pemeriksaan `isiTangki + jumlah > kapasitas`                   |
| 5  | Jumlah gir sepeda harus lebih dari 0                       | Validasi di constructor `Sepeda`                               |
| 6  | Nilai tipe bahan bakar hanya yang terdaftar                | Enum, bukan konstanta `int`                                    |
| 7  | Biaya pengisian tidak boleh dihitung dari jumlah negatif   | Validasi di `biayaPengisian()`                                 |
| 8  | Nilai `NaN` tidak dianggap angka yang sah                  | `Double.isNaN` / `is_nan` dicek terpisah                       |

## Struktur Berkas

```
.
├── java/
│   ├── Movable.java
│   ├── Fuelable.java
│   ├── TipeBahanBakar.java
│   ├── Kendaraan.java
│   ├── Mobil.java
│   ├── Sepeda.java
│   └── Main.java
├── php/
│   ├── abstraksi.php      (interface, enum, trait, dan semua kelas)
│   └── main.php
├── keputusan.md
└── README.md
```

## Konsep Utama

### 1. Interface vs abstract class

| Aspek                | Interface                              | Abstract class                           |
|----------------------|----------------------------------------|------------------------------------------|
| Menjawab             | **Apa yang bisa** dilakukan            | **Apa** benda ini                        |
| Kode bersama         | Hampir tidak ada (kontrak)             | Ada (atribut dan method yang sama)       |
| Jumlah per kelas     | Boleh banyak                           | Hanya satu                               |
| Contoh di sini       | `Movable`, `Fuelable`                  | `Kendaraan`                              |

Sepeda bisa bergerak, tetapi tidak punya tangki. Karena itu `Sepeda` hanya `implements Movable`. Memaksanya memenuhi `Fuelable` berarti mengisi nilai palsu dan melanggar kontrak.

### 2. Satu `extends`, banyak `implements`

Java hanya mengizinkan satu induk class untuk menghindari *diamond problem* (dua induk dengan method sama tetapi implementasi berbeda). Interface aman dari masalah itu karena hanya berisi kontrak, sehingga satu kelas boleh memenuhi banyak kontrak sekaligus.

### 3. Default method (Java)

`Movable.ringkasanGerak()` punya implementasi bawaan yang memakai `kecepatanMaksimum()`:

```java
default String ringkasanGerak() {
    return String.format("kecepatan maksimum %.0f km/jam", kecepatanMaksimum());
}
```

`Mobil` dan `Sepeda` mendapatkannya tanpa menulis ulang. PHP tidak punya padanan ini pada interface.

### 4. Bergantung pada kontrak, bukan kelas konkret

```java
static void isiPenuh(Fuelable kendaraan) { ... }
```

Method ini tidak peduli `Mobil` atau kelas lain. Kelas baru yang `implements Fuelable` langsung bisa dipakai tanpa mengubah satu baris pun.

### 5. Penolakan saat kompilasi vs saat dijalankan

`isiPenuh(sepeda)` ditolak berbeda di kedua bahasa:

| Bahasa | Kapan ditolak           | Pesan                                                                  |
|--------|-------------------------|------------------------------------------------------------------------|
| Java   | Saat **kompilasi**      | `incompatible types: Sepeda cannot be converted to Fuelable`           |
| PHP    | Saat **baris dijalankan** | `TypeError: ... must be of type Fuelable, Sepeda given`              |

Java menangkap kesalahan sebelum program berjalan. Di PHP, kode yang tidak dieksekusi bisa menyembunyikan kesalahan yang sama.

### 6. Enum yang punya perilaku

Enum menggantikan `public static final int BENSIN = 1;` yang membiarkan angka 99 lolos. Enum juga boleh punya method (`biayaPengisian()`, `ramahLingkungan()`), hal yang tidak bisa dilakukan konstanta `int`. Di PHP, `match ($this)` tanpa `default` memaksa semua case ditangani (jika ada yang terlewat, muncul `UnhandledMatchError`).

### 7. Trait (khusus PHP)

`Loggable` disisipkan ke `Mobil` **dan** `Pesanan` yang tidak sekerabat. Itulah arti "penggunaan ulang horizontal": berbagi kode tanpa harus berbagi induk. `static::class` pada trait mengembalikan nama kelas pemakainya, sehingga `Mobil` mencetak `Mobil` dan `Pesanan` mencetak `Pesanan`.

## Cara Menjalankan

### Java

Prasyarat: **JDK 8 atau lebih baru** (default method).

```bash
cd java
javac -d out *.java
java -cp out Main
```

### PHP

Prasyarat: **PHP 8.1 atau lebih baru** (enum dan `readonly`).

```bash
cd php
php main.php
```

> `abstraksi.php` hanya boleh berisi kelas dan antarmuka. Jangan menaruh kode uji di dalamnya, karena `main.php` memuatnya dengan `require_once`. Kode uji di dua tempat menyebabkan `isiPenuh()` terdeklarasi dua kali dan keluaran tercetak dobel.

## Contoh Keluaran

**PHP** (jam pada dua baris terakhir mengikuti waktu Anda)

```
=== Semua Movable ===
  Toyota Avanza melaju di jalan raya
    kecepatan maksimum 180 km/jam
  Polygon Path dikayuh (21 gir)
    kecepatan maksimum 30 km/jam

=== Hanya yang Fuelable ===
  Diisi penuh Bensin — biaya Rp540.000

=== Enum punya perilaku ===
  Bensin   ramah lingkungan? tidak  biaya 10 satuan: Rp120.000
  Solar    ramah lingkungan? tidak  biaya 10 satuan: Rp105.000
  Listrik  ramah lingkungan? ya     biaya 10 satuan: Rp25.000

=== Trait dipakai kelas yang tidak sekerabat ===
[21:29:00] Mobil: servis berkala selesai
[21:29:00] Pesanan: pesanan #1042 dibuat
```

**Java** menghasilkan isi yang sama untuk tiga bagian pertama. Format angka mengikuti locale perangkat (misalnya `Rp540,000` pada en-US), dan bagian enum memakai `true` / `false`. Java tidak memiliki trait, sehingga bagian terakhir tidak ada.

### Rincian Perhitungan

| Item                       | Perhitungan            | Hasil       |
|----------------------------|------------------------|-------------|
| Isi penuh Avanza (bensin)  | 45 liter × Rp12.000    | Rp540.000   |
| 10 satuan bensin           | 10 × Rp12.000          | Rp120.000   |
| 10 satuan solar            | 10 × Rp10.500          | Rp105.000   |
| 10 kWh listrik             | 10 × Rp2.500           | Rp25.000    |

## Perbandingan Java vs PHP

| Konsep                    | Java                                     | PHP                                              |
|---------------------------|------------------------------------------|--------------------------------------------------|
| Interface                 | `public interface Movable`               | `interface Movable`                              |
| Memenuhi kontrak          | `implements Movable, Fuelable`           | `implements Movable, Fuelable`                   |
| Default method            | Ada (`default`)                          | Tidak ada pada interface                         |
| Enum                      | `enum TipeBahanBakar { BENSIN(...) }`    | `enum TipeBahanBakar: string { case Bensin }`    |
| Daftar semua nilai enum   | `TipeBahanBakar.values()`                | `TipeBahanBakar::cases()`                        |
| Perilaku per nilai enum   | Field + constructor, atau `switch`       | `match ($this) { ... }`                          |
| Penggunaan ulang horizontal | Tidak ada (interface + komposisi)      | `trait` dan `use`                                |
| Kelas tak bisa diwarisi   | `final class`                            | `final class`                                    |
| Cek enum                  | `this == LISTRIK`                        | `$this === self::Listrik`                        |
| Tipe salah dikirim ke fungsi | Error kompilasi                       | `TypeError` saat dijalankan                      |

## Catatan Desain

- **Kontrak sempit dan jujur:** `Fuelable` hanya dipenuhi benda yang memang punya tangki. Sistem tipe ikut menjaga kebenaran program.
- **`final` pada kelas konkret (PHP):** `Mobil`, `Sepeda`, dan `Pesanan` tidak dirancang untuk diwarisi.
- **`getMerek()` (Java):** `Mobil.bergerak()` memakai getter ini, jadi `Kendaraan` perlu menyediakannya.
- **`isiPenuh()` mengasumsikan tangki kosong:** method ini mengisi sebanyak kapasitas penuh. Jika tangki sudah terisi sebagian, `isiBahanBakar()` melempar exception karena total melebihi kapasitas.
- **Yang belum divalidasi:** `merek` kosong dan `tahun` yang tidak masuk akal pada `Kendaraan`, serta nomor pesanan yang kosong. Parameter `Sepeda` (merek, tahun, jumlah gir) adalah asumsi penulis karena soal tidak menetapkannya.