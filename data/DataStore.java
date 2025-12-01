package data;

import model.Question;
import model.StudentSession;
import java.util.ArrayList;
import java.util.List;

public class DataStore {
    // This static list lives in RAM as long as the app runs
    public static List<Question> questionList = new ArrayList<>();
    public static StudentSession currentSession = new StudentSession();

    public static void addQuestion(Question q) {
        questionList.add(q);
        System.out.println("✅ [System]: Question saved to RAM. Total Questions: " + questionList.size());
    }

    // Future US07 will call this to get questions
    public static List<Question> getAllQuestions() {
        return questionList;
    }
}