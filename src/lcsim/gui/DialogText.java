package lcsim.gui;

import java.awt.Font;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class DialogText extends JDialog
{

    public DialogText(String title, String text)
    {
        // TODO Auto-generated constructor stub
        JLabel label = new JLabel("<html>"+text+"</html>");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        this.setTitle(title);
        this.add(label);
        this.setVisible(true);
        this.pack();
        this.setSize(this.getWidth()+20,this.getHeight()+10);
        this.setResizable(false);
    }
}
