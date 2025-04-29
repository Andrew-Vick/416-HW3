package question1;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

/**
 * This program generates and prints all permutations of a given input string
 * using a recursive approach, following clean code principles.
 */
public class RecursiveStringPermutations {

    public static void main(String[] args) {
        String input = readInput();
        if (!isValidInput(input)) {
            System.out.println("Invalid input: Input must be a non-empty string containing only characters.");
            return;
        }

        List<String> permutations = new ArrayList<>();
        generatePermutations("", input, permutations);
        printPermutations(permutations);
    }

    /**
     * Reads input from the user.
     *
     * @return the input string entered by the user
     */
    private static String readInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a string to permute: ");
        String input = scanner.nextLine();
        scanner.close();
        return input;
    }

    /**
     * Validates the input to ensure it is non-empty and only contains characters.
     *
     * @param input the input string to validate
     * @return true if input is valid, false otherwise
     */
    private static boolean isValidInput(String input) {
        return input != null && !input.trim().isEmpty();
    }

    /**
     * Recursively generates all permutations of the remaining characters,
     * building up the current permutation.
     *
     * @param current      the current built-up string
     * @param remaining    the remaining characters left to permute
     * @param permutations the list to collect all full permutations
     */
    private static void generatePermutations(String current, String remaining, List<String> permutations) {
        if (remaining.isEmpty()) {
            permutations.add(current);
            return;
        }

        // Try inserting each character into the current permutation
        for (int i = 0; i < remaining.length(); i++) {
            char ch = remaining.charAt(i);
            String before = remaining.substring(0, i);
            String after = remaining.substring(i + 1);
            String nextRemaining = before + after;

            generatePermutations(current + ch, nextRemaining, permutations);
        }
    }

    /**
     * Prints all the permutations from the list.
     *
     * @param permutations the list of permutations to print
     */
    private static void printPermutations(List<String> permutations) {
        System.out.println("Generated Permutations:");
        for (String perm : permutations) {
            System.out.println(perm);
        }
    }
}
