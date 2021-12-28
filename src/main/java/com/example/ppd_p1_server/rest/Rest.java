package com.example.ppd_p1_server.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Rest {

    @GetMapping("/hi")
    public String hi() {
        try
        {
            Thread.sleep(10000);
            System.out.println("javatpoint");
        }catch(InterruptedException e){
            throw new RuntimeException("Thread interrupted..."+e);

        }
        return "hi";
    }
}
