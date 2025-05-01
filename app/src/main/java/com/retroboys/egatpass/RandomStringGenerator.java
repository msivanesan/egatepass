package com.retroboys.egatpass;
import java.security.SecureRandom;

public class RandomStringGenerator {

    // Define the characters that can appear in the random string
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    // SecureRandom instance to generate a secure random string
    private static final SecureRandom random = new SecureRandom();

    // Method to generate a random string of 10 characters
    public static String generateRandomString() {
        StringBuilder result = new StringBuilder(10);

        for (int i = 0; i < 10; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            result.append(CHARACTERS.charAt(randomIndex));
        }

        return result.toString();
    }

}
