package com.iletka.event;

/**
 * Signals that a text violates the {@link EventType} validation policy.
 *
 * <p>It extends {@link IllegalArgumentException} because the cause is always an unacceptable
 * argument supplied by the caller.
 */
public class InvalidEventTypeException extends IllegalArgumentException {

    public InvalidEventTypeException(String message) {
        super(message);
    }
}
