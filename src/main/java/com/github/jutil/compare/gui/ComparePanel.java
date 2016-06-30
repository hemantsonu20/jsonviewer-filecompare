package com.github.jutil.compare.gui;

import java.awt.GridLayout;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.JScrollBar;

import com.github.jutil.compare.parser.ParserManager;
import com.github.jutil.core.gui.AbstractPanel;

public class ComparePanel extends AbstractPanel {

    private static final long serialVersionUID = 114747990205L;

    private static final String BAR_IDENTIFIER = "identifier";

    // private static final Logger LOGGER =
    // LoggerFactory.getLogger(ComparePanel.class);

    private static final ComparePanel INSTANCE = new ComparePanel();

    public static ComparePanel getInstance() {

        return INSTANCE;
    }

    private TextPanel leftPanel;
    private TextPanel rightPanel;

    private ParserManager parserManager;

    public ComparePanel() {

        init();
        parserManager = new ParserManager(leftPanel.getTextPane(), rightPanel.getTextPane());
    }

    @Override
    public void removeNotify() {

        parserManager.stopParsing();
        super.removeNotify();
    }

    private void init() {

        setSize(getMaximumSize());
        setLayout(new GridLayout(1, 2));

        add(leftPanel = new TextPanel());
        add(rightPanel = new TextPanel());

        // hiding right scroll bar
        // rightPanel.getScrollPane().setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        // leftPanel.getScrollPane().setModel(rightPanel.getScrollPane().getVerticalScrollBar().getModel());

        JScrollBar leftVBar = leftPanel.getScrollPane().getVerticalScrollBar();
        leftVBar.addAdjustmentListener(adjustmentListener);

        JScrollBar rightVBar = rightPanel.getScrollPane().getVerticalScrollBar();
        rightVBar.addAdjustmentListener(adjustmentListener);

        JScrollBar leftHBar = leftPanel.getScrollPane().getHorizontalScrollBar();
        leftHBar.addAdjustmentListener(adjustmentListener);

        JScrollBar rightHBar = rightPanel.getScrollPane().getHorizontalScrollBar();
        rightHBar.addAdjustmentListener(adjustmentListener);

        // the logic is to update the left when right is scrolled and viceversa
        leftVBar.putClientProperty(BAR_IDENTIFIER, rightVBar);
        rightVBar.putClientProperty(BAR_IDENTIFIER, leftVBar);

        leftHBar.putClientProperty(BAR_IDENTIFIER, rightHBar);
        rightHBar.putClientProperty(BAR_IDENTIFIER, leftHBar);
    }


    private AdjustmentListener adjustmentListener = (AdjustmentEvent e) -> {

        JScrollBar bar = (JScrollBar) e.getAdjustable();

        JScrollBar anotherBar = (JScrollBar) bar.getClientProperty(BAR_IDENTIFIER);
        anotherBar.setValue(bar.getValue());
    };
    

    // private void popup(Exception e) {
    //
    // LOGGER.warn("Exception occurred", e);
    // }

    // private void popup(String msg) {
    //
    // JOptionPane.showMessageDialog(this, msg, "Info",
    // JOptionPane.INFORMATION_MESSAGE);
    // }
}
