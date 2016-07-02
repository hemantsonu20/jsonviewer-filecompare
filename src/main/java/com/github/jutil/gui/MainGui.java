package com.github.jutil.gui;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jutil.base64.gui.Base64UrlPanel;
import com.github.jutil.compare.gui.ComparePanel;
import com.github.jutil.core.gui.AbstractPanel;
import com.github.jutil.json.gui.JsonViewerPanel;

public class MainGui {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainGui.class);

    private JFrame mainFrame;
    private JButton jsonMenu;

    public static void main(String[] args) {

        MainGui mainGui = new MainGui();

        mainGui.init();

        SwingUtilities.invokeLater(() -> mainGui.setVisible());

    }

    private void setVisible() {

        mainFrame.setVisible(true);
        jsonMenu.doClick();
    }

    private void init() {

        LOGGER.info("starting the gui in {}*{}", GuiConstants.MAIN_GUI_WIDTH, GuiConstants.MAIN_GUI_HEIGHT);
        mainFrame = new JFrame(GuiConstants.MAIN_GUI_TITLE);
        mainFrame.setSize(GuiConstants.MAIN_GUI_WIDTH, GuiConstants.MAIN_GUI_HEIGHT);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(true);
        mainFrame.setLocationByPlatform(true);
        mainFrame.setLayout(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(jsonMenu = getJMenu("JSON Viewer", 'J', "A JSON Formatter", JsonViewerPanel.getInstance()));
        menuBar.add(getJMenu("Base64/URL", 'B', "A Base64/Url Encoder/Decoder", Base64UrlPanel.getInstance()));
        menuBar.add(getJMenu("Compare", 'C', "A Text Compare Tool", ComparePanel.getInstance()));

        mainFrame.setJMenuBar(menuBar);

        mainFrame.add(ComparePanel.getInstance(), BorderLayout.CENTER);
    }

    private JButton getJMenu(String title, char mnemonic, String toolTip, AbstractPanel panel) {

        JButton button = new JButton(title);
        button.setMnemonic(mnemonic);
        button.setToolTipText(toolTip);
        button.addActionListener(e -> {

            Container contentPane = mainFrame.getContentPane();

            if (contentPane.getComponents().length <= 0 || contentPane.getComponent(0) != panel) {
                resetMainGui();
                mainFrame.getContentPane().add(panel);
                refreshMainGui();
            }
        });

        return button;

    }

    private void resetMainGui() {

        mainFrame.getContentPane().removeAll();
    }

    private void refreshMainGui() {

        mainFrame.revalidate();
        mainFrame.repaint();
    }
}
