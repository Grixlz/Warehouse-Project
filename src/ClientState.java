import java.util.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
import java.text.*;
import java.awt.*;
import java.awt.event.*;


public class ClientState extends WarehouseState  implements ActionListener{
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));  
    private static ClientState instance;
    private final String LINE_ITEM_FORMAT = "%-4s   %-4s | %-20s | %-10s | %-10s | %-10s";
	//id, name, stock, price
	private final String PRODUCT_FORMAT = "%-4s | %-20s | %-10s | %-10s";
    //date, transactionTitle, transactionDescription, money amount
    private final String TRANSACTION_FORMAT = "%-21s | %-20s | %-60s | %-10s";
    //clientName, clientId, quantity
	private final String WAITLIST_ITEM_FORMAT = "%-20s (%4s) - x%-10s";
	
	//GUI 
    private JFrame frame;
    private AbstractButton clientDetails, showProducts, showClientTrans, ModCart, displayWait, Logout, back;
    static JFrame clientMenu;
    static JLabel clientMenuLabel;
    static JTextField clientIdField;
    private JTextArea textArea;
    
    
	private ClientState()
	{
        super();
	}

    public static ClientState instance() {
      if (instance == null) {
        instance = new ClientState();
      }
      return instance;
    }
// GUI
//====================================================================

public void process() {//gui stuff
	frame = WarehouseContext.instance().getFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JPanel ClientMenuPane = new JPanel();
    BoxLayout boxlayout = new BoxLayout(ClientMenuPane, BoxLayout.Y_AXIS);
    ClientMenuPane.setLayout(boxlayout);
    
    //button and listeners
    clientDetails = new JButton("Show client details"); 
    showProducts = new JButton("Show Products"); 
    showClientTrans = new JButton("Show client transactions"); 
    ModCart = new JButton("Modify Client Cart"); 
    displayWait = new JButton("Display waitlist"); 
    Logout  = new JButton("Logout"); 
    clientDetails.addActionListener(this);
    showProducts.addActionListener(this);
    showClientTrans.addActionListener(this);
    ModCart.addActionListener(this);
    displayWait.addActionListener(this);
    Logout.addActionListener(this);
    
    //label
    clientMenuLabel = new JLabel("Client Menu");
    clientMenuLabel.setFont(new Font("Helvetica", Font.PLAIN, 20));
    
    //center align
    clientMenuLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    clientDetails.setAlignmentX(Component.CENTER_ALIGNMENT);
    showProducts.setAlignmentX(Component.CENTER_ALIGNMENT);
    showClientTrans.setAlignmentX(Component.CENTER_ALIGNMENT);
    ModCart.setAlignmentX(Component.CENTER_ALIGNMENT);
    displayWait.setAlignmentX(Component.CENTER_ALIGNMENT);
    Logout.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    //adding to pane
    ClientMenuPane.add(clientMenuLabel);
    ClientMenuPane.add(this.clientDetails);
    ClientMenuPane.add(this.showProducts);
    ClientMenuPane.add(this.showClientTrans);
    ClientMenuPane.add(this.ModCart);
    ClientMenuPane.add(this.displayWait);
    ClientMenuPane.add(this.Logout);
    frame.getContentPane().add(ClientMenuPane);
    frame.setVisible(true);
    frame.paint(frame.getGraphics()); 
    frame.toFront();
    frame.requestFocus();
  }

public void actionPerformed(ActionEvent event) {
	if (event.getSource().equals(this.clientDetails)){
		this.showClientDetails();
	}
	else if(event.getSource().equals(this.showProducts)){
		this.showProducts();
	}
	else if(event.getSource().equals(this.showClientTrans)){
		this.showClientTrans();
	}
	else if(event.getSource().equals(this.ModCart)){
		this.modifyClientCart();
	}
	else if(event.getSource().equals(this.displayWait)){
		this.displayWaitlist();
	}
	else if(event.getSource().equals(this.back)){
		clear();
		this.process();
	}
	else {
		clear();
		this.logout();
		
	}
}



//====================================================================
// Client Methods
//====================================================================
    private void showClientDetails(){
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
        String ClientID = WarehouseContext.instance().getCurrentUser();
        System.out.println(Warehouse.getInstance().getClient(ClientID));
    }


    private void showProducts(){
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
        Iterator<Product> products = Warehouse.getInstance().getProducts();
		System.out.println("PRODUCTS IN SYSTEM");
		System.out.println(
			String.format(PRODUCT_FORMAT,
				"ID", "Name", "Stock", "Sell Price"
			)
		);
		while (products.hasNext())
		{
			Product p = (Product) products.next();
			System.out.println(
				String.format(PRODUCT_FORMAT,
					p.getProductID(),
					p.getName(),
					String.format("x%d", p.getCurrentStock()),
					String.format("$%-9.02f", p.getBuyPrice())
				)
			);
		}
    }

    private void showClientTrans(){ 
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
        String clientId = WarehouseContext.instance().getCurrentUser();
        Warehouse warehouse = Warehouse.getInstance();
		
		Client client = warehouse.getClient(clientId);
		
		if (client != null)
		{
			Iterator<Transaction> transactions = warehouse.getClientTransactions(clientId);
			System.out.println("Transactions for " + client.getClientName() + " (" + clientId + ")");
			System.out.println("-----");
			System.out.println(String.format(
					TRANSACTION_FORMAT,
					"DATE",
					"TYPE",
					"DESCRIPTION",
					"AMOUNT"
				)
			);
			while (transactions.hasNext())
			{
				Transaction trans = (Transaction) transactions.next();
				Date date = trans.getDate();
				String title = trans.getTitle();
				String desc = trans.getDescription();
				double money = trans.getMoney();
				
				String formattedDate = new SimpleDateFormat("MM/dd/yyyy, hh:mm aaa").format(date);
				
				System.out.println(String.format(
						TRANSACTION_FORMAT,
						formattedDate,
						title,
						desc,
						String.format("$%-9.02f", money)
					)
				);
			}
			
		}
		else
		{
			System.out.println("Error! Client with id " + clientId + " was not found!");
		}
    }

    private void displayWaitlist(){
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
        Warehouse warehouse = Warehouse.getInstance();
        String clientId = WarehouseContext.instance().getCurrentUser();
		Client client = warehouse.getClient(clientId);
		
		if (client != null)
		{
			Iterator<WaitlistItem> waitlistItems = warehouse.getWaitlistedItemsFromClient(clientId);
			
			System.out.println("Waitlisted Orders for " + client.getClientName() + "(" + clientId + "):");
			if(waitlistItems.hasNext() == false)            // Tell user if it's empty
                System.out.println("Wishlist is empty");
            else                                            // Otherwise display contents
			while (waitlistItems.hasNext())
			{
				WaitlistItem wItem = (WaitlistItem) waitlistItems.next();
				String productId = wItem.getProductId();
				int itemQuantity = wItem.getQuantity();
				Product product = warehouse.getProduct(productId);
				
				System.out.println(String.format(WAITLIST_ITEM_FORMAT,
						product.getName(),
						productId,
						itemQuantity
					)
				);
			}
		}
		else
		{
			System.out.println("Error! Unable to find client with id " + clientId);
		}
    }
    

    public void modifyClientCart() {
    	clear();
        (WarehouseContext.instance()).changeState(4);
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