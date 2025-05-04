package com.ekip.yakupmelih.library_manage_system.repository;

import com.ekip.yakupmelih.library_manage_system.model.Kategori;
import com.ekip.yakupmelih.library_manage_system.model.Kitap;
import com.ekip.yakupmelih.library_manage_system.model.Yazar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KitapRepository extends JpaRepository<Kitap, Integer> {
    Optional<Kitap> findByIsbn(String isbn);

    List<Kitap> findByKategori(Kategori kategori);

    List<Kitap> findByYazar(Yazar yazar);

    Optional<Kitap> findByKitapAdiContainingIgnoreCase(String kitapAdi);

    List<Kitap> findByAktifTrue();

    List<Kitap> findByDurum(Kitap.KitapDurum durum);

    List<Kitap> findByAdetGreaterThan(int minAdet);

    List<Kitap> findByKategoriAndAktifTrue(Kategori kategori);
}
