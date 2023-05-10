package salesmanagement.salesmanagement.ViewController;

/**
 * The InputValidator interface defines methods for validating user input and handling invalid input alerts.
 * <p>
 * This interface is used by classes in the salesmanagement.salesmanagement.ViewController package.
 */
public interface InputValidator {

    /**
     * Adds a regular expression checker to the input validator. This method allows input validators to perform
     * regular expression checks on input data to ensure that it meets specific requirements.
     */
    void addRegexChecker();

    /**
     * Validates user input to ensure that it meets specific requirements. This method checks the input data
     * using the regular expression checker added with the addRegexChecker() method. Returns true if the input
     * is valid and false otherwise.
     *
     * @return true if the input is valid, false otherwise.
     */
    boolean validInput();

    /**
     * Removes an invalid input alert from the user interface. This method is called when the user has corrected
     * their input data and no longer needs to be alerted to invalid input.
     */
    void removeInvalidAlert();
}