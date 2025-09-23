package com.gio.peminjaman_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "peminjamans")
public class Peminjaman {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long bukuId;
  private Long anggotaId;
  private String tanggalPinjam;
  private String tanggalKembali;
}
