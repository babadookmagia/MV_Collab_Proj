import javax.print.Doc;
import java.util.ArrayList;

public class SemanticAnalysis {
    public static void main(String[] args) {
        Document corpus = Document.findDocument("C:\\Users\\ralran059\\IdeaProjects\\MV_Collab_Proj3\\data\\Question 1.txt");
        ArrayList<String> negativeWords = (TextLib.sortDataPerValue(TextLib.readFileAsString("C:\\Users\\ralran059\\IdeaProjects\\MV_Collab_Proj3\\data\\particularPointerWords\\allSubtractiveWords.csv")));
        printArrayList(negativeWords);
        ArrayList<String> positiveWords = (TextLib.sortDataPerValue(TextLib.readFileAsString("C:\\Users\\ralran059\\IdeaProjects\\MV_Collab_Proj3\\data\\particularPointerWords\\allExperienceOrExamples.csv")));
        printArrayList(positiveWords);
        String text = corpus.getText();
        String answerTxt = text.substring(text.indexOf("Answer") + 7);
        String question = findQuestion(text);
        String[] answers = seperateAnswers(answerTxt);
        String[] ordered = reorderAnswers(question, answers); //ordered[0] is the most useful answer

    }

    private static void printArrayList(ArrayList<String> negativeWords) {
        for (String negativeWord : negativeWords) {
            System.out.println(negativeWord);
        }
    }

    private static int scoreAnswer(String answer, String question) {
        int score = answer.length();

        return answer.length();
    }

    private static String[] reorderAnswers(String question, String[] answers) {
        String[] reorder = new String[answers.length];
        int[] pScores = new int[answers.length]; //professionalism Scores
        findAllScores(pScores, answers, question);
        reorderBasedonScore(reorder, pScores, answers);
        return reorder;
    }

    private static void reorderBasedonScore(String[] reorder, int[] pScores, String[] answers) {
        for (int i = 0; i < answers.length; i++) {
            int maxIndex = findmaxindex(pScores);
            reorder[i] = answers[maxIndex];
            pScores[maxIndex] = 0;
        }
    }

    private static int findmaxindex(int[] pScores) {
        int max = pScores[0];
        int maxIndex = 0;
        for (int i = 1; i < pScores.length; i++) {
            if (pScores[i] > max) {
                max = pScores[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    private static void findAllScores(int[] pScores, String[] answers, String question) {
        for (int i = 0; i < answers.length; i++) {
            pScores[i] = scoreAnswer(answers[i], question);
        }
    }

    private static String[] seperateAnswers(String answerTxt) {
        return answerTxt.split("Answer");
    }

    private static String findQuestion(String text) {
        int startindex = text.indexOf("Question") + 8;
        int endindex = text.indexOf("Answer");
        return text.substring(startindex, endindex);
    }
}