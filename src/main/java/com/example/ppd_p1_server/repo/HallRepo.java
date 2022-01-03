package com.example.ppd_p1_server.repo;

import com.example.ppd_p1_server.model.Hall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface HallRepo extends JpaRepository<Hall,Long> {
}