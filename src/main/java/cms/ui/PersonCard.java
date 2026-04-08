package cms.ui;

import cms.commons.util.MaskingUtil;
import cms.model.person.Person;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";
    private static final String ROLE_STUDENT_STYLE_CLASS = "role-student";
    private static final String ROLE_TUTOR_STYLE_CLASS = "role-tutor";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label role;
    @FXML
    private Label tutorialGroup;
    @FXML
    private Text nusMatricValue;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex, boolean isMasked) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        role.setText(person.getRole().value.toUpperCase());
        tutorialGroup.setText(String.format("T%02d", person.getTutorialGroup().value));
        role.getStyleClass().removeAll(ROLE_STUDENT_STYLE_CLASS, ROLE_TUTOR_STYLE_CLASS);
        role.getStyleClass().add(person.getRole().value.equals("student")
                ? ROLE_STUDENT_STYLE_CLASS : ROLE_TUTOR_STYLE_CLASS);
        nusMatricValue.setText(isMasked
                ? MaskingUtil.maskNusMatric(person.getNusMatric())
                : person.getNusMatric().value);
    }
}
