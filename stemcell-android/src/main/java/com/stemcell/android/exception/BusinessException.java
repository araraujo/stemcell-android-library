package com.stemcell.android.exception;

import java.util.Arrays;

/**
 *
 */
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = -6578431977912288874L;

    private Object[] params;

    public BusinessException(String message, Throwable cause, Object... messageParams) {
        super(message, cause);
        this.params = messageParams;
    }


    public BusinessException(String message, Object... messageParams) {
        super(message);
        this.params = messageParams;
    }

    public Object[] getMessageParams() {
        Object[] copyParams = null;

        if (this.params != null) {
            copyParams = Arrays.copyOf(this.params, params.length);
        }

        return copyParams;
    }

    public void setMessageParams(Object[] params) {
        if (params != null) {
            this.params = Arrays.copyOf(params, params.length);
        }
    }

}
