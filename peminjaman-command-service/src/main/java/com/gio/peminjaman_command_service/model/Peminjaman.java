package com.gio.peminjaman_command_service.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "peminjamans")
public class Peminjaman implements java.io.Serializable {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long bukuId;
  private Long anggotaId;
  @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd")
  private java.util.Date tanggalPinjam;
  @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd")
  private java.util.Date tanggalKembali;

}
