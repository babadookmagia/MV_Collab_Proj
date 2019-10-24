public class SemanticAnalysis {
    public static void main(String[] args) {
        Document corpus = Document.findDocument("C:\\Users\\kduval139\\IdeaProjects\\MV_Collab_Proj\\data\\Corpus (Testing) - Copy.txt");
        String text = corpus.getText();
        String question = findquestion(text);
        String answerTxt = text.substring(text.indexOf("Answer") + 7);
        String[] answers = seperateAnswers(answerTxt);
    }

    private static String[] seperateAnswers(String answerTxt) {
       String[] answers = answerTxt.split("Answer");
        for (String anser: answers) {
        }
        return answers;
    }

    private static String findquestion(String text) {
        int startindex = text.indexOf("Question") + 8;
        int endindex = text.indexOf("Answer");
        return text.substring(startindex, endindex);
    }
}
