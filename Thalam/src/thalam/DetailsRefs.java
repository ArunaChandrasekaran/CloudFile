package thalam;

import java.util.List;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


public final class DetailsRefs {

    private DetailsRefs() {
    }

    public static void fill(VBox box, List<String> lines) {
        box.getChildren().clear();
        if (lines == null || lines.isEmpty()) {
            Label empty = new Label("No records yet");
            empty.getStyleClass().add("details-ref-empty");
            box.getChildren().add(empty);
            return;
        }
        for (String line : lines) {
            Label row = new Label(line);
            row.setWrapText(true);
            row.setMaxWidth(Double.MAX_VALUE);
            row.getStyleClass().add("details-ref-row");
            box.getChildren().add(row);
        }
    }
}
