package br.com.stemcell.android.exception;

public class GcmRegistrationException extends RuntimeException {

    private static final String MESSAGE = "An error occurred while either fetching the InstanceID token,\n" +
            "sending the fetched token to the server or subscribing to the PubSub topic. Please try\n" +
            "running the sample again.";


    public GcmRegistrationException(Throwable throwable) {
        super(MESSAGE, throwable);
    }
}
