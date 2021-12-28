package com.example.ppd_p1_server.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class SoldPlace {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long place;

    @ManyToOne(fetch = FetchType.LAZY)
    private Sell sell;
}
