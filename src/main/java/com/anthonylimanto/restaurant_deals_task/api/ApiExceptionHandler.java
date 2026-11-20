package com.anthonylimanto.restaurant_deals_task.api;

import com.anthonylimanto.restaurant_deals_task.exception.InvalidTimeOfDayException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;
import org.springframework.http.HttpStatus;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(InvalidTimeOfDayException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleInvalidTimeOfDay(InvalidTimeOfDayException e) {
        return new ApiError("INVALID_TIME_OF_DAY", e.getMessage());
    }

    @ExceptionHandler(RestClientException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ApiError handleSourceFailure(RestClientException e) {
        return new ApiError("SOURCE_UNAVAILABLE", "The restaurant data source did not respond.");
    }
}
