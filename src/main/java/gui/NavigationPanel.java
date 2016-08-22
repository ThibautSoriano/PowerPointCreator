package main.java.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import main.java.utils.Internationalization;

@SuppressWarnings("serial")
public class NavigationPanel extends JPanel implements ActionListener{
    
    
    
    
    private JButton btnNext;
    private JButton btnPrevious;
    private JButton btnValidate;
    private IMainFrame mainFrame;
    
    
    public JButton getBtnNext() {
        return btnNext;
    }


    
    
    public static final int NAVIGATION_HEIGHT = 62;
    
    public static final int NAVIGATION_WIDTH = 584;
    
    public NavigationPanel(IMainFrame mainFrame) {
        
       this.mainFrame = mainFrame;
        
        setOpaque(false);
        setLayout(null);
        setBounds(0, SettingsChoicePanel.PANEL_HEIGHT+10, NAVIGATION_WIDTH, NAVIGATION_HEIGHT);
        
        
        btnNext = new JButton(Internationalization.getKey("Next"));
        btnNext.setBounds(NAVIGATION_WIDTH - 120, 15, 100,30);
        btnNext.addActionListener(this);
        
        
        add(btnNext);
        
        
        
        btnValidate = new JButton(Internationalization.getKey("Validate"));
        btnValidate.setBounds(NAVIGATION_WIDTH - 120, 15, 100,30);
        btnValidate.addActionListener(this);
        
        
        add(btnValidate);
        
        
       
        
        
        
        btnPrevious = new JButton(Internationalization.getKey("Previous"));
        btnPrevious.setBounds(20, 15, 100,30);
        btnPrevious.addActionListener(this);
        
        add(btnPrevious);
    
       
        this.setVisible(true);
        
    }
    
 
    

  public void hidePreviousButton(){
      btnPrevious.setVisible(false);
  }

  public void showPreviousButton(){
      btnPrevious.setVisible(true);
  }


 public void showNextButton() {
     btnNext.setVisible(true);
     btnValidate.setVisible(false);
 }
  
 
 public void showValidateButton() {
     btnValidate.setVisible(true);
     btnNext.setVisible(false);
 }
  
  
  public NavigationPanel getNewInstance() {
      NavigationPanel np = new NavigationPanel(mainFrame);
      return np;
  }
    
  
  
  @Override
  public void actionPerformed(ActionEvent e) {
      if (Internationalization.getKey("Previous").equals(e.getActionCommand())) {
          mainFrame.previousPanel();    
      }        
      else if (Internationalization.getKey("Next").equals(e.getActionCommand())) {
          
         mainFrame.nextPanel();
      }
      else
          mainFrame.validation();
       
  }
  
  
  
}
