package com.worldpay.screeningtest.domain;

public class OrderException extends Exception {

    private int errorCode;

    public OrderException(int errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
