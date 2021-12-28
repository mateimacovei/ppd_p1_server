package com.example.ppd_p1_server.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Sell {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Show show;

    @ManyToOne(fetch = FetchType.LAZY)
    private Hall hall;

    private Integer ticketsSold;

    private Long sum;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sell", cascade = {CascadeType.REMOVE, CascadeType.ALL})
    private List<SoldPlace> soldPlaces;
}
