package com.github.jutil.core.gui;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

import org.fife.ui.rtextarea.RTextScrollPane;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jutil.compare.parser.DiffParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GuiUtils {

    public static RTextScrollPane getScrollTextPane(String contentType) {

        RTextScrollPane scrollPane = new RTextScrollPane(getTextPane(contentType));
        scrollPane.setFoldIndicatorEnabled(true);
        scrollPane.setLineNumbersEnabled(true);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        return scrollPane;
    }

    private static ExtendedTextPane getTextPane(String contentType) {

        ExtendedTextPane textPane = new ExtendedTextPane();
        textPane.setSyntaxEditingStyle(contentType);
        textPane.setAutoscrolls(true);
        return textPane;
    }

    public static void applyShortcut(JComponent component, int keyCode, String actionKey, AbstractAction abstractAction) {

        InputMap im = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        im.put(KeyStroke.getKeyStroke(keyCode, KeyEvent.CTRL_DOWN_MASK), actionKey);

        ActionMap am = component.getActionMap();
        am.put(actionKey, abstractAction);
    }

    public static String readFile(File selectedFile) throws IOException {

        StringBuilder builder = new StringBuilder();
        Files.lines(Paths.get(selectedFile.getAbsolutePath())).forEachOrdered(
                s -> builder.append(s).append(DiffParser.NEW_LINE));
        
        return builder.toString();
    }

    public static String toPrettyJson(String text) throws IOException {

        GsonBuilder builder = new GsonBuilder();
        
        builder.setPrettyPrinting().disableHtmlEscaping().serializeNulls().setLenient();
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
    
    public static String escapeHtmlEntites(String str) {
        return str.replace("&lt;", "<").replace("&gt;", ">");
    }
}
