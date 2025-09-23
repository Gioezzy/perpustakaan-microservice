package com.gio.pengembalian_service.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.gio.pengembalian_service.model.Pengembalian;
import com.gio.pengembalian_service.repository.PengembalianRepository;
import com.gio.pengembalian_service.vo.Anggota;
import com.gio.pengembalian_service.vo.Buku;
import com.gio.pengembalian_service.vo.Peminjaman;
import com.gio.pengembalian_service.vo.ResponseTemplate;

@Service
public class PengembalianService {

    private final PengembalianRepository pengembalianRepository;

    @Autowired
    public RestTemplate restTemplate;

    public PengembalianService(PengembalianRepository pengembalianRepository) {
        this.pengembalianRepository = pengembalianRepository;
    }

    public List<Pengembalian> getAllPengembalians() {
        return pengembalianRepository.findAll();
    }

    public Pengembalian getPengembalianById(Long id) {
        return pengembalianRepository.findById(id).orElse(null);
    }

    public Pengembalian createPengembalian(Pengembalian pengembalian) throws ParseException {
        Peminjaman peminjaman = this.getPeminjaman(pengembalian.getPeminjamanId());

        if (peminjaman == null) {
            throw new RuntimeException("Data peminjaman tidak ditemukan untuk id: " + pengembalian.getPeminjamanId());
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date tanggalKembali = sdf.parse(peminjaman.getTanggalKembali());
        Date tanggalDikembalikan = sdf.parse(pengembalian.getTanggalDikembalikan());

        long kembali = tanggalDikembalikan.getTime() - tanggalKembali.getTime();
        long jumlahHari = kembali < 0 ? 0 : Math.abs(kembali);
        long terlambat = TimeUnit.DAYS.convert(jumlahHari, TimeUnit.MILLISECONDS);
        double denda = terlambat * 1000;

        pengembalian.setTerlambat(terlambat + " Hari");
        pengembalian.setDenda(denda);

        return pengembalianRepository.save(pengembalian);
    }

    public Peminjaman getPeminjaman(Long id) {
        try {
            return restTemplate.getForObject("http://localhost:8083/api/peminjaman/" + id, Peminjaman.class);
        } catch (Exception e) {
            return null;
        }
    }

    public void deletePengembalian(Long id) {
        pengembalianRepository.deleteById(id);
    }

    public List<ResponseTemplate> getPengembalianWithPeminjamanById(Long id) {
        List<ResponseTemplate> responseTemplates = new ArrayList<>();
        Pengembalian pengembalian = getPengembalianById(id);

        if (pengembalian == null) {
            return null;
        }

        Peminjaman peminjaman = restTemplate.getForObject("http://localhost:8083/api/peminjaman/" + pengembalian.getPeminjamanId(), Peminjaman.class);
        Anggota anggota = restTemplate.getForObject("http://localhost:8081/api/anggota/" + peminjaman.getAnggotaId(), Anggota.class);
        Buku buku = restTemplate.getForObject("http://localhost:8082/api/buku/" + peminjaman.getBukuId(), Buku.class);

        ResponseTemplate vo = new ResponseTemplate();
        vo.setPeminjaman(peminjaman);
        vo.setAnggota(anggota);
        vo.setBuku(buku);

        responseTemplates.add(vo);
        return responseTemplates;
    }
}
