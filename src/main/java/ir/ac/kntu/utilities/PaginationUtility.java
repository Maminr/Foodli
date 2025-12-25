package ir.ac.kntu.utilities;

import ir.ac.kntu.helper.Logger;
import ir.ac.kntu.managers.InputManager;
import ir.ac.kntu.models.enums.TextColor;

import java.util.List;

/**
 * PaginationUtility - Utility class for displaying paginated results
 * <p>
 * Implements pagination as required by specification:
 * - Display limited results per page (default: 10)
 * - Navigation options: Next, Previous, Jump to page, Back
 * - User-friendly page information display
 */
public class PaginationUtility {

    private static final int DEFAULT_PAGE_SIZE = 10;
    private final InputManager inputManager;
    private final Logger logger;

    public PaginationUtility() {
        this.inputManager = InputManager.getInstance();
        this.logger = Logger.getInstance();
    }

    /**
     * Display paginated list with navigation
     *
     * @param items           List of items to paginate
     * @param displayFunction Function to display a single item
     * @param <T>             Type of items in the list
     * @return Selected item index (0-based) or -1 if cancelled
     */
    public <T> int displayPaginatedList(List<T> items, ItemDisplayFunction<T> displayFunction) {
        return displayPaginatedList(items, displayFunction, DEFAULT_PAGE_SIZE);
    }

    /**
     * Display paginated list with custom page size
     *
     * @param items           List of items to paginate
     * @param displayFunction Function to display a single item
     * @param pageSize        Number of items per page
     * @param <T>             Type of items in the list
     * @return Selected item index (0-based) or -1 if cancelled
     */
    public <T> int displayPaginatedList(List<T> items, ItemDisplayFunction<T> displayFunction, int pageSize) {
        if (items == null || items.isEmpty()) {
            logger.print("No items to display.", TextColor.YELLOW);
            return -1;
        }

        int totalPages = (int) Math.ceil((double) items.size() / pageSize);
        int currentPage = 0;

        while (true) {
            // Display current page
            displayPage(items, displayFunction, currentPage, pageSize, totalPages);

            // Show navigation options
            if (totalPages > 1) {
                logger.print("\nNavigation:", TextColor.CYAN);
                if (currentPage > 0) {
                    logger.print("  'p' or 'prev' - Previous page", TextColor.BLUE);
                }
                if (currentPage < totalPages - 1) {
                    logger.print("  'n' or 'next' - Next page", TextColor.BLUE);
                }
                logger.print("  'j' or 'jump' - Jump to page (1-" + totalPages + ")", TextColor.BLUE);
            }
            logger.print("  Enter item number to select", TextColor.GREEN);
            logger.print("  'back' - Return to previous menu", TextColor.RED);
            logger.print("Choose: ");

            String choice = inputManager.getLine().trim().toLowerCase();

            // Handle navigation
            if (choice.equals("back") || choice.equals("b")) {
                return -1;
            } else if (choice.equals("p") || choice.equals("prev") || choice.equals("previous")) {
                if (currentPage > 0) {
                    currentPage--;
                } else {
                    logger.print("Already on first page!", TextColor.YELLOW);
                }
            } else if (choice.equals("n") || choice.equals("next")) {
                if (currentPage < totalPages - 1) {
                    currentPage++;
                } else {
                    logger.print("Already on last page!", TextColor.YELLOW);
                }
            } else if (choice.equals("j") || choice.equals("jump")) {
                currentPage = handleJumpToPage(totalPages, currentPage);
            } else {
                // Try to parse as item number
                try {
                    int itemNumber = Integer.parseInt(choice);
                    int startIndex = currentPage * pageSize;
                    int endIndex = Math.min(startIndex + pageSize, items.size());

                    if (itemNumber >= 1 && itemNumber <= (endIndex - startIndex)) {
                        int selectedIndex = startIndex + itemNumber - 1;
                        return selectedIndex;
                    } else {
                        logger.print("Invalid item number! Please enter a number between 1 and " + (endIndex - startIndex), TextColor.RED);
                    }
                } catch (NumberFormatException e) {
                    logger.print("Invalid input! Please enter a valid option.", TextColor.RED);
                }
            }
        }
    }

