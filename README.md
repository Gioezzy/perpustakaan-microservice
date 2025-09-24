# 📚 Perpustakaan Microservice

## 📝 Deskripsi Singkat
Proyek ini adalah sistem **manajemen perpustakaan** berbasis **arsitektur microservices**.  
Setiap layanan berjalan independen dan saling berkomunikasi melalui **API Gateway** dengan **Eureka Discovery Server**.  
Tujuannya adalah untuk mengelola data anggota, buku, peminjaman, dan pengembalian dengan skala yang lebih fleksibel.

---

## ✨ Fitur
- Registrasi & discovery service dengan **Eureka**  
- Routing request dengan **API Gateway**  
- CRUD untuk data **Anggota**  
- CRUD untuk data **Buku**  
- Peminjaman buku oleh anggota  
- Pengembalian buku & perhitungan denda  

---

## 📂 Struktur Project
```
perpustakaan-microservice/
│── eureka-server/              # Service registry
│── api-gateway-pustaka/        # API Gateway
│── anggota-service/            # Service untuk data anggota
│── buku-service/               # Service untuk data buku
│── peminjaman-service/         # Service untuk data peminjaman
│── pengembalian-service/       # Service untuk data pengembalian
└── README.md
```

---

## ▶️ Cara Menjalankan
1. Jalankan **Eureka Server**  
   ```bash
   mvn spring-boot:run -pl eureka-server
   ```
2. Jalankan **API Gateway**  
   ```bash
   mvn spring-boot:run -pl api-gateway-pustaka
   ```
3. Jalankan service lainnya:  
   ```bash
   mvn spring-boot:run -pl anggota-service
   mvn spring-boot:run -pl buku-service
   mvn spring-boot:run -pl peminjaman-service
   mvn spring-boot:run -pl pengembalian-service
   ```

---

## 📌 Endpoint & Contoh Request

### 1. Anggota Service  
Base URL `http://localhost:8081/`
| Method | Endpoint                  | Deskripsi                        |
|--------|---------------------------|----------------------------------|
| POST   | `/api/anggota`              | Membuat anggota baru               |
| GET    | `/api/anggota`              | Mendapatkan semua anggota          |
| GET    | `/api/anggota/{id}`         | Mendapatkan anggota berdasarkan ID |
| DELETE | `/api/anggota/{id}`         | Menghapus anggota berdasarkan ID   |
| GET    | `/api/anggota/product/{id}` | Mendapatkan anggota + product by ID |


```json
{
  "nim": "20250101",
  "nama": "Giovanni Yuda",
  "alamat": "Jl. Merdeka No. 123",
  "jenis_kelamin": "Laki-laki"
}
```

---

### 2. Buku Service  
Base URL `http://localhost:8082/`
| Method | Endpoint                  | Deskripsi                        |
|--------|---------------------------|----------------------------------|
| POST   | `/api/buku`              | Membuat buku baru               |
| GET    | `/api/buku`              | Mendapatkan semua buku          |
| GET    | `/api/buku/{id}`         | Mendapatkan buku berdasarkan ID |
| DELETE | `/api/buku/{id}`         | Menghapus buku berdasarkan ID   |
| GET    | `/api/buku/product/{id}` | Mendapatkan buku + product by ID |

```json
{
  "judul": "Belajar Spring Boot",
  "pengarang": "Andi Santoso",
  "penerbit": "Informatika",
  "tahun_terbit": "2025"
}
```

---

### 3. Peminjaman Service  
Base URL `http://localhost:8083/`
| Method | Endpoint                  | Deskripsi                        |
|--------|---------------------------|----------------------------------|
| POST   | `/api/peminjaman`              | Membuat peminjaman baru               |
| GET    | `/api/peminjaman`              | Mendapatkan semua peminjaman          |
| GET    | `/api/peminjaman/{id}`         | Mendapatkan peminjaman berdasarkan ID |
| DELETE | `/api/peminjaman/{id}`         | Menghapus peminjaman berdasarkan ID   |
| GET    | `/api/peminjaman/product/{id}` | Mendapatkan peminjaman + product by ID |

```json
{
  "bukuId": 1,
  "anggotaId": 1,
  "tanggalPinjam": "2025-09-24",
  "tanggalKembali": "2025-10-01"
}
```

---

### 4. Pengembalian Service  
Base URL `http://localhost:8084/`
| Method | Endpoint                  | Deskripsi                        |
|--------|---------------------------|----------------------------------|
| POST   | `/api/pengembalian`              | Membuat pengembalian baru               |
| GET    | `/api/pengembalian`              | Mendapatkan semua pengembalian          |
| GET    | `/api/pengembalian/{id}`         | Mendapatkan pengembalian berdasarkan ID |
| DELETE | `/api/pengembalian/{id}`         | Menghapus pengembalian berdasarkan ID   |
| GET    | `/api/pengembalian/product/{id}` | Mendapatkan pengembalian + product by ID |

```json
{
  "peminjamanId": 1,
  "tanggalDikembalikan": "2025-10-02",
  "terlambat": "1 hari",
  "denda": 5000.0
}
```

---

## 🛠️ Teknologi yang Dipakai
- **Java 17**  
- **Spring Boot 3.5.5**  
- **Spring Cloud Netflix Eureka**  
- **Spring Cloud Gateway**  
- **Spring Data JPA**  
- **H2 Database**  
- **Maven**  
