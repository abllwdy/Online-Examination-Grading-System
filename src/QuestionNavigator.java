public interface QuestionNavigator {
    int size();
    int currentIndex();
    boolean hasNext();
    boolean hasPrevious();
    String currentQuestion();
    String next();
    String previous();
    void reset();
}
