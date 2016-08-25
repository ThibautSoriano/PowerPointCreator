package main.java.gui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class ProgressBarWindow extends JFrame {

    private int height;
    private int width;

    private int maxValue;

    private JProgressBar progressBar;
    private JLabel label;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public ProgressBarWindow() {

        Dimension dimension = java.awt.Toolkit.getDefaultToolkit()
                .getScreenSize();
        int height = (int) dimension.getHeight();
        int width = (int) dimension.getWidth();

        this.height = 120;
        this.width = 300;
        maxValue = 100;

        setAlwaysOnTop(true);
        setBounds(width / 2 - this.width / 2, height / 2 - this.height / 2,
                this.width, this.height);
        setTitle("Loading");
        setLayout(new BorderLayout());
        setResizable(false);
        setIconImage(Toolkit.getDefaultToolkit()
                .getImage(getClass().getResource("/icon.png")));

        progressBar = new JProgressBar(0, maxValue);
        progressBar.setBounds(10, 45, this.width - 25, 30);

        label = new JLabel("In progress...");
        label.setFont(new Font("Tahoma", Font.PLAIN, 13));
        label.setBounds(10, -2, this.width - 25, 40);
        label.setHorizontalAlignment(JLabel.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        panel.add(progressBar);
        panel.add(label);
        getContentPane().add(panel);

        setVisible(false);
    }

    public static void main(String[] args) {

        try {
            // set for file chooser look
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        ProgressBarWindow pbw = new ProgressBarWindow();
        pbw.setVisible(true);
        pbw.fill();
    }

    private void fill() {
        while (true) {
            for (int i = 0; i < 101; i++) {
                progressBar.setValue(i);
                setText(Integer.toString(i));

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public void setValue(int value) {
        progressBar.setValue(value);
        if (value >= maxValue) {
            dispose();
            progressBar.setValue(0);
        }
    }

    public void setText(String text) {
        // Thread t = new Thread(new Runnable() {
        //
        // @Override
        // public void run() {
        // System.out.println(text);
        // label.setText(text);
        //
        // }
        // });
        //
        // t.start();
        label.setText(text);
    }

}
