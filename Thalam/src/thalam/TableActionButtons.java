package thalam;

import java.util.function.Consumer;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;

/**
 * Shared Edit / Delete buttons for module list Action columns.
 */
public final class TableActionButtons {

    private TableActionButtons() {
    }

    public static <T> void attach(
            TableColumn<T, Void> column,
            Consumer<T> onEdit,
            Consumer<T> onDelete) {
        column.setSortable(false);
        column.setCellFactory(col -> {
            return new TableCell<T, Void>() {
                private final Button editButton = new Button("Edit");
                private final Button deleteButton = new Button("Delete");
                private final HBox box = new HBox(6, editButton, deleteButton);

                {
                    box.setAlignment(Pos.CENTER);
                    editButton.getStyleClass().add("table-action-edit");
                    deleteButton.getStyleClass().add("table-action-delete");
                    editButton.setStyle(
                            "-fx-background-color: #E3F2FD; -fx-text-fill: #1565C0; "
                                    + "-fx-background-radius: 6; -fx-border-radius: 6; "
                                    + "-fx-border-color: #90CAF9; -fx-border-width: 1; "
                                    + "-fx-padding: 4 10; -fx-font-size: 11px; -fx-font-weight: bold; "
                                    + "-fx-cursor: hand;");
                    deleteButton.setStyle(
                            "-fx-background-color: #FDECEA; -fx-text-fill: #C62828; "
                                    + "-fx-background-radius: 6; -fx-border-radius: 6; "
                                    + "-fx-border-color: #EF9A9A; -fx-border-width: 1; "
                                    + "-fx-padding: 4 10; -fx-font-size: 11px; -fx-font-weight: bold; "
                                    + "-fx-cursor: hand;");
                    editButton.setFocusTraversable(false);
                    deleteButton.setFocusTraversable(false);
                    editButton.setMnemonicParsing(false);
                    deleteButton.setMnemonicParsing(false);

                    editButton.setOnAction(event -> {
                        int index = getIndex();
                        if (getTableView() == null
                                || index < 0
                                || index >= getTableView().getItems().size()) {
                            return;
                        }
                        onEdit.accept(getTableView().getItems().get(index));
                    });
                    deleteButton.setOnAction(event -> {
                        int index = getIndex();
                        if (getTableView() == null
                                || index < 0
                                || index >= getTableView().getItems().size()) {
                            return;
                        }
                        onDelete.accept(getTableView().getItems().get(index));
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(box);
                    }
                    setText(null);
                    setAlignment(Pos.CENTER);
                }
            };
        });
    }
}
