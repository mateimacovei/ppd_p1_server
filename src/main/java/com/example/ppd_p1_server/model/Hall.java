package com.example.ppd_p1_server.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Hall {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long nrPlaces;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "hall", cascade = {CascadeType.REMOVE, CascadeType.ALL})
    private List<Sell> sells;
}
