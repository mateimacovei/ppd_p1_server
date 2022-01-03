package com.example.ppd_p1_server.repo;

import com.example.ppd_p1_server.model.Sell;
import com.example.ppd_p1_server.model.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface SellRepo extends JpaRepository<Sell,Long> {
    List<Sell> findByShow(Show show);
}
