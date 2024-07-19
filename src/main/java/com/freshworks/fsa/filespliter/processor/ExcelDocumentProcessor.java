package com.freshworks.fsa.filespliter.processor;

import com.freshworks.fsa.filespliter.exceptions.DocumentReaderException;
import com.freshworks.fsa.filespliter.exceptions.ExcelDocumentReaderException;
import com.freshworks.fsa.filespliter.model.DocumentType;
import com.freshworks.fsa.filespliter.reader.DocumentReader;
import com.freshworks.fsa.filespliter.reader.ExcelDocumentReader;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@AllArgsConstructor
@DocumentTypeSelector(type = DocumentType.EXCEL)
@Slf4j
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
            log.error(EXCEPTION_MESSAGE, exception);
            throw new ExcelDocumentReaderException(EXCEPTION_MESSAGE, exception);
        }
    }
}
