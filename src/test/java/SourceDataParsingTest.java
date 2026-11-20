import com.anthonylimanto.restaurant_deals_task.source.Deal;
import com.anthonylimanto.restaurant_deals_task.source.Restaurant;
import com.anthonylimanto.restaurant_deals_task.source.RestaurantList;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SourceDataParsingTest {

    @Test
    void parsesAllRestaurantsAndDeals() {
        RestaurantList data = parseFixture();

        assertEquals(6, data.restaurants().size());

        int dealCount = data.restaurants().stream()
                .mapToInt(restaurant -> restaurant.deals().size())
                .sum();
        assertEquals(11, dealCount);
    }

    private RestaurantList parseFixture() {
        InputStream json = getClass().getResourceAsStream("/challengedata.json");
        return JsonMapper.builder().build().readValue(json, RestaurantList.class);
    }
}
