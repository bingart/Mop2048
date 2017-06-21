package com.mopinfo.lib.common;

/**
 * Created by mop on 2016/8/24.
 */
public class NutchException extends Exception {

    public ErrorCode errorCode;

    public String errorMessage;

    public NutchException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public NutchException(ErrorCode errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
