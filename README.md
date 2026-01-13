# Perpustakaan Microservices System

Sistem Manajemen Perpustakaan berbasis **Microservices Architecture** menggunakan **Spring Boot**, **Spring Cloud**, **RabbitMQ**, dan **Docker**. Sistem ini menerapkan pola **CQRS** (Command Query Responsibility Segregation) untuk modul Peminjaman dan komunikasi *Event-Driven* untuk notifikasi.

---

## 🏗️ Arsitektur & Teknologi

### Topologi Sistem
*   **Service Discovery**: Eureka Server (Naming Server).
*   **Gateway**: API Gateway (Entry Point & Load Balancer).
*   **Message Broker**: RabbitMQ (Asynchronous Communication).
*   **Database**: H2 Database (File-based per microservice).
*   **CI/CD**: Jenkins.
*   **Monitoring**: ELK Stack (Elasticsearch, Logstash, Kibana).

### Daftar Microservices
| Service Name | Port | Deskripsi |
| :--- | :--- | :--- |
| **Api Gateway** | `9000` | Pintu masuk utama untuk Client. |
| **Server Eureka** | `8761` | Service Registry & Discovery. |
| **Anggota Service** | `8081` | CRUD Data Anggota. |
| **Buku Service** | `8082` | CRUD Data Buku. |
| **Peminjaman Command** | `8087` | (Write) Mencatat peminjaman & publish event. |
| **Peminjaman Query** | `8088` | (Read) Menyimpan data denormalisasi untuk report. |
| **Pengembalian Service** | `8084` | Mencatat pengembalian & hitung denda. |
| **RabbitMQ Peminjaman** | `8085` | Consumer event peminjaman (Audit/Notif). |
| **RabbitMQ Pengembalian** | `8086` | Consumer event pengembalian (Kirim Email). |

---

## 🚀 Langkah-Langkah (How to Run)

### 1. Prasyarat
*   Docker & Docker Compose terinstall.
*   RAM minimal 4GB free (Microservices di-limit ~4GB total).

### 2. Menjalankan Aplikasi (Docker Compose)
Cara paling mudah menjalankan seluruh sistem:

```bash
# Build & Run semua service
docker-compose up -d --build
```

Tunggu beberapa menit hingga semua container sehat (`healthy`). Cek status:
```bash
docker-compose ps
```

### 3. Akses Aplikasi
*   **API Gateway**: `http://localhost:9000`
*   **Eureka Dashboard**: `http://localhost:8761`
*   **Jenkins**: `http://localhost:8080`
*   **Kibana (Logs)**: `http://localhost:5601`
*   **RabbitMQ Management**: `http://localhost:15672` (User: `admin`, Pass: `password`)

---

## 🛠️ File Konfigurasi

### 1. Docker Compose (`docker-compose.yml`)
Konfigurasi orkestrasi container.
*   **Optimasi Resource**: Setiap service Java dibatasi RAM-nya menggunakan environment variable:
    ```yaml
    environment:
      JAVA_TOOL_OPTIONS: "-Xms64m -Xmx128m"  # Max Heap 128MB
    mem_limit: 200m                          # Wadah container max 200MB
    ```
*   **Network**: Menggunakan bridge network `microservices-network`.
*   **Volume**: Data persisten untuk H2, Jenkins, dan Elasticsearch disimpan di Docker Volumes.

### 2. Jenkins CI/CD (`Jenkinsfile`)
Pipeline otomatisasi untuk build semua microservice secara paralel.
*   **Stages**:
    *   `Checkout`: Mengambil kodingan dari git.
    *   `Build Services`: Menjalankan `./mvnw clean package` untuk 8 service sekaligus secara paralel untuk mempercepat waktu build.
*   **Agent**: Menggunakan agent `any` (default Jenkins).

### 3. Monitoring (ELK Stack)
*   **Elasticsearch**: Database log (`port 9200`).
*   **Logstash**: Log collector & processor (`port 5000`).
*   **Kibana**: Visualisasi log (`port 5601`).
*   **Konfigurasi**: Dilimit memori (ES max 256MB) agar ringan di local development.

---

## 🧪 Verifikasi Manual (Testing Flow)

### Skenario 1: Peminjaman Buku (CQRS)
1.  **Request Pinjam**:
    ```bash
    curl -X POST http://localhost:9000/api/peminjaman/command \
    -H "Content-Type: application/json" \
    -d '{ "bukuId": 1, "anggotaId": 1, "tanggalPinjam": "2023-10-10", "tanggalKembali": "2023-10-17" }'
    ```
2.  **Cek Query (Read)**:
    ```bash
    curl -X GET http://localhost:9000/api/peminjaman/query
    ```
    *Jika data muncul di Query, berarti flow RabbitMQ Command -> Query berhasil.*

### Skenario 2: Pengembalian Buku
1.  **Request Kembali**:
    ```bash
    curl -X POST http://localhost:9000/api/pengembalian \
    -H "Content-Type: application/json" \
    -d '{ "peminjamanId": 1, "tanggalDikembalikan": "2023-10-20" }'
    ```
2.  **Cek Log RabbitMQ Consumer**:
    Cek log container `rabbitmq-pengembalian-service` untuk melihat simulasi pengiriman email.
    ```bash
    docker logs -f rabbitmq-pengembalian-service
    ```

---

## 🔧 Jenkins Setup (Pertama Kali)
1.  Buka `http://localhost:8080`.
2.  Ambil password unlock: `docker logs jenkins`.
3.  Install plugins default.
4.  Buat **New Item** -> **Pipeline**.
5.  Set **Definition** -> **Pipeline script from SCM** -> **Git**.
6.  Masukkan URL repo ini.
7.  **Build Now**.
