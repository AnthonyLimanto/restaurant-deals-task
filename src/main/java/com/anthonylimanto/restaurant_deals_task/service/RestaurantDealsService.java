package com.anthonylimanto.restaurant_deals_task.service;

import com.anthonylimanto.restaurant_deals_task.api.DealItem;
import com.anthonylimanto.restaurant_deals_task.helper.TimeHelper;
import com.anthonylimanto.restaurant_deals_task.source.Deal;
import com.anthonylimanto.restaurant_deals_task.source.Restaurant;
import com.anthonylimanto.restaurant_deals_task.source.RestaurantSource;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantDealsService {
    private final RestaurantSource restaurantSource;

    public RestaurantDealsService(RestaurantSource restaurantSource) {
        this.restaurantSource = restaurantSource;
    }

    public List<DealItem> findActiveDeals(String timeOfDay) {
        LocalTime time = TimeHelper.parseTime(timeOfDay);

        List<DealItem> activeDeals = new ArrayList<>();
        for (Restaurant restaurant : restaurantSource.fetchRestaurants()) {
            for (Deal deal : restaurant.deals()) {
                if (isActive(restaurant, deal, time)) {
                    activeDeals.add(toDealItem(restaurant, deal));
                }
            }
        }

        return activeDeals;
    }

    private boolean isActive(Restaurant restaurant, Deal deal, LocalTime time) {
        String startText = firstNonNull(deal.open(), deal.start(), restaurant.open());
        String endText = firstNonNull(deal.close(), deal.end(), restaurant.close());
        if (startText == null || endText == null) {
            return false;
        }

        LocalTime start = TimeHelper.parseTime(startText);
        LocalTime end = TimeHelper.parseTime(endText);
        return isWithin(time, start, end);
    }

    /**
     * Start is inclusive and end is exclusive.
     * If the end is before the start we assume the time window is overnight eg 6pm start - 1am end
     */
    private static boolean isWithin(LocalTime time, LocalTime start, LocalTime end) {
        if (start.isBefore(end)) {
            return !time.isBefore(start) && time.isBefore(end);
        }
        return !time.isBefore(start) || time.isBefore(end);
    }

    private static String firstNonNull(String... candidates) {
        for (String candidate : candidates) {
            if (candidate != null) {
                return candidate;
            }
        }
        return null;
    }

    public static DealItem toDealItem(Restaurant restaurant, Deal deal) {
        return new DealItem(
                restaurant.objectId(),
                restaurant.name(),
                restaurant.address1(),
                restaurant.suburb(),
                restaurant.open(),
                restaurant.close(),
                deal.objectId(),
                deal.discount(),
                deal.dineIn(),
                deal.lightning(),
                deal.qtyLeft()
        );
    }
}
