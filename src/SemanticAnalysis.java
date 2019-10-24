import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class SemanticAnalysis {
    public static void main(String[] args) {
        Document.findDocument("C:\\Users\\kyled\\IdeaProjects\\MV_Collab_Proj\\data\\Corpus (Testing) - Copy.txt");

    }

    public int countOccurences(String text, String target) {
        int count = 0;

        for(int index = text.indexOf(target); index != -1; index = text.indexOf(target, index + 1)) {
            ++count;
        }

        return count;
    }

    public ArrayList<Integer> occuranceIndex(String text,String target) {
        ArrayList<Integer> answer = new ArrayList();

        for(int index = text.indexOf(target); index != -1; index = text.indexOf(target, index + 1)) {
            answer.add(index);
        }

        return answer;
    }
    public boolean coOccurWithinDist(String text,String target_1, String target_2, int distance) {
        for(int i = 0; i < text.length() - distance; ++i) {
            String checkArea = text.substring(i, i + distance + 1);
            if (checkArea.contains(target_1) && checkArea.contains(target_2)) {
                return true;
            }
        }

        return false;
    }
    private static boolean wordsContain(String input, String search) {
        return (input.contains(search));
    }
    private static ArrayList<String> getUniqueWords(ArrayList<String> words) {
        ArrayList<String> uniqueWords = new ArrayList();
        Iterator var2 = words.iterator();

        while(var2.hasNext()) {
            String word = (String)var2.next();
            if (!uniqueWords.contains(word)) {
                uniqueWords.add(word);
            }
        }

        return uniqueWords;
    }

    private static ArrayList<String> inputToWords(String input) {
        ArrayList<String> answer = new ArrayList<>();

        input = input.toLowerCase();
        input = removePunctuation(input);
        String[] words = input.split(" ");
        addtoarraylist(answer, words);

        return answer;
    }

    private static void addtoarraylist(ArrayList<String> answer, String[] words) {
        for (String word:words) {
            if (!word.equals("")) {
                answer.add(word);
            }
        }
    }

    private static String removePunctuation(String sentence) {
        String answer = "";

        for(int i = 0; i < sentence.length(); ++i) {
            String letter = sentence.substring(i, i + 1);
            if (isletter(letter)) {
                answer = answer + letter;
            } else {
                answer = answer + " ";
            }
        }

        return answer;
    }

    private static boolean isletter(String letter) {
        String characters = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        return characters.contains(letter);
    }
}
