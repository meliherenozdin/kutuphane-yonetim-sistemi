package com.ekip.yakupmelih.library_manage_system.repository;


import com.ekip.yakupmelih.library_manage_system.model.Bagis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BagisRepository extends JpaRepository<Bagis, Integer> {
}