package com.gio.pengembalian_service.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Peminjaman {
  private Long id;
  private Long bukuId;
  private Long anggotaId;
  @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd")
  private java.util.Date tanggalPinjam;
  @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd")
  private java.util.Date tanggalKembali;
}
