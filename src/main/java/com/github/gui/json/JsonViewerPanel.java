package com.github.gui.json;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.gui.AbstractPanel;
import com.github.gui.GuiUtils;

public class JsonViewerPanel extends AbstractPanel {

    private static final long serialVersionUID = 7554118114747990205L;

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonViewerPanel.class);

    private static final JsonViewerPanel INSTANCE = new JsonViewerPanel();

    public static JsonViewerPanel getInstance() {

        return INSTANCE;
    }

    private JTextPane textPane;

    private File lastDirectory;

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

        textPane = new JTextPane();
        add(GuiUtils.getScrollPane(textPane), BorderLayout.CENTER);
    }

    private void loadFile() {

        JFileChooser fileChooser = new JFileChooser(lastDirectory);
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            lastDirectory = fileChooser.getCurrentDirectory();
            readFile(fileChooser.getSelectedFile());
        }

    }

    private void toSimpleString() {

        String text = textPane.getText();
        if (StringUtils.isBlank(text)) {
            return;
        }

        if (!isValidJson(text)) {
            return;
        }
        textPane.setText(StringUtils.deleteWhitespace(text));
    }

    private void toPrettyString() {

        String text = textPane.getText();
        if (StringUtils.isBlank(text)) {
            return;
        }

        if (!isValidJson(text)) {
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            String prettyString;

            prettyString = mapper.writer().withDefaultPrettyPrinter().writeValueAsString(mapper.readTree(text));

            textPane.setText(prettyString);
        } catch (IOException e) {
            popup(e);
        }
    }

    private boolean isValidJson(String text) {

        try {
            if (!StringUtils.startsWithAny(text, "[", "{")) {
                throw new IOException("Invalid JSON, should start it '[' or '}'");
            }

            ObjectMapper mapper = new ObjectMapper();
            mapper.readTree(text);
            return true;
        } catch (IOException e) {
            popup(e);
            return false;
        }
    }

    private void readFile(File selectedFile) {

        try {
            StringBuilder builder = new StringBuilder();
            Files.lines(Paths.get(selectedFile.getAbsolutePath())).forEachOrdered(
                    s -> builder.append(s).append(System.lineSeparator()));
            textPane.setText(builder.toString());
        } catch (Exception e) {
            popup(e);
        }
    }

    private void popup(Exception e) {

        LOGGER.warn(e.getMessage(), e);
        JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
