import java.util.ArrayList;

public class SemanticAnalysis {
    public static void main(String[] args) {
        for (int i = 1; i < 6; i++) {
            String questionNum = "Question " + i;
            Document corpus = Document.findDocument("C:\\Users\\kduval139\\IdeaProjects\\MV_Collab_Proj\\data\\" + questionNum + ".txt");
            String text = corpus.getText();
            String answerTxt = text.substring(text.indexOf("Answer") + "Answers".length()).trim();
            String question = findQuestion(text);
            Answers[] answers = setIndividualAnswerText(i, answerTxt);
            reorderAnswers(question, answers); //ordered[0] is the most useful Answers
        }
    }

    private static Answers[] setIndividualAnswerText(int questionNum, String fullAnswerString) {
        String[] textData = seperateAnswers(fullAnswerString);
        Answers[] answers = new Answers[textData.length];
        for (int i = 0; i < answers.length; i++) {
            Answers single = new Answers(textData[i], questionNum);
            answers[i] = single;
        }
        return answers;
    } //gives each answer object correct text and question number

    private static ArrayList<String> getWordList(String filename) {
        return (TextLib.sortDataPerValue(TextLib.readFileAsString(filename)));
    } //uses TextLib class to parse (filename.csv) into an arraylist of words

    private static void printArrayList(ArrayList<String> negativeWords) { //tester method to print array list (delete?)
        for (String negativeWord : negativeWords) {
            System.out.println(negativeWord);
        }
    }

    private static void scoreAnswer(Answers[] answer, String question) { //(unfinished) important for roy to finish reasonable scoring by assigning a score to each Answer object
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

    private static int findWords(String answer, String filename) { //Seaches for certain amounts of a word in an answer from corpus (filename)
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

    private static Answers[] reorderAnswers(String question, Answers[] answers) { //gets scores and reorders the Answers
        scoreAnswer(answers[],question);
        return reorderBasedonScore(answers);
    }

    private static Answers[] reorderBasedonScore(Answers[] answers) { //makes a new array that reorders the answers based on score
        Answers[] reorder = new Answers[answers.length];

        for (int i = 0; i < answers.length; i++) {
            int maxIndex = findmaxindex(answers);
            reorder[i] = answers[maxIndex];
            answers[maxIndex].setScore(0);
        }
        return reorder;
    }

    private static int findmaxindex(Answers[] answers) { //will find the index of the answer with the highest score
        int max = answers[0].getScore();
        int maxIndex = 0;
        for (int i = 1; i < answers.length; i++) {
            if (answers[i].getScore() > max) {
                max = answers[i].getScore();
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    private static String[] seperateAnswers(String answerTxt) { //splits answer text into a array of strings
        return answerTxt.split("Answer");
    }

    private static String findQuestion(String text) { //Just looks for the question with substring
        int startindex = text.indexOf("Question") + 8;
        int endindex = text.indexOf("Answer");
        return text.substring(startindex, endindex);
    }
}