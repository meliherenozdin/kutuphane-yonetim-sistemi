package com.ekip.yakupmelih.library_manage_system.service;

import com.ekip.yakupmelih.library_manage_system.model.Personel;
import com.ekip.yakupmelih.library_manage_system.repository.PersonelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PersonelService {

    private final PersonelRepository personelRepository;

    public PersonelService(PersonelRepository personelRepository) {
        this.personelRepository = personelRepository;
    }

    public List<Personel> tumPersonelleriGetir() {
        return personelRepository.findAll();
    }

    public List<Personel> aktifPersonelleriGetir() {
        return personelRepository.findByAktifTrue();
    }

    public Optional<Personel> personelBulById(int id) {
        return personelRepository.findById(id);
    }

    public Optional<Personel> personelBulByEmail(String email) {
        return personelRepository.findByEmail(email);
    }

    @Transactional
    public List<Personel> topluPersonelEkle(List<Personel> personeller) {
        return personelRepository.saveAll(personeller);
    }

    @Transactional
    public List<Personel> topluPersonelGuncelle(List<Personel> personeller) {
        return personelRepository.saveAll(personeller);
    }

    public Personel personelEkle(Personel personel) {
        return personelRepository.save(personel);
    }

    public Personel personelGuncelle(int id, Personel yeniPersonel) {
        return personelRepository.findById(id)
                .map(p -> {
                    p.setPerAdi(yeniPersonel.getPerAdi());
                    p.setPerSoyad(yeniPersonel.getPerSoyad());
                    p.setEmail(yeniPersonel.getEmail());
                    p.setTelNo(yeniPersonel.getTelNo());
                    p.setAciklama(yeniPersonel.getAciklama());
                    p.setAktif(yeniPersonel.isAktif());
                    return personelRepository.save(p);
                })
                .orElseThrow();
    }

    public void personelSil(int id) {
        personelRepository.findById(id).ifPresent(p -> {
            p.setAktif(false);
            personelRepository.save(p);
        });
    }
}
