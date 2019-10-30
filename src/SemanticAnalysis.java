import java.util.ArrayList;
import java.util.Scanner;

public class SemanticAnalysis {

    final static String positiveConnotationFile = "C:\\Users\\sharoni\\IdeaProjects\\MV_Collab_Proj\\data\\particularPointerWords\\allExperienceOrExamples.csv";
    final static String negativeConnotationFile = "C:\\Users\\sharoni\\IdeaProjects\\MV_Collab_Proj\\data\\particularPointerWords\\badWordsAndSlang.txt";
    final static String unecessaryWords = "C:\\Users\\sharoni\\IdeaProjects\\MV_Collab_Proj\\data\\particularPointerWords\\stop-words.txt";
    static ArrayList<ForumThread> threads = new ArrayList<>();

    public static void main(String[] args) {
        for (int i = 1; i < 6; i++) {
            String questionNum = "Question " + i;
            Document corpus = Document.findDocument("C:\\Users\\sharoni\\IdeaProjects\\MV_Collab_Proj\\data\\forumQuestions\\Question " + questionNum + ".txt");
            String text = corpus.getText();
            String question = findQuestion(text);
            String[] answers = seperateAnswers(text.substring(text.indexOf("Answer") + "answer".length()).trim());
            ForumThread thread = new ForumThread(i,question,answers);
            threads.add(thread);
        }


        Scanner keyboard = new Scanner(System.in);
        System.out.println("What question would you like to reorder");
        int questionNum = keyboard.nextInt();
        String[] ordered = reorderAnswers(questionNum); //ordered[0] is the most useful answer
        //We need  to  structure the code around new objects and change from the array format
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

    private static int scoreAnswer(String[] allAnswers, String answer, String question, int questionNum) {
        int score = 1000;
        String stopWords =
        findRelevancyOfAnswer(questionNum);
        //Find and Compare Size
        score += findPositiveOrNegativeConnotations(answer, positiveConnotationFile );
        score -= findPositiveOrNegativeConnotations(answer, negativeConnotationFile);
        if (score > 0) {
            return score;
        }
        return 0;
    }

    private static String findRelevancyOfAnswer(int questionNum) {
        ArrayList<String> listOfStopWords = getWordList(unecessaryWords);
        removeUncessesaryWordsFromAnswer(questionNum, listOfStopWords); //removes Stop Words from individual words in answer for given question
        compareRelevantAnswerWords(questionNum);
    }

    private static void compareRelevantAnswerWords(int questionNum) {
        for (int i = 0; i <  ; i++) {

        }
    }

    private static void removeUncessesaryWordsFromAnswer(int questionNum, ArrayList<String> stopWords) {
        for (int i = 0; i < threads.get(questionNum).getNumAnswers(); i++) { //Is this a problem?
            TextLib.removeOccurances(threads.get(questionNum).getAnswerData(i).getIndividualWordsList(), stopWords); //Array list of individual Words for Answer I from whatever specified question Then Its removing stop Words
        }
    }

    private static int findPositiveOrNegativeConnotations(String answer, String filename) {
        int score = 0;
        ArrayList<String> getListOfSpecifiedWord = getWordList(filename);
        String answerLow = answer.toLowerCase();
        for (String word : getListOfSpecifiedWord) {
            if (answerLow.contains(word)) {
                score++;
            }
        }
        return score;
    }

    private static String[] reorderAnswers(int questionNum) {
        findAllScores(pScores, answers, question, questionNum);
        reorderBasedonScore(reorder, pScores, answers, questionNum);
        return reorder;
    }

    private static void reorderBasedonScore(String[] reorder, int[] pScores, String[] answers,int questionNum) {
        for (int i = 0; i < threads.get(questionNum).getNumAnswers(); i++) {
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

    private static void findAllScores(int[] pScores, String[] answers, String question, int questionNum) {
        for (int i = 0; i < answers.length; i++) {
            pScores[i] = scoreAnswer(answers ,answers[i], question, questionNum);
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