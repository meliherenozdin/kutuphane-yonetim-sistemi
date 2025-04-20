package com.ekip.yakupmelih.library_manage_system.service;

import com.ekip.yakupmelih.library_manage_system.model.Personel;
import com.ekip.yakupmelih.library_manage_system.repository.PersonelRepository;
import org.springframework.stereotype.Service;

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

    public Optional<Personel> personelBulById(int id) {
        return personelRepository.findById(id);
    }

    public Optional<Personel> personelBulByEmail(String email) {
        return personelRepository.findByEmail(email);
    }

    public Personel personelEkle(Personel personel) {
        return personelRepository.save(personel);
    }

    public void personelSil(int id) {
        personelRepository.deleteById(id);
    }
}
