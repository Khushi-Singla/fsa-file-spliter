package com.freshworks.fsa.filespliter.processor;

import com.freshworks.fsa.filespliter.exceptions.DocumentReaderException;
import com.freshworks.fsa.filespliter.reader.DocumentReader;

import java.io.InputStream;

public interface DocumentProcessor {
    DocumentReader getReader(InputStream inputStream) throws DocumentReaderException;

    DocumentReader getReader(InputStream inputStream, String dateFormat) throws DocumentReaderException;
}
