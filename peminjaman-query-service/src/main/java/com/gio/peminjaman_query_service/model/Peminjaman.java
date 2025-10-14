package com.gio.peminjaman_query_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "peminjaman_query")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Peminjaman {
    @Id
    private Long id;

    private Long bukuId;
    private Long anggotaId;
    private LocalDate tanggalPinjam;
    private LocalDate tanggalKembali;
}