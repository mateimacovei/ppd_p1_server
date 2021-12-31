package com.example.ppd_p1_server.repo;

import com.example.ppd_p1_server.model.SoldPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface SoldPlaceRepo extends JpaRepository<SoldPlace, Long> {
    @Transactional(readOnly = true)
    List<SoldPlace> findAllBySell_Show_Id(Long showId);
}
