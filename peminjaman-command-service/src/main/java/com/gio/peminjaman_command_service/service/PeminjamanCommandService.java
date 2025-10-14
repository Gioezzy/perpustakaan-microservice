package com.gio.peminjaman_command_service.service;

import org.springframework.stereotype.Service;

import com.gio.peminjaman_command_service.dto.PeminjamanRequestDTO;
import com.gio.peminjaman_command_service.event.PeminjamanEventPublisher;
import com.gio.peminjaman_command_service.model.Peminjaman;
import com.gio.peminjaman_command_service.repository.PeminjamanRepository;

import java.util.List;

@Service
public class PeminjamanCommandService {

  private final PeminjamanRepository repository;
  private final PeminjamanEventPublisher eventPublisher;

  public PeminjamanCommandService(PeminjamanRepository repository, PeminjamanEventPublisher eventPublisher){
    this.repository = repository;
    this.eventPublisher = eventPublisher;
  }

  public Peminjaman createPeminjaman(PeminjamanRequestDTO dto){
    Peminjaman peminjaman = new Peminjaman();
    peminjaman.setBukuId(dto.getBukuId());
    peminjaman.setAnggotaId(dto.getAnggotaId());
    peminjaman.setTanggalPinjam(dto.getTanggalPinjam());
    peminjaman.setTanggalKembali(dto.getTanggalKembali());

    Peminjaman saved = repository.save(peminjaman);
    eventPublisher.publishPeminjamanEvent(saved);
    return saved;
  }

  public List<Peminjaman> getAllPeminjaman() {
    return repository.findAll();
  }

  public void deletePeminjaman(Long id){
    repository.deleteById(id);
  }
}
