package com.iletka.event;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class EventTypeTest {

    private static String repeat(String part, int times) {
        return part.repeat(times);
    }

    private static void assertRejected(String text) {
        assertThatThrownBy(() -> new EventType(text)).isInstanceOf(InvalidEventTypeException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "report.completed", "Report.Completed", "user_created", "user-created", "v2.invoice.paid",
            "a", "Z", "0", "9", "a1", "1a",
            "a__b", "a___b", "a--b", "a---b", "a.b.c", "a_b-c.d", "a__b--c.d", "a-b_c",
            "007.event", "event.007"
    })
    void acceptsValidNamesAndKeepsTheExactText(String text) {
        EventType eventType = new EventType(text);

        assertThat(eventType.value()).isEqualTo(text);
        assertThat(eventType).hasToString(text);
    }

    @Test
    void preservesLetterCase() {
        assertThat(new EventType("Report.Completed").value()).isEqualTo("Report.Completed");
    }

    @ParameterizedTest
    @ValueSource(strings = {"a._b", "a.-b", "a_.b", "a-.b", "a_-b", "a-_b"})
    void rejectsEverySeparatorPairOfDifferentKinds(String text) {
        assertRejected(text);
    }

    @ParameterizedTest
    @ValueSource(strings = {"a..b", "a...b"})
    void rejectsConsecutiveDots(String text) {
        assertRejected(text);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            ".a", "_a", "-a", "a.", "a_", "a-", ".", "_", "-", "..", "__", "--",
            "_a_", ".a.", "-a-"
    })
    void rejectsSeparatorsAtTheStartOrEnd(String text) {
        assertRejected(text);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""})
    void rejectsNullAndEmptyText(String text) {
        assertRejected(text);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            " ", "  ", "\t", "\n", "\r\n", " a", "a ", " a ", "a b", "a\tb", "a\nb",
            "report.completed\n", "report.completed\r\n", "\nreport.completed", "report.completed\t"
    })
    void rejectsWhitespaceAnywhere(String text) {
        assertRejected(text);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "a\b", "a\\b", "a/b", "a:b", "a@b", "a+b", "a*b", "a#b", "a$b", "a,b", "a;b", "a=b",
            "a\"b", "a'b", "a~b", "a!b", "a?b", "a(b", "a|b", "a%b",
            "şarkı", "rapor.tamamlandı", "İstanbul", "ğüşiöç", "a.ç",
            "é", "é", "ａbc", "日本語", "aéb"
    })
    void rejectsCharactersOutsideTheAllowedSet(String text) {
        assertRejected(text);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "a\u0000b", "a\u0001b", "a\u001fb", "a\u007fb", "a\u0080b", "a\u009fb",
            "a b", "a​b", "a‍b", "a⁠b", "a﻿b", "﻿report", "report​",
            "a‮b", "a b", "a b", "a　b", "a­b", "a😀b"
    })
    void rejectsControlAndInvisibleUnicodeCharacters(String text) {
        assertRejected(text);
    }

    static Stream<Arguments> lengthBoundaries() {
        return Stream.of(
                Arguments.of(repeat("a", 127), true),
                Arguments.of(repeat("a", 128), true),
                Arguments.of(repeat("a", 129), false),
                Arguments.of("a" + repeat("_", 126) + "a", true),
                Arguments.of("a" + repeat("_", 127) + "a", false),
                Arguments.of("a" + repeat("-", 126) + "a", true),
                Arguments.of("a" + repeat("-", 127) + "a", false),
                Arguments.of(repeat("a.", 63) + "aa", true),
                Arguments.of(repeat("a.", 64) + "a", false)
        );
    }

    @ParameterizedTest
    @MethodSource("lengthBoundaries")
    void enforcesLengthBoundaryOf128Characters(String text, boolean valid) {
        if (valid) {
            assertThat(text).hasSizeLessThanOrEqualTo(128);
            assertThat(new EventType(text).value()).isEqualTo(text);
        } else {
            assertThat(text.length()).isGreaterThan(128);
            assertRejected(text);
        }
    }

    @Test
    void lengthChecksReportTheLimit() {
        assertThatThrownBy(() -> new EventType("a".repeat(129)))
                .isInstanceOf(InvalidEventTypeException.class)
                .hasMessageContaining("128")
                .hasMessageContaining("129");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "a__-b", "a--_b", "a__.b", "a--.b", "a.__b", "a.--b", "a_-_b", "a-_-b",
            "a___-b", "a---_b", "a__b--_c", "a_--b"
    })
    void rejectsLongSeparatorRunsContainingADifferentSeparator(String text) {
        assertRejected(text);
    }

    @Test
    void acceptsDifferentSeparatorKindsWhenLettersSeparateThem() {
        assertThat(new EventType("a__b--c.d").value()).isEqualTo("a__b--c.d");
    }

    @Test
    void sameTextProducesEqualValuesWithTheSameHashCode() {
        EventType first = new EventType("report.completed");
        EventType second = new EventType(new String("report.completed"));

        assertThat(first).isEqualTo(second);
        assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }

    @Test
    void valuesDifferingOnlyInCaseAreNotEqual() {
        assertThat(new EventType("Report.Completed")).isNotEqualTo(new EventType("report.completed"));
    }

    @Test
    void differentTextsAreNotEqual() {
        assertThat(new EventType("report.completed")).isNotEqualTo(new EventType("report.failed"));
    }

    @Test
    void validationErrorsNameTheViolatedRule() {
        assertThatThrownBy(() -> new EventType(null)).hasMessageContaining("null");
        assertThatThrownBy(() -> new EventType("")).hasMessageContaining("empty");
        assertThatThrownBy(() -> new EventType("a b")).hasMessageContaining("U+0020");
        assertThatThrownBy(() -> new EventType(".a")).hasMessageContaining("start");
        assertThatThrownBy(() -> new EventType("a.")).hasMessageContaining("end");
        assertThatThrownBy(() -> new EventType("a..b")).hasMessageContaining("dots");
        assertThatThrownBy(() -> new EventType("a_-b")).hasMessageContaining("different separators");
    }
}
