import java.util.Scanner;

public class WordCounter {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter a sentence: ");
        String sentence = sc.nextLine();

        // Count words
        String[] words = sentence.split(" ");
        int wordCount = words.length;

        // Count characters (excluding spaces)
        int charCount = 0;
        int vowelCount = 0;

        for (int i = 0; i < sentence.length(); i++) {
            char ch = sentence.charAt(i);

            if (ch != ' ') {
                charCount++;
            }

            ch = Character.toLowerCase(ch);
            if (ch == 'a' || ch == 'e' || ch == 'i' ||
                ch == 'o' || ch == 'u') {
                vowelCount++;
            }
        }

        System.out.println("Total Characters (without spaces): " + charCount);
        System.out.println("Total Words: " + wordCount);
        System.out.println("Total Vowels: " + vowelCount);

       
    }
}