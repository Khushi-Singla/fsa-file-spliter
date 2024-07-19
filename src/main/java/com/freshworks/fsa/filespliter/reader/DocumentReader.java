package com.freshworks.fsa.filespliter.reader;

import com.freshworks.fsa.filespliter.model.Row;

import java.io.Closeable;

public interface DocumentReader
        extends Closeable, Iterable<Row> {
}
