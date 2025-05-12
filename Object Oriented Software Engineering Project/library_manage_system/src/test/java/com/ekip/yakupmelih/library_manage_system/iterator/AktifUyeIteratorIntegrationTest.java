package com.ekip.yakupmelih.library_manage_system.iterator;

import com.ekip.yakupmelih.library_manage_system.model.Uye;
import com.ekip.yakupmelih.library_manage_system.repository.UyeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AktifUyeIteratorIntegrationTest {

    @Autowired
    private UyeRepository uyeRepository;

    @Test
    void iteratorGercekVeriIleCalisir() {
        // Test için veritabanında en az 1 aktif üye olmalı!
        List<Uye> aktifUyeler = uyeRepository.findByAktifTrue();
        AktifUyeIterator iterator = new AktifUyeIterator(uyeRepository);

        int count = 0;
        while (iterator.hasNext()) {
            Uye uye = iterator.next();
            assertNotNull(uye);
            assertTrue(uye.isAktif());
            count++;
        }
        assertEquals(aktifUyeler.size(), count);
    }
}