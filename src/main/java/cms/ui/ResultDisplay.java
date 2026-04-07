package cms.ui;

import static java.util.Objects.requireNonNull;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

/**
 * A ui for the status bar that is displayed at the header of the application.
 */
public class ResultDisplay extends UiPart<Region> {

    private static final String FXML = "ResultDisplay.fxml";
    private static final String ERROR_STYLE_CLASS = "error";
    private static final double COLLAPSED_HEIGHT = 48;
    private static final double TEXT_AREA_VERTICAL_PADDING = 18;
    private static final double TEXT_AREA_HORIZONTAL_PADDING = 18;
    private static final double MIN_WRAP_WIDTH = 100;
    private static final double HEIGHT_EPSILON = 0.5;

    @FXML
    private TextArea resultDisplay;

    @FXML
    private Hyperlink expandLink;

    private final Text measurementText = new Text();
    private boolean isExpanded;
    private double expandedHeight = COLLAPSED_HEIGHT;

    /**
     * Creates a {@code ResultDisplay}.
     */
    public ResultDisplay() {
        super(FXML);
        measurementText.setManaged(false);
        measurementText.setVisible(false);

        resultDisplay.widthProperty().addListener((observable, oldValue, newValue) ->
                updateExpansionState(false));
    }

    public void setFeedbackToUser(String feedbackToUser, boolean isError) {
        requireNonNull(feedbackToUser);
        isExpanded = false;
        resultDisplay.setText(feedbackToUser);
        resultDisplay.positionCaret(0); // Scroll to top when new feedback is set
        updateStyle(isError);
        Platform.runLater(() -> updateExpansionState(true));
    }

    @FXML
    private void handleExpandToggle() {
        isExpanded = !isExpanded;
        updateDisplayedHeight();
    }

    private void updateStyle(boolean isError) {
        resultDisplay.getStyleClass().remove(ERROR_STYLE_CLASS);
        expandLink.getStyleClass().remove(ERROR_STYLE_CLASS);

        if (isError) {
            resultDisplay.getStyleClass().add(ERROR_STYLE_CLASS);
            expandLink.getStyleClass().add(ERROR_STYLE_CLASS);
        }
    }

    private void updateExpansionState(boolean recalculateHeight) {
        if (recalculateHeight || expandedHeight == COLLAPSED_HEIGHT) {
            expandedHeight = calculateExpandedHeight();
        }

        boolean canExpand = expandedHeight > COLLAPSED_HEIGHT + HEIGHT_EPSILON;
        expandLink.setManaged(canExpand);
        expandLink.setVisible(canExpand);

        if (!canExpand) {
            isExpanded = false;
        }

        updateDisplayedHeight();
    }

    private void updateDisplayedHeight() {
        double targetHeight = isExpanded ? expandedHeight : COLLAPSED_HEIGHT;
        resultDisplay.setPrefHeight(targetHeight);
        resultDisplay.setMinHeight(targetHeight);
        resultDisplay.setMaxHeight(targetHeight);
        expandLink.setText(isExpanded ? "Collapse" : "Expand");
    }

    private double calculateExpandedHeight() {
        double wrapWidth = Math.max(MIN_WRAP_WIDTH, resultDisplay.getWidth() - TEXT_AREA_HORIZONTAL_PADDING);
        measurementText.setText(resultDisplay.getText());
        measurementText.setFont(resultDisplay.getFont());
        measurementText.setWrappingWidth(wrapWidth);
        return Math.max(COLLAPSED_HEIGHT, Math.ceil(measurementText.getLayoutBounds().getHeight())
                + TEXT_AREA_VERTICAL_PADDING);
    }

}
