package com.freshworks.fsa.filespliter.exceptions;

public class DocumentReaderException
        extends RuntimeException {
    public DocumentReaderException(String message) {
        super(message);
    }

    public DocumentReaderException(String message, Throwable exception) {
        super(message, exception);
    }
}
