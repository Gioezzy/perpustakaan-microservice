package com.gio.rabbitmq_pengembalian_service.vo;

public class Buku {
  private Long id;
  private String judul;
  private String pengarang;
  private String penerbit;
  private String tahun_terbit;

  public Buku() {
    
  }
  
  public Buku(Long id, String judul, String pengarang, String penerbit, String tahun_terbit) {
    this.id = id;
    this.judul = judul;
    this.pengarang = pengarang;
    this.penerbit = penerbit;
    this.tahun_terbit = tahun_terbit;
  }

  public Long getId() {
    return id;
  }
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public String getJudul() {
    return judul;
  }
  
  public void setJudul(String judul) {
    this.judul = judul;
  }
  
  public String getPengarang() {
    return pengarang;
  }
  
  public void setPengarang(String pengarang) {
    this.pengarang = pengarang;
  }
  
  public String getPenerbit() {
    return penerbit;
  }
  
  public void setPenerbit(String penerbit) {
    this.penerbit = penerbit;
  }
  
  public String getTahunTerbit() {
    return tahun_terbit;
  }
  
  public void setTahunTerbit(String tahun_terbit) {
    this.tahun_terbit = tahun_terbit;
  }

}
