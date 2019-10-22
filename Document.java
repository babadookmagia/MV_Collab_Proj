import java.util.ArrayList;
import java.util.Iterator;

public class Document {
    private String text;
    private ArrayList<String> words;
    private ArrayList<String> sentences;
    private ArrayList<String> uniqueWords;
    private boolean updated;

    public Document(String text) {
        this.text = text;
        this.words = this.getWordlist(text);
        this.sentences = TextLib.splitIntoSentences(text);
        this.uniqueWords = getUniqueWords(this.words);
        this.updated = true;
    }

    public static Document findDocument(String filename) {
        return new Document(TextLib.readFileAsString(filename));
    }

    public String getText() {
        return this.text;
    }

    public int getWordCount() {
        return this.words.size();
    }

    public int getSentenceCount() {
        return this.sentences.size();
    }

    public double getAvgWordsPerSentence() {
        return (double)this.words.size() / (double)this.sentences.size();
    }

    public double getAvgCharPerWord() {
        double totalChar = 0.0D;

        String word;
        for(Iterator var3 = this.words.iterator(); var3.hasNext(); totalChar += (double)word.length()) {
            word = (String)var3.next();
        }

        return totalChar / (double)this.words.size();
    }

    public int getVocabSize() {
        return this.uniqueWords.size();
    }

    private ArrayList<String> getVocabList() {
        return this.uniqueWords;
    }

    public double getFleschKincaidScore() {
        return FKReadability(this.sentences);
    }

    public int countOccurences(String target) {
        int count = 0;

        for(int index = this.text.indexOf(target); index != -1; index = this.text.indexOf(target, index + 1)) {
            ++count;
        }

        return count;
    }

    public ArrayList<Integer> occuranceIndex(String target) {
        ArrayList<Integer> answer = new ArrayList();

        for(int index = this.text.indexOf(target); index != -1; index = this.text.indexOf(target, index + 1)) {
            answer.add(index);
        }

        return answer;
    }

    public boolean sentenceCoOccur(String target_1, String target_2) {
        Iterator var3 = this.sentences.iterator();

        String sentence;
        do {
            if (!var3.hasNext()) {
                return false;
            }

            sentence = (String)var3.next();
        } while(!sentence.contains(target_1) || !sentence.contains(target_2));

        return true;
    }

    public boolean coOccurWithinDist(String target_1, String target_2, int distance) {
        for(int i = 0; i < this.text.length() - distance; ++i) {
            String checkArea = this.text.substring(i, i + distance + 1);
            if (checkArea.contains(target_1) && checkArea.contains(target_2)) {
                return true;
            }
        }

        return false;
    }

    public int getUnusualWordCount() {
        return 0;
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

    private static ArrayList<String> sentencesToWords(ArrayList<String> sentences) {
        ArrayList<String> answer = new ArrayList();
        Iterator var2 = sentences.iterator();

        while(var2.hasNext()) {
            String sentence = (String)var2.next();
            sentence = sentence.toLowerCase();
            sentence = removePunctuation(sentence);
            String[] words = sentence.split(" ");
            addtoarraylist(answer, words);
        }

        return answer;
    }

    private static void addtoarraylist(ArrayList<String> answer, String[] words) {
        String[] var2 = words;
        int var3 = words.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String word = var2[var4];
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

    private ArrayList<String> getWordlist(String document) {
        ArrayList<String> sentences = TextLib.splitIntoSentences(document);
        return sentencesToWords(sentences);
    }

    private static double FKReadability(ArrayList<String> sentences) {
        ArrayList<String> wordList = sentencesToWords(sentences);
        double syllables = totalSyllables(wordList);
        double readability = 206.835D + -1.015D * ((double)wordList.size() / (double)sentences.size()) + -84.6D * (syllables / (double)wordList.size());
        double consistentError = 16.76D;
        return readability + consistentError;
    }

    private static double totalSyllables(ArrayList<String> wordlist) {
        double total = 0.0D;

        String word;
        for(Iterator var3 = wordlist.iterator(); var3.hasNext(); total += (double)countSyllable(word)) {
            word = (String)var3.next();
        }

        return total;
    }

    private static int countSyllable(String input) {
        int syllables = 0;
       int syllable = syllables + countVowelChains(input);
        if (input.length() == 4 && silentE(input)) {
            --syllable;
        }

        return syllable;
    }

    private static int countVowelChains(String input) {
        int chains = 0;

        for(int i = 0; i < input.length(); ++i) {
            if (isVowel(input.substring(i, i + 1))) {
                ++chains;
                i = nextConstanantIndex(input, i + 1);
            }
        }

        return chains;
    }

    private static boolean silentE(String input) {
        return input.substring(input.length() - 1).equals("e") && isVowel(input.substring(input.length() - 3, input.length() - 2)) && !isVowel(input.substring(input.length() - 2, input.length() - 1));
    }

    private static boolean isVowel(String letter) {
        return "aeiouy".contains(letter);
    }

    private static int nextConstanantIndex(String input, int start) {
        for(int i = start; i < input.length(); ++i) {
            if (!isVowel(input.substring(i, i + 1))) {
                return i;
            }
        }

        return input.length();
    }
}
