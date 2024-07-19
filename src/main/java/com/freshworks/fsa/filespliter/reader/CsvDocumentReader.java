package com.freshworks.fsa.filespliter.reader;

import com.freshworks.fsa.filespliter.exceptions.CSVDocumentReaderException;
import com.freshworks.fsa.filespliter.model.Row;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class CsvDocumentReader
        implements DocumentReader {
    private final CSVParser parser;

    public CsvDocumentReader(InputStream stream, Charset encoding)
                throws IOException {
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder().setSkipHeaderRecord(false).build();
        this.parser = CSVParser.parse(stream, encoding, csvFormat);
    }

    @Override
    public Iterator<Row> iterator() {
        return new RowIterator();
    }

    private final class RowIterator
            implements Iterator<Row> {
        final Iterator<CSVRecord> it;

        int rowsWithError = 0;

        Row nextRow = null;

        public RowIterator() {
            it = parser.iterator();
        }

        @Override
        public Row next() {
            if (nextRow == null) {
                throw new NoSuchElementException("Element doesn't exists");
            }
            var nextRowTemp = nextRow;
            nextRow = null;
            return nextRowTemp;
        }

        @Override
        public boolean hasNext() {
            return nextRow != null || calculatedHasNext();
        }

        private boolean calculatedHasNext() {
            try {
                Row row = new Row();
                List<String> values;
                boolean isEmptyRow;
                do {
                    if (it.hasNext()) {
                        values = it.next().toList();
                        isEmptyRow = isRowEmpty(values);
                    } else {
                        return false;
                    }
                } while (isEmptyRow);
                row.setValues(values);
                nextRow = row;
                return true;
            } catch (Exception exception) {
                rowsWithError++;
                throw new CSVDocumentReaderException("Exception occurred while iterating over the rows.", exception);
            }
        }

        private boolean isRowEmpty(List<String> row) {
            if (row == null || row.isEmpty()) {
                return true;
            }
            return row.stream().allMatch(StringUtils::isBlank);
        }
    }

    @Override
    public void close() {
        Try.run(parser::close);
    }
}
