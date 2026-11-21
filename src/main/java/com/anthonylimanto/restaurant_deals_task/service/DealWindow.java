package com.anthonylimanto.restaurant_deals_task.service;

import java.time.LocalTime;

public record DealWindow(
        LocalTime start,
        LocalTime end
) {}
