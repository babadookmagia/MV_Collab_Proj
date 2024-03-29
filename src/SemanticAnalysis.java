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
    private static int multiplierForReadabilityScore = 0;

    private static int underMinWordPenaltyScore = 0;
    private static int overMaxWordPenaltyScore = 0;

    private static int scoreAdditionIfResemblesQuestion = 0;

    private static int scorePenaltyForInclusionOfNegativeWords = 0;
    private static int scoreAdditionForInclusionOfpositiveWords = 0;

    private static double errorCheck = 0;
    private static double errorTotal = 0;



    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);

         promptUserInput(keyboard);

        double successCount = 0;
        int total = 0;
        for (int i = 1; i < 6; i++) {
            String questionNum = "Question " + i;
            Document corpus = Document.findDocument("data\\forumQuestions\\" + questionNum + ".txt");
            String text = corpus.getText();
            String answerTxt = text.substring(text.indexOf("Answer") + "Answers".length()).trim();
            String question = findQuestion(text);
            Answers[] unOrderedanswers = setIndividualAnswerText(i, answerTxt);
            Answers[] answers = reorderAnswers(question, unOrderedanswers); //ordered[0] is the most useful Answers
            double error = checkError(unOrderedanswers, answers, successCount, total);
            System.out.println(error);
            errorTotal++;
            errorCheck += error;
        }
     System.out.println(errorCheck*100/errorTotal);

    }

    private static double checkError(Answers[] unOrderedanswers, Answers[] answers, double success, double total) {
        for (int i = 0; i < answers.length; i++) {
            total++;
            if (answers[i].getCompleteAnswer().equals(unOrderedanswers[i].getCompleteAnswer())) {
                success++;
            }
        }
        return success/total;
    }

    private static void promptUserInput(Scanner keyboard) {


            System.out.println("(Input a 'Yes' or 'no')Would you like to decide a value for every specific criteria or set a constant score for every one? (This means every criteria has the same impact on the score).");
            String specificInput = keyboard.next();
            if (specificInput.equalsIgnoreCase("yes"))
                do {
                    System.out.println("All of the following values must be positive. If one value does not follow this criteria you will have to endure this long procedure again.");

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

                    System.out.println("In order to add points for according to the complexity of the answer a multiplier must be determined to control the impact of this criteria on the overall score. How much would you like to multiply the readability of the answer by to determine the impact?");
                    multiplierForMaxLength = keyboard.nextInt();
                }
                while (multiplierForMaxLength <= 0 || multiplierForMinLength <= 0 || multiplierForReadabilityScore <= 0 || overMaxWordPenaltyScore <= 0 || underMinWordPenaltyScore <= 0 || scoreAdditionIfResemblesQuestion <= 0 || scoreAdditionForInclusionOfpositiveWords <= 0 || scorePenaltyForInclusionOfNegativeWords <= 0);
            if (specificInput.equalsIgnoreCase("no")) {
                multiplierForMinLength = 4;
                multiplierForMaxLength = 4;
                multiplierForReadabilityScore = 1;
                overMaxWordPenaltyScore = 1;
                underMinWordPenaltyScore = 1;
                scoreAdditionIfResemblesQuestion = 1;
                scoreAdditionForInclusionOfpositiveWords = 1;
                scorePenaltyForInclusionOfNegativeWords = 1;
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
        int meanOfAnswers = findMeanLengthOfAnswer(answer);
        for (int answerNum = 0; answerNum < answer.length; answerNum++) {
            scoringAccordingToLength(answer[answerNum], meanOfAnswers);
            fkReadability(answer[answerNum]);
            findNegativeConnotationOfWords(answer, listOfNegativeConnotingWords, answerNum);
            findPositiveConnotationOfWords(answer, listOfPositiveConnotingWords, answerNum);
            compareAnswersToQuestion(answer, answerNum, question);

        }

    } //keep in mind it is void so just set each thing in the answer list

    private static void fkReadability(Answers answers) {
      //  System.out.println(answers.getScore() + " b");

        Document fk = new Document(answers.getCompleteAnswer());
        answers.addToAnswerScore((int) fk.getFleschKincaidScore() * multiplierForReadabilityScore);

     //   System.out.println(answers.getScore() + " a");
    }

    private static void findNegativeConnotationOfWords(Answers[] answer, ArrayList<String> listOfConnotingWords, int answerNum) {
      // System.out.println(answer[answerNum].getScore() + " b");

        for (String listOfNegativeConnotingWord : listOfNegativeConnotingWords) {
            for (int j = 0; j < answer[answerNum].getIndividualWords().size(); j++) {
                if (answer[answerNum].getIndividualWords().get(j).toLowerCase().contains(listOfNegativeConnotingWord))
                    answer[answerNum].addToAnswerScore(-scorePenaltyForInclusionOfNegativeWords);
            }
        }
       //System.out.println(answer[answerNum].getScore() + " a");
    }

    private static void findPositiveConnotationOfWords(Answers[] answer, ArrayList<String> listOfConnotingWords, int answerNum) {
        // System.out.println(answer[answerNum].getScore() + " b");

        for (String listOfPositiveConnotingWord : listOfPositiveConnotingWords) {
            for (int j = 0; j < answer[answerNum].getIndividualWords().size(); j++) {
                if (answer[answerNum].getIndividualWords().get(j).toLowerCase().contains(listOfPositiveConnotingWord))
                    answer[answerNum].addToAnswerScore(scoreAdditionForInclusionOfpositiveWords);
            }
        }
        //System.out.println(answer[answerNum].getScore() + " a");
    }

    private static int findMeanLengthOfAnswer(Answers[] answer) {
        int totalSumLength = 0;
        for (Answers answers : answer) {
            totalSumLength += answers.getIndividualWords().size();
        }
        return totalSumLength / answer.length;
    }

    private static void scoringAccordingToLength(Answers answer, int meanOfAnswers) {//different if statments if you want to change how each size impacts score
      //  System.out.println(answer.getScore() + "b");

        if (answer.getIndividualWords().size() < meanOfAnswers * multiplierForMinLength) {
            answer.addToAnswerScore(-underMinWordPenaltyScore); //can Change Later
           // System.out.println("hey");
        }

        if (answer.getIndividualWords().size() > meanOfAnswers * multiplierForMaxLength) {
           // System.out.println("hey");
            answer.addToAnswerScore(-overMaxWordPenaltyScore); //can Change Later
        }

      //  System.out.println(answer.getScore() + "a");

    }

    private static void compareAnswersToQuestion(Answers[] answer, int answerNum, String question) {
       // System.out.println(answer[answerNum].getScore() + "b");

        answer[answerNum].setIndividualWords(removeStopWords(answer[answerNum].getIndividualWords())); //removes the stop words and replaces the old list of words (Instead of this we could do this from inside the answers object?)
        ArrayList<String> individualQuestionWords = Document.stringToWords(question);
        checkForSharedWordsBetweenAnswerAndQuestion(answer[answerNum], individualQuestionWords);

      //  System.out.println(answer[answerNum].getScore() + "a");
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