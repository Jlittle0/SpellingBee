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
        // Calls the substring method
        substring("", letters, 0);
    }
    public void substring(String currentString, String letters, int location) {
        /*
            Base case for recursive step that returns/exits if letters is empty or once location
            aka the variable tracing the current letter being included/excluded is the last letter
        */
        if (letters.isEmpty() || location == letters.length()) {
            // Base case called the permutation method on the currentString that holds all
            // letters that were included from the original set of letters in this recursion
            permute("", currentString);
            return;
        }
        /*
            Recursive step has two parts, one that decides to not include the current letter that
            location would be pointing to, and another that does decide to include it.
        */
        substring(currentString, letters, location + 1);
        substring(currentString + letters.substring(location, location + 1), letters, location + 1);
    }

    public void permute(String word, String letters) {
        // Base case for the permutation method that returns once letters is empty.
        if (letters.isEmpty()) {
            return;
        }
        for (int i = 0; i < letters.length(); i++) {
            /*
                 For each possible combination of the letters provided, add the permutation to the
                 list of words and then recurse on the new word as well as removing the used letter
                 from the letters string that contains the combination given by substring.
            */
            words.add(word + letters.substring(i, i +1));
            permute(word + letters.substring(i, i +1), letters.substring(0, i) + letters.substring(i + 1, letters.length()));
        }
    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        // Calls the mergeSort method and sets words equal to the returned sorted arraylist
        words = mergeSort(words, 0 , words.size());
    }

    public ArrayList<String> mergeSort(ArrayList<String> words, int low, int high) {
        // Base case that returns each individual word
         if (low == high) {
            ArrayList<String> tempArr = new ArrayList<String>();
            // If the index would create an out of bounds error, return the word before in the list
             // TODO - Make this more correct by preventing this issue from ever occuring
            if (low == words.size()) {
                tempArr.add(words.get(low - 1));
                return tempArr;
            }
            tempArr.add(words.get(low));
            return tempArr;
        }

        int mid = (high + low) / 2;
         // Two recursive parts in each call, each covering half of the current words but eventually
        // returning a single string for each recursive call that is then merged in merge method
        ArrayList<String> arr1 =  mergeSort(words, low, mid);
        ArrayList<String> arr2 = mergeSort(words, mid + 1, high);
        return merge(arr1, arr2);
    }

    public ArrayList<String> merge(ArrayList<String> arr1, ArrayList<String> arr2) {
        ArrayList<String> stuff = new ArrayList<String>();
        int i = 0, j = 0;
        // While neither of the indices are greater than the number of elements in each array
        // compare the two array contents and organize them by adding them to stuff.
        while (i < arr1.size() && j < arr2.size()) {
            // If the current element in the second array is first alphabetically or the same,
            // add it to stuff and increment j to move onto the next element for 2nd array.
            if (arr1.get(i).compareTo(arr2.get(j)) >= 0) {
                stuff.add(arr2.get(j++));
            }
            // Same thing as the if statement above except for when the word in the first array
            // come first alphabetically and i is incremeneted.
            else if (arr1.get(i).compareTo(arr2.get(j)) < 0) {
                stuff.add(arr1.get(i++));
            }
        }
        // Once one of the lists has no more words to add into the array, check the other and
        // add the remaining words in their sorted order.
        while (i >= arr1.size() && j < arr2.size()) {
            stuff.add(arr2.get(j++));
        }
        while (i < arr1.size() && j >= arr2.size()) {
            stuff.add(arr1.get(i++));
        }
        // return the merged list.
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
        // Calls binarySearch on each word in words to check if it belogns to the dictionary
        // and removing it if it doesn't.
        for (int i = 0; i < words.size(); i++) {
            if (binarySearch(words.get(i), DICTIONARY) == false) {
                words.remove(words.get(i));
                i--;
            }
        }
    }

    public boolean binarySearch(String word, String[] dictionary) {
        // Calls the search method that actually does stuff and recurses
        return search(word, dictionary, 0, dictionary.length - 1);
    }

    public boolean search(String word, String[] dictionary, int low, int high) {
        // Base case where low is greater than high aka the word isn't present in the dictionary
        if (low > high) {
            return false;
        }
        int med = (high + low) / 2 ;
        // If the word is in the dictionary, return true
        if (dictionary[med].equals(word)) {
            return true;
        }
        // If the word is after the med index of dictionary, move to the greater/further half
        if (dictionary[med].compareTo(word) < 0) {
             return search(word, dictionary, med + 1, high);
        }
        // Otherwise, move to the lesser/sooner half of the dictionary and recurse
        else {
            return search(word, dictionary, low, med - 1);
        }
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
