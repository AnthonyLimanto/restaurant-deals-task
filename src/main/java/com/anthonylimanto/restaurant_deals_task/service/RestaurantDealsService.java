package com.anthonylimanto.restaurant_deals_task.service;

import com.anthonylimanto.restaurant_deals_task.api.DealItem;
import com.anthonylimanto.restaurant_deals_task.helper.TimeHelper;
import com.anthonylimanto.restaurant_deals_task.source.Deal;
import com.anthonylimanto.restaurant_deals_task.source.Restaurant;
import com.anthonylimanto.restaurant_deals_task.source.RestaurantSource;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantDealsService {
    private final RestaurantSource restaurantSource;

    public RestaurantDealsService(RestaurantSource restaurantSource) {
        this.restaurantSource = restaurantSource;
    }

    private enum EventType { END, START }

    // A time event is the start time or end time of a deal, type refers to if it's a start or end time
    private record TimeEvent(LocalTime time, EventType type) {}

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

    public Optional<DealWindow> findPeakTime() {

        List<TimeEvent> events = new ArrayList<>();

        for (Restaurant restaurant : restaurantSource.fetchRestaurants()) {
            for (Deal deal : restaurant.deals()) {
                DealWindow dealWindow = resolveWindow(restaurant, deal);
                if (dealWindow != null) {
                    events.add(new TimeEvent(dealWindow.start(), EventType.START));
                    events.add(new TimeEvent(dealWindow.end(), EventType.END));
                }
            }
        }

        if (events.isEmpty()) {
            return Optional.empty();
        }

        // In the event the time of 2 events is the same the one that is of type end will go first, this is because a deal's valid time has inclusive start time and exclusive end time
        events.sort(Comparator.comparing(TimeEvent::time)
                .thenComparing(TimeEvent::type));

        int maxAmountOfDeals = 0;
        int currCount = 0;

        for (TimeEvent timeEvent : events) {
            if (timeEvent.type() == EventType.START) {
                currCount++;
                maxAmountOfDeals = Math.max(maxAmountOfDeals, currCount);
            } else {
                currCount--;
            }
        }

        currCount = 0;
        LocalTime peakStart = null;
        LocalTime peakEnd = null;

        for (TimeEvent timeEvent : events) {
            if (timeEvent.type() == EventType.START) {
                currCount++;
            } else {
                currCount--;
            }

            if (peakStart == null && currCount == maxAmountOfDeals) {
                peakStart = timeEvent.time();
            } else if (peakStart != null && currCount < maxAmountOfDeals) {
                peakEnd = timeEvent.time();
                break;
            }
        }

        return Optional.of(new DealWindow(peakStart, peakEnd));
    }

    private static DealWindow resolveWindow(Restaurant restaurant, Deal deal) {
        String startText = firstNonNull(deal.open(), deal.start(), restaurant.open());
        String endText = firstNonNull(deal.close(), deal.end(), restaurant.close());
        if (startText == null || endText == null) {
            return null;
        }
        return new DealWindow(TimeHelper.parseTime(startText), TimeHelper.parseTime(endText));
    }

    private boolean isActive(Restaurant restaurant, Deal deal, LocalTime time) {
        DealWindow window = resolveWindow(restaurant, deal);
        return window != null && isWithin(time, window.start(), window.end());
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

    private static DealItem toDealItem(Restaurant restaurant, Deal deal) {
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
