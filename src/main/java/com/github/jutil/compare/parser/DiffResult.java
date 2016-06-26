package com.github.jutil.compare.parser;

import java.util.ArrayList;
import java.util.List;

public class DiffResult {

    private int firstLineParsed;
    private int lastLineParsed;
    private List<DiffNotice> leftNotices;
    private List<DiffNotice> rightNotices;
    private long parseTime;
    private Exception error;

    public DiffResult() {

        leftNotices = new ArrayList<DiffNotice>();
        rightNotices = new ArrayList<DiffNotice>();
    }

    public int getFirstLineParsed() {

        return firstLineParsed;
    }

    public int getLastLineParsed() {

        return lastLineParsed;
    }

    public List<DiffNotice> getLeftNotices() {

        return leftNotices;
    }

    public List<DiffNotice> getRightNotices() {

        return leftNotices;
    }

    public DiffResult addNotice(DiffNotice leftNotice, DiffNotice rightNotice) {

        this.leftNotices.add(leftNotice);
        this.rightNotices.add(rightNotice);
        return this;
    }

    public DiffResult addLeftNotice(DiffNotice leftNotice) {

        this.leftNotices.add(leftNotice);
        return this;
    }

    public DiffResult addRightNotice(DiffNotice rightNotice) {

        this.rightNotices.add(rightNotice);
        return this;
    }

    public void clearNotices() {

        leftNotices.clear();
        rightNotices.clear();
    }

    public void setParsedLines(int first, int last) {

        firstLineParsed = first;
        lastLineParsed = last;
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

    public DiffResult setError(Exception error) {

        this.error = error;
        return this;
    }
}
