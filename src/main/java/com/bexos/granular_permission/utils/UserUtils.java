package com.bexos.granular_permission.utils;

import java.security.SecureRandom;
import java.util.Random;

public class UserUtils {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int PASSWORD_LENGTH = 8;
    private static final Random RANDOM = new SecureRandom();

    public static String generateRandomPassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            password.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return password.toString();
    }

    public static String extractUsername(String email) {
        return email.split("@")[0];
    }
}
