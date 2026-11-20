package com.anthonylimanto.restaurant_deals_task.source;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Deal(
        String objectId,
        String discount,
        String dineIn,
        String lightning,
        String qtyLeft,
        String open,
        String close,
        String start,
        String end
) {}