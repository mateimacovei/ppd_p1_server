package com.example.ppd_p1_server.service;

import com.example.ppd_p1_server.model.Reservation;
import com.example.ppd_p1_server.model.Sell;
import com.example.ppd_p1_server.model.SoldPlace;
import com.example.ppd_p1_server.repo.HallRepo;
import com.example.ppd_p1_server.repo.SellRepo;
import com.example.ppd_p1_server.repo.ShowRepo;
import com.example.ppd_p1_server.repo.SoldPlaceRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Component
@Slf4j
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
        var distPlaces = new HashSet<Long>();
        for (var pl : reservation.getPlaces()) {
            if (!distPlaces.add(pl))
                throw new Exception("Requested places must be distinct");
        }

        var places = soldPlaceRepo.findAllBySell_Show_Id(reservation.getShowId()).stream()
                .map(SoldPlace::getPlace)
                .collect(Collectors.toList());
        var show = showRepo.findById(reservation.getShowId()).get();
        var paidPrice = reservation.getPlaces().size() * show.getPrice();

        var hall = show.getHall();
        for (Long place : reservation.getPlaces())
            if (places.contains(place) || place > hall.getNrPlaces())
                throw new Exception("CAN NOT RESERVE PLACE " + place);

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

    synchronized public void verify() {
        AtomicReference<String> message = new AtomicReference<>("valid");

        List<AbstractMap.SimpleEntry<Long, Long>> soldPerShow = new ArrayList<>();
        List<AbstractMap.SimpleEntry<List<Long>, Long>> sellsPerShow = new ArrayList<>();
//        List<>
        AtomicReference<Boolean> isValid = new AtomicReference<>(true);
        hallRepo.findAll().forEach(hall -> showRepo.findByHall_Id(hall.getId())
                .forEach(show -> {
                    soldPerShow.add(new AbstractMap.SimpleEntry<>(show.getSold(), show.getId()));
                    var soldTicketsPlaceNr = new ArrayList<Long>();
                    AtomicLong checkedSold = new AtomicLong(0L);
                    sellRepo.findByShow(show).forEach(sell -> {
                        checkedSold.addAndGet(sell.getTicketsSold() * show.getPrice());
                        var soldTickets = sell.getSoldPlaces()
                                .stream()
                                .map(SoldPlace::getPlace)
                                .collect(Collectors.toList());
                        soldTicketsPlaceNr.addAll(soldTickets);
                    });
                    sellsPerShow.add(new AbstractMap.SimpleEntry<>(new ArrayList<>(soldTicketsPlaceNr), show.getId()));

                    if (checkedSold.get() != show.getSold()
                            || soldTicketsPlaceNr.size() > hall.getNrPlaces()) {
                        message.set("error found in sold amout");
                        isValid.set(false);
                    }

                    List<Long> range = LongStream.range(1L, hall.getNrPlaces() + 1).boxed().collect(Collectors.toList());
                    List<Long> copy = new ArrayList<>(range);
                    soldTicketsPlaceNr.forEach(copy::remove);
                    soldTicketsPlaceNr.addAll(copy);
                    soldTicketsPlaceNr.sort(null);
                    if (!soldTicketsPlaceNr.equals(range)) {
                        System.out.println(soldTicketsPlaceNr);
                        System.out.println(range);
                        System.out.println(copy);
                        message.set("error found in sold places");
                        isValid.set(false);
                    }
                }));

        log.info("sold_per_show:{}\tsells_per_show:{}\tvalid:{}\tmessage:{}", soldPerShow, sellsPerShow, isValid.get(), message.get());
    }
}
