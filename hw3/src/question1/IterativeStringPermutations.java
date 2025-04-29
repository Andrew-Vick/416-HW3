package question1;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This program generates and prints all permutations of a given input string
 * using an iterative approach without recursion or special libraries.
 */
public class IterativeStringPermutations {

    public static void main(String[] args) {
        String input = readInput();
        if (!isValidInput(input)) {
            System.out.println("Invalid input: Input must be a non-empty string containing only characters.");
            return;
        }

        List<String> permutations = generatePermutations(input);
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
     * Generates all permutations of the input string iteratively.
     *
     * @param input the string for which to generate permutations
     * @return a list containing all permutations of the input string
     */
    private static List<String> generatePermutations(String input) {
        List<String> permutations = new ArrayList<>();
        permutations.add(""); // Start with an empty permutation

        // Iteratively build permutations by adding one character at a time
        for (char currentChar : input.toCharArray()) {
            List<String> newPermutations = new ArrayList<>();

            for (String perm : permutations) {
                for (int pos = 0; pos <= perm.length(); pos++) {
                    String newPerm = insertCharAtPosition(perm, currentChar, pos);
                    newPermutations.add(newPerm);
                }
            }

            // Update permutations with newly created ones
            permutations = newPermutations;
        }

        return permutations;
    }

    /**
     * Inserts a character at a specific position in a string.
     *
     * @param str       the original string
     * @param ch        the character to insert
     * @param position  the position at which to insert the character
     * @return a new string with the character inserted
     */
    private static String insertCharAtPosition(String str, char ch, int position) {
        String before = str.substring(0, position);
        String after = str.substring(position);
        return before + ch + after;
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
