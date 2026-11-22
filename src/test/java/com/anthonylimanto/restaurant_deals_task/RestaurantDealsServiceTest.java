package com.anthonylimanto.restaurant_deals_task;

import com.anthonylimanto.restaurant_deals_task.api.DealItem;
import com.anthonylimanto.restaurant_deals_task.service.DealWindow;
import com.anthonylimanto.restaurant_deals_task.service.RestaurantDealsService;
import com.anthonylimanto.restaurant_deals_task.source.FileRestaurantSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class RestaurantDealsServiceTest {
    private RestaurantDealsService service;

    @BeforeEach
    void setUp() {
        service = new RestaurantDealsService(new FileRestaurantSource());
    }

    @Test
    void findsDealsActiveAtThreePm() {
        List<DealItem> deals = service.findActiveDeals("3:00pm");

        // Masala Kitchen x2, ABC Chicken x2, Vrindavan x1, Kekou's 2:00pm deal.
        assertEquals(6, deals.size());
        assertTrue(hasRestaurant(deals, "Masala Kitchen"));   // starts at 3:00pm, start is inclusive
        assertTrue(hasRestaurant(deals, "Vrindavan"));        // deal times win over opening hours
        assertFalse(hasRestaurant(deals, "OzzyThai Cafe Bar ")); // closes at 3:00pm, end is exclusive
    }

    @Test
    void findsDealsActiveAtSixPm() {
        List<DealItem> deals = service.findActiveDeals("6:00pm");

        assertEquals(9, deals.size());
    }

    @Test
    void findsDealsActiveAtNinePm() {
        List<DealItem> deals = service.findActiveDeals("9:00pm");

        assertEquals(4, deals.size());
    }

    @Test
    void findsThePeakWindowInTheSavedData() {
        RestaurantDealsService service = new RestaurantDealsService(new FileRestaurantSource());

        DealWindow peak = service.findPeakTime().orElseThrow();

        assertEquals(LocalTime.of(17, 0), peak.start());
        assertEquals(LocalTime.of(21, 0), peak.end());
    }

    @Test
    void returnsNoPeakWhenThereAreNoDeals() {
        RestaurantDealsService service = new RestaurantDealsService(List::of);

        assertTrue(service.findPeakTime().isEmpty());
    }

    private static boolean hasRestaurant(List<DealItem> deals, String name) {
        return deals.stream().anyMatch(deal -> deal.restaurantName().equals(name));
    }

}
