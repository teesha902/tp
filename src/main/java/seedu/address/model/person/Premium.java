package seedu.address.model.person;

import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a person's premium membership or subscription level in the system.
 * Guarantees: immutable; is valid as declared in {@link #isValidPremium(Integer)}
 */
public class Premium {

    /**
     * Message that describes the constraints on valid premium values.
     */
    public static final String MESSAGE_CONSTRAINTS = "Premium should only contain numbers, and should be a positive integer";

    /**
     * Predicate to validate that a premium value is a positive integer.
     */
    public static final Predicate<Integer> VALIDATION_PREDICATE = premium -> premium != null && premium >= 0;

    /**
     * The premium value associated with a person.
     */
    private final Integer premium;

    /**
     * Constructs a {@code Premium} object.
     *
     * @param premium A valid premium value.
     */
    public Premium(Integer premium) {
        requireNonNull(premium);
        checkArgument(isValidPremium(premium), MESSAGE_CONSTRAINTS);
        this.premium = premium;
    }

    /**
     * Returns true if the given integer is a valid premium value.
     *
     * @param test The integer to check.
     * @return True if the integer is positive and non-null.
     */
    public static boolean isValidPremium(Integer test) {
        return VALIDATION_PREDICATE.test(test);
    }

    @Override
    public String toString() {
        return String.valueOf(premium);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Premium)) {
            return false;
        }

        Premium otherPremium = (Premium) other;
        return premium.equals(otherPremium.premium);
    }

    @Override
    public int hashCode() {
        return premium.hashCode();
    }
}
