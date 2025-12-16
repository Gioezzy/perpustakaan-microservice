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
  private String tanggalPinjam;
  private String tanggalKembali;

}
