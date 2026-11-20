package com.anthonylimanto.restaurant_deals_task.source;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Restaurant(
        String objectId,
        String name,
        String address1,
        String suburb,
        List<String> cuisines,
        String imageLink,
        String open,
        String close,
        List<Deal> deals
) {}
