package katas;


import exceptions.NegativeNumberException;

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
    private static final Predicate<Integer> minorTo1000 = num -> num < 1000;
    private static final Predicate<Integer> negativeNum = majorToZero.negate();

    private String defaultDelimiter = ",";

    public Integer add(String input) {
        if (input.isEmpty()) {
            return 0;
        }

        String[] numbersArray = extractNumbersFromInput(input);
        validateNegativeNumbers(numbersArray);

        return sumNumbers(numbersArray);

    }

    private String[] extractNumbersFromInput(String input) {
        if (isSpecialDelimiter.test(input)) {
            String[] arrayNumbers = input.split(REGEX);
            defaultDelimiter = extractDelimiter(arrayNumbers[0]);
            input = arrayNumbers[1];
        } else {
            input = input.replace(REGEX, defaultDelimiter);
        }
        String escapedDelimiter = Pattern.quote(defaultDelimiter);    
        return input.split(escapedDelimiter);
    }

    private String extractDelimiter(String input) {
        Pattern pattern = Pattern.compile("^//(.*)");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            String delimiter = matcher.group(1);
            if (delimiter.startsWith("[")) {
                pattern = Pattern.compile("\\[(.*?)\\]");
                matcher = pattern.matcher(delimiter);
                if (matcher.find()) {
                    return matcher.group(1);
                }
            } else {
                return delimiter;
            }
        }
        return input;
    }

    private int sumNumbers(String[] numbersArray) {
        return Arrays.stream(numbersArray)
                .map(this::convertToNumber)
                .filter(majorToZero)
                .filter(minorTo1000)
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
