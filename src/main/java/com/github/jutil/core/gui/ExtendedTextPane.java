package com.github.jutil.core.gui;

import javax.swing.text.BadLocationException;
import javax.swing.text.Element;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

public class ExtendedTextPane extends RSyntaxTextArea {

    private static final long serialVersionUID = -2461865660916776135L;

    public ExtendedTextPane() {

        super();

    }

    /**
     * method to set caret position at start of the specified line
     * 
     * @param line
     */
    public void setCaretLineNumber(int line) throws BadLocationException {

        Element rootElement = getDocument().getDefaultRootElement();
        if(line < 1 || line > rootElement.getElementCount()) {
            throw new BadLocationException("invalid line number", line);
        }

        int offset = rootElement.getElement(line - 1).getStartOffset();
        setCaretPosition(offset);
    }

    /**
     * Method to get line count in current textpane
     * 
     */
    public int getLineCount() {

        return getDocument().getDefaultRootElement().getElementCount();
    }

}
