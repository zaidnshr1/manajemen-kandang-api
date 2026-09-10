CREATE TABLE kandang (
    id BIGSERIAL PRIMARY KEY,
    nama_kandang VARCHAR(100) NOT NULL,
    kode_kandang VARCHAR(50) NOT NULL,
    kapasitas INT NOT NULL,
    owner_id BIGINT,
    created_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ
);

CREATE TABLE kategori_ternak (
    id BIGSERIAL PRIMARY KEY,
    nama_kategori VARCHAR(20) NOT NULL,
    jenis_satuan VARCHAR(20) NOT NULL,
    tipe_pencatatan VARCHAR(20) NOT NULL,
    owner_id BIGINT,
    created_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ
);

CREATE TABLE pakan (
    id BIGSERIAL PRIMARY KEY,
    nama_pakan VARCHAR(100) NOT NULL,
    satuan VARCHAR(25) NOT NULL,
    stok NUMERIC(10, 2) NOT NULL,
    harga_per_satuan NUMERIC(12, 2) NOT NULL,
    owner_id BIGINT,
    created_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ
);

CREATE TABLE ternak (
    id BIGSERIAL PRIMARY KEY,
    kode_tag VARCHAR(50),
    kategori_id BIGINT NOT NULL REFERENCES kategori_ternak(id),
    kandang_id BIGINT NOT NULL REFERENCES kandang(id),
    tanggal_masuk DATE NOT NULL,
    jumlah_populasi INT NOT NULL,
    bobot_awal NUMERIC(8, 2),
    status VARCHAR(20),
    owner_id BIGINT,
    created_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ
);

CREATE TABLE log_kesehatan (
    id BIGSERIAL PRIMARY KEY,
    ternak_id BIGINT NOT NULL REFERENCES ternak(id),
    tanggal DATE NOT NULL,
    jenis_kejadian VARCHAR(30) NOT NULL,
    jumlah_terdampak INT NOT NULL,
    catatan TEXT,
    biaya NUMERIC(12, 2),
    owner_id BIGINT,
    created_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ
);

CREATE TABLE log_pemberian_pakan (
    id BIGSERIAL PRIMARY KEY,
    pakan_id BIGINT NOT NULL REFERENCES pakan(id),
    kandang_id BIGINT NOT NULL REFERENCES kandang(id),
    jumlah_pakai NUMERIC(10, 2) NOT NULL,
    tanggal TIMESTAMP NOT NULL,
    owner_id BIGINT,
    created_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ
);

CREATE TABLE log_penimbangan (
    id BIGSERIAL PRIMARY KEY,
    ternak_id BIGINT NOT NULL REFERENCES ternak(id),
    tanggal_timbang DATE NOT NULL,
    bobot NUMERIC(8, 2) NOT NULL,
    owner_id BIGINT,
    created_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ
);

CREATE INDEX idx_penimbangan_ternak_tanggal ON log_penimbangan (ternak_id, tanggal_timbang DESC)