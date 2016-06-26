package com.github.jutil.json.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.commons.lang3.StringUtils;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.jutil.core.gui.AbstractPanel;
import com.github.jutil.core.gui.ExtendedTextPane;
import com.github.jutil.core.gui.GuiUtils;
import com.github.jutil.gui.GuiConstants;
import com.google.gson.JsonSyntaxException;

public class JsonViewerPanel extends AbstractPanel {

    private static final long serialVersionUID = 7554118114747990205L;

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonViewerPanel.class);

    private static final JsonViewerPanel INSTANCE = new JsonViewerPanel();

    public static JsonViewerPanel getInstance() {

        return INSTANCE;
    }

    

    private JLabel jsonIndicator = new JLabel();
    private Icon validJsonIcon;
    private Icon invalidJsonIcon;

    private ExtendedTextPane textPane;
    private JTextField searchField;

    private Timer timer;

    private JLabel msgLabel = new JLabel();
    private JPanel msgPanel = new JPanel();

    private File lastDirectory;

    public JsonViewerPanel() {

        init();
        
        timer = new Timer(GuiConstants.DEFAULT_DELAY_MS, (e) -> validateJson());
        timer.setRepeats(false);

    }

    private void init() {

        setSize(getMaximumSize());
        setLayout(new BorderLayout());

        JPanel settingPanel = new JPanel();
        settingPanel.add(jsonIndicator);
        settingPanel.add(getLoadFileButton());
        settingPanel.add(getFormatButton());
        settingPanel.add(getDeformatButton());
        settingPanel.add(new JLabel("Search"));
        settingPanel.add(searchField = getSearchField());
        settingPanel.add(msgLabel);

        msgPanel.add(msgLabel);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(settingPanel, BorderLayout.NORTH);
        topPanel.add(msgPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        RTextScrollPane scrollPane = GuiUtils.getScrollTextPane(SyntaxConstants.SYNTAX_STYLE_JSON);
        textPane = (ExtendedTextPane) scrollPane.getTextArea();

        textPane.setCodeFoldingEnabled(true);
        textPane.setHighlightCurrentLine(true);
        // textPane.setAutoIndentEnabled(true);
        // textPane.setHyperlinksEnabled(true);
        textPane.setBracketMatchingEnabled(true);
        textPane.setPaintMatchedBracketPair(true);

        GuiUtils.applyShortcut(textPane, KeyEvent.VK_L, "lineNumber", new AbstractAction() {

            private static final long serialVersionUID = -506406567119696504L;

            @Override
            public void actionPerformed(ActionEvent e) {

                try {

                    textPane.setCaretLineNumber(Integer.parseInt((String) JOptionPane.showInputDialog(
                            JsonViewerPanel.this, String.format("Enter Line Number (1, %d)", textPane.getLineCount()),
                            "Go to Line", JOptionPane.PLAIN_MESSAGE, null, null,
                            Integer.toString(textPane.getCaretLineNumber() + 1))));
                } catch (NumberFormatException ex) {
                    // ignore
                } catch (Exception ex) {
                    LOGGER.warn("excption while taking line number as input", ex);
                }
            }
        });

        textPane.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent e) {

                timer.restart();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {

                timer.restart();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
        add(scrollPane, BorderLayout.CENTER);

        validJsonIcon = new ImageIcon(getClass().getResource("/tick.png"));
        invalidJsonIcon = new ImageIcon(getClass().getResource("/error.png"));
        
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
            jsonIndicator.setIcon(null);
            msgPanel.setVisible(false);
            return;
        }

        try {
            GuiUtils.validateJson(text);
            msgPanel.setVisible(false);
            jsonIndicator.setIcon(validJsonIcon);
        } catch (Exception e) {
            jsonIndicator.setIcon(invalidJsonIcon);
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
            msgPanel.setVisible(false);
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
            msgPanel.setVisible(false);
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
            // textPane.requestFocusInWindow();
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

        LOGGER.warn("Exception occurred", e);
        // JOptionPane.showMessageDialog(this, e.getMessage(), "Error",
        // JOptionPane.ERROR_MESSAGE);

        StringBuilder filteredMsg = new StringBuilder();
        if (e instanceof JsonProcessingException) {
            JsonProcessingException jpe = (JsonProcessingException) e;
            filteredMsg.append(jpe.getOriginalMessage());
            JsonLocation location = jpe.getLocation();
            String locationStr = String.format(" at line %d col %d", location.getLineNr(), location.getColumnNr());
            filteredMsg.append(locationStr);
            // textPane.setCaretPosition(textPane.getDocument().getDefaultRootElement()
            // .getElement(location.getLineNr() - 1).getStartOffset());
            // textPane.requestFocusInWindow();
        }
        else if (e instanceof JsonSyntaxException) {

            String msg = StringUtils.substringAfter(e.getMessage(), "Exception: ");
            filteredMsg.append(msg);
        }

        else {
            filteredMsg.append(e.getMessage());
        }

        msgLabel.setText(filteredMsg.toString());
        msgLabel.setForeground(Color.RED);
        msgPanel.setVisible(true);
    }
}
