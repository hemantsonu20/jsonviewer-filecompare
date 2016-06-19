package com.github.gui;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GuiUtils {

    public static RTextScrollPane getScrollTextPane(String contentType) {

        RTextScrollPane scrollPane = new RTextScrollPane(getTextPane(contentType));
        scrollPane.setFoldIndicatorEnabled(true);
        scrollPane.setLineNumbersEnabled(true);
        return scrollPane;
    }

    private static RSyntaxTextArea getTextPane(String contentType) {

        RSyntaxTextArea textPane = new RSyntaxTextArea();
        textPane.setSyntaxEditingStyle(contentType);
        textPane.setCodeFoldingEnabled(true);
        textPane.setHighlightCurrentLine(true);
        // textPane.setAutoIndentEnabled(true);
        // textPane.setHyperlinksEnabled(true);
        textPane.setBracketMatchingEnabled(true);
        return textPane;
    }

    public static void applyShortcut(JComponent component, int keyCode, String actionKey, AbstractAction abstractAction)
 {

        InputMap im = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        im.put(KeyStroke.getKeyStroke(keyCode, KeyEvent.CTRL_DOWN_MASK), actionKey);
        
        ActionMap am = component.getActionMap();
        am.put(actionKey, abstractAction);
    }

    public static String readFile(File selectedFile) throws IOException {

        StringBuilder builder = new StringBuilder();
        Files.lines(Paths.get(selectedFile.getAbsolutePath())).forEachOrdered(
                s -> builder.append(s).append(System.lineSeparator()));
        return builder.toString();
    }

    public static String toPrettyJson(String text) throws IOException {

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting().disableHtmlEscaping().serializeNulls();
        Gson gson = builder.create();
        Object jsonObj = gson.fromJson(text, Object.class);
        return gson.toJson(jsonObj);
    }

    public static String toSimpleJson(String text) {

        Gson gson = new Gson();
        Object jsonObj = gson.fromJson(text, Object.class);
        return gson.toJson(jsonObj);
    }

    public static void validateJson(String text) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.readTree(text);
    }
}
