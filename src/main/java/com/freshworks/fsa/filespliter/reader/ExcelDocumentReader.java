package com.freshworks.fsa.filespliter.reader;

import com.freshworks.fsa.filespliter.exceptions.ExcelDocumentReaderException;
import com.freshworks.fsa.filespliter.model.Row;
import com.github.pjfanning.xlsx.StreamingReader;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.NoSuchElementException;

@Slf4j
public class ExcelDocumentReader
        implements DocumentReader {
    private final Workbook workbook;

    private final DateFormat df;

    public ExcelDocumentReader(InputStream inputStream, String dateFormat) {
        this.workbook = StreamingReader.builder().bufferSize(64 * 1024).open(inputStream);
        df = new SimpleDateFormat(dateFormat);
    }

    @Override
    public Iterator<Row> iterator() {
        log.trace("Creating a new Iterator for the workbook sheet {}", workbook.getSheetName(0));
        return new RowIterator();
    }

    private final class RowIterator
            implements Iterator<Row> {
        final Iterator<org.apache.poi.ss.usermodel.Row> it;
        int columnStarts = 0;
        int columnEnd = 0;
        int rowsWithError = 0;
        boolean headerProcessed = false;
        Row nextRow = null;

        public RowIterator() {
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                String message = "Can't find the first sheet in the document. Can't iterate";
                log.error(message);
                throw new ExcelDocumentReaderException(message);
            }
            it = sheet.iterator();
        }

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

        public boolean calculatedHasNext() {
            try {
                var row = new Row();
                org.apache.poi.ss.usermodel.Row sourceRow = null;
                boolean isEmptyRow = true;
                do {
                    if (it.hasNext()) {
                        sourceRow = it.next();
                        isEmptyRow = isRowEmpty(sourceRow);
                    } else {
                        return false;
                    }
                } while (isEmptyRow);
                if (!headerProcessed) {
                    columnEnd = sourceRow.getLastCellNum();
                    log.trace("Computed document's column start and end indices {} - {}", columnStarts, columnEnd);
                    headerProcessed = true;
                }
                for (int cellNum = columnStarts; cellNum < columnEnd; cellNum++) {
                    read(row, sourceRow, cellNum);
                }
                nextRow = row;
                return true;
            } catch (Exception exception) {
                String message = "Error occurred while iterating over rows of the document.";
                log.error(message, exception);
                throw new ExcelDocumentReaderException(message, exception);
            }
        }

        private void read(Row row, org.apache.poi.ss.usermodel.Row sourceRow, int cellNum) {
            try {
                Cell cell = sourceRow.getCell(cellNum,
                        org.apache.poi.ss.usermodel.Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                if (cell == null) {
                    log.trace("Found no value in the cell. Row {} column {}. Setting null",
                            sourceRow.getRowNum(), cellNum);
                    row.addValue(null);
                } else {
                    processCell(cell, row);
                }
            } catch (Exception exception) {
                row.addValue(null);
                rowsWithError++;
                log.error("Exception occurred while processing current row {} and column {}. "
                                + "Rows with error {}.  Setting null",
                        sourceRow.getRowNum(), cellNum, rowsWithError, exception);
            }
        }

        private void processCell(Cell cell, Row row) {
            switch (cell.getCellType()) {
            case BOOLEAN:
                row.addValue(Boolean.toString(cell.getBooleanCellValue()));
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    row.addValue(formatDate(cell));
                } else {
                    double numericValue = cell.getNumericCellValue();
                    if (numericValue % 1 == 0) {
                        row.addValue(Long.toString((long) numericValue));
                    } else {
                        row.addValue(Double.toString(numericValue));
                    }
                }
                break;
            case FORMULA:
            case STRING:
                String value = cell.getStringCellValue();
                if (StringUtils.isBlank(value)) {
                    row.addValue(null);
                } else {
                    row.addValue(value);
                }
                break;
            case _NONE:
            case BLANK:
            case ERROR:
            default:
                row.addValue(null);
                break;
            }
        }

        private String formatDate(Cell cell) {
            Date date = cell.getDateCellValue();
            return df.format(date);
        }

        private boolean isRowEmpty(org.apache.poi.ss.usermodel.Row row) {
            if (row == null) {
                return true;
            }
            if (row.getLastCellNum() <= 0) {
                return true;
            }
            for (int cellNum = 0; cellNum < row.getLastCellNum(); cellNum++) {
                Cell cell = row.getCell(cellNum,
                        org.apache.poi.ss.usermodel.Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                if (cell != null && cell.getCellType() != CellType.BLANK
                        && StringUtils.isNotBlank(cell.toString())) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public void close() {
        Try.run(this.workbook::close);
    }
}
