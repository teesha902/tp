package seedu.address.model.person;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

public class PremiumTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Premium(null));
    }

    @Test
    public void constructor_invalidPremiumNegative_throwsIllegalArgumentException() {
        int invalidPremium = -1;
        assertThrows(IllegalArgumentException.class, () -> new Premium(invalidPremium));
    }

    @Test
    public void isValidPremium() {
        // null premium
        assertThrows(NullPointerException.class, () -> Premium.isValidPremium(null));

        //invalid premium
        assertFalse(Premium.isValidPremium(-1)); // negative number
    }

    @Test
    public void equals() {
        Premium premium = new Premium(111);

        // same values -> return true
        assertTrue(premium.equals(new Premium(111)));

        // same object -> return true
        assertTrue(premium.equals(premium));

        // null -> return false
        assertFalse(premium.equals(null));

        // different types -> return false
        assertFalse(premium.equals("111"));

        // different values -> return false
        assertFalse(premium.equals(new Premium(222)));
    }
}
