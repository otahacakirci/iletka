package com.iletka.event;

/**
 * Immutable, case-sensitive name of an event kind, for example {@code report.completed}.
 *
 * <p>Policy, enforced at construction:
 * <ul>
 *   <li>1 to 128 characters, only ASCII letters, digits, {@code .}, {@code _} and {@code -};
 *   <li>the first and last character are an ASCII letter or digit;
 *   <li>adjacent separators are allowed only when identical and not a dot, so {@code a__b} and
 *       {@code a--b} are valid while {@code a..b}, {@code a._b} and {@code a_-b} are not.
 * </ul>
 *
 * <p>The accepted text is kept exactly as given: no trimming, case folding or other normalization.
 * Equality and hash code are based on that exact text.
 *
 * @param value the accepted event type text
 * @throws InvalidEventTypeException if the text violates the policy
 */
public record EventType(String value) {

    static final int MAX_LENGTH = 128;

    public EventType {
        validate(value);
    }

    @Override
    public String toString() {
        return value;
    }

    private static void validate(String text) {
        if (text == null) {
            throw new InvalidEventTypeException("Event type must not be null");
        }
        if (text.isEmpty()) {
            throw new InvalidEventTypeException("Event type must not be empty");
        }
        if (text.length() > MAX_LENGTH) {
            throw new InvalidEventTypeException(
                    "Event type must be at most " + MAX_LENGTH + " characters but was " + text.length());
        }
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (!isAlphanumeric(c) && !isSeparator(c)) {
                throw new InvalidEventTypeException(String.format(
                        "Event type may contain only ASCII letters, digits, '.', '_' and '-' "
                                + "but has U+%04X at index %d", (int) c, i));
            }
        }
        if (!isAlphanumeric(text.charAt(0))) {
            throw new InvalidEventTypeException("Event type must start with an ASCII letter or digit");
        }
        if (!isAlphanumeric(text.charAt(text.length() - 1))) {
            throw new InvalidEventTypeException("Event type must end with an ASCII letter or digit");
        }
        for (int i = 1; i < text.length(); i++) {
            char previous = text.charAt(i - 1);
            char current = text.charAt(i);
            if (isSeparator(previous) && isSeparator(current)) {
                if (previous == '.' && current == '.') {
                    throw new InvalidEventTypeException(
                            "Event type must not contain consecutive dots (at index " + i + ")");
                }
                if (previous != current) {
                    throw new InvalidEventTypeException("Event type must not place different separators "
                            + "next to each other ('" + previous + current + "' at index " + (i - 1) + ")");
                }
            }
        }
    }

    private static boolean isAlphanumeric(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9');
    }

    private static boolean isSeparator(char c) {
        return c == '.' || c == '_' || c == '-';
    }
}
