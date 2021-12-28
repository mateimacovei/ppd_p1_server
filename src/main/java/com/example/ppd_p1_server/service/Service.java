package com.example.ppd_p1_server.service;

import com.example.ppd_p1_server.model.Hall;
import com.example.ppd_p1_server.model.Reservation;
import com.example.ppd_p1_server.model.Sell;
import com.example.ppd_p1_server.model.SoldPlace;
import com.example.ppd_p1_server.repo.HallRepo;
import com.example.ppd_p1_server.repo.SellRepo;
import com.example.ppd_p1_server.repo.ShowRepo;
import com.example.ppd_p1_server.repo.SoldPlaceRepo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.stream.Collectors;

@Component
public class Service {
    @Resource
    HallRepo hallRepo;
    @Resource
    SellRepo sellRepo;
    @Resource
    ShowRepo showRepo;
    @Resource
    SoldPlaceRepo soldPlaceRepo;


    synchronized public void reserve(Reservation reservation) throws Exception {
        var places = soldPlaceRepo.findAllBySell_Show_Id(reservation.getShowId()).stream()
                .map(SoldPlace::getPlace)
                .collect(Collectors.toList());
        var show = showRepo.findById(reservation.getShowId()).get();
        var paidPrice = reservation.getPlaces().size() * show.getPrice();

        var hall = show.getHall();
        for (Long place : reservation.getPlaces())
            if (places.contains(place) || place > hall.getNrPlaces())
                throw new Exception("CAN NOT RESERVE PLACE");

        var sell = Sell.builder()
                .show(show)
                .hall(hall)
                .ticketsSold(reservation.getPlaces().size())
                .sum(paidPrice)
                .build();
        var placesToSell = reservation.getPlaces().stream()
                .map(x -> SoldPlace.builder().place(x).sell(sell).build())
                .collect(Collectors.toList());

        sellRepo.save(sell);
        soldPlaceRepo.saveAll(placesToSell);
        show.setPrice(show.getPrice() + paidPrice);
        showRepo.save(show);
    }
}
