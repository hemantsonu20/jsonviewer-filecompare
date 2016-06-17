package com.github.gui.base64;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import com.github.gui.AbstractPanel;

public class Base64Panel extends AbstractPanel {

    private static final long serialVersionUID = 7554118114747990205L;

    private static final Base64Panel INSTANCE = new Base64Panel();

    public static Base64Panel getInstance() {

        return INSTANCE;
    }

    private JTextArea inputTextArea;
    private JTextArea outputTextArea;

    public Base64Panel() {

        init();
    }

    private void init() {

        setSize(getMaximumSize());
        setLayout(new BorderLayout());

        JPanel settingPanel = new JPanel();

        JButton encodeButton = new JButton("Base64 Encode");
        encodeButton.setToolTipText("encodes into Base64 string");
        encodeButton.addActionListener(e -> {

            encodeContent();
        });
        settingPanel.add(encodeButton);

        JButton decodeButton = new JButton("Base64 Decode");
        decodeButton.setToolTipText("decodes into Base64 string");
        decodeButton.addActionListener(e -> {

            decodeContent();
        });
        settingPanel.add(decodeButton);

        add(settingPanel, BorderLayout.NORTH);

        JPanel textAreaPanel = new JPanel();
        textAreaPanel.setLayout(new GridLayout(1, 2, 20, 0));
        textAreaPanel.setSize(getMaximumSize());

        Border outsideBorder = BorderFactory.createLineBorder(Color.BLUE, 2, true);
        Border insideBorder = BorderFactory.createEmptyBorder(20, 20, 20, 20);
        Border compoundBorder = BorderFactory.createCompoundBorder(outsideBorder, insideBorder);
        
        inputTextArea = new JTextArea();
        inputTextArea.setBorder(compoundBorder);
        JScrollPane inputScrollPane = new JScrollPane(inputTextArea);
        textAreaPanel.add(inputScrollPane);

        outputTextArea = new JTextArea();
        outputTextArea.setBorder(compoundBorder);
        JScrollPane outputScrollPane = new JScrollPane(outputTextArea);
        textAreaPanel.add(outputScrollPane);
        add(textAreaPanel, BorderLayout.CENTER);

    }

    private void encodeContent() {

        // TODO Auto-generated method stub

    }

    private void decodeContent() {

        // TODO Auto-generated method stub

    }
}
