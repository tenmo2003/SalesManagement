package salesmanagement.salesmanagement.ViewController;

public interface InputValidator {
    void addRegexChecker();

    boolean validInput();

    void removeInvalidAlert();
}