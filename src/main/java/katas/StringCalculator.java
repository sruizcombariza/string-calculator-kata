package katas;


import exceptions.NegativeNumberException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StringCalculator {
    public static final String REGEX = "\n";
    private static final Predicate<String> isSpecialDelimiter = delimiter -> delimiter.startsWith("//");
    private static final Predicate<Integer> majorToZero = num -> num >= 0;
    private static final Predicate<Integer> minorOrEqualTo1000 = num -> num <= 1000;
    private static final Predicate<Integer> negativeNum = majorToZero.negate();

    private static final String COMMA = ",";

    public Integer add(String input) {
        if (input.isEmpty()) {
            return 0;
        }

        String[] numbersArray = extractNumbersFromInput(input);
        validateNegativeNumbers(numbersArray);

        return sumNumbers(numbersArray);

    }

    private String[] extractNumbersFromInput(String input) {
        List<String> delimiters = new ArrayList<>();
        // Always allow comma and newline as delimiters
        delimiters.add(COMMA);
        delimiters.add(REGEX);

        String numbersSection = input;

        if (isSpecialDelimiter.test(input)) {
            String[] headerAndBody = input.split(REGEX, 2);
            List<String> customDelimiters = extractDelimiters(headerAndBody[0]);
            delimiters.addAll(customDelimiters);
            numbersSection = headerAndBody.length > 1 ? headerAndBody[1] : "";
        }

        String splitPattern = buildSplitPattern(delimiters);
        return numbersSection.split(splitPattern);
    }

    private List<String> extractDelimiters(String headerLine) {
        // headerLine is like "//;" or "//[***]" or "//[*][%]" or "//[**][%%]"
        String spec = headerLine.substring(2); // remove leading "//"
        List<String> result = new ArrayList<>();
        if (spec.startsWith("[")) {
            Matcher matcher = Pattern.compile("\\[(.*?)]").matcher(spec);
            while (matcher.find()) {
                result.add(matcher.group(1));
            }
            if (result.isEmpty()) {
                result.add(spec);
            }
        } else {
            result.add(spec);
        }
        return result;
    }

    private String buildSplitPattern(List<String> delimiters) {
        return delimiters.stream()
                .map(Pattern::quote)
                .collect(Collectors.joining("|"));
    }

    private int sumNumbers(String[] numbersArray) {
        return Arrays.stream(numbersArray)
                .map(this::convertToNumber)
                .filter(majorToZero)
                .filter(minorOrEqualTo1000)
                .reduce(0, Integer::sum);
    }

    private void validateNegativeNumbers(String[] numbersArray) {
        List<Integer> lisNegativeNumbers = getNegativeListNumbers(numbersArray);
        if (lisNegativeNumbers != null && !lisNegativeNumbers.isEmpty()) {
            String message = buildMessageNegativeNumbers(lisNegativeNumbers);
            throw new NegativeNumberException(message);
        }
    }

    private List<Integer> getNegativeListNumbers(String[] numbersArray) {
        return Arrays.stream(numbersArray)
                .map(this::convertToNumber)
                .filter(negativeNum)
                .collect(Collectors.toList());
    }

    private String buildMessageNegativeNumbers(List<Integer> lisNegativeNumbers) {
        return lisNegativeNumbers.stream()
                .map(String::valueOf).
                collect(Collectors.joining(" ", " ", ""));
    }

    private int convertToNumber(String num) {
        try {
            return Integer.parseInt(num);
        } catch (Exception e) {
            return 0;
        }
    }


}
