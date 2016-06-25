package com.github.jutil.compare.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jutil.core.gui.GuiUtils;

public class CompareTextPanel extends JPanel {

    private static final long serialVersionUID = -2379871518310505299L;

    private static final Logger LOGGER = LoggerFactory.getLogger(ComparePanel.class);

    private File lastDirectory;
    private RSyntaxTextArea textPane;

    public RSyntaxTextArea getTextPane() {

        return textPane;
    }

    public void setTextPane(RSyntaxTextArea textPane) {

        this.textPane = textPane;
    }

    public CompareTextPanel() {

        init();
    }

    private void init() {

        setLayout(new BorderLayout());

        JPanel settingPanel = new JPanel();
        settingPanel.add(getLoadFileButton());

        RTextScrollPane scrollPane = GuiUtils.getScrollTextPane(SyntaxConstants.SYNTAX_STYLE_NONE);
        setTextPane((RSyntaxTextArea) scrollPane.getTextArea());

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
