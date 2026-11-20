package com.anthonylimanto.restaurant_deals_task.controller;

import com.anthonylimanto.restaurant_deals_task.api.DealsResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DealsController {

    @GetMapping("/deals")
    public DealsResponse getDeals(@RequestParam String timeOfDay) {
        return new DealsResponse(List.of());
    }
}
