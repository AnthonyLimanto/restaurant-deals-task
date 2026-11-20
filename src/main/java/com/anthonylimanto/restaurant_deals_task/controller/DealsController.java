package com.anthonylimanto.restaurant_deals_task.controller;

import com.anthonylimanto.restaurant_deals_task.api.DealsResponse;
import com.anthonylimanto.restaurant_deals_task.service.RestaurantDealsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DealsController {

    private final RestaurantDealsService restaurantDealsService;

    public DealsController(RestaurantDealsService restaurantDealsService) {
        this.restaurantDealsService = restaurantDealsService;
    }

    @GetMapping("/deals")
    public DealsResponse getDeals(@RequestParam String timeOfDay) {

        return new DealsResponse(restaurantDealsService.findActiveDeals(timeOfDay));
    }
}
