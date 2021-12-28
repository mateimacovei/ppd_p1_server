package com.example.ppd_p1_server.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@Entity
@NoArgsConstructor
public class Hall {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;
    private Long nrPlaces;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "hall", cascade = {CascadeType.REMOVE, CascadeType.ALL})
    private List<Sell> sells;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "hall", cascade = {CascadeType.REMOVE, CascadeType.ALL})
    private List<Show> shows;
}
