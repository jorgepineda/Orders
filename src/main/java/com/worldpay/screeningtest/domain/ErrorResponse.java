package com.worldpay.screeningtest.domain;

public class ErrorResponse {
    private int apiErrorCode;
    private String apiErrorMessage;

    public ErrorResponse() {}

    public ErrorResponse(int apiErrorCode, String apiErrorMessage) {
        this.apiErrorCode = apiErrorCode;
        this.apiErrorMessage = apiErrorMessage;
    }

    public int getApiErrorCode() {
        return apiErrorCode;
    }

    public void setApiErrorCode(int apiErrorCode) {
        this.apiErrorCode = apiErrorCode;
    }

    public String getApiErrorMessage() {
        return apiErrorMessage;
    }

    public void setApiErrorMessage(String apiErrorMessage) {
        this.apiErrorMessage = apiErrorMessage;
    }
}
