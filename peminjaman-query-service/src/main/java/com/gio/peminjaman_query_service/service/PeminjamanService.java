package com.gio.peminjaman_query_service.service;

import com.gio.peminjaman_query_service.model.Peminjaman;
import com.gio.peminjaman_query_service.repository.PeminjamanRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PeminjamanService {
    private final PeminjamanRepository repository;

    public PeminjamanService(PeminjamanRepository repository) {
        this.repository = repository;
    }

    public List<Peminjaman> findAll() {
        return repository.findAll();
    }

    public Optional<Peminjaman> findById(Long id) {
        return repository.findById(id);
    }

    public Peminjaman save(Peminjaman peminjaman) {
        return repository.save(peminjaman);
    }
}
