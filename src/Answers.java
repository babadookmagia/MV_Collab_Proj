import javax.print.Doc;
import javax.print.attribute.standard.DocumentName;
import java.util.ArrayList;

public class Answers {

    private String completeAnswer;
    private ArrayList<String> individualWords;
    private int score;
    private int questionNum;

    public Answers(String completeAnswer,int questionNum){
        this.completeAnswer = completeAnswer;
        breakUpAnswerIntoWords();
        this.score = 1000;
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

    public void setQuestionNum(int questionNum){
        this.questionNum = questionNum;
    }

    public int getQuestionNum() {
        return questionNum;
    }
}
