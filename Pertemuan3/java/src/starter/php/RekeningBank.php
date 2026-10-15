<?php
declare(strict_types=1);

/**
 * Sesi 3 — PHP tidak punya constructor overloading.
 * Padanannya: default parameter + named constructor (static factory).
 */
class RekeningBank
{
    // TODO 1: konstanta bernama pengganti angka ajaib
    public const float BUNGA_TAHUNAN = 0.025;
    public const float BIAYA_ADMIN   = 5000.0;
    public const float BATAS_TARIK   = 5000000.0;

    // TODO 2: penghitung jumlah rekening (static, privat, awal 0)
    private static int $jumlahRekening = 0;

    private float $saldo;

    /**
     * Default parameter menggantikan constructor overloading.
     * TODO 3: validasi nomor kosong dan saldo awal negatif.
     * TODO 4: naikkan penghitung jumlah rekening.
     */
    public function __construct(
        private readonly string $nomor,
        private readonly string $pemilik,
        float $saldoAwal = 0,
    ) {
        // TODO 3
        if (trim($nomor) === '') {
            throw new InvalidArgumentException('Nomor rekening tidak boleh kosong');
        }
        if (is_nan($saldoAwal) || $saldoAwal < 0) {
            throw new InvalidArgumentException(
                'Saldo awal tidak boleh negatif, tetapi diterima: ' . $saldoAwal
            );
        }

        $this->saldo = $saldoAwal;

        // TODO 4: dinaikkan setelah validasi lolos, jadi percobaan yang gagal tidak terhitung
        self::$jumlahRekening++;
    }

    /**
     * TODO 5: named constructor — rekening pelajar, saldo awal nol.
     *         `new static()` memakai late static binding: jika dipanggil lewat subclass,
     *         yang dibuat adalah objek subclass itu. `new self()` selalu membuat RekeningBank.
     */
    public static function rekeningPelajar(string $nomor, string $pemilik): static
    {
        return new static($nomor, $pemilik, 0);
    }

    public function setor(float $jumlah): void
    {
        // TODO 6
        if (is_nan($jumlah) || $jumlah <= 0) {
            throw new InvalidArgumentException(
                'Jumlah setoran harus lebih dari 0, tetapi diterima: ' . $jumlah
            );
        }
        $this->saldo += $jumlah;
    }

    public function tarik(float $jumlah): void
    {
        // TODO 7: tolak <= 0, tolak melebihi batas sekali tarik, tolak melebihi saldo
        if (is_nan($jumlah) || $jumlah <= 0) {
            throw new InvalidArgumentException(
                'Jumlah penarikan harus lebih dari 0, tetapi diterima: ' . $jumlah
            );
        }
        if ($jumlah > self::BATAS_TARIK) {
            throw new InvalidArgumentException(
                'Penarikan ' . $jumlah . ' melebihi batas sekali transaksi ' . self::BATAS_TARIK
            );
        }
        if ($jumlah > $this->saldo) {
            throw new InvalidArgumentException(
                'Saldo tidak cukup: saldo ' . $this->saldo . ', penarikan ' . $jumlah
            );
        }
        $this->saldo -= $jumlah;
    }

    /** TODO 8: kurangi saldo sebesar biaya admin, tetapi jangan sampai negatif. */
    public function potongBiayaAdmin(): void
    {
        $this->saldo = max(0.0, $this->saldo - self::BIAYA_ADMIN);
    }

    /** TODO 9: jumlah rekening yang pernah dibuat. */
    public static function getJumlahRekening(): int
    {
        return self::$jumlahRekening;
    }

    /** TODO 10: bunga setahun dari pokok; tidak membaca keadaan objek, jadi static. */
    public static function bungaSetahun(float $pokok): float
    {
        return $pokok * self::BUNGA_TAHUNAN;
    }

    public function getSaldo(): float { return $this->saldo; }
    public function getNomor(): string { return $this->nomor; }

    public function __toString(): string
    {
        return sprintf('Rekening[%s] %-14s Rp%s',
            $this->nomor, $this->pemilik, number_format($this->saldo, 2, ',', '.'));
    }
}

// Demo singkat (opsional, boleh dihapus)
$a = new RekeningBank('001', 'Rakha');
$b = new RekeningBank('002', 'Budi', 1000000);
$c = RekeningBank::rekeningPelajar('003', 'Siti');

$a->setor(200000);
$b->tarik(250000);
$b->potongBiayaAdmin();

echo $a, PHP_EOL;
echo $b, PHP_EOL;
echo $c, PHP_EOL;
echo 'Jumlah rekening : ', RekeningBank::getJumlahRekening(), PHP_EOL;
echo 'Bunga 1 juta    : ', RekeningBank::bungaSetahun(1000000), PHP_EOL;

try {
    $b->tarik(6000000);
} catch (InvalidArgumentException $e) {
    echo 'Ditolak: ', $e->getMessage(), PHP_EOL;
}
try {
    new RekeningBank('', 'Tanpa Nomor');
} catch (InvalidArgumentException $e) {
    echo 'Ditolak: ', $e->getMessage(), PHP_EOL;
}
echo 'Jumlah rekening : ', RekeningBank::getJumlahRekening(), ' (tetap 3)', PHP_EOL;