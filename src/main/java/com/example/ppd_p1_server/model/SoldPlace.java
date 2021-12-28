package com.example.ppd_p1_server.model;

import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SoldPlace {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;
    private Long place;

    @ManyToOne(fetch = FetchType.LAZY)
    private Sell sell;
}
