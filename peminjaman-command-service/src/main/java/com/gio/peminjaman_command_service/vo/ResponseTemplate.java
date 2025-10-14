package com.gio.peminjaman_command_service.vo;

import com.gio.peminjaman_command_service.model.Peminjaman;

import lombok.Data;

@Data
public class ResponseTemplate {
  private Peminjaman peminjaman;
  private Anggota anggota;
  private Buku buku;

}
