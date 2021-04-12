// Tyler
import java.util.*;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;

public class QueryClientState extends WarehouseState  implements ActionListener{
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));  
    private static QueryClientState instance;
    private final String CLIENTTrans_FORMAT = "%-4s | %-20s | %-20s";
    private final String CLIENT_FORMAT = "%-4s | %-20s | %-20s | %10s";

    
    //GUI
    private JFrame frame;
    private AbstractButton showAllClientsButton, listClientsOutstandingButton, ListCLientsNoTransactionsButton, Logout, back;
    static JLabel  QueryClientMenuLabel;
    static JTextField removeItemPIDField;
    private JTextArea textArea;
    
	private QueryClientState()
	{
        super();
	}

    public static QueryClientState instance() {
      if (instance == null) {
        instance = new QueryClientState();
      }
      return instance;
    }
//====================================================================
// Options
//====================================================================

public void process() {
	frame = WarehouseContext.instance().getFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JPanel cartPane = new JPanel();
    BoxLayout boxlayout = new BoxLayout(cartPane, BoxLayout.Y_AXIS);
    cartPane.setLayout(boxlayout);
    
    //button and listeners
    showAllClientsButton = new JButton("Show All Clients"); 
    listClientsOutstandingButton = new JButton("Clients with Outstanding Balance"); 
    ListCLientsNoTransactionsButton = new JButton("Clients with no Transactions"); 
    Logout  = new JButton("Logout"); 
    showAllClientsButton.addActionListener(this);
    listClientsOutstandingButton.addActionListener(this);
    ListCLientsNoTransactionsButton.addActionListener(this);
    Logout.addActionListener(this);
    
    //label
    QueryClientMenuLabel = new JLabel("Query Clients");
    QueryClientMenuLabel.setFont(new Font("Helvetica", Font.PLAIN, 20));
    
    //center align
    QueryClientMenuLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    showAllClientsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    listClientsOutstandingButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    ListCLientsNoTransactionsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    Logout.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    //adding to pane
    cartPane.add(this.QueryClientMenuLabel);
    cartPane.add(this.showAllClientsButton);
    cartPane.add(this.listClientsOutstandingButton);
    cartPane.add(this.ListCLientsNoTransactionsButton);
    cartPane.add(this.Logout);
    frame.getContentPane().add(cartPane);
    frame.setVisible(true);
    frame.paint(frame.getGraphics()); 
    frame.toFront();
    frame.requestFocus();
	
}


public void actionPerformed(ActionEvent event) {
	if (event.getSource().equals(this.showAllClientsButton)){
		this.showAllClients();
	}
	else if(event.getSource().equals(this.listClientsOutstandingButton)){
		this.showClientsWithOutstanding();
	}
	else if(event.getSource().equals(this.ListCLientsNoTransactionsButton)){
		this.showClientsNoTransactions();
	}
	else if(event.getSource().equals(this.back)){
		clear();
		this.process();
	}
	else if(event.getSource().equals(this.Logout)){
		clear();
		this.logout();
		
	}
}

//====================================================================
// QueryClientState Methods
//====================================================================
public void showAllClients() {
	clear();
	JPanel textbox = new JPanel();
	frame = WarehouseContext.instance().getFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    textArea = new JTextArea(15, 32);
    textbox.add(new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
            JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));
    back = new JButton("Back");
    back.addActionListener(this);
    textbox.add(this.back);
    frame.add(textbox);
    frame.setVisible(true); 
    frame.paint(frame.getGraphics());
    frame.toFront();
    frame.requestFocus();
    this.redirectSystemStreams();
	Warehouse warehouse = Warehouse.getInstance();
	Iterator<Client> clients = warehouse.getClients();
	System.out.println("CLIENTS IN SYSTEM");
	System.out.println(
		String.format(CLIENT_FORMAT,
			"ID", "Name", "Address", "Balance"
		)
	);
	while (clients.hasNext())
	{
		Client c = (Client) clients.next();
		System.out.println(
			String.format(CLIENT_FORMAT,
				c.getClientID(),
				c.getClientName(),
				c.getClientAddress(),
				String.format("$%.02f", c.getClientBalance())
			)
		);
		
	}
}

public void showClientsWithOutstanding() {
	clear();
	JPanel textbox = new JPanel();
	frame = WarehouseContext.instance().getFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    textArea = new JTextArea(15, 32);
    textbox.add(new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
            JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));
    back = new JButton("Back");
    back.addActionListener(this);
    textbox.add(this.back);
    textArea.setAlignmentX(Component.CENTER_ALIGNMENT);
    back.setAlignmentX(Component.CENTER_ALIGNMENT);
    frame.add(textbox);
    frame.setVisible(true); 
    frame.paint(frame.getGraphics());
    frame.toFront();
    frame.requestFocus();
    this.redirectSystemStreams();
	Warehouse warehouse = Warehouse.getInstance();
	Iterator<Client> clients = warehouse.getClients();
	
	System.out.println("Clients with Outstanding Balance");
	System.out.println("-----");
	System.out.println(
		String.format(CLIENT_FORMAT,
			"ID", "Name", "Address", "Balance"
		)
	);
	while (clients.hasNext())
	{
		Client client = (Client) clients.next();
		if (client.getClientBalance() > 0)
		{
			System.out.println(
				String.format(CLIENT_FORMAT,
					client.getClientID(),
					client.getClientName(),
					client.getClientAddress(),
					String.format("$%9.02f", client.getClientBalance())
				)
			);
		}
	}
}
   
public void showClientsNoTransactions() {
	clear();
	JPanel textbox = new JPanel();
	frame = WarehouseContext.instance().getFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    textArea = new JTextArea(15, 32);
    textbox.add(new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
            JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));
    back = new JButton("Back");
    back.addActionListener(this);
    textbox.add(this.back);
    textArea.setAlignmentX(Component.CENTER_ALIGNMENT);
    back.setAlignmentX(Component.CENTER_ALIGNMENT);
    frame.add(textbox);
    frame.setVisible(true); 
    frame.paint(frame.getGraphics());
    frame.toFront();
    frame.requestFocus();
    this.redirectSystemStreams();
	Warehouse warehouse = Warehouse.getInstance();
	Iterator<Client> clients = warehouse.getClients();
	System.out.println("Clients with No Transactions");
	System.out.println("-----");
	while (clients.hasNext())
	{
		Client client = (Client) clients.next();
		Iterator<Transaction> transactions = warehouse.getClientTransactions(client.getClientID());
		if (!transactions.hasNext())
		{
			System.out.println(
				String.format(CLIENTTrans_FORMAT,
					client.getClientID(),
					client.getClientName(),
					client.getClientAddress())
				);
		}
	}
}
   



public void logout(){
    int loginUser = WarehouseContext.instance().getLoginUser();
    if(loginUser == WarehouseContext.IS_CLERK || loginUser == WarehouseContext.IS_MANAGER)
        (WarehouseContext.instance()).changeState(1);
    else
        (WarehouseContext.instance()).changeState(3);
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