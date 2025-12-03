import java.util.List;

public class ListQuestionNavigator implements QuestionNavigator {
    private final List<String> questions;
    private int index;

    public ListQuestionNavigator(List<String> questions) {
        this.questions = questions;
        this.index = 0;
    }

    public int size() {
        return questions.size();
    }

    public int currentIndex() {
        return index;
    }

    public boolean hasNext() {
        return index < questions.size() - 1;
    }

    public boolean hasPrevious() {
        return index > 0;
    }

    public String currentQuestion() {
        if (questions.isEmpty()) return "";
        return questions.get(index);
    }

    public String next() {
        if (hasNext()) index++;
        return currentQuestion();
    }

    public String previous() {
        if (hasPrevious()) index--;
        return currentQuestion();
    }

    public void reset() {
        index = 0;
    }
}
