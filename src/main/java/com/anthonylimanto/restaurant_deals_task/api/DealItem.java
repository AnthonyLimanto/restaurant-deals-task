package com.anthonylimanto.restaurant_deals_task.api;

public record DealItem(
        String restaurantObjectId,
        String restaurantName,
        String restaurantAddress1,
        // In the task spec it asks for the object to have "restarantSuburb" instead of "restaurantSuburb".
        // However, I believe this to be a typo, so I have fixed it accordingly, just pointing it out since it differs
        // from what the spec asks for
        String restaurantSuburb,
        String restaurantOpen,
        String restaurantClose,
        String dealObjectId,
        String discount,
        String dineIn,
        String lightning,
        String qtyLeft
) {}
