package com.github.jutil.compare.parser;

import java.awt.event.ActionEvent;

import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.github.jutil.core.gui.ExtendedTextPane;
import com.github.jutil.gui.GuiConstants;

public class ParserManager {

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

        timer = new Timer(GuiConstants.DEFAULT_DELAY_MS, this::startParsing);
        timer.setRepeats(false);
    }

    /**
     * called by timer, when document is changed
     * 
     * @param e
     */
    private void startParsing(ActionEvent e) {

        diffParser.parse(leftTextPane.getDocument(), rightTextPane.getDocument());
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
}
