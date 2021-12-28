package com.example.ppd_p1_server.rest;

import com.example.ppd_p1_server.model.Reservation;
import com.example.ppd_p1_server.service.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RestController
@Slf4j
public class Rest {
    ExecutorService executorService = Executors.newFixedThreadPool(10);
    @Autowired
    Service service;

    @GetMapping("/hi")
    public String hi() throws Exception {
        Future<String> future = executorService.submit(() -> "Hello World");
        return future.get();
    }

    @PostMapping("/reservation")
    public ResponseEntity<?> reserve(@RequestBody Reservation reservation) throws ExecutionException, InterruptedException {
//        return new ResponseEntity<>(reservation, HttpStatus.OK);
        Future<ResponseEntity<?>> future = executorService.submit(() -> {
            var map = new HashMap<String, Boolean>();
            try {
                service.reserve(reservation);
                map.put("success", true);
            } catch (Exception ex) {
                log.error("Exception thrown", ex);
                map.put("success", false);
            }
            return new ResponseEntity<>(map, HttpStatus.OK);
        });
        return future.get();
    }
}
