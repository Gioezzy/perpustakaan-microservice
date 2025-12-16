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
- **CQRS Pattern** untuk Peminjaman (Command & Query Separation)
- Pengembalian buku & perhitungan denda 
- Service untuk menangani message-broker dengan RabbitMQ 
- **Centralized Logging** dengan ELK Stack

---

## 📂 Struktur Project
```
perpustakaan-microservice/
│── eureka-server/                     # Service registry
│── api-gateway-pustaka/               # API Gateway
│── anggota-service/                   # Service untuk data anggota
│── buku-service/                      # Service untuk data buku
│── peminjaman-command-service/        # Service write/command peminjaman (CQRS)
│── peminjaman-query-service/          # Service read/query peminjaman (CQRS)
│── pengembalian-service/              # Service untuk data pengembalian
│── rabbitmq-peminjaman-service/       # Library consumer untuk peminjaman
│── rabbitmq-pengembalian-service/     # Library consumer untuk pengembalian
└── README.md
```

---

## 🐳 Cara Menjalankan dengan Docker (Disarankan)
Sangat praktis, cukup satu perintah untuk menjalankan semua layanan termasuk database dan monitoring.

1.  **Build & Run Semua Service**:
    ```bash
    docker-compose up -d --build
    ```
    *Perintah ini akan mendownload base image, membuild aplikasi, dan menjalankan 8 container sekaligus.*

2.  **Akses Aplikasi**:
    *   **API Gateway**: `http://localhost:9000`
    *   **Eureka Dashboard**: `http://localhost:8761`
    *   **RabbitMQ Dashboard**: `http://localhost:15672` (User: `admin`, Pass: `password`)

---

## 📊 Monitoring (ELK Stack)
Project ini sudah dilengkapi dengan **Elasticsearch, Logstash, dan Kibana** untuk sentralisasi log.

1.  **Akses Kibana**: Buka `http://localhost:5601`
2.  **Setup Awal**:
    *   Masuk ke **Stack Management** > **Index Patterns**.
    *   Buat index pattern baru dengan nama: `microservices-logs-*`.
    *   Pilih `@timestamp` sebagai filter waktu.
3.  **Lihat Log**: Masuk ke menu **Discover** untuk melihat log dari semua service secara real-time.

---

## ▶️ Cara Menjalankan Manual (Tanpa Docker)
1. Jalankan **Eureka Server**  
   ```bash
   mvn spring-boot:run -pl eureka-server
   ```
2. Jalankan **API Gateway**  
   ```bash
   mvn spring-boot:run -pl api-gateway-pustaka
   ```
3. Jalankan service utama:  
   ```bash
   mvn spring-boot:run -pl anggota-service
   mvn spring-boot:run -pl buku-service
   mvn spring-boot:run -pl peminjaman-command-service
   mvn spring-boot:run -pl peminjaman-query-service
   mvn spring-boot:run -pl pengembalian-service
   ```

4. Jalankan service RabbitMQ
    ```bash
    mvn spring-boot:run -pl rabbitmq-peminjaman-service
    mvn spring-boot:run -pl rabbitmq-pengembalian-service
    ```
---

## 📌 Endpoint & Contoh Request

### 🌐 Akses Melalui API Gateway (Direkomendasikan)
**Base URL: `http://localhost:9000/`**

Semua endpoint dapat diakses melalui API Gateway tanpa perlu mengingat port masing-masing service:

| Service | Endpoint Gateway | Service Asli |
|---------|------------------|--------------|
| Anggota | `http://localhost:9000/api/anggota/**` | `http://localhost:8081/` |
| Buku | `http://localhost:9000/api/buku/**` | `http://localhost:8082/` |
| Peminjaman (Cmd) | `http://localhost:9000/api/peminjaman/command/**` | `http://localhost:8087/` |
| Peminjaman (Qry) | `http://localhost:9000/api/peminjaman/query/**` | `http://localhost:8088/` |
| Pengembalian | `http://localhost:9000/api/pengembalian/**` | `http://localhost:8084/` |

---

### 1. Anggota Service  
Base URL `http://localhost:8081/` (atau melalui gateway: `http://localhost:9000/api/anggota/`)
| Method | Endpoint                  | Deskripsi                        |
|--------|---------------------------|----------------------------------|
| POST   | `/api/anggota`              | Membuat anggota baru               |
| GET    | `/api/anggota`              | Mendapatkan semua anggota          |
| GET    | `/api/anggota/{id}`         | Mendapatkan anggota berdasarkan ID |
| DELETE | `/api/anggota/{id}`         | Menghapus anggota berdasarkan ID   |
| GET    | `/api/anggota/detail/{id}` | Mendapatkan anggota + detail by ID |

---

### 2. Buku Service  
Base URL `http://localhost:8082/` (atau melalui gateway: `http://localhost:9000/api/buku/`)
| Method | Endpoint                  | Deskripsi                        |
|--------|---------------------------|----------------------------------|
| POST   | `/api/buku`              | Membuat buku baru               |
| GET    | `/api/buku`              | Mendapatkan semua buku          |
| GET    | `/api/buku/{id}`         | Mendapatkan buku berdasarkan ID |
| DELETE | `/api/buku/{id}`         | Menghapus buku berdasarkan ID   |
| GET    | `/api/buku/detail/{id}` | Mendapatkan buku + detail by ID |

---

### 3. Peminjaman Service (CQRS Pattern)

#### A. Command Service (Write Operations)
Base URL `http://localhost:8087/` (Gateway: `/api/peminjaman/command/`)
| Method | Endpoint                  | Deskripsi                        |
|--------|---------------------------|----------------------------------|
| POST   | `/api/peminjaman/command` | **(Create)** Membuat peminjaman baru |
| DELETE | `/api/peminjaman/command/{id}` | **(Delete)** Menghapus peminjaman |

#### B. Query Service (Read Operations)
Base URL `http://localhost:8088/` (Gateway: `/api/peminjaman/query/`)
| Method | Endpoint                  | Deskripsi                        |
|--------|---------------------------|----------------------------------|
| GET    | `/api/peminjaman/query`   | **(Read)** Mendapatkan semua peminjaman |
| GET    | `/api/peminjaman/query/{id}/detail` | **(Read)** Mendapatkan detail peminjaman |

---

### 4. Pengembalian Service  
Base URL `http://localhost:8084/` (atau melalui gateway: `http://localhost:9000/api/pengembalian/`)
| Method | Endpoint                  | Deskripsi                        |
|--------|---------------------------|----------------------------------|
| POST   | `/api/pengembalian`              | Membuat pengembalian baru               |
| GET    | `/api/pengembalian`              | Mendapatkan semua pengembalian          |
| GET    | `/api/pengembalian/{id}`         | Mendapatkan pengembalian berdasarkan ID |
| DELETE | `/api/pengembalian/{id}`         | Menghapus pengembalian berdasarkan ID   |
| GET    | `/api/pengembalian/detail/{id}` | Mendapatkan pengembalian + detail by ID |

---

## 🛠️ Teknologi yang Dipakai
- **Java 17**  
- **Spring Boot 3.5.5**  
- **Spring Cloud Netflix Eureka**  
- **Spring Cloud Gateway**  
- **Spring Data JPA**  
- **RabbitMQ**
- **H2 Database**  
- **ELK Stack** (Elasticsearch, Logstash, Kibana)
- **Maven**  
