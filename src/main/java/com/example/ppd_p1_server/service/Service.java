package com.example.ppd_p1_server.service;

import com.example.ppd_p1_server.model.Reservation;
import com.example.ppd_p1_server.model.Sell;
import com.example.ppd_p1_server.model.SoldPlace;
import com.example.ppd_p1_server.repo.HallRepo;
import com.example.ppd_p1_server.repo.SellRepo;
import com.example.ppd_p1_server.repo.ShowRepo;
import com.example.ppd_p1_server.repo.SoldPlaceRepo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

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

    static Long count = 0L;

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
//        show.setPrice(show.getPrice() + paidPrice);
        show.setSold(show.getSold() + paidPrice);
        showRepo.save(show);
    }

    @Scheduled(fixedRate = 2000)
    synchronized public void verify() {
        AtomicBoolean isValid = new AtomicBoolean(true);
        hallRepo.findAll().forEach(hall -> {
            showRepo.findByHall_Id(hall.getId()).forEach(show -> {
                var set = new ArrayList<Long>();
                AtomicLong checkedSold = new AtomicLong(0L);
                sellRepo.findByShow(show).forEach(sell -> {
                    checkedSold.addAndGet(sell.getTicketsSold() * show.getPrice());
                    var soldTickets = sell.getSoldPlaces()
                            .stream()
                            .map(SoldPlace::getPlace)
                            .collect(Collectors.toList());
                    set.addAll(soldTickets);
                });

                if (checkedSold.get() != show.getSold()
                        || set.size() > hall.getNrPlaces()) {
                    System.out.println("in sold");
                    isValid.set(false);
                }
                List<Long> range = LongStream.range(1L, hall.getNrPlaces() + 1).boxed().collect(Collectors.toList());
                List<Long> copy = new ArrayList<>(range);
                set.forEach(copy::remove);
                set.addAll(copy);
                set.sort(null);
                if (!set.equals(range)) {
                    System.out.println(set);
                    System.out.println(range);
                    System.out.println(copy);
                    isValid.set(false);
                }
            });
        });
        System.out.println(isValid.get());
    }
}
