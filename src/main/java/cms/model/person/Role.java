package cms.model.person;

import static cms.commons.util.AppUtil.checkArgument;
import static java.util.Objects.requireNonNull;

/**
 * Represents a Person's role in the address book. It can be either "student" or "tutor".
 * Guarantees: immutable; is valid as declared in {@link #isValidRole(String)}
 */
public enum Role {
    STUDENT("student"),
    TUTOR("tutor");

    public static final String MESSAGE_CONSTRAINTS =
        "Roles should be either 'student' or 'tutor'";
    public final String value;

    Role(String role) {
        requireNonNull(role);
        checkArgument(isValidRole(role), MESSAGE_CONSTRAINTS);
        value = role;
    }

    /**
     * Returns true if a given string is a valid role.
     */
    public static boolean isValidRole(String test) {
        return "student".equals(test) || "tutor".equals(test);
    }

    @Override
    public String toString() {
        return value;
    }

}
