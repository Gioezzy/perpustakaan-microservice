package com.gio.peminjaman_query_service.controller;

import com.gio.peminjaman_query_service.model.Peminjaman;
import com.gio.peminjaman_query_service.service.PeminjamanService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/peminjaman/query")
public class PeminjamanController {

    private final PeminjamanService service;

    public PeminjamanController(PeminjamanService service) {
        this.service = service;
    }

    @GetMapping
    public List<Peminjaman> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Peminjaman> getById(@PathVariable Long id) {
        return service.findById(id);
    }
}
