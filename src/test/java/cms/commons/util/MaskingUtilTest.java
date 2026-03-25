package cms.commons.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class MaskingUtilTest {

    @Test
    public void maskPhone() {
        assertNull(MaskingUtil.maskPhone(null));
        assertEquals("", MaskingUtil.maskPhone(""));
        assertEquals("***", MaskingUtil.maskPhone("123"));
        assertEquals("****", MaskingUtil.maskPhone("1234"));
        assertEquals("****5678", MaskingUtil.maskPhone("12345678"));
    }

    @Test
    public void maskEmail() {
        assertNull(MaskingUtil.maskEmail(null));
        assertEquals("", MaskingUtil.maskEmail(""));
        assertEquals("abc@u.nus.edu", MaskingUtil.maskEmail("abc@u.nus.edu"));
        assertEquals("abc****@u.nus.edu", MaskingUtil.maskEmail("abcdefg@u.nus.edu"));
        assertEquals("abc*", MaskingUtil.maskEmail("abc@"));
        assertEquals("abc***", MaskingUtil.maskEmail("abcdef"));
    }

    @Test
    public void maskUsername() {
        assertNull(MaskingUtil.maskUsername(null));
        assertEquals("", MaskingUtil.maskUsername(""));
        assertEquals("abc", MaskingUtil.maskUsername("abc"));
        assertEquals("abc****", MaskingUtil.maskUsername("abcdefg"));
    }
}
