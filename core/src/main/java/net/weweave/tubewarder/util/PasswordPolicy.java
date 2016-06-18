package net.weweave.tubewarder.util;

import java.util.regex.Pattern;

public class PasswordPolicy {
    private static final Pattern PATTERN = Pattern.compile(
            "(?=.{8,})" +       // At least 8 chars
            "(?=.*[0-9])" +     // At least 1 number
            "(?=.*?[a-z])" +    // At least 1 lowercase char
            "(?=.*?[A-Z])" +    // At least 1 uppercase char
            ".*");

    public static boolean matches(String password) {
        if (password == null) {
            return false;
        }
        return PATTERN.matcher(password).matches();
    }
}
