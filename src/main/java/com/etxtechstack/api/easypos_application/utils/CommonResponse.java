package com.etxtechstack.api.easypos_application.utils;

public class CommonResponse {
    public static final Integer SUCCESS_CODE = 0;
    public static final String SUCCESS_MSG = "SUCCESSFUL";

    public static final Integer ERROR_CODE = 500;
    public static final String ERROR_MSG = "ERROR";

    public static final String INVALID_OTP = "INVALID OTP";
    public static final String OTP_EXPIRED_MSG = "OTP Has Expired!";

    public static int INSUFFICIENT_PERMISSION_CODE = 22;
    public static String INSUFFICIENT_PERMISSION_MSG = "INSUFFICIENT PERMISSION";

    public static final int LOGIN_FAILED_CODE = 5;
    public static final String LOGIN_FAILED_MSG = "LOGIN FAILED..INCORRECT USERNAME / PASSWORD";

    public static final int USER_DIABLED_CODE = 44;
    public static final String USER_DISABLED_MSG = "ACCOUNT DISABLED..PLEASE CONTACT YOUR ADMIN";

    public static final int TOKEN_INVALID_CODE = 55;
    public static  final String TOKEN_INVALID_MSG = "INVALID TOKEN";

    public static final int PAYMENT_UNSUCCESSFUL_CODE = 66;
    public static final String PAYMENT_UNSUCCESSFUL_MSG = "PAYMENT FAILED!";

    public static final String INVALID_JWT_TOKEN = "INVALID ACCESS TOKEN";
}
