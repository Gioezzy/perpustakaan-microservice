package com.gio.peminjaman_command_service.controller;

import com.gio.peminjaman_command_service.dto.PeminjamanRequestDTO;
import com.gio.peminjaman_command_service.model.Peminjaman;
import com.gio.peminjaman_command_service.service.PeminjamanCommandService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/peminjaman/command")
public class PeminjamanCommandController {

  private final PeminjamanCommandService service;

  public PeminjamanCommandController(PeminjamanCommandService service){
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<Peminjaman> create(@RequestBody PeminjamanRequestDTO dto) {
    return ResponseEntity.ok(service.createPeminjaman(dto));
  }

  @GetMapping
  public ResponseEntity<List<Peminjaman>> getAll(){
    return ResponseEntity.ok(service.getAllPeminjaman());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id){
    service.deletePeminjaman(id);
    return ResponseEntity.noContent().build();
  }

}
