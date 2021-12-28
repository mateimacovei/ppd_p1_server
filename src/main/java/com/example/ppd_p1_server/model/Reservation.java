package com.example.ppd_p1_server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    private Long showId;
    private List<Long> places;
}
