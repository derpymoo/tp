package cms.commons.util;

/**
 * Utility methods for masking sensitive values in displays.
 */
public class MaskingUtil {

    private static final String MASK = "*";

    /**
     * Masks a phone number by showing only the last 4 characters.
     */
    public static String maskPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return phone;
        }
        if (phone.length() <= 4) {
            return MASK.repeat(phone.length());
        }
        return MASK.repeat(phone.length() - 4) + phone.substring(phone.length() - 4);
    }

    /**
     * Masks an email by keeping first 3 characters of local-part and full domain.
     */
    public static String maskEmail(String email) {
        if (email == null || email.isEmpty()) {
            return email;
        }
        int atIndex = email.indexOf('@');
        if (atIndex <= 0 || atIndex == email.length() - 1) {
            return maskKeepFirstThree(email);
        }
        String localPart = email.substring(0, atIndex);
        String domain = email.substring(atIndex);
        if (localPart.length() <= 3) {
            return localPart + domain;
        }
        return localPart.substring(0, 3) + MASK.repeat(localPart.length() - 3) + domain;
    }

    /**
     * Masks a username by keeping only first 3 characters.
     */
    public static String maskUsername(String username) {
        return maskKeepFirstThree(username);
    }

    private static String maskKeepFirstThree(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }
        if (value.length() <= 3) {
            return value;
        }
        return value.substring(0, 3) + MASK.repeat(value.length() - 3);
    }
}
