package thalam;

import java.util.ArrayList;
import java.util.Locale;
import java.util.function.Function;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

/**
 * Type-to-filter + client-side pagination for module list tables.
 */
public final class TableSearch {

    public static final int PAGE_SIZE = 15;

    private TableSearch() {
    }

    /**
     * Binds search + pagination (15 rows/page). Left summary shows total filtered items.
     *
     * @return page state; use {@link PageState#firstRowNumber()} for absolute serial column
     */
    public static <T> PageState bind(
            TextField searchField,
            TableView<T> table,
            ObservableList<T> master,
            Function<T, String> searchableText,
            Label summaryLabel,
            Button prevPageButton,
            Button pageNumberButton,
            Button nextPageButton) {

        FilteredList<T> filtered = new FilteredList<>(master, row -> true);
        ObservableList<T> pageItems = FXCollections.observableArrayList();
        table.setItems(pageItems);
        if (table.getPlaceholder() == null) {
            table.setPlaceholder(new Region());
        }

        PageState state = new PageState();

        Runnable refreshPage = () -> {
            int total = filtered.size();
            int pageCount = Math.max(1, (int) Math.ceil(total / (double) PAGE_SIZE));
            int page = state.pageIndex;
            if (page >= pageCount) {
                page = pageCount - 1;
            }
            if (page < 0) {
                page = 0;
            }
            state.pageIndex = page;

            int from = page * PAGE_SIZE;
            int to = Math.min(from + PAGE_SIZE, total);
            if (total == 0) {
                pageItems.clear();
                state.firstRowNumber = 1;
            } else {
                pageItems.setAll(new ArrayList<>(filtered.subList(from, to)));
                state.firstRowNumber = from + 1;
            }

            if (summaryLabel != null) {
                summaryLabel.setText("Total items: " + total);
            }
            if (pageNumberButton != null) {
                pageNumberButton.setText(String.valueOf(page + 1));
            }
            if (prevPageButton != null) {
                prevPageButton.setDisable(page <= 0 || total == 0);
            }
            if (nextPageButton != null) {
                nextPageButton.setDisable(total == 0 || page >= pageCount - 1);
            }
        };

        Runnable applySearch = () -> {
            String q = searchField == null || searchField.getText() == null
                    ? ""
                    : searchField.getText().trim().toLowerCase(Locale.ROOT);
            state.pageIndex = 0;
            if (q.isEmpty()) {
                filtered.setPredicate(row -> true);
            } else {
                filtered.setPredicate(row -> {
                    if (row == null) {
                        return false;
                    }
                    String haystack = searchableText.apply(row);
                    return haystack != null && haystack.toLowerCase(Locale.ROOT).contains(q);
                });
            }
            refreshPage.run();
        };

        if (searchField != null) {
            searchField.textProperty().addListener((obs, oldVal, newVal) -> applySearch.run());
        }

        filtered.addListener((ListChangeListener<? super T>) c -> {
            int pageCount = Math.max(1, (int) Math.ceil(filtered.size() / (double) PAGE_SIZE));
            if (state.pageIndex >= pageCount) {
                state.pageIndex = pageCount - 1;
            }
            refreshPage.run();
        });

        if (prevPageButton != null) {
            prevPageButton.setOnAction(e -> {
                if (state.pageIndex > 0) {
                    state.pageIndex--;
                    refreshPage.run();
                }
            });
        }
        if (nextPageButton != null) {
            nextPageButton.setOnAction(e -> {
                int pageCount = Math.max(1, (int) Math.ceil(filtered.size() / (double) PAGE_SIZE));
                if (state.pageIndex < pageCount - 1) {
                    state.pageIndex++;
                    refreshPage.run();
                }
            });
        }

        applySearch.run();
        return state;
    }

    public static String safe(String value) {
        return value == null ? "" : value.trim();
    }

    /** Mutable page offset for serial columns (1-based first row on current page). */
    public static final class PageState {
        private int pageIndex;
        private int firstRowNumber = 1;

        public int firstRowNumber() {
            return firstRowNumber;
        }
    }
}
