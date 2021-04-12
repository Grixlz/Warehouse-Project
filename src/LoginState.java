import java.util.*;
import java.io.*;
import javax.swing.*;
import java.text.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Component;

public class LoginState extends WarehouseState  implements ActionListener{
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));  
    private static LoginState instance;
    private JFrame frame;
    private AbstractButton clientButton, clerkButton, managerButton, logoutButton, clientLoginButton, back;
    static JFrame cleintLogin;
    static JLabel WarehouseLabel, clientLoginLabel;
    private JTextArea textArea;
    static JTextField clientIdField;
    
    
    
	private LoginState()
	{
        super();
	}

    public static LoginState instance() {
      if (instance == null) {
        instance = new LoginState();
      }
      return instance;
    }
//====================================================================
// GUI
//====================================================================
public void process() {

    frame = WarehouseContext.instance().getFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JPanel LoginStatePanel = new JPanel();
    BoxLayout boxlayout = new BoxLayout(LoginStatePanel, BoxLayout.Y_AXIS);
    LoginStatePanel.setLayout(boxlayout);
    
    //button and listeners
    clientButton = new JButton("Client");
    clerkButton =  new JButton("Clerk");
    managerButton = new JButton("Manager");
    logoutButton = new JButton("Logout");  
    clientButton.addActionListener(this);
    clerkButton.addActionListener(this);
    managerButton.addActionListener(this);
    logoutButton.addActionListener(this);
    
    //label
    WarehouseLabel = new JLabel("Login Menu");
    WarehouseLabel.setFont(new Font("Helvetica", Font.PLAIN, 20));

    
    //center align
    WarehouseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    clientButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    clerkButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    managerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    
    
    //adding to pane
    LoginStatePanel.add(WarehouseLabel);
    LoginStatePanel.add(this.clientButton);
    LoginStatePanel.add(this.clerkButton);
    LoginStatePanel.add(this.managerButton);
    LoginStatePanel.add(this.logoutButton);
    frame.getContentPane().add(LoginStatePanel);
    frame.setVisible(true);
    frame.paint(frame.getGraphics()); 
    frame.toFront();
    frame.requestFocus();
  }

public void actionPerformed(ActionEvent event) {
	  if (event.getSource().equals(this.clientButton)) {
		  clear(); 
		  this.client();
	  }
	  else if (event.getSource().equals(this.logoutButton)) {
		  clear();
		  this.logout();
	  }
		   
	  else if (event.getSource().equals(this.clerkButton)) {
		  clear();
		  this.clerk();
	  }
	  else if(event.getSource().equals(this.managerButton)) {
		  clear();
		  this.manager();
	  }
		  
	  else if(event.getSource().equals(this.back)) {
		  clear();
		  this.process();
	  }
	  else if (event.getSource().equals(this.clientLoginButton)) {
		  String id;
	  	  id = clientIdField.getText();
	  	  this.clientStateTransfer(id);
		  
	  }
	}


//====================================================================
// Login Methods
//====================================================================
//Remove after completing Context 
    private void client(){
    	clear();
	 	cleintLogin = WarehouseContext.instance().getFrame();
	 	cleintLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    clientLoginLabel = new JLabel("Enter Client ID");
	    clientLoginButton = new JButton("Login");
	    clientIdField = new JTextField(10);
	    frame.getContentPane().setLayout(new FlowLayout());
	    back = new JButton("Back");
        back.addActionListener(this);
	    clientLoginButton.addActionListener(this);
	    cleintLogin.add(this.clientLoginLabel);
	    cleintLogin.add(this.clientIdField);
	    cleintLogin.add(clientLoginButton);
	    cleintLogin.add(back);
	    cleintLogin.setVisible(true);
	    cleintLogin.paint(cleintLogin.getGraphics());
	    frame.toFront();
	    frame.requestFocus();
	    
    }
    private void clientStateTransfer(String id) {
        
    	clear();
        if( (Warehouse.getInstance()).getClient(id) != null) // NOTE: Uncomment this once you're done
        {
            (WarehouseContext.instance()).setLoginUser(WarehouseContext.IS_CLIENT);
            (WarehouseContext.instance()).setCurrentUser(id);      
            (WarehouseContext.instance()).changeState(0);
        }
        else
        {
        	clear();
        	JPanel textbox = new JPanel();
        	JPanel backbutton = new JPanel();
        	frame = WarehouseContext.instance().getFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            textArea = new JTextArea(15, 30);
            textbox.add(new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
            back = new JButton("Back");
            back.addActionListener(this);
            backbutton.add(back);
            frame.add(textbox);
            frame.add(backbutton);
            frame.setVisible(true); 
            frame.paint(frame.getGraphics());
    	    frame.toFront();
    	    frame.requestFocus();
    	    this.redirectSystemStreams();
            System.out.println("Error! Couldn't find client with ID " + id);
        }
    }

    private void clerk(){
        (WarehouseContext.instance()).setLoginUser(WarehouseContext.IS_CLERK);      
        (WarehouseContext.instance()).changeState(1);
    }
    private void manager(){
        (WarehouseContext.instance()).setLoginUser(WarehouseContext.IS_MANAGER);    
        (WarehouseContext.instance()).changeState(2);
    }
    public void logout(){
        System.exit(0);
    }
//====================================================================
// Auxilary Methods
//====================================================================
    private void updateTextArea(final String text) {
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            textArea.append(text);
          }
        });
      }

    private void redirectSystemStreams() {
        OutputStream out = new OutputStream() {
          @Override
          public void write(int b) throws IOException {
            updateTextArea(String.valueOf((char) b));
          }

          @Override
          public void write(byte[] b, int off, int len) throws IOException {
            updateTextArea(new String(b, off, len));
          }

          @Override
          public void write(byte[] b) throws IOException {
            write(b, 0, b.length);
          }
        };

        System.setOut(new PrintStream(out, true));
        System.setErr(new PrintStream(out, true));
      }

	public void clear() { 
	    frame.getContentPane().removeAll();
	    frame.paint(frame.getGraphics());   
	}  
    
    public void run() {
        process();
    }
}