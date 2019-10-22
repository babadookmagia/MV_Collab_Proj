public class WordFreq {
    private String word;
    private long freqency;

    public WordFreq(String word, long freqency) {
        this.word = word;
        this.freqency = freqency;
    }

    public void setFreqency(long freqency) {
        this.freqency = freqency;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public long getFreqency() {
        return this.freqency;
    }

    public String getWord() {
        return this.word;
    }
}
