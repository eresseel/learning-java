package hu.laci.learning;

/**
 * Minimal password policy for practicing parameterized tests.
 */
public class PasswordPolicy {

    /**
     * Checks whether the password meets the minimum length requirement.
     */
    public boolean isValid(String password) {
        return password != null && password.length() >= 8;
    }
}
