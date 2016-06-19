package com.github.gui.json;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.apache.commons.lang3.StringUtils;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.gui.AbstractPanel;
import com.github.gui.GuiUtils;

public class JsonViewerPanel extends AbstractPanel {

    private static final long serialVersionUID = 7554118114747990205L;

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonViewerPanel.class);

    private static final JsonViewerPanel INSTANCE = new JsonViewerPanel();

    public static JsonViewerPanel getInstance() {

        return INSTANCE;
    }

    private RSyntaxTextArea textPane;
    private JTextField searchField;
    private JButton validateButton;
    
    private File lastDirectory;

    public JsonViewerPanel() {

        init();
    }

    

    private void init() {

        setSize(getMaximumSize());
        setLayout(new BorderLayout());

        JPanel settingPanel = new JPanel();

        settingPanel.add(getLoadFileButton());
        settingPanel.add(getFormatButton());
        settingPanel.add(getDeformatButton());
        settingPanel.add(validateButton = getValidateButton());
        settingPanel.add(new JLabel("Search"));
        settingPanel.add(searchField = getSearchField());

        add(settingPanel, BorderLayout.NORTH);

        RTextScrollPane scrollPane = GuiUtils.getScrollTextPane(SyntaxConstants.SYNTAX_STYLE_JSON);
        textPane = (RSyntaxTextArea) scrollPane.getTextArea();

        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadFile() {

        JFileChooser fileChooser = new JFileChooser(lastDirectory);
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            lastDirectory = fileChooser.getCurrentDirectory();
            readFile(fileChooser.getSelectedFile());
        }
    }

    private void validateJson() {

        String text = textPane.getText();
        if (StringUtils.isBlank(text)) {
            return;
        }

        try {
            GuiUtils.validateJson(text);
            validateButton.setForeground(Color.GREEN.darker());
        } catch (Exception e) {
            validateButton.setForeground(Color.RED);
            popup(e);
        }
    }

    private void toSimpleJson() {

        String text = textPane.getText();
        if (StringUtils.isBlank(text)) {
            return;
        }

        try {
            textPane.setText(GuiUtils.toSimpleJson(text));
        } catch (Exception e) {
            popup(e);
        }
    }

    private void toPrettyJson() {

        String text = textPane.getText();
        if (StringUtils.isBlank(text)) {
            return;
        }

        try {
            textPane.setText(GuiUtils.toPrettyJson(text));
        } catch (Exception e) {
            popup(e);
        }
    }

    private void readFile(File selectedFile) {

        try {
            textPane.setText(GuiUtils.readFile(selectedFile));
        } catch (Exception e) {
            popup(e);
        }
    }

    private void findInJson() {

        String findText = searchField.getText();

        SearchContext context = new SearchContext(findText);
        if (!SearchEngine.find(textPane, context).wasFound()) {
            UIManager.getLookAndFeel().provideErrorFeedback(textPane);
        }
        else {
            //textPane.requestFocusInWindow();
        }
        RTextArea.setSelectedOccurrenceText(findText);
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

    private JButton getFormatButton() {

        JButton formatButton = new JButton("Format");
        formatButton.setToolTipText("formats the input provided");
        GuiUtils.applyShortcut(formatButton, KeyEvent.VK_Q, "Format", new AbstractAction() {

            private static final long serialVersionUID = 123523535L;

            @Override
            public void actionPerformed(ActionEvent e) {

                toPrettyJson();

            }
        });
        formatButton.addActionListener(e -> {

            toPrettyJson();
        });
        return formatButton;

    }

    private JButton getDeformatButton() {

        JButton deformatButton = new JButton("DeFormat");
        deformatButton.setToolTipText("compresses json, should be used to send compressed data over networks");
        GuiUtils.applyShortcut(deformatButton, KeyEvent.VK_W, "Deformat", new AbstractAction() {

            private static final long serialVersionUID = 12352003535L;

            @Override
            public void actionPerformed(ActionEvent e) {

                toSimpleJson();

            }
        });
        deformatButton.addActionListener(e -> {

            toSimpleJson();
        });
        return deformatButton;
    }

    private JButton getValidateButton() {

        JButton validateButton = new JButton("Validate");
        validateButton.setToolTipText("validates the json");
        GuiUtils.applyShortcut(validateButton, KeyEvent.VK_E, "Validate", new AbstractAction() {

            private static final long serialVersionUID = 1235235135L;

            @Override
            public void actionPerformed(ActionEvent e) {

                validateJson();

            }
        });
        validateButton.addActionListener(e -> {

            validateJson();
        });
        return validateButton;
    }

    private JTextField getSearchField() {

        JTextField searchField = new JTextField(10);
        searchField.setToolTipText("CTRL+K (fwd) CTRL+SHIFT+K (bkd)");
        GuiUtils.applyShortcut(searchField, KeyEvent.VK_F, "Find", new AbstractAction() {

            private static final long serialVersionUID = 12352311535L;

            @Override
            public void actionPerformed(ActionEvent e) {

                searchField.requestFocusInWindow();

            }
        });
        searchField.addActionListener(e -> {

            findInJson();
        });
        return searchField;
    }

    private void popup(Exception e) {

        LOGGER.warn(e.getMessage(), e);
        JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

//    private void popup(String msg) {
//
//        JOptionPane.showMessageDialog(this, msg, "Info", JOptionPane.INFORMATION_MESSAGE);
//    }
}
