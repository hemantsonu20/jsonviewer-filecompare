package com.github.gui.json;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import com.github.gui.AbstractPanel;

public class JsonViewerPanel extends AbstractPanel {

    private static final long serialVersionUID = 7554118114747990205L;

    private static final JsonViewerPanel INSTANCE = new JsonViewerPanel();
    
    public static JsonViewerPanel getInstance() {
        return INSTANCE;
    }
    
    private JTextArea textArea;
    
    public JsonViewerPanel() {
        
        init();
    }
    
    private void init() {
        
        setSize(getMaximumSize());
        setLayout(new BorderLayout());
        
        JPanel settingPanel = new JPanel();

        JButton loadFileButton = new JButton("Load File");
        loadFileButton.setToolTipText("loads contents from file");
        loadFileButton.addActionListener(e -> {
            
            loadFile();
        });
        settingPanel.add(loadFileButton);
        
        JButton formatButton = new JButton("Format");
        formatButton.setToolTipText("formats the input provided");
        formatButton.addActionListener(e -> {
            
            toPrettyString();
        });
        settingPanel.add(formatButton);
        
        JButton deformatButton = new JButton("DeFormat");
        deformatButton.setToolTipText("removes space characters");
        deformatButton.addActionListener(e -> {
            
            toSimpleString();
        });
        settingPanel.add(deformatButton);
        
        add(settingPanel, BorderLayout.NORTH);
        
        textArea = new JTextArea();
        Border outsideBorder = BorderFactory.createLineBorder(Color.BLUE, 2, true);
        Border insideBorder = BorderFactory.createEmptyBorder(20, 20, 20, 20);
        textArea.setBorder(BorderFactory.createCompoundBorder(outsideBorder, insideBorder));
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadFile() {

        // TODO Auto-generated method stub
        
    }

    private void toSimpleString() {

        // TODO Auto-generated method stub
        
    }

    private void toPrettyString() {

        // TODO Auto-generated method stub
        
    }
}
