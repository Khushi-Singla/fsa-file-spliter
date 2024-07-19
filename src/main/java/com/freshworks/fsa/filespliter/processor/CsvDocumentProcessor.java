package com.freshworks.fsa.filespliter.processor;

import com.freshworks.fsa.filespliter.exceptions.CSVDocumentReaderException;
import com.freshworks.fsa.filespliter.exceptions.DocumentReaderException;
import com.freshworks.fsa.filespliter.reader.CsvDocumentReader;
import com.freshworks.fsa.filespliter.reader.DocumentReader;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static com.freshworks.fsa.filespliter.processor.ExcelDocumentProcessor.DD_MM_YYYY_HH_MM_SS;
import static org.apache.commons.io.ByteOrderMark.UTF_16BE;
import static org.apache.commons.io.ByteOrderMark.UTF_16LE;
import static org.apache.commons.io.ByteOrderMark.UTF_32BE;
import static org.apache.commons.io.ByteOrderMark.UTF_32LE;
import static org.apache.commons.io.ByteOrderMark.UTF_8;

public class CsvDocumentProcessor
        implements DocumentProcessor {

    @Override
    public DocumentReader getReader(InputStream inputStream) throws DocumentReaderException {
        return getReader(inputStream, DD_MM_YYYY_HH_MM_SS);
    }

    @Override
    public DocumentReader getReader(InputStream inputStream, String dateFormat) throws DocumentReaderException {
        try {
            BOMInputStream bomInputStream = BOMInputStream.builder().setInputStream(inputStream)
                    .setByteOrderMarks(UTF_8, UTF_16LE, UTF_16BE, UTF_32LE, UTF_32BE).get();
            String charsetName = bomInputStream.getBOMCharsetName();
            BufferedInputStream bufferedStream = new BufferedInputStream(bomInputStream);
            Charset charset = StandardCharsets.UTF_8;
            if (StringUtils.isNotBlank(charsetName)) {
                charset =  Charset.forName(charsetName);
            }
            return new CsvDocumentReader(bufferedStream, charset);
        } catch (Exception exception) {
            throw new CSVDocumentReaderException("Exception occurred while parsing the CSV document", exception);
        }
    }
}
