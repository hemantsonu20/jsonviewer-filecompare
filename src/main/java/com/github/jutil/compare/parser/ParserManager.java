package com.github.jutil.compare.parser;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.Timer;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

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

    private boolean isEditing;

    private TextDocumentFilter textDocumentFilter;

    public ParserManager(ExtendedTextPane leftTextPane, ExtendedTextPane rightTextPane) {

        this.leftTextPane = leftTextPane;
        this.rightTextPane = rightTextPane;

//        ((AbstractDocument) leftTextPane.getDocument())
//                .setDocumentFilter(textDocumentFilter = new TextDocumentFilter());
//        ((AbstractDocument) rightTextPane.getDocument()).setDocumentFilter(textDocumentFilter);

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

            isEditing = true;

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

            isEditing = false;
        }

    }

    private void applyLineHighlights(DiffResult diffResult) throws BadLocationException {

        if (diffResult.hasError()) {
            popup(diffResult.getError());
            return;
        }
        
        leftTextPane.removeAllLineHighlights();
        rightTextPane.removeAllLineHighlights();

        int currentRow = 0;
        for (DiffRow row : diffResult.getDiffRows()) {

            applyLineHighLight(row, currentRow++);
        }
    }

    private void applyLineHighLight(DiffRow row, int line) throws BadLocationException {

        if(line == 0) {
            leftTextPane.append(row.getOldLine());
            rightTextPane.append(row.getNewLine());
        }
        else {
            leftTextPane.append(StringUtils.join(DiffParser.NEW_LINE, row.getOldLine()));
            rightTextPane.append(StringUtils.join(DiffParser.NEW_LINE, row.getNewLine()));
        }
        

        if (row.getTag() != Tag.EQUAL) {

           System.out.println("highlight left " + line + " tag " + row.getTag());
           
            
           
            
            System.out.println("highlight right " + line + " tag " + row.getTag());
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

    private class TextDocumentFilter extends DocumentFilter {

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {

            super.insertString(fb, offset, string, attr);
            //System.out.println("insert called");
            handleDocumentChanged(string == null ? 0 : string.length());
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {

            super.remove(fb, offset, length);
            System.out.println("remove called");
            handleDocumentChanged(length);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {

            super.replace(fb, offset, length, text, attrs);
            System.out.println();
            handleDocumentChanged(text == null ? 0 : text.length());
        }
    }

    private void handleDocumentChanged(int length) {

        if (!isEditing && length > 0) {
            timer.restart();
        }
    }

    private void popup(Exception e) {

        LOGGER.warn(e.getMessage(), e);

    }
}
