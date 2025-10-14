package com.gio.peminjaman_command_service.vo;

import lombok.Data;

@Data
public class Anggota {
  private Long id;
  private String nim;
  private String nama;
  private String alamat;
  private String email;
  private String jenisKelamin;

}
