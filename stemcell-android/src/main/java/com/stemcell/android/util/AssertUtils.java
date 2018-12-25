package com.stemcell.android.util;

import java.util.Collection;

import com.stemcell.android.exception.BusinessException;

public abstract class AssertUtils {
    private AssertUtils(){
    }

    /**
     * Generates a known exception if object <b> object </ b> is null
     * @param object Object to be verified
     * @param message Error Message Id
     */
    public static void assertNotNull(Object object, String message) {
        if (object == null) {
            throw new BusinessException(message);
        }
    }

    /**
     * Gera uma exceção se a String <b>string</b> é nula ou vazia com a mensagem <b>s</b>.
     * @param string The string that will be checked.
     * @param message  Error Message Id
     */
    public static void assertNotNullNotEmpty(String string, String message) {
        if (string == null || string.trim().length() == 0) {
            throw new BusinessException(message);
        }
    }

    /**
     * Generates an exception if Collection <b> collection </ b> is null or empty with <b> s </ b> message.
     * @param collection A collection that will be verified.
     * @param message Error Message Id
     */
    public static void assertNotNullNotEmpty(Collection collection, String message) {
        if (collection == null || collection.isEmpty()) {
            throw new BusinessException(message);
        }
    }

    /**
     * Generates an exception if the <b> object </ b> is null with the message <b> s </ b>.
     * @param expression Condition to be checked
     * @param message Error Message Id
     */
    public static void assertExpression(boolean expression, String message) {
        if (!expression) {
            throw new BusinessException(message);
        }
    }

    /**
     * Generates an exception if the <b> object </ b> is null with the message <b> s </ b>.
     * @param expression Condition to be checked
     * @param message Error Message Id
     * @param params Message Parameters
     */
    public static void assertExpression(boolean expression, String message, Object... params) {
        if (!expression) {
            throw new BusinessException(message, params);
        }
    }

    
}
