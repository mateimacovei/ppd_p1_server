package com.example.ppd_p1_server.repo;

import com.example.ppd_p1_server.model.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ShowRepo extends JpaRepository<Show,Long> {
    List<Show> findByHall_Id(Long id);
}
