package com.ekip.yakupmelih.library_manage_system.iterator;

import com.ekip.yakupmelih.library_manage_system.model.Uye;
import com.ekip.yakupmelih.library_manage_system.repository.UyeRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AktifUyeIterator implements UyeIterator {
    private final List<Uye> uyeler;
    private int pozisyon = 0;

    public AktifUyeIterator(UyeRepository uyeRepository) {
        this.uyeler = uyeRepository.findByAktifTrue();
    }

    @Override
    public boolean hasNext() {
        return pozisyon < uyeler.size();
    }

    @Override
    public Uye next() {
        if (hasNext()) {
            return uyeler.get(pozisyon++);
        }
        return null;
    }

    @Override
    public void reset() {
        pozisyon = 0;
    }
}