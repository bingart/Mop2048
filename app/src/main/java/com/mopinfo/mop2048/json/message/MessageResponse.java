package com.mopinfo.mop2048.json.message;

/**
 * Created by mop on 2017/1/22.
 */
public class MessageResponse extends MessageRequest {

    private String errorCode;

    private String errorMessage;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
