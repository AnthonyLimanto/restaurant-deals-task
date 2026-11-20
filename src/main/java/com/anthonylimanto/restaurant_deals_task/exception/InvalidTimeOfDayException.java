package com.anthonylimanto.restaurant_deals_task.exception;

public class InvalidTimeOfDayException extends RuntimeException {
    public InvalidTimeOfDayException(String text) {
        super("Invalid timeOfDay: '" + text + "'. Use a form such as 3:00pm or 15:00.");
    }
}
