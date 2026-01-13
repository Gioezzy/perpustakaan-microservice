package com.gio.rabbitmq_pengembalian_service.vo;

import java.util.Date;

public class Pengembalian {
  private Long id;
  private Long peminjamanId;
  @com.fasterxml.jackson.annotation.JsonFormat(pattern = "yyyy-MM-dd")
  private Date tanggalDikembalikan;
  private String terlambat;
  private Double denda;
  
  public Pengembalian() {
    
  }

  public Pengembalian(Long id, Long peminjamanId, Date tanggalDikembalikan, String terlambat, Double denda) {
    this.id = id;
    this.peminjamanId = peminjamanId;
    this.tanggalDikembalikan = tanggalDikembalikan;
    this.terlambat = terlambat;
    this.denda = denda;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPeminjamanId() {
    return peminjamanId;
  }

  public void setPeminjamanId(Long peminjamanId) {
    this.peminjamanId = peminjamanId;
  }

  public Date getTanggalDikembalikan() {
    return tanggalDikembalikan;
  }

  public void setTanggalDikembalikan(Date tanggalDikembalikan) {
    this.tanggalDikembalikan = tanggalDikembalikan;
  }

  public String getTerlambat() {
    return terlambat;
  }

  public void setTerlambat(String terlambat) {
    this.terlambat = terlambat;
  }

  public Double getDenda() {
    return denda;
  }

  public void setDenda(Double denda) {
    this.denda = denda;
  }
}
