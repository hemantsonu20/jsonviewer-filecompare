package com.github.jutil.compare.parser;

import java.util.Collections;
import java.util.List;

import difflib.DiffRow;

public class DiffResult {

    private List<DiffRow> diffRows;
    private long parseTime;
    private Exception error;

    public DiffResult() {

        clear();
    }

    public long getParseTime() {

        return parseTime;
    }

    public DiffResult setParseTime(long parseTime) {

        this.parseTime = parseTime;
        return this;
    }

    public Exception getError() {

        return error;
    }

    public boolean hasError() {

        return error != null;
    }

    public DiffResult setError(Exception error) {

        this.error = error;
        return this;
    }

    public List<DiffRow> getDiffRows() {

        return diffRows;
    }

    public DiffResult setDiffRows(List<DiffRow> diffRows) {

        this.diffRows = diffRows;
        return this;
    }

    /**
     * reinitializes fields to reuse the object
     * 
     */
    public void clear() {

        diffRows = Collections.emptyList();
        parseTime = 0;
        error = null;
    }
}
