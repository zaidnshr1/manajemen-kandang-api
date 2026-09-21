# manajemen-kandang-api

Backend RESTful API berbasis Java & Spring Boot untuk sistem manajemen inventaris peternakan, operasional harian, dan pencatatan kesehatan/pakan dengan dukungan arsitektur multi-tenant (data isolation) dan autentikasi JWT.

---

## Teknologi

* **Language**: Java 17
* **Framework**: Spring Boot 4.1.1 (WebMVC, Data JPA, Security, Validation)
* **Database**: PostgreSQL
* **Database Migration**: Flyway
* **Security**: Spring Security, JJWT (io.jsonwebtoken 0.12.5)
* **Documentation**: Springdoc OpenAPI / Swagger UI 3.1.0
* **Testing**: JUnit 5, Mockito, MockMvc, H2 Database

---

## Fitur Utama

1. **Authentication & Authorization**: Stateless JWT (Access Token 15 menit & Refresh Token Rotation 7 hari).
2. **Data Isolation (Tenant Isolation)**: Pemfilteran data otomatis berbasis `owner_id` untuk mencegah pengaksesan lintas user.
3. **Master Data Management**: Pengelolaan data kandang, kategori ternak, dan pakan.
4. **Operasional & Inventaris**: Pencatatan populasi ternak, pengurangan populasi, riwayat penimbangan (ADG), log kesehatan, dan penggunaan pakan.
5. **Database Migration**: Skema versi otomatis menggunakan Flyway.
6. **API Documentation**: Integrasi Swagger UI interaktif dengan dukungan *Bearer Auth*.

---

## Cara Instalasi & Menjalankan
Prasyarat
* Java 17 atau lebih baru
* Maven 3.8+
* PostgreSQL 14+

1. git clone [https://github.com/username/manajemen-kandang-api.git](https://github.com/username/manajemen-kandang-api.git)  
   cd manajemen-kandang-api
2. Buat database PostgreSQL sesuai pada konfigurasi application.properties
3. Sesuaikan src/main/resources/application.properties  
   spring.datasource.url=jdbc:postgresql://localhost:5432/`db_name`  
   spring.datasource.username=`postgres`  
   spring.datasource.password=`postgres`  
   app.jwt.secret=`change_32_random_chars`  
   app.jwt.expiration-ms=`900000`  
   app.jwt.refresh-expiration-ms=`604800000`
4. Jalankan Aplikasi
   `mvn spring-boot:run`
5. Akses swagger UI `http://localhost:8080/swagger-ui.html`

---

## Testing
Proyek ini dilengkapi dengan Unit Test (Mockito) dan Integration Test (MockMvc & H2 In-Memory Database).  
Jalankan seluruh test suite dengan perintah:  
`mvn test`

## Skema Database (ERD)

```mermaid
erDiagram
    users {
        BIGSERIAL id PK
        VARCHAR_50 username UK
        VARCHAR_100 email UK
        VARCHAR_255 password
        VARCHAR_20 role
        TIMESTAMPTZ created_at
        TIMESTAMPTZ updated_at
        BIGINT owner_id
    }

    kandang {
        BIGSERIAL id PK
        VARCHAR_100 nama_kandang
        VARCHAR_50 kode_kandang
        INT kapasitas
        BIGINT owner_id FK
        TIMESTAMPTZ created_at
        TIMESTAMPTZ updated_at
    }

    kategori_ternak {
        BIGSERIAL id PK
        VARCHAR_20 nama_kategori
        VARCHAR_20 jenis_satuan
        VARCHAR_20 tipe_pencatatan
        BIGINT owner_id FK
        TIMESTAMPTZ created_at
        TIMESTAMPTZ updated_at
    }

    pakan {
        BIGSERIAL id PK
        VARCHAR_100 nama_pakan
        VARCHAR_25 satuan
        NUMERIC_10_2 stok
        NUMERIC_12_2 harga_per_satuan
        BIGINT owner_id FK
        TIMESTAMPTZ created_at
        TIMESTAMPTZ updated_at
    }

    ternak {
        BIGSERIAL id PK
        VARCHAR_50 kode_tag
        BIGINT kategori_id FK
        BIGINT kandang_id FK
        DATE tanggal_masuk
        INT jumlah_populasi
        NUMERIC_8_2 bobot_awal
        VARCHAR_20 status
        BIGINT owner_id FK
        TIMESTAMPTZ created_at
        TIMESTAMPTZ updated_at
    }

    log_kesehatan {
        BIGSERIAL id PK
        BIGINT ternak_id FK
        DATE tanggal
        VARCHAR_30 jenis_kejadian
        INT jumlah_terdampak
        TEXT catatan
        NUMERIC_12_2 biaya
        BIGINT owner_id FK
        TIMESTAMPTZ created_at
        TIMESTAMPTZ updated_at
    }

    log_pemberian_pakan {
        BIGSERIAL id PK
        BIGINT pakan_id FK
        BIGINT kandang_id FK
        NUMERIC_10_2 jumlah_pakai
        TIMESTAMP tanggal
        BIGINT owner_id FK
        TIMESTAMPTZ created_at
        TIMESTAMPTZ updated_at
    }

    log_penimbangan {
        BIGSERIAL id PK
        BIGINT ternak_id FK
        DATE tanggal_timbang
        NUMERIC_8_2 bobot
        BIGINT owner_id FK
        TIMESTAMPTZ created_at
        TIMESTAMPTZ updated_at
    }

    refresh_tokens {
        BIGSERIAL id PK
        BIGINT user_id FK
        VARCHAR_255 token UK
        TIMESTAMPTZ expiry_date
        TIMESTAMPTZ created_at
        TIMESTAMPTZ updated_at
        BIGINT owner_id
    }

    %% Relasi Kepemilikan Data (Multi-tenancy / Owner)
    users ||--o{ kandang : "owns"
    users ||--o{ kategori_ternak : "owns"
    users ||--o{ pakan : "owns"
    users ||--o{ ternak : "owns"
    users ||--o{ log_kesehatan : "owns"
    users ||--o{ log_pemberian_pakan : "owns"
    users ||--o{ log_penimbangan : "owns"

    %% Relasi Autentikasi
    users ||--o| refresh_tokens : "has"

    %% Relasi Operasional Peternakan
    kategori_ternak ||--o{ ternak : "categorizes"
    kandang ||--o{ ternak : "houses"
    ternak ||--o{ log_kesehatan : "logs"
    ternak ||--o{ log_penimbangan : "logs"
    pakan ||--o{ log_pemberian_pakan : "used_in"
    kandang ||--o{ log_pemberian_pakan : "receives"
```
