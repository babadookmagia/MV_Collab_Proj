import javax.print.Doc;
import javax.print.attribute.standard.DocumentName;
import java.util.ArrayList;

public class answer {

    String completeAnswer;
    ArrayList<String> individualWords;
    int score;

    public answer(String completeAnswer){
        this.completeAnswer = completeAnswer;
        breakUpAnswerIntoWords();
    }

    public String getCompleteAnswer() {
        return completeAnswer;
    }

    public void breakUpAnswerIntoWords(){
       individualWords = Document.stringToWords(completeAnswer);
    }

    public ArrayList<String> getIndividualWordsList() {
        return individualWords;
    }

    public void addToAnswerScore(int score){
        this.score += score;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setCompleteAnswer(String completeAnswer) {
        this.completeAnswer = completeAnswer;
    }
}
