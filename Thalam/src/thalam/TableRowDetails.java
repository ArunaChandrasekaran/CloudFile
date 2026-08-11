package thalam;

import java.util.function.Consumer;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;

/**
 * Opens a handler when a table row is double-clicked.
 */
public final class TableRowDetails {

    private TableRowDetails() {
    }

    public static <T> void attach(TableView<T> table, Consumer<T> onOpenDetails) {
        table.setRowFactory(tv -> {
            TableRow<T> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY
                        && event.getClickCount() == 2
                        && !row.isEmpty()) {
                    onOpenDetails.accept(row.getItem());
                    event.consume();
                }
            });
            return row;
        });
    }
}
