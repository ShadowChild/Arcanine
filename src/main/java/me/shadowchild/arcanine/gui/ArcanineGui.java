package me.shadowchild.arcanine.gui;

import javax.swing.*;
import java.awt.*;

public class ArcanineGui implements Runnable {

    public static JFrame frame;

    @Override
    public void run() {

//        System.setProperty("java.awt.headless", "true");
        Toolkit tk = Toolkit.getDefaultToolkit();
        frame = new JFrame();
        frame.setTitle("Arcanine Output Window");
        frame.setSize(new Dimension(600, 480));
        frame.setContentPane(getMainContent());
        frame.setLocationRelativeTo(null);

//        frame.pack();
        frame.setVisible(true);
    }

    public JPanel getMainContent() {

        JPanel panel = new JPanel();
        GridLayout layout = new GridLayout(2, 1);
        panel.setLayout(layout);
        panel.setMaximumSize(new Dimension(600, 480));
        panel.setOpaque(true);

        JTextPane pane = new JTextPane();
        pane.setEditable(false);
        pane.setSize(600, 400);
        pane.setMinimumSize(new Dimension(600, 400));
        panel.add(pane);

        JButton button = new JButton("Upload To Hastebin.");
        button.addActionListener(e -> {

        });
        panel.add(button);

        return panel;
    }
}
