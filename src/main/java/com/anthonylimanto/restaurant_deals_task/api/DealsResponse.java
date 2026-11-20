package com.anthonylimanto.restaurant_deals_task.api;

import java.util.List;

public record DealsResponse(
        List<DealItem> deals
) {}
