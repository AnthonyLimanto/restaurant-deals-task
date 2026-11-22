# Restaurant Deals API

Java 21 and Spring Boot. Reads https://eccdn.com.au/misc/challengedata.json.

## Run

    ./mvnw spring-boot:run
    ./mvnw test

## Endpoints

- Task 1: `GET /deals?timeOfDay=3:00pm` (also accepts `15:00`). An invalid time returns 400.
- Task 2: `GET /peak`
- Task 3: [schema.md](schema.md)

## Results

3:00pm: 6 deals. 6:00pm: 9 deals. 9:00pm: 4 deals. 

Peak: 5:00pm to 9:00pm.

## Assumptions

- The source data is correct, deals that are available outside of restaurant hours are valid.
- Deal times come from `open`/`close`, then `start`/`end`, then the restaurant hours.
- The start time of a deal is inclusive and the end is exclusive.
- The peak is the first window with the maximum number of active deals.
  It assumes that no window crosses midnight.
- The task spec spells one field `restarantSuburb`. I assumed was a typo and fixed it as `restaurantSuburb`.


## Project structure

- `controller`: the HTTP endpoints.
- `service`: the deal and peak logic.
- `source`: the upstream data shape and the HTTP client. `RestaurantSource`
  is an interface, so the tests use a saved file and not the network.
- `api`: the response records and the error handling.
- `helper`: time parsing and formatting.