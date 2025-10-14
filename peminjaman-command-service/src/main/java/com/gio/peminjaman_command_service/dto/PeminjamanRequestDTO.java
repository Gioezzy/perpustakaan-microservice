package com.gio.peminjaman_command_service.dto;

import lombok.Data;

@Data
public class PeminjamanRequestDTO {
    private Long bukuId;
    private Long anggotaId;
    private String tanggalPinjam;
    private String tanggalKembali;
}
