package com.anthonylimanto.restaurant_deals_task.source;

import java.util.List;

public interface RestaurantSource {
    List<Restaurant> fetchRestaurants();
}
