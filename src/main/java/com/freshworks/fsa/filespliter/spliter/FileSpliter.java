package com.freshworks.fsa.filespliter.spliter;

import com.freshworks.fsa.filespliter.exceptions.DocumentReaderException;
import com.freshworks.fsa.filespliter.model.Row;
import com.freshworks.fsa.filespliter.processor.CsvDocumentProcessor;
import com.freshworks.fsa.filespliter.processor.DocumentProcessor;
import com.freshworks.fsa.filespliter.processor.ExcelDocumentProcessor;
import com.freshworks.fsa.filespliter.reader.DocumentReader;
import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static java.nio.file.Files.newInputStream;

public class FileSpliter {
    private DocumentProcessor csvProcessor = new CsvDocumentProcessor();

    private DocumentProcessor excelProcessor = new ExcelDocumentProcessor();
    private static final int BATCH_LIMIT_IN_MB = 1;
    private static final int MAX_BATCH_LIMIT_IN_BYTES = BATCH_LIMIT_IN_MB * 1024 * 1024;
    public int process(String filePath) throws IOException {
        BatchedRecordReader reader;
        int filePartNumber = 1;
        Path path = Paths.get(filePath);
        try (InputStream inputStream = newInputStream(path)) {
            System.out.println("Original File :: " + getFileSize(filePath) + " MB");
            if (isCSV(filePath)) {
                reader = new BatchedRecordReader(this.csvProcessor.getReader(inputStream));
            } else {
                reader = new BatchedRecordReader(this.excelProcessor.getReader(inputStream, "DD/MM/YYYY"));
            }
            while (reader.hasMore()) {
                List<Row> batch = reader.read();
                write(batch, filePartNumber, path);
                filePartNumber++;

            }
        }
        return 0;
    }

    private Double getFileSize(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return (Files.size(path) / (double)(1024 * 1024));
    }

    private static boolean isCSV(String fileName) throws IOException {
        Path filePath = Paths.get(fileName);
        return Objects.equals(Files.probeContentType(filePath), "text/csv");
    }

    private void write(List<Row> batch, int filePartNumber, Path path) throws IOException {
        String fileName = "batch_" + filePartNumber + ".csv";
        File tmpFile = new File(getTmpDir(path), fileName);
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(tmpFile, StandardCharsets.UTF_8))) {
            for (Row row : batch) {
                // Writing data to CSV file
                csvWriter.writeNext(row.getValues().toArray(new String[]{}));
            }
            System.out.println("Batch File :: " + fileName + " :: " + getFileSize(tmpFile.getAbsolutePath()) + " MB");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private static File getTmpDir(Path path) throws IOException {
        File file = new File(path.getParent() + "/tmp");
        if (!file.exists()) {
            Files.createDirectories(Path.of(path.getParent() + "/tmp"));
        }
        return file;
    }

    public static class BatchedRecordReader {
        private final Iterator<Row> iterator;
        private Row header;

        public BatchedRecordReader(DocumentReader documentReader) {
            iterator = documentReader.iterator();
            if (iterator.hasNext()) {
                header = iterator.next();
            }
        }

        public boolean hasMore() {
            return iterator.hasNext();
        }

        public List<Row> read() {
            List<Row> records = new ArrayList<>();
            records.add(header);
            int bytesRead = 0;
            byte[] rowBytes = new byte[0];
            try {
                while (bytesRead + rowBytes.length < MAX_BATCH_LIMIT_IN_BYTES && iterator.hasNext()) {
                    Row row = iterator.next();
                    records.add(row);
                    rowBytes = row.toString().getBytes(StandardCharsets.UTF_8);
                    bytesRead += rowBytes.length;
                }
            } catch (DocumentReaderException e) {
                e.printStackTrace();
            }
            return records.size() != 1 ? records : Collections.emptyList();
        }
    }

}
