package com.github.jsonview.compare.gui;

import java.awt.GridLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jsonview.core.gui.AbstractPanel;

public class ComparePanel extends AbstractPanel {

    private static final long serialVersionUID = 114747990205L;

    private static final Logger LOGGER = LoggerFactory.getLogger(ComparePanel.class);

    private static final ComparePanel INSTANCE = new ComparePanel();

    public static ComparePanel getInstance() {

        return INSTANCE;
    }

    private CompareTextPanel leftPanel;
    private CompareTextPanel rightPanel;

   

    public ComparePanel() {

        init();
    }

    private void init() {

        setSize(getMaximumSize());
        setLayout(new GridLayout(1, 2));
        
        add(leftPanel = new CompareTextPanel());
        add(rightPanel = new CompareTextPanel());
        
        //leftPanel.getTextPane().loadMacro(macro);append(str);

    }

       

//    private void popup(Exception e) {
//
//        LOGGER.warn("Exception occurred", e);
//    }

    // private void popup(String msg) {
    //
    // JOptionPane.showMessageDialog(this, msg, "Info",
    // JOptionPane.INFORMATION_MESSAGE);
    // }
}
