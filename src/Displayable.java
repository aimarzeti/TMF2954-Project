/**
 * Class/Interface: Displayable
 * Creator: Reselda Anak Robie
 * Tester: G04/SE Group 14
 * Description: Defines the contract for UI components that display educational content.
 * Implemented by: LearningPage, LearningModule
 */

import javax.swing.JPanel;

public interface Displayable {
    /**
     * Display the content on the given panel.
     * @param panel The JPanel to render content on
     */
    void displayContent(JPanel panel);

    /**
     * Get the title of the content.
     * @return Title string
     */
    String showTitle();

    /**
     * Get a summary of the content.
     * @return Summary string
     */
    String getSummary();
}
