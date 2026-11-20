package com.anthonylimanto.restaurant_deals_task.source;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.json.JsonMapper;

import java.time.Duration;
import java.util.List;

@Component
public class HttpRestaurantSource implements RestaurantSource {

    private final RestClient restClient;
    private final String sourceUrl;
    private final JsonMapper jsonMapper = JsonMapper.builder().build();

    public HttpRestaurantSource(@Value("${restaurant-source.url}") String sourceUrl) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(5));
        requestFactory.setReadTimeout(Duration.ofSeconds(5));
        this.restClient = RestClient.builder().requestFactory(requestFactory).build();
        this.sourceUrl = sourceUrl;
    }

    @Override
    public List<Restaurant> fetchRestaurants() {
        String json = restClient.get().uri(sourceUrl).retrieve().body(String.class);
        return jsonMapper.readValue(json, RestaurantList.class).restaurants();
    }
}