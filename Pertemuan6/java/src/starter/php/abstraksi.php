<?php
declare(strict_types=1);

// ══ INTERFACE — kontrak "apa yang bisa dilakukan" ═══════════════
interface Movable
{
    public function bergerak(): void;
    public function kecepatanMaksimum(): float;
}

interface Fuelable
{
    public function isiBahanBakar(float $jumlah): void;
    public function kapasitasTangki(): float;
    public function tipeBahanBakar(): TipeBahanBakar;
}

// ══ ENUM (PHP 8.1+) — backed enum, punya nilai string ══════════
enum TipeBahanBakar: string
{
    case Bensin  = 'bensin';
    case Solar   = 'solar';
    case Listrik = 'listrik';   // TODO 1 (Langkah 2)

    /** TODO 2: label yang enak dibaca. */
    public function label(): string
    {
        return match ($this) {
            self::Bensin  => 'Bensin',
            self::Solar   => 'Solar',
            self::Listrik => 'Listrik',
        };
    }

    /** TODO 3: Bensin 12000 · Solar 10500 · Listrik 2500 */
    public function hargaPerSatuan(): float
    {
        return match ($this) {
            self::Bensin  => 12000.0,
            self::Solar   => 10500.0,
            self::Listrik => 2500.0,
        };
    }

    /** TODO 4 */
    public function biayaPengisian(float $jumlah): float
    {
        if (is_nan($jumlah) || $jumlah < 0) {
            throw new InvalidArgumentException(
                'Jumlah pengisian tidak boleh negatif, tetapi diterima: ' . $jumlah
            );
        }
        return $jumlah * $this->hargaPerSatuan();
    }

    /** TODO 5: hanya Listrik yang ramah lingkungan. */
    public function ramahLingkungan(): bool
    {
        return $this === self::Listrik;
    }
}

// ══ TRAIT — penggunaan ulang horizontal, khas PHP ══════════════
trait Loggable
{
    /**
     * TODO 6: cetak baris log berformat:
     *         [14:32:05] Mobil: servis berkala selesai
     */
    public function log(string $pesan): void
    {
        printf("[%s] %s: %s%s", date('H:i:s'), static::class, $pesan, PHP_EOL);
    }
}

// ══ ABSTRACT CLASS — kode yang benar-benar sama ═══════════════
abstract class Kendaraan
{
    public function __construct(
        protected readonly string $merek,
        protected readonly int    $tahun,
    ) {}

    /** TODO 7: umur kendaraan, tidak boleh negatif. */
    public function umur(int $tahunSekarang): int
    {
        return max(0, $tahunSekarang - $this->tahun);
    }

    abstract public function jumlahRoda(): int;

    public function __toString(): string
    {
        return sprintf('%s (%d, %d roda)', $this->merek, $this->tahun, $this->jumlahRoda());
    }
}

final class Mobil extends Kendaraan implements Movable, Fuelable
{
    use Loggable;                       // trait disisipkan

    private float $isiTangki = 0;

    public function __construct(string $merek, int $tahun, private readonly float $kapasitas)
    {
        parent::__construct($merek, $tahun);

        if (is_nan($kapasitas) || $kapasitas <= 0) {
            throw new InvalidArgumentException(
                'Kapasitas tangki harus lebih dari 0, tetapi diterima: ' . $kapasitas
            );
        }
    }

    public function jumlahRoda(): int { return 4; }

    // TODO 8: lengkapi kontrak Movable dan Fuelable.
    public function bergerak(): void
    {
        echo "  {$this->merek} melaju di jalan raya", PHP_EOL;
    }

    public function kecepatanMaksimum(): float { return 180; }

    public function isiBahanBakar(float $jumlah): void
    {
        if (is_nan($jumlah) || $jumlah <= 0) {
            throw new InvalidArgumentException(
                'Jumlah bahan bakar harus lebih dari 0, tetapi diterima: ' . $jumlah
            );
        }
        if ($this->isiTangki + $jumlah > $this->kapasitas) {
            throw new InvalidArgumentException(
                "Melebihi kapasitas tangki: isi {$this->isiTangki} + $jumlah > kapasitas {$this->kapasitas}"
            );
        }
        $this->isiTangki += $jumlah;
    }

    public function kapasitasTangki(): float { return $this->kapasitas; }
    public function tipeBahanBakar(): TipeBahanBakar { return TipeBahanBakar::Bensin; }
    public function getIsiTangki(): float { return $this->isiTangki; }
}

// TODO Langkah 4: Sepeda — extends Kendaraan implements Movable, BUKAN Fuelable.
final class Sepeda extends Kendaraan implements Movable
{
    public function __construct(string $merek, int $tahun, private readonly int $jumlahGir)
    {
        parent::__construct($merek, $tahun);

        if ($jumlahGir <= 0) {
            throw new InvalidArgumentException(
                'Jumlah gir harus lebih dari 0, tetapi diterima: ' . $jumlahGir
            );
        }
    }

    public function jumlahRoda(): int { return 2; }

    public function bergerak(): void
    {
        echo "  {$this->merek} dikayuh ({$this->jumlahGir} gir)", PHP_EOL;
    }

    public function kecepatanMaksimum(): float { return 30; }
}

/**
 * TODO Langkah 5: Pesanan juga memakai trait Loggable.
 * Kelas ini sama sekali bukan kerabat Kendaraan — itulah maksud
 * "penggunaan ulang horizontal".
 */
final class Pesanan
{
    use Loggable;
}