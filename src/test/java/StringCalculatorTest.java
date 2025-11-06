import exceptions.NegativeNumberException;
import katas.StringCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StringCalculatorTest {

    private final StringCalculator stringCalculator = new StringCalculator();

    @BeforeEach
    void setUp() {

    }

    @Test
    void givenEmptyStringWhenAddThenReturnZero() {
        // given
        StringCalculator stringCalculator = this.stringCalculator;
        // when
        Integer actual = stringCalculator.add("");
        // then
        assertThat(actual).isZero();
    }

    @Test
    void givenStringWithNumberWhenAddThenReturnNumber() {
        // given
        StringCalculator stringCalculator = this.stringCalculator;
        // when
        Integer actual = stringCalculator.add("4");
        // then
        assertThat(actual).isEqualTo(4);
    }

    @Test
    void givenStringWithNewLinesWhenAddThenReturnAddedNumber() {
        // given
        StringCalculator stringCalculator = this.stringCalculator;
        // when
        Integer actual = stringCalculator.add("1\n2,3");
        // then
        assertThat(actual).isEqualTo(6);
    }

    @Test
    void givenStringWithDifferentDelimitersWhenAddThenReturnAddedNumber() {
        // given
        StringCalculator stringCalculator = this.stringCalculator;
        // when
        Integer actual = stringCalculator.add("//;\n1;2");
        // then
        assertThat(actual).isEqualTo(3);
    }

    @Test
    void givenStringWithNegativeNumbersWhenAddThenThrowException() {
        assertThatThrownBy(() -> {
            // given
            StringCalculator stringCalculator = this.stringCalculator;
            // when
            stringCalculator.add("-1,-2");
            // then
        }).isInstanceOf(NegativeNumberException.class)
                .hasMessageContaining("negatives not allowed: -1 -2");
    }

    @Test
    void givenStringWithNumbersBiggerThan1000WhenAddThenNoSum() {
        // given
        StringCalculator stringCalculator = this.stringCalculator;
        // when
        Integer actual = stringCalculator.add("//;\n2;1001");
        // then
        assertThat(actual).isEqualTo(2);
    }

    // Step 2: unknown amount of numbers
    @Test
    void givenUnknownAmountOfNumbersWhenAddThenReturnSum() {
        // given
        StringCalculator stringCalculator = this.stringCalculator;
        // when
        Integer actual = stringCalculator.add("1,2,3");
        // then
        assertThat(actual).isEqualTo(6);
    }

    // Step 6 (default delimiter): numbers bigger than 1000 should be ignored
    @Test
    void givenDefaultDelimiterAndNumberBiggerThan1000WhenAddThenIgnored() {
        // given
        StringCalculator stringCalculator = this.stringCalculator;
        // when
        Integer actual = stringCalculator.add("2,1001");
        // then
        assertThat(actual).isEqualTo(2);
    }

    // Step 8: multiple single-char delimiters
    @Test
    void givenMultipleSingleCharDelimitersWhenAddThenReturnSum() {
        // given
        StringCalculator stringCalculator = this.stringCalculator;
        // when
        Integer actual = stringCalculator.add("//[*][%]\n1*2%3");
        // then
        assertThat(actual).isEqualTo(6);
    }

    @Test
    void givenStringWithLongDelimiterWhenAddThenReturnSum() {
        // given
        StringCalculator stringCalculator = this.stringCalculator;
        // when
        Integer actual = stringCalculator.add("//[***]\n1***2***3");
        // then
        assertThat(actual).isEqualTo(6);
    }

    // Step 9: multiple multi-char delimiters
    @Test
    void givenMultipleMultiCharDelimitersWhenAddThenReturnSum() {
        // given
        StringCalculator stringCalculator = this.stringCalculator;
        // when
        Integer actual = stringCalculator.add("//[**][%%]\n1**2%%3");
        // then
        assertThat(actual).isEqualTo(6);
    }

}