    /**
     * Display a single page of items
     */
    private <T> void displayPage(List<T> items, ItemDisplayFunction<T> displayFunction, int currentPage, int pageSize, int totalPages) {
        int startIndex = currentPage * pageSize;
        int endIndex = Math.min(startIndex + pageSize, items.size());

        logger.print("\n" + "=".repeat(60), TextColor.CYAN);
        logger.print(String.format("Page %d of %d (Showing items %d-%d of %d)", currentPage + 1, totalPages, startIndex + 1, endIndex, items.size()), TextColor.CYAN);
        logger.print("=".repeat(60), TextColor.CYAN);
        logger.print("");

        int displayNumber = 1;
        for (int i = startIndex; i < endIndex; i++) {
            logger.print(displayNumber + ". ", TextColor.GREEN);
            displayFunction.display(items.get(i), i);
            displayNumber++;
        }
    }

    /**
     * Handle jump to page navigation
     */
    private int handleJumpToPage(int totalPages, int currentPage) {
        logger.print("Enter page number (1-" + totalPages + "): ");
        String input = inputManager.getLine().trim();

        try {
            int pageNumber = Integer.parseInt(input);
            if (pageNumber >= 1 && pageNumber <= totalPages) {
                return pageNumber - 1; // Convert to 0-based index
            } else {
                logger.print("Invalid page number! Please enter a number between 1 and " + totalPages, TextColor.RED);
                return currentPage;
            }
        } catch (NumberFormatException e) {
            logger.print("Invalid input! Please enter a valid page number.", TextColor.RED);
            return currentPage;
        }
    }

    /**
     * Display paginated list without selection (just viewing)
     */
    public <T> void displayPaginatedView(List<T> items, ItemDisplayFunction<T> displayFunction) {
        displayPaginatedView(items, displayFunction, DEFAULT_PAGE_SIZE);
    }

    /**
     * Display paginated list without selection (just viewing)
     */
    public <T> void displayPaginatedView(List<T> items, ItemDisplayFunction<T> displayFunction, int pageSize) {
        if (items == null || items.isEmpty()) {
            logger.print("No items to display.", TextColor.YELLOW);
            logger.print("Press Enter to continue...");
            inputManager.getLine();
            return;
        }

        int totalPages = (int) Math.ceil((double) items.size() / pageSize);
        int currentPage = 0;

        while (true) {
            // Display current page
            displayPage(items, displayFunction, currentPage, pageSize, totalPages);

            // Show navigation options
            if (totalPages > 1) {
                logger.print("\nNavigation:", TextColor.CYAN);
                if (currentPage > 0) {
                    logger.print("  'p' or 'prev' - Previous page", TextColor.BLUE);
                }
                if (currentPage < totalPages - 1) {
                    logger.print("  'n' or 'next' - Next page", TextColor.BLUE);
                }
                logger.print("  'j' or 'jump' - Jump to page (1-" + totalPages + ")", TextColor.BLUE);
            }
            logger.print("  'back' - Return to previous menu", TextColor.RED);
            logger.print("Choose: ");

            String choice = inputManager.getLine().trim().toLowerCase();

            if (choice.equals("back") || choice.equals("b")) {
                return;
            } else if (choice.equals("p") || choice.equals("prev") || choice.equals("previous")) {
                if (currentPage > 0) {
                    currentPage--;
                } else {
                    logger.print("Already on first page!", TextColor.YELLOW);
                }
            } else if (choice.equals("n") || choice.equals("next")) {
                if (currentPage < totalPages - 1) {
                    currentPage++;
                } else {
                    logger.print("Already on last page!", TextColor.YELLOW);
                }
            } else if (choice.equals("j") || choice.equals("jump")) {
                currentPage = handleJumpToPage(totalPages, currentPage);
            } else {
                logger.print("Invalid input! Please enter a valid option.", TextColor.RED);
            }
        }
    }

    /**
     * Functional interface for displaying items
     */
    @FunctionalInterface
    public interface ItemDisplayFunction<T> {
        void display(T item, int index);
    }
}

