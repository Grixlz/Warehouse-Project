//Usually you will require both swing and awt packages
// even if you are working with just swings.
import javax.swing.*;
import java.awt.*;
public class ClientGUI extends JFrame{
private JLabel label;
private JButton button;
private JTextField textfield;
    
public GUI() {
	//==================================================================== 
	//Client GUI Materials
	//==================================================================== 
	setLayout(new FlowLayout());
	label = new JLabel("Client State");
	add(label);
	label = new JLabel("Client State");
	add(label);
    textfield = new JTextField(15);
    add(textfield);
    button = new JButton("BUTTON");
    add(button);
    //==================================================================== 
  	//Clerk GUI Materials
  	//==================================================================== 
    
    //==================================================================== 
  	//Manager GUI Materials
  	//==================================================================== 
    
    //==================================================================== 
  	//Modify Card GUI Materials
  	//==================================================================== 
    
    //==================================================================== 
  	//Query system about clients  GUI Materials
  	//==================================================================== 
    
    //==================================================================== 
  	//Login GUI Materials
  	//==================================================================== 
	
	
}
    
    
    
    
    
public void clientGUI(){
   GUI gui = new GUI();
   gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   gui.setSize(400,600);
   gui.setVisible(true);
   gui.setTitle("Warehouse Program");
}





}