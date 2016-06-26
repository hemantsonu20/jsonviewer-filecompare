package com.github.jutil.compare.parser;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import org.apache.commons.lang3.StringUtils;
import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jutil.core.gui.ExtendedTextPane;
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

        leftTextPane.getDocument().addDocumentListener(new TextDocumentListener());
        rightTextPane.getDocument().addDocumentListener(new TextDocumentListener());
        
        diffParser = new DiffParser();

        timer = new Timer(GuiConstants.DEFAULT_DELAY_MS, this::startParsing);
        timer.setRepeats(false);
    }

    /**
     * called by timer, when document is changed
     * 
     * @param e
     */
    private void startParsing(ActionEvent e) {

        RSyntaxDocument leftDoc = (RSyntaxDocument) leftTextPane.getDocument();
        RSyntaxDocument rightDoc = (RSyntaxDocument) leftTextPane.getDocument();

        DiffResult diffResult = diffParser.parse(leftDoc, rightDoc);

        try {
            leftTextPane.beginAtomicEdit();
            rightTextPane.beginAtomicEdit();

            // clear text areas
            leftTextPane.setText(StringUtils.EMPTY);
            rightTextPane.setText(StringUtils.EMPTY);

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

        int currentRow = 0;
        for (DiffRow row : diffResult.getDiffRows()) {

            applyLineHighLight(row, currentRow++);
        }
    }

    private void applyLineHighLight(DiffRow row, int line) throws BadLocationException {

        leftTextPane.append(row.getOldLine());
        leftTextPane.append(DiffParser.NEW_LINE);
        
        rightTextPane.append(row.getNewLine());
        rightTextPane.append(DiffParser.NEW_LINE);

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

    private class TextDocumentListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {

            // try {
            // int offs = e.getOffset();
            // if (firstLineChanged == null || offs <
            // firstLineChanged.getOffset()) {
            // firstLineChanged = e.getDocument().createPosition(offs);
            // }
            //
            // offs = e.getOffset() + e.getLength();
            // if (lastLineChanged == null || offs >
            // lastLineChanged.getOffset()) {
            // lastLineChanged = e.getDocument().createPosition(offs);
            // }
            // } catch (BadLocationException e1) {
            // // should not happen
            // }
            timer.restart();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {

            // try {
            // int offs = e.getOffset();
            // if (firstLineChanged == null || offs <
            // firstLineChanged.getOffset()) {
            // firstLineChanged = e.getDocument().createPosition(offs);
            // }
            //
            // if (lastLineChanged == null || offs >
            // lastLineChanged.getOffset()) {
            // lastLineChanged = e.getDocument().createPosition(offs);
            // }
            // } catch (BadLocationException e1) {
            // // should not happen
            // }
            timer.restart();

        }

        @Override
        public void changedUpdate(DocumentEvent e) {

        }
    }

    private void popup(Exception e) {

        LOGGER.warn(e.getMessage(), e);

    }
}
