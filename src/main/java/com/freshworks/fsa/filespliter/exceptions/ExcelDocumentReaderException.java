package com.freshworks.fsa.filespliter.exceptions;

public class ExcelDocumentReaderException
        extends DocumentReaderException {
    public ExcelDocumentReaderException(String message) {
        super(message);
    }

    public ExcelDocumentReaderException(String message, Throwable exception) {
        super(message, exception);
    }
}
