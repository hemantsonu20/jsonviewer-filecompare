package com.github.jutil.compare.parser;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.Timer;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.apache.commons.lang3.StringUtils;
import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jutil.core.gui.ExtendedTextPane;
import com.github.jutil.core.gui.GuiUtils;
import com.github.jutil.gui.GuiConstants;

import difflib.DiffRow;
import difflib.DiffRow.Tag;

public class ParserManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParserManager.class);

    // to denote a code was changed/ added at right side
    private static final Color GREEN_COLOR = new Color(204, 255, 204);

    // to denote code changed at left side, to denote code deleted at right side
    private static final Color RED_COLOR = new Color(255, 230, 230);

    // to denote color at left side, when code was added at right side
    // to denote color at right side when code was removed at left side
    private static final Color GREY_COLOR = new Color(242, 242, 242);

    private Timer timer;
    private DiffParser diffParser;

    // private Position firstLineChanged;
    // private Position lastLineChanged;

    private ExtendedTextPane leftTextPane;
    private ExtendedTextPane rightTextPane;

    public ParserManager(ExtendedTextPane leftTextPane, ExtendedTextPane rightTextPane) {

        this.leftTextPane = leftTextPane;
        this.rightTextPane = rightTextPane;

        // ((AbstractDocument) leftTextPane.getDocument())
        // .setDocumentFilter(textDocumentFilter = new TextDocumentFilter());
        // ((AbstractDocument)
        // rightTextPane.getDocument()).setDocumentFilter(textDocumentFilter);

        diffParser = new DiffParser();

        timer = new Timer(GuiConstants.DEFAULT_DELAY_MS, this::startParsing);
        timer.setRepeats(false);
        timer.start();
    }

    public void startParsing() {

        startParsing(null);
    }

    public void stopParsing() {

        timer.stop();
    }

    /**
     * called by timer, when document is changed
     * 
     * @param e
     */
    private void startParsing(ActionEvent e) {

        RSyntaxDocument leftDoc = (RSyntaxDocument) leftTextPane.getDocument();
        RSyntaxDocument rightDoc = (RSyntaxDocument) rightTextPane.getDocument();

        DiffResult diffResult;
        try {
            leftDoc.readLock();
            rightDoc.readLock();

            // just following convention, ideally this method doesn't throw
            // exception
            diffResult = diffParser.parse(leftDoc, rightDoc);
        } finally {
            leftDoc.readUnlock();
            rightDoc.readUnlock();
        }

        // applying file compare diff, this changes textpane texts, to avoid
        // multiple parsing retriggers through timer, using flag isEditing
        try {

            leftTextPane.beginAtomicEdit();
            rightTextPane.beginAtomicEdit();

            applyLineHighlights(diffResult);

        } catch (Exception ex) {
            popup(ex);
        } finally {
            leftTextPane.endAtomicEdit();
            rightTextPane.endAtomicEdit();
        }
    }

    private void applyLineHighlights(DiffResult diffResult) throws BadLocationException {

        if (diffResult.hasError()) {
            popup(diffResult.getError());
            return;
        }

        if (!diffResult.getDiffRows().isEmpty()) {
            // removing earlier highlights
            leftTextPane.removeAllLineHighlights();
            rightTextPane.removeAllLineHighlights();

            // clearing text areas
            leftTextPane.setText(StringUtils.EMPTY);
            rightTextPane.setText(StringUtils.EMPTY);
        }

        int currentRow = 0;
        for (DiffRow row : diffResult.getDiffRows()) {

            applyLineHighLight(row, currentRow++);
        }

        deleteLastChar(leftTextPane);
        deleteLastChar(rightTextPane);
    }

    private void applyLineHighLight(DiffRow row, int line) throws BadLocationException {

        // escaping &lt; and &gt;
        row.setOldLine(GuiUtils.escapeHtmlEntites(row.getOldLine()));
        row.setNewLine(GuiUtils.escapeHtmlEntites(row.getNewLine()));

        leftTextPane.append(StringUtils.join(row.getOldLine(), DiffParser.NEW_LINE));
        rightTextPane.append(StringUtils.join(row.getNewLine(), DiffParser.NEW_LINE));

        if (row.getTag() != Tag.EQUAL) {

            leftTextPane.addLineHighlight(line, getLeftColor(row.getTag()));
            rightTextPane.addLineHighlight(line, getRightColor(row.getTag()));
        }

    }

    private Color getLeftColor(Tag tag) {

        switch (tag) {

            case CHANGE:
                return RED_COLOR;

            case DELETE:
                return RED_COLOR;

            case INSERT:
                return GREY_COLOR;

            default:
                return Color.WHITE;
        }
    }

    private Color getRightColor(Tag tag) {

        switch (tag) {

            case CHANGE:
                return GREEN_COLOR;

            case DELETE:
                return GREY_COLOR;

            case INSERT:
                return GREEN_COLOR;

            default:
                return Color.WHITE;
        }
    }

    private void deleteLastChar(ExtendedTextPane textPane) {

        Document doc = textPane.getDocument();
        try {
            doc.remove(doc.getLength() - 1, 1);
        } catch (BadLocationException e) {
        }
    }

    private void popup(Exception e) {

        LOGGER.warn(e.getMessage(), e);

    }
}
