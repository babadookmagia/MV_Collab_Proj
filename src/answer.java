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
       individualWords = TextLib.parseDataIntoArrayList(completeAnswer, " ");
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


}
