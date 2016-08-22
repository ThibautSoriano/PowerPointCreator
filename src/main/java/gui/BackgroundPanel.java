package main.java.gui;

import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * Demonstrate use of GlassPane in JWindow & friends. Buttons enable/disable it.
 * @author Eckstein et al, in the O'Reilly book "Java Swing"
 */
@SuppressWarnings("serial")
public class BackgroundPanel extends JPanel
{
    // The Image to store the background image in.
    Image img;
    public BackgroundPanel()
    {
        JLabel l = new JLabel("pute");
        // Loads the background image and stores in img object.
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        img = Toolkit.getDefaultToolkit().createImage(classLoader.getResource("background_white.jpg"));
        add(l);
        
    }

    @Override
    public void paintComponent(Graphics g)
    {
        // Draws the img to the BackgroundPanel.
        super.paintComponent(g);
        g.drawImage(img, 0, 0, this);
    }
    
    
    public static void main(String []args) {
        JFrame f = new JFrame();
        
        BackgroundPanel fdp =new BackgroundPanel();
        fdp.setLayout(null);
        f.getContentPane().add(fdp);
//        f.getContentPane().add(new JButton());
        
        JPanel zk = new JPanel();
        zk.setBounds(10, 10, 100, 100);
        zk.add(new JLabel("zk"));
//      zk.setOpaque(false);
        fdp.add(zk);
       
        f.setVisible(true);
        f.setSize(600, 500);
    }
    
}