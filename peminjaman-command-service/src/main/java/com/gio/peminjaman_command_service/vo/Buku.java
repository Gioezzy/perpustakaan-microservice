package com.gio.peminjaman_command_service.vo;

import lombok.Data;

@Data
public class Buku {
  private Long id;
  private String judul;
  private String pengarang;
  private String penerbit;
  private String tahunTerbit;

}
