// File: data/DataStore.java (Corrected)

package data;

import model.Question;
import model.SessionManager;
import model.StudentSession; // <-- This import line is necessary
import java.util.ArrayList;
import java.util.List;

public class DataStore {
    // This static list lives in RAM as long as the app runs
    public static List<Question> questionList = new ArrayList<>();
    // This line should be a reference, not a full class definition
    public static SessionManager currentSession = new StudentSession();

    public static void addQuestion(Question q) {
        questionList.add(q);
        System.out.println("✅ [System]: Question saved to RAM. Total Questions: " + questionList.size());
    }

    public static List<Question> getAllQuestions() {
        return questionList;
    }

    public static Question getQuestion(int index) {
        if (index >= 0 && index < questionList.size()) {
            return questionList.get(index);
        }
        return null;
    }
}