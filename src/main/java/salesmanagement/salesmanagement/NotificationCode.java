package salesmanagement.salesmanagement;

public enum NotificationCode {
    INVALID_USERNAME,
    INVALID_PASSWORD,
    INVALID_EMAIL,
    INVALID_ADDRESS,
    INVALID_PHONE_NUMBER,
    INVALID_BUSINESS_CODE,
    INVALID_ENTERPRISE,
    INVALID_LOGIN,
    NETWORK_ERROR,
    INVALID_LOGIN_INFO,
    INVALID_SECURITY_CODE,
    SUCCEED_VERIFY_MAIL,
    SUCCEED_RESET_PASSWORD,
    SUCCEED_ADD_NEW_EMPLOYEE;

    public static NotificationCode getErrorCode(int code) {
        return switch (code) {
            case 0 -> INVALID_USERNAME;
            case 1 -> INVALID_PASSWORD;
            case 2 -> INVALID_EMAIL;
            case 3 -> INVALID_ADDRESS;
            case 4 -> INVALID_PHONE_NUMBER;
            case 5 -> INVALID_BUSINESS_CODE;
            case 6 -> INVALID_ENTERPRISE;
            case 7 -> INVALID_LOGIN;
            case 8 -> NETWORK_ERROR;
            case 9 -> INVALID_LOGIN_INFO;
            case 10 -> INVALID_SECURITY_CODE;
            case 11 -> SUCCEED_VERIFY_MAIL;
            case 12 -> SUCCEED_RESET_PASSWORD;
            case 13 -> SUCCEED_ADD_NEW_EMPLOYEE;
            default -> throw new IllegalArgumentException("Invalid error code: " + code);
        };
    }


}