package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.function.Predicate;

/**
 * Represents a person's premium membership or subscription level in the system.
 * Guarantees: immutable; is valid as declared in {@link #isValidPremium(Integer)}
 */
public class Premium {

    /**
     * Message that describes the constraints on valid premium values.
     */
    public static final String MESSAGE_CONSTRAINTS =
            "Premium should only contain numbers, and should be a positive integer";

    /**
     * Predicate to validate that a premium value is a positive integer.
     */
    public static final Predicate<Integer> VALIDATION_PREDICATE = premium -> premium != null && premium >= 0;

    /**
     * The premium value associated with a person.
     */
    public final Integer value;

    /**
     * Constructs a {@code Premium} object.
     *
     * @param premium A valid premium value.
     */
    public Premium(Integer premium) {
        requireNonNull(premium);
        checkArgument(isValidPremium(premium), MESSAGE_CONSTRAINTS);
        this.value = premium;
    }

    /**
     * Returns true if the given integer is a valid premium value.
     *
     * @param test The integer to check.
     * @return True if the integer is positive and non-null.
     */
    public static boolean isValidPremium(Integer test) {
        return matches(test);
    }

    /**
     * Checks whether the given integer matches the validation predicate.
     *
     * @param test The integer to validate.
     * @return True if the integer meets the validation criteria.
     * @throws NullPointerException if test is null.
     */
    public static boolean matches(Integer test) {
        if (test == null) {
            throw new NullPointerException(MESSAGE_CONSTRAINTS);
        }
        return VALIDATION_PREDICATE.test(test);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
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
        return value.equals(otherPremium.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
