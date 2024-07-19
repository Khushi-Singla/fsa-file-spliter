package com.freshworks.fsa.filespliter.processor;

import com.freshworks.fsa.filespliter.exceptions.DocumentReaderException;
import com.freshworks.fsa.filespliter.exceptions.ExcelDocumentReaderException;
import com.freshworks.fsa.filespliter.reader.DocumentReader;
import com.freshworks.fsa.filespliter.reader.ExcelDocumentReader;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

public class ExcelDocumentProcessor
        implements DocumentProcessor {
    private static final String EXCEPTION_MESSAGE = "GenericException occurred while initializing the Excel document "
            + "reader.";
    public static final String DD_MM_YYYY_HH_MM_SS = "dd/MM/yyyy HH:mm:ss z";

    @Override
    public DocumentReader getReader(InputStream inputStream) throws DocumentReaderException {
        return getReader(inputStream, DD_MM_YYYY_HH_MM_SS);
    }

    @Override
    public DocumentReader getReader(InputStream inputStream, String dateFormat) throws DocumentReaderException {
        try {
            return new ExcelDocumentReader(inputStream, dateFormat);
        } catch (Exception exception) {
            throw new ExcelDocumentReaderException(EXCEPTION_MESSAGE, exception);
        }
    }
}
