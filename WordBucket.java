
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class WordBucket {
    private ArrayList<WordFreq> bucket = new ArrayList();

    public WordBucket() {
    }

    public String getTopMostFreq() {
        long max = ((WordFreq)this.bucket.get(0)).getFreqency();
        String maxWord = ((WordFreq)this.bucket.get(0)).getWord();
        Iterator var4 = this.bucket.iterator();

        while(var4.hasNext()) {
            WordFreq wordFreq = (WordFreq)var4.next();
            if (wordFreq.getFreqency() > max) {
                max = wordFreq.getFreqency();
                maxWord = wordFreq.getWord();
            }
        }

        return maxWord;
    }

    public long getUniqueNum() {
        return (long)this.bucket.size();
    }

    public void addWord(String word) {
        ArrayList<String> words = new ArrayList();
        Iterator var3 = this.bucket.iterator();

        while(var3.hasNext()) {
            WordFreq wordFreq = (WordFreq)var3.next();
            if (wordFreq.getWord().equals(word)) {
                words.add(wordFreq.getWord());
                wordFreq.setFreqency(wordFreq.getFreqency() + 1L);
            }
        }

        if (!words.contains(word)) {
            this.bucket.add(new WordFreq(word, 1L));
        }

        sortWords(this.bucket);
    }

    public void addWord(String word, long amount) {
        ArrayList<String> words = new ArrayList();
        Iterator var5 = this.bucket.iterator();

        while(var5.hasNext()) {
            WordFreq wordFreq = (WordFreq)var5.next();
            if (wordFreq.getWord().equals(word)) {
                words.add(wordFreq.getWord());
                wordFreq.setFreqency(wordFreq.getFreqency() + amount);
            }
        }

        if (!words.contains(word)) {
            this.bucket.add(new WordFreq(word, amount));
        }

        sortWords(this.bucket);
    }

    public long getCountof(String word) {
        Iterator var2 = this.bucket.iterator();

        WordFreq wordFreq;
        do {
            if (!var2.hasNext()) {
                return 0L;
            }

            wordFreq = (WordFreq)var2.next();
        } while(!wordFreq.getWord().equals(word));

        return wordFreq.getFreqency();
    }

    public long size() {
        long sum = 0L;

        WordFreq wordfreq;
        for(Iterator var3 = this.bucket.iterator(); var3.hasNext(); sum += wordfreq.getFreqency()) {
            wordfreq = (WordFreq)var3.next();
        }

        return sum;
    }

    public void parsefile(String filename) {
        try {
            Scanner scanner = new Scanner(new FileReader(filename));

            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                WordFreq word = processline(line);
                this.bucket.add(word);
            }

            scanner.close();
        } catch (FileNotFoundException var5) {
            System.out.println("File not found " + filename);
        }

    }

    private static WordFreq processline(String line) {
        int startingIndex = firstInt(line);
        String word = line.substring(0, startingIndex);
        long frequency = Long.parseLong(line.substring(startingIndex));
        return new WordFreq(word, frequency);
    }

    private static int firstInt(String line) {
        for(int i = 0; i < line.length(); ++i) {
            if (isNum(line.substring(i, i + 1))) {
                return i;
            }
        }

        return 0;
    }

    private static boolean isNum(String digit) {
        return "1234567890".contains(digit);
    }

    public ArrayList<WordFreq> getBucket() {
        return this.bucket;
    }

    public static void sortWords(ArrayList<WordFreq> bucket) {
        for(int i = 0; i < bucket.size(); ++i) {
            for(int j = 0; j < i; ++j) {
                if (((WordFreq)bucket.get(i)).getFreqency() > ((WordFreq)bucket.get(j)).getFreqency()) {
                    switchIndex(bucket, i, j);
                }
            }
        }

    }

    public static void switchIndex(ArrayList<WordFreq> bucket, int i, int j) {
        WordFreq first = (WordFreq)bucket.get(i);
        WordFreq second = (WordFreq)bucket.get(j);
        bucket.set(i, second);
        bucket.set(j, first);
    }
}
