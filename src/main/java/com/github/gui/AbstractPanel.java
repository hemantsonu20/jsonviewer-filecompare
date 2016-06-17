package com.github.gui;

import javax.swing.BorderFactory;
import javax.swing.JPanel;


public class AbstractPanel extends JPanel {

    private static final long serialVersionUID = -5139768247575572270L;
    
    public AbstractPanel() {
        
        setSize(getMaximumSize());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
    }
    
}
