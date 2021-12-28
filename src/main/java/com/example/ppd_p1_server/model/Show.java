package com.example.ppd_p1_server.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@Entity
public class Show {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;
    private Date date;
    private String title;
    private Long price;
    private Long sold;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "show", cascade = {CascadeType.REMOVE, CascadeType.ALL})
    private List<Sell> sells;

    @ManyToOne(fetch = FetchType.EAGER)
    private Hall hall;
}
