import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, Josh Little
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        substring("", letters, 0);
    }
    public void substring(String currentString, String letters, int location) {
        if (letters.isEmpty() || location == letters.length()) {
            permute("", currentString);
            return;
        }
        substring(currentString, letters, location + 1);
        substring(currentString + letters.substring(location, location + 1), letters, location + 1);
    }

    public void permute(String word, String letters) {
        if (letters.isEmpty()) {
            return;
        }
        for (int i = 0; i < letters.length(); i++) {
            words.add(word + letters.substring(i, i +1));
            permute(word + letters.substring(i, i +1), letters.substring(0, i) + letters.substring(i + 1, letters.length()));
        }
    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        words = mergeSort(words, 0 , words.size());
    }

    public ArrayList<String> mergeSort(ArrayList<String> words, int low, int high) {
         if (low == high) {
            ArrayList<String> tempArr = new ArrayList<String>();
            if (low == words.size()) {
                tempArr.add(words.get(low - 1));
                return tempArr;
            }
            tempArr.add(words.get(low));
            return tempArr;
        }

        int mid = (high + low) / 2;
        ArrayList<String> arr1 =  mergeSort(words, low, mid);
        ArrayList<String> arr2 = mergeSort(words, mid + 1, high);
        return merge(arr1, arr2);
    }

    public ArrayList<String> merge(ArrayList<String> arr1, ArrayList<String> arr2) {
        ArrayList<String> stuff = new ArrayList<String>();
        int i = 0, j = 0;
        while (i < arr1.size() && j < arr2.size()) {
            if (arr1.get(i).compareTo(arr2.get(j)) >= 0) {
                stuff.add(arr2.get(j++));
            }
            else if (arr1.get(i).compareTo(arr2.get(j)) < 0) {
                stuff.add(arr1.get(i++));
            }
        }
        while (i >= arr1.size() && j < arr2.size()) {
            stuff.add(arr2.get(j++));
        }
        while (i < arr1.size() && j >= arr2.size()) {
            stuff.add(arr1.get(i++));
        }
        return stuff;
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        // YOUR CODE HERE
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
