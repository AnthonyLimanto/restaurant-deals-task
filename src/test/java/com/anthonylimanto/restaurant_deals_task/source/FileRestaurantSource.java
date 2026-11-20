package com.anthonylimanto.restaurant_deals_task.source;

import tools.jackson.databind.json.JsonMapper;

import java.io.InputStream;
import java.util.List;

public class FileRestaurantSource implements RestaurantSource {
    @Override
    public List<Restaurant> fetchRestaurants() {
        InputStream json = getClass().getResourceAsStream("/challengedata.json");
        RestaurantList data = JsonMapper.builder().build().readValue(json, RestaurantList.class);
        return data.restaurants();
    }
}
