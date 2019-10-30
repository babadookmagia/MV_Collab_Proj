import javax.print.Doc;
import java.util.ArrayList;

public class SemanticAnalysis {
    public static void main(String[] args) {
        for (int i = 1; i < 6; i++) {
            String questionNum = "Question " + i;
            Document corpus = Document.findDocument("C:\\Users\\kduval139\\IdeaProjects\\MV_Collab_Proj\\data\\" + questionNum + ".txt");
            String text = corpus.getText();
            String answerTxt = text.substring(text.indexOf("Answer") + "answer".length()).trim();
            String question = findQuestion(text);
            String[] answers = seperateAnswers(answerTxt);
            String[] ordered = reorderAnswers(question, answers); //ordered[0] is the most useful answer
        }
    }

    private static ArrayList<String> getWordList(String filename) {
        ArrayList<String> wordsList = (TextLib.sortDataPerValue(TextLib.readFileAsString(filename)));
        return wordsList;
    }

    private static void printArrayList(ArrayList<String> negativeWords) {
        for (String negativeWord : negativeWords) {
            System.out.println(negativeWord);
        }
    }

    private static int scoreAnswer(String[] answer, String question) {
        int score = 1000;
        //Find and compare relevancy
        //Find and Compare Size
        Document.stringToWords(answer);
        score = score + findWords(answer, "C:\\Users\\kduval139\\IdeaProjects\\MV_Collab_Proj\\data\\particularPointerWords\\allExperienceOrExamples.csv");
        score = score - findWords(answer, "C:\\Users\\kduval139\\IdeaProjects\\MV_Collab_Proj\\data\\particularPointerWords\\allSubtractiveWords.csv");
        if (score > 0) {
            return score;
        }
        return 0;
    }

    private static int findWords(String answer, String filename) {
        int score = 0;
        ArrayList<String> getNegativeWords = getWordList(filename);
        String answerLow = answer.toLowerCase();
        for (String word : getNegativeWords) {
            if (answerLow.contains(word)) {
                score++;
            }
        }
        return score;
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
            pScores[i] = scoreAnswer(answers, question);
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