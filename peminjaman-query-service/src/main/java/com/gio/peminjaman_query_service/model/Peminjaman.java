package com.gio.peminjaman_query_service.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "peminjamans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Peminjaman {
    @Id
    private Long id;
    private Long bukuId;
    private Long anggotaId;
    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd")
    private Date tanggalPinjam;
    @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd")
    private Date tanggalKembali;

    @Override
    public String toString() {
        return "Peminjaman{" +
                "id=" + id +
                ", bukuId=" + bukuId +
                ", anggotaId=" + anggotaId +
                ", tanggalPinjam=" + tanggalPinjam +
                ", tanggalKembali=" + tanggalKembali +
                '}';
    }
}