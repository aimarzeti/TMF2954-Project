/**
 * Interface: Quizable
 * Purpose: Defines the contract for quiz modules.
 * Implemented by: QuizModule
 */

// for defining quiz contract, allowing for consistent quiz behavior across different implementations 
// for example, multiple choice quizzes, true/false quizzes, etc.
public interface Quizable {
    /**
     * Start a new quiz session.
     */
    void startQuiz();

    /**
     * Check if the given answer is correct.
     * @param answer The user's answer
     * @return true if answer is correct, false otherwise
     */
    boolean checkAnswer(String answer);

    /**
     * Calculate the final score as a percentage.
     * @return Score percentage (0-100)
     */
    int calculateScore();

    /**
     * Get a motivational message based on the score percentage.
     * @param percentage The score percentage
     * @return Motivational message
     */
    String getMotivationalMessage(int percentage);

    /**
     * Reset the quiz to initial state.
     */
    void resetQuiz();
}
