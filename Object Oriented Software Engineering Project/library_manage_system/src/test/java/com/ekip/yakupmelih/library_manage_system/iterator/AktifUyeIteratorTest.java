package com.ekip.yakupmelih.library_manage_system.iterator;

import com.ekip.yakupmelih.library_manage_system.model.Uye;
import com.ekip.yakupmelih.library_manage_system.repository.UyeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AktifUyeIteratorTest {

    private UyeRepository uyeRepository;
    private AktifUyeIterator iterator;

    @BeforeEach
    void setUp() {
        uyeRepository = mock(UyeRepository.class);

        Uye uye1 = new Uye();
        uye1.setUyeAdi("Ali");
        Uye uye2 = new Uye();
        uye2.setUyeAdi("Veli");

        List<Uye> aktifUyeler = Arrays.asList(uye1, uye2);
        when(uyeRepository.findByAktifTrue()).thenReturn(aktifUyeler);

        iterator = new AktifUyeIterator(uyeRepository);
    }

    @Test
    void testIterator() {
        assertTrue(iterator.hasNext());
        Uye first = iterator.next();
        assertEquals("Ali", first.getUyeAdi());

        assertTrue(iterator.hasNext());
        Uye second = iterator.next();
        assertEquals("Veli", second.getUyeAdi());

        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    void testReset() {
        iterator.next();
        iterator.reset();
        assertTrue(iterator.hasNext());
        Uye firstAgain = iterator.next();
        assertEquals("Ali", firstAgain.getUyeAdi());
    }
} 