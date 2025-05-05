package com.ekip.yakupmelih.library_manage_system.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ekip.yakupmelih.library_manage_system.model.Bagis;
import com.ekip.yakupmelih.library_manage_system.service.BagisService;

@RestController
@RequestMapping("/api/bagis")
public class BagisController {

    private final BagisService bagisService;

    public BagisController(BagisService bagisService) {
        this.bagisService = bagisService;
    }

    @GetMapping
    public List<Bagis> tumBagislariGetir() {
        return bagisService.tumBagislariGetir();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bagis> bagisGetir(@PathVariable int id) {
        return bagisService.bagisGetir(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Bagis bagisEkle(@RequestBody Bagis bagis) {
        return bagisService.bagisKaydet(bagis);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bagis> bagisGuncelle(@PathVariable int id, @RequestBody Bagis bagis) {
        try {
            Bagis guncelBagis = bagisService.bagisGuncelle(id, bagis);
            return ResponseEntity.ok(guncelBagis);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> bagisSil(@PathVariable int id) {
        try {
            bagisService.bagisSil(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/bagisci/{bagisciId}")
    public List<Bagis> bagisciBagislariGetir(@PathVariable int bagisciId) {
        return bagisService.bagisciBagislariGetir(bagisciId);
    }
} 