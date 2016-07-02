package com.github.jutil.compare.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jutil.core.gui.ExtendedTextPane;
import com.github.jutil.core.gui.GuiUtils;

public class TextPanel extends JPanel {

    private static final long serialVersionUID = -2379871518310505299L;

    private static final Logger LOGGER = LoggerFactory.getLogger(TextPanel.class);

    private File lastDirectory;
    private ExtendedTextPane textPane;
    private RTextScrollPane scrollPane;

    public RTextScrollPane getScrollPane() {

        return scrollPane;
    }

    public TextPanel setScrollPane(RTextScrollPane scrollPane) {

        this.scrollPane = scrollPane;
        return this;
    }

    public ExtendedTextPane getTextPane() {

        return textPane;
    }

    public TextPanel setTextPane(ExtendedTextPane textPane) {

        this.textPane = textPane;
        return this;
    }

    public TextPanel() {

        init();
    }

    public Color getBackgroundColor() {

        return textPane.getBackground();
    }

    private void init() {

        setLayout(new BorderLayout());

        JPanel settingPanel = new JPanel();
        settingPanel.add(getLoadFileButton());

        scrollPane = GuiUtils.getScrollTextPane(SyntaxConstants.SYNTAX_STYLE_NONE);

        setTextPane((ExtendedTextPane) scrollPane.getTextArea());
        textPane.setHighlightCurrentLine(false);

        add(settingPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JButton getLoadFileButton() {

        JButton loadFileButton = new JButton("Open File");
        loadFileButton.setToolTipText("loads contents from file");
        GuiUtils.applyShortcut(loadFileButton, KeyEvent.VK_O, "Open", new AbstractAction() {

            private static final long serialVersionUID = 1235235L;

            @Override
            public void actionPerformed(ActionEvent e) {

                loadFile();

            }
        });
        loadFileButton.addActionListener(e -> {

            loadFile();
        });
        return loadFileButton;
    }

    private void loadFile() {

        JFileChooser fileChooser = new JFileChooser(lastDirectory);
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            lastDirectory = fileChooser.getCurrentDirectory();
            readFile(fileChooser.getSelectedFile());
        }
    }

    private void readFile(File selectedFile) {

        try {
            textPane.setText(GuiUtils.readFile(selectedFile));
        } catch (Exception e) {
            popup(e);
        }

    }

    private void popup(Exception e) {

        LOGGER.warn("Exception occurred", e);
        JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
