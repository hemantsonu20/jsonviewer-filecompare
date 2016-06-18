package com.github.gui;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;



public class GuiUtils {
    

    
    public static JScrollPane getScrollPane(JTextPane textPane) {
        
        JScrollPane scrollPane = new JScrollPane(textPane);
        TextLineNumber lineNumber = new TextLineNumber(textPane);
        scrollPane.setRowHeaderView(lineNumber);
        return scrollPane;
    }
}
