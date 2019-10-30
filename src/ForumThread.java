public class ForumThread {
    int questionNum;
    String question;
    String[] answers;
    answer[] answerData;

    public ForumThread(int questionNum, String question, String[] answers){
        this.questionNum = questionNum;
        this.question = question;
        this.answers = answers;
        initializeAnswerData();
    }

    private void initializeAnswerData() {
        for (String answer : answers) {
            answer answerOccurance = new answer(answer);
        }
    }

    public int getQuestionNum() {
        return questionNum;
    }

    public String getQuestion() {
        return question;
    }

    public answer getAnswerData(int answerNum) {
       return answerData[answerNum];
    }

    public int getNumAnswers(){
       return answers.length;
    }



}
