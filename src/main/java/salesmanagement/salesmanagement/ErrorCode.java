package salesmanagement.salesmanagement;

public enum ErrorCode {
    INVALID_USERNAME,
    INVALID_PASSWORD,
    INVALID_EMAIL,
    INVALID_ADDRESS,
    INVALID_PHONE_NUMBER,
    INVALID_BUSINESS_CODE,
    INVALID_ENTERPRISE,
    INVALID_LOGIN;

    public static ErrorCode getErrorCode(int code) {
        return switch (code) {
            case 0 -> INVALID_USERNAME;
            case 1 -> INVALID_PASSWORD;
            case 2 -> INVALID_EMAIL;
            case 3 -> INVALID_ADDRESS;
            case 4 -> INVALID_PHONE_NUMBER;
            case 5 -> INVALID_BUSINESS_CODE;
            case 6 -> INVALID_ENTERPRISE;
            case 7 -> INVALID_LOGIN;
            default -> throw new IllegalArgumentException("Invalid error code: " + code);
        };
    }


}