package com.computingfacts.api.util;

/**
 *
 * @author Joseph
 */
public final class EcValidator {

    private EcValidator() {
    }

    private static final String VALID_EC_REGEX = "([1-7](\\.n?[\\d\\-]+){3})";

    public static boolean validateEc(String ec) {
        boolean isValid = false;

        if (ec.matches(VALID_EC_REGEX)) {

            String[] digits = ec.split("\\.");
            boolean invalid = digits[1].equals("-") && !digits[2].equals("-") && !digits[3].equals("-")
                    || digits[2].equals("-") && !digits[3].equals("-");

            isValid = !invalid;
        }
        return isValid;

    }

    public static String transformIncompleteEc(String ec) {

        String[] digits = ec.split("\\.");

        if (digits[0].equals("-")) {
            return ec;
        } else if (digits[1].equals("-")) {

            return ec;
        } else if (digits[2].equals("-")) {
            ec = digits[0] + "." + digits[1] + ".*";
            return ec;
        } else if (digits[3].equals("-")) {
            ec = digits[0] + "." + digits[1] + "." + digits[2] + ".*";
            return ec;
        }

        return ec;

    }

}
