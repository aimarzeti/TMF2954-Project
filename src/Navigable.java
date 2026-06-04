/**
 * Interface: Navigable
 * Purpose: Defines navigation contract for multi-page content modules.
 * Implemented by: LearningModule
 */
public interface Navigable {
    /**
     * Move to the next page.
     */
    void nextPage();

    /**
     * Move to the previous page.
     */
    void prevPage();

    /**
     * Go to a specific page by index.
     * @param index The page index (0-based)
     * @throws LearningModule.InvalidPageException if index is out of range
     */
    void goToPage(int index) throws LearningModule.InvalidPageException;

    /**
     * Get the current page index.
     * @return Current page index
     */
    int getCurrentPageIndex();

    /**
     * Get total number of pages.
     * @return Total pages
     */
    int getTotalPages();
}
