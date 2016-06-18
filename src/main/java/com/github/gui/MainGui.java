package com.github.gui;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.gui.base64.Base64Panel;
import com.github.gui.json.JsonViewerPanel;
import com.github.gui.url.URLPanel;

public class MainGui {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainGui.class);

    private JFrame mainFrame;
    
    public static void main(String[] args) {

        MainGui mainGui = new MainGui();
        mainGui.init();
    }

    private void init() {

        LOGGER.info("starting the gui in {}*{}", GuiConstants.MAIN_GUI_WIDTH, GuiConstants.MAIN_GUI_HEIGHT);
        mainFrame = new JFrame(GuiConstants.MAIN_GUI_TITLE);
        mainFrame.setSize(GuiConstants.MAIN_GUI_WIDTH, GuiConstants.MAIN_GUI_HEIGHT);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(true);
        mainFrame.setLocationByPlatform(true);
        mainFrame.setLayout(new GridLayout(1, 1));

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(getJMenu("JSON Viewer", "A JSON Formatter", JsonViewerPanel.getInstance()));
        menuBar.add(getJMenu("Base64", "A Base64 Encoder/Decoder", Base64Panel.getInstance()));
        menuBar.add(getJMenu("URL Encoding", "A URL Encoder/Decoder", URLPanel.getInstance()));
        menuBar.add(getJMenu("XML Viewer", "A XML Formatter", JsonViewerPanel.getInstance()));
        menuBar.add(getJMenu("Compare", "A Text Compare Tool", URLPanel.getInstance()));
        mainFrame.setJMenuBar(menuBar);
        mainFrame.setVisible(true);
    }
    
    private JMenu getJMenu(String title, String toolTip, AbstractPanel panel){
        JMenu jsonViewerMenu = new JMenu(title);
        jsonViewerMenu.setToolTipText(toolTip);
        jsonViewerMenu.addMenuListener(new MenuListener() {
            
            @Override
            public void menuSelected(MenuEvent e) {
            
                resetMainGui();
                mainFrame.getContentPane().add(panel);
                refreshMainGui();
            }
            
            @Override
            public void menuDeselected(MenuEvent e) {
            }
            
            @Override
            public void menuCanceled(MenuEvent e) {
                System.out.println("menuCanceled");
            }
        });
        return jsonViewerMenu;
    }
    
    private void resetMainGui() {
        mainFrame.getContentPane().removeAll();
    }
    
    private void refreshMainGui() {
        mainFrame.revalidate();
        mainFrame.repaint();
    }
}
