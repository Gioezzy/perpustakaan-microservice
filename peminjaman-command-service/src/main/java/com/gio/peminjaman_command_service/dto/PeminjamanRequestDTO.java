package com.gio.peminjaman_command_service.dto;

import lombok.Data;

@Data
public class PeminjamanRequestDTO {
    private Long bukuId;
    private Long anggotaId;
    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date tanggalPinjam;
    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date tanggalKembali;
}
