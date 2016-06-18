package com.github.gui.base64;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.gui.AbstractPanel;
import com.github.gui.GuiUtils;

public class Base64UrlPanel extends AbstractPanel {

    private static final long serialVersionUID = 7554118114747990205L;

    private static final Logger LOGGER = LoggerFactory.getLogger(Base64UrlPanel.class);

    private static final Base64UrlPanel INSTANCE = new Base64UrlPanel();

    public static Base64UrlPanel getInstance() {

        return INSTANCE;
    }

    private JTextPane inputTextPane;
    private JTextPane outputTextPane;

    public Base64UrlPanel() {

        init();
    }

    private void init() {

        setSize(getMaximumSize());
        setLayout(new BorderLayout());

        JPanel settingPanel = new JPanel();

        JButton base64EncodeButton = new JButton("Base64 Encode");
        base64EncodeButton.setToolTipText("encodes into Base64 string");
        base64EncodeButton.addActionListener(e -> {

            base64EncodeContent();
        });
        settingPanel.add(base64EncodeButton);

        JButton base64DecodeButton = new JButton("Base64 Decode");
        base64DecodeButton.setToolTipText("decodes into Base64 string");
        base64DecodeButton.addActionListener(e -> {

            base64DecodeContent();
        });
        settingPanel.add(base64DecodeButton);

        JButton urlEncodeButton = new JButton("URL Encode");
        urlEncodeButton.setToolTipText("URL encoded form");
        urlEncodeButton.addActionListener(e -> {

            urlEncodeContent();
        });
        settingPanel.add(urlEncodeButton);

        JButton urlDecodeButton = new JButton("URL Decode");
        urlDecodeButton.setToolTipText("URL decoded form");
        urlDecodeButton.addActionListener(e -> {

            urlDecodeContent();
        });
        settingPanel.add(urlDecodeButton);

        add(settingPanel, BorderLayout.NORTH);

        JPanel textAreaPanel = new JPanel();
        textAreaPanel.setLayout(new GridLayout(1, 2, 20, 0));
        textAreaPanel.setSize(getMaximumSize());

        inputTextPane = new JTextPane();
        outputTextPane = new JTextPane();

        textAreaPanel.add(GuiUtils.getScrollPane(inputTextPane));
        textAreaPanel.add(GuiUtils.getScrollPane(outputTextPane));

        add(textAreaPanel, BorderLayout.CENTER);

    }

    private void base64EncodeContent() {

        String inputText = inputTextPane.getText();
        if (StringUtils.isEmpty(inputText)) {
            return;
        }
        try {
            String outputText = Base64.getEncoder().encodeToString(inputText.getBytes());
            outputTextPane.setText(outputText);
        } catch (Exception e) {
            popup(e);
        }
    }

    private void base64DecodeContent() {

        String inputText = inputTextPane.getText();
        if (StringUtils.isEmpty(inputText)) {
            return;
        }
        try {
            String outputText = new String(Base64.getDecoder().decode(inputText));
            outputTextPane.setText(outputText);
        } catch (Exception e) {
            popup(e);
        }

    }

    private void urlEncodeContent() {

        String inputText = inputTextPane.getText();
        if (StringUtils.isEmpty(inputText)) {
            return;
        }
        try {
            String outputText = URLEncoder.encode(inputText, "UTF-8");
            outputTextPane.setText(outputText);
        } catch (Exception e) {
            popup(e);
        }

    }

    private void urlDecodeContent() {

        String inputText = inputTextPane.getText();
        if (StringUtils.isEmpty(inputText)) {
            return;
        }
        try {
            String outputText = new String(URLDecoder.decode(inputText, "UTF-8"));
            outputTextPane.setText(outputText);
        } catch (Exception e) {
            popup(e);
        }

    }

    private void popup(Exception e) {

        LOGGER.warn(e.getMessage(), e);
        JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
