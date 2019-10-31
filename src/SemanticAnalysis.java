import java.util.ArrayList;
import java.util.Scanner;

public class SemanticAnalysis {


    private final static String positiveWordsfile = "data\\particularPointerWords\\allExperienceOrExamples.csv";
    private final static String negativeWordFile = "data\\particularPointerWords\\allSubtractiveWords.csv";
    private final static String stopWords = "data\\particularPointerWords\\stop-words.txt";

    private final static ArrayList<String> listOfNegativeConnotingWords = getWordList(negativeWordFile);
    private final static ArrayList<String> listOfPositiveConnotingWords = getWordList(positiveWordsfile);
    private final static ArrayList<String> listOfStopWords = getWordList(stopWords);


    private static int multiplierForMinLength = 0;
    private static int multiplierForMaxLength = 0;

    private static int underMinWordPenaltyScore = 0;
    private static int overMaxWordPenaltyScore = 0;

    private static int scoreAdditionIfResemblesQuestion = 0;

    private static int scorePenaltyForInclusionOfNegativeWords = 0;
    private static int scoreAdditionForInclusionOfpositiveWords = 0;






    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);

        promptUserInput(keyboard);

        for (int i = 1; i < 6; i++) {
            String questionNum = "Question " + i;
            Document corpus = Document.findDocument("data\\" + questionNum + ".txt");
            String text = corpus.getText();
            String answerTxt = text.substring(text.indexOf("Answer") + "Answers".length()).trim();
            String question = findQuestion(text);
            Answers[] answers = setIndividualAnswerText(i, answerTxt);
            answers = reorderAnswers(question, answers); //ordered[0] is the most useful Answers
        }
    }

    private static void promptUserInput(Scanner keyboard) {
        System.out.println("In order to deduct points for a length that surpasses the maximum length, a maximum length must be determined. How much would you like to multiply the average length of the answers by to make the generate the wanted maximum.");
        multiplierForMaxLength = keyboard.nextInt();

        System.out.println("In order to deduct points for a length that surpasses the minimum length, a minimum length must be determined. How much would you like to multiply the average length of the answers by to generate the wanted minimum.");
        multiplierForMinLength = keyboard.nextInt();

        System.out.println("How many points would you like to deduct if the answer length surpasses the maximum previously created?");
        overMaxWordPenaltyScore = keyboard.nextInt();

        System.out.println("How many points would you like to deduct if the answer length does not meet the minimum previously created?(input still positive)");
        underMinWordPenaltyScore = keyboard.nextInt();

        System.out.println("How many points would you like to add if the answer contains a word that is contained in the question. (this shows if question is relevant)");
        scoreAdditionIfResemblesQuestion = keyboard.nextInt();

        System.out.println("How many points would you like to add if the answer incorporates an experience connoting phrase?");
        scoreAdditionForInclusionOfpositiveWords = keyboard.nextInt();

        System.out.println("How many points would you like to deduct if the answer incorporates a swear word or unprofessional language?");
        scorePenaltyForInclusionOfNegativeWords = keyboard.nextInt();


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
        int meanOfAnswers = findMeanLengthOfAnswer(answer);
        for (int answerNum = 0; answerNum < answer.length; answerNum++) {
            scoringAccordingToLength(answer[answerNum], meanOfAnswers);
            compareAnswersToQuestion(answer, answerNum, question);
            findConnotationOfWords(answer, listOfNegativeConnotingWords, answerNum);
            findConnotationOfWords(answer, listOfPositiveConnotingWords, answerNum);
        }

    } //keep in mind it is void so just set each thing in the answer list

    private static void findConnotationOfWords(Answers[] answer, ArrayList<String> listOfConnotingWords, int answerNum) {
        String answerLow = answer[answerNum].getCompleteAnswer().toLowerCase();
        for (String word : listOfPositiveConnotingWords) {
            if (answerLow.contains(word)) {
                if (listOfNegativeConnotingWords.contains(listOfConnotingWords))
                    answer[answerNum].addToAnswerScore(-scorePenaltyForInclusionOfNegativeWords);
                else
                answer[answerNum].addToAnswerScore(scoreAdditionForInclusionOfpositiveWords);
            }
        }
    }

    private static int findMeanLengthOfAnswer(Answers[] answer) {
        int totalSumLength = 0;
        for (Answers answers : answer) {
            totalSumLength += answers.getIndividualWords().size();
        }
        return totalSumLength / answer.length;
    }

    private static void scoringAccordingToLength(Answers answer, int meanOfAnswers) {//different if statments if you want to change how each size impacts score

        if (answer.getIndividualWords().size() < meanOfAnswers * multiplierForMinLength)
            answer.addToAnswerScore(underMinWordPenaltyScore); //can Change Later

        if (answer.getIndividualWords().size() > meanOfAnswers * multiplierForMaxLength)
            answer.addToAnswerScore(overMaxWordPenaltyScore); //can Change Later


    }

    private static void compareAnswersToQuestion(Answers[] answer, int answerNum, String question) {
        answer[answerNum].setIndividualWords(removeStopWords(answer[answerNum].getIndividualWords())); //removes the stop words and replaces the old list of words (Instead of this we could do this from inside the answers object?)
        ArrayList<String> individualQuestionWords = Document.stringToWords(question);
        checkForSharedWordsBetweenAnswerAndQuestion(answer[answerNum], individualQuestionWords);
    }

    private static void checkForSharedWordsBetweenAnswerAndQuestion(Answers answer, ArrayList<String> individualQuestionWords) {
        for (String individualQuestionWord : individualQuestionWords) {
            if (answer.getCompleteAnswer().contains(individualQuestionWord))
                answer.addToAnswerScore(scoreAdditionIfResemblesQuestion);  //can change number later (Is this a problem because maybe it doesnt change value of ansewr in array)
        }
    }


    private static ArrayList<String> removeStopWords(ArrayList<String> individualWords) {
        for (int i = 0; i < individualWords.size(); i++) {
            if (listOfStopWords.contains(individualWords.get(i))) {
                individualWords.remove(i);
                i--;
            }
        }
        return individualWords;
    }

    private static Answers[] reorderAnswers(String question, Answers[] answers) { //gets scores and reorders the Answers
        scoreAnswer(answers, question);
        return reorderBasedonScore(answers);
    }

    private static Answers[] reorderBasedonScore(Answers[] answers) { //makes a new array that reorders the answers based on score
        Answers[] reorder = new Answers[answers.length];

        for (int i = 0; i < answers.length; i++) {
            int maxIndex = findmaxindex(answers);
            Answers max = answers[maxIndex];
            reorder[i] = max;
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