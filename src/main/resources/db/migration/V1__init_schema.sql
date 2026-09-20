CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ,
    owner_id BIGINT
);

INSERT INTO users (username, email, password, role, created_at)
VALUES ('system', 'system@peternakan.com', '$2a$10$xyz', 'ROLE_ADMIN', NOW());

CREATE TABLE kandang (
    id BIGSERIAL PRIMARY KEY,
    nama_kandang VARCHAR(100) NOT NULL,
    kode_kandang VARCHAR(50) NOT NULL,
    kapasitas INT NOT NULL,
    owner_id BIGINT NOT NULL,
    created_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ,

    CONSTRAINT fk_kandang_owner FOREIGN KEY (owner_id) REFERENCES users(id)
);

CREATE TABLE kategori_ternak (
    id BIGSERIAL PRIMARY KEY,
    nama_kategori VARCHAR(20) NOT NULL,
    jenis_satuan VARCHAR(20) NOT NULL,
    tipe_pencatatan VARCHAR(20) NOT NULL,
    owner_id BIGINT NOT NULL,
    created_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ,

    CONSTRAINT fk_kategori_owner FOREIGN KEY (owner_id) REFERENCES users(id)
);

CREATE TABLE pakan (
    id BIGSERIAL PRIMARY KEY,
    nama_pakan VARCHAR(100) NOT NULL,
    satuan VARCHAR(25) NOT NULL,
    stok NUMERIC(10, 2) NOT NULL,
    harga_per_satuan NUMERIC(12, 2) NOT NULL,
    owner_id BIGINT NOT NULL,
    created_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ,

    CONSTRAINT fk_pakan_owner FOREIGN KEY (owner_id) REFERENCES users(id)
);

CREATE TABLE ternak (
    id BIGSERIAL PRIMARY KEY,
    kode_tag VARCHAR(50),
    kategori_id BIGINT NOT NULL,
    kandang_id BIGINT NOT NULL,
    tanggal_masuk DATE NOT NULL,
    jumlah_populasi INT NOT NULL,
    bobot_awal NUMERIC(8, 2),
    status VARCHAR(20),
    owner_id BIGINT NOT NULL,
    created_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ,

    CONSTRAINT fk_ternak_kategori FOREIGN KEY (kategori_id) REFERENCES kategori_ternak(id),
    CONSTRAINT fk_ternak_kandang FOREIGN KEY (kandang_id) REFERENCES kandang(id),
    CONSTRAINT fk_ternak_owner FOREIGN KEY (owner_id) REFERENCES users(id)
);

CREATE TABLE log_kesehatan (
    id BIGSERIAL PRIMARY KEY,
    ternak_id BIGINT NOT NULL,
    tanggal DATE NOT NULL,
    jenis_kejadian VARCHAR(30) NOT NULL,
    jumlah_terdampak INT NOT NULL,
    catatan TEXT,
    biaya NUMERIC(12, 2),
    owner_id BIGINT NOT NULL,
    created_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ,

    CONSTRAINT fk_log_kesehatan_ternak FOREIGN KEY (ternak_id) REFERENCES ternak(id),
    CONSTRAINT fk_log_kesehatan_owner FOREIGN KEY (owner_id) REFERENCES users(id)
);

CREATE TABLE log_pemberian_pakan (
    id BIGSERIAL PRIMARY KEY,
    pakan_id BIGINT NOT NULL,
    kandang_id BIGINT NOT NULL,
    jumlah_pakai NUMERIC(10, 2) NOT NULL,
    tanggal TIMESTAMP NOT NULL,
    owner_id BIGINT NOT NULL,
    created_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ,

    CONSTRAINT fk_log_pemberian_pakan_pakan FOREIGN KEY (pakan_id) REFERENCES pakan(id),
    CONSTRAINT fk_log_pemberian_pakan_kandang FOREIGN KEY (kandang_id) REFERENCES kandang(id),
    CONSTRAINT fk_log_pemberian_pakan_owner FOREIGN KEY (owner_id) REFERENCES users(id)
);

CREATE TABLE log_penimbangan (
    id BIGSERIAL PRIMARY KEY,
    ternak_id BIGINT NOT NULL,
    tanggal_timbang DATE NOT NULL,
    bobot NUMERIC(8, 2) NOT NULL,
    owner_id BIGINT NOT NULL,
    created_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ,

    CONSTRAINT fk_log_penimbangan_ternak FOREIGN KEY (ternak_id) REFERENCES ternak(id),
    CONSTRAINT fk_log_penimbangan_owner FOREIGN KEY (owner_id) REFERENCES users(id)
);

CREATE TABLE refresh_tokens (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    token VARCHAR(255) NOT NULL UNIQUE,
    expiry_date TIMESTAMPTZ NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ,
    owner_id BIGINT NOT NULL,
    CONSTRAINT fk_refresh_tokens_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_penimbangan_ternak_tanggal ON log_penimbangan (ternak_id, tanggal_timbang DESC)