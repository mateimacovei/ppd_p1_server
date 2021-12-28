package com.example.ppd_p1_server.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sell {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
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
