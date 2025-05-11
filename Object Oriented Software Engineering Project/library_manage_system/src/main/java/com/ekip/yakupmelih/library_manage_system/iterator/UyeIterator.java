package com.ekip.yakupmelih.library_manage_system.iterator;

import com.ekip.yakupmelih.library_manage_system.model.Uye;

public interface UyeIterator {
    boolean hasNext();

    Uye next();

    void reset();
}