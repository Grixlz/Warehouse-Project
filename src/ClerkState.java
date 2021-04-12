import java.util.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
import java.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ClerkState extends WarehouseState implements ActionListener{
	private final String CLIENT_FORMAT = "%-4s | %-20s | %-20s | %10s";
	//id, name, stock, price
	private final String PRODUCT_FORMAT = "%-4s | %-20s | %-10s | %-10s";
	//productId, productName, purchasePrice
	private final String SUPPLIED_PRODUCT_FORMAT = "%-4s | %-20s | %-10s";
	//clientName, clientId, quantity
	private final String WAITLIST_ITEM_FORMAT = "%-20s (%4s) - x%-10s";
	
	private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); 
	public static ClerkState instance;
	
	private JFrame frame;
	private AbstractButton addClientButton, showProductsButton, querySystemButton, loginClientButton, showClientWaitButton, back, addStockFromSupplierButton, Logout, 
		addClientConfirmButton, pWaitIDButton, shipPIDButton, shipProdButton, CIDButton;
	static JLabel clerkMenuLabel, addClientLabel, addNameLabel, addAddressLabel, pWaitLabel, ShipProdLabel, shipPIDLabel, CIDLabel;
	static JTextField addNameFielf, addAddressField, pWaitIDField, shipeProdField, shipPIDField, CIDField;
	private JTextArea textArea;
	
	private ClerkState()
	{
		super();
	}
	
	public static ClerkState instance()
	{
		if (instance == null)
		{
			instance = new ClerkState();
		}
		return instance;
	}

//====================================================================
// GUI
//====================================================================	
	public void process() {

		frame = WarehouseContext.instance().getFrame();
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    JPanel clerkPane = new JPanel();
	    BoxLayout boxlayout = new BoxLayout(clerkPane, BoxLayout.Y_AXIS);
	    clerkPane.setLayout(boxlayout);
	    
	    //button and listeners
	    addClientButton = new JButton("Add Client"); 
	    showProductsButton = new JButton("Shows list of products in database"); 
	    querySystemButton = new JButton("Query system about clients"); 
	    loginClientButton = new JButton("Log in as Client");  
	    showClientWaitButton = new JButton("Shows all clients who have waitlisted a product");  
	    addStockFromSupplierButton = new JButton("Adds to a product's stock");  
	    Logout  = new JButton("Logout"); 
	    
	    addClientButton.addActionListener(this);
	    showProductsButton.addActionListener(this);
	    querySystemButton.addActionListener(this);
	    loginClientButton.addActionListener(this);
	    showClientWaitButton.addActionListener(this);
	    addStockFromSupplierButton.addActionListener(this);
	    Logout.addActionListener(this);
	    
	    //label
	    clerkMenuLabel = new JLabel("Clerk Menu");
	    clerkMenuLabel.setFont(new Font("Helvetica", Font.PLAIN, 20));
	    
	    //center align
	    clerkMenuLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	    addClientButton.setAlignmentX(Component.CENTER_ALIGNMENT);
	    showProductsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
	    querySystemButton.setAlignmentX(Component.CENTER_ALIGNMENT);
	    loginClientButton.setAlignmentX(Component.CENTER_ALIGNMENT);
	    showClientWaitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
	    addStockFromSupplierButton.setAlignmentX(Component.CENTER_ALIGNMENT);
	    Logout.setAlignmentX(Component.CENTER_ALIGNMENT);
	    
	    //adding to pane
	    clerkPane.add(this.clerkMenuLabel);
	    clerkPane.add(this.addClientButton);
	    clerkPane.add(this.showProductsButton);
	    clerkPane.add(this.querySystemButton);
	    clerkPane.add(this.loginClientButton);
	    clerkPane.add(this.showClientWaitButton);
	    clerkPane.add(this.addStockFromSupplierButton);
	    clerkPane.add(this.Logout);
	    frame.add(clerkPane);
	    frame.setVisible(true);
	    frame.paint(frame.getGraphics()); 
	    frame.toFront();
	    frame.requestFocus();
	}

	public void actionPerformed(ActionEvent event) {
		if (event.getSource().equals(this.addClientButton)){
			this.addClientMenu();
		}
		else if(event.getSource().equals(this.showProductsButton)){
			this.showProducts();
		}
		else if(event.getSource().equals(this.querySystemButton)){
			this.QuerySystemAboutClients();
			
		}
		else if(event.getSource().equals(this.loginClientButton)){
			this.getCIDTOBe();
		}
		else if(event.getSource().equals(this.CIDButton)){
			String CID = CIDField.getText(); 
			clear();
			this.becomeClient(CID);
		}
		else if(event.getSource().equals(this.showClientWaitButton)){//aldo idfk what this does anymore
			this.showWaitlistFromProductMenu();
		}
		else if(event.getSource().equals(this.pWaitIDButton)){
			String PID = pWaitIDField.getText(); 
			clear();
			this.showWaitlistFromProduct(PID);
		}
		else if(event.getSource().equals(this.addStockFromSupplierButton)){//-----
			this.shipProductFromSupplierMenu();
		}
		else if(event.getSource().equals(this.shipProdButton)){
			String id = shipeProdField.getText();
			clear();
			this.shipProductFromSupplier(id);
		}
		else if(event.getSource().equals(this.shipPIDButton)){
			String PID = shipPIDField.getText();
			clear();
			this.shipProd(PID);
		}
		else if(event.getSource().equals(this.addClientConfirmButton)){
			String name = addNameFielf.getText();
			String address = addAddressField.getText(); 
			clear();
			this.addClient(name, address);
			this.process();
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
// Clerk Methods
//====================================================================
	private void addClientMenu() {
		clear();
    	frame = WarehouseContext.instance().getFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel updateCartPanel = new JPanel();
        BoxLayout boxlayout = new BoxLayout(updateCartPanel, BoxLayout.Y_AXIS);
        updateCartPanel.setLayout(boxlayout);
        addClientLabel = new JLabel("Add Client");
        addNameLabel = new JLabel("Client Name:");
        addAddressLabel = new JLabel("Client Address");
        back = new JButton("Back");
        addClientConfirmButton = new JButton("Add Client");
        addNameFielf = new JTextField(10);
        addAddressField = new JTextField(10);
        addClientConfirmButton.addActionListener(this);
        back.addActionListener(this);
	    frame.getContentPane().setLayout(new FlowLayout());
	    frame.add(Box.createHorizontalStrut(6));
	    frame.add(this.addNameLabel);
	    frame.add(Box.createHorizontalStrut(5));
	    frame.add(this.addNameFielf);
	    frame.add(Box.createHorizontalStrut(90));
	    frame.add(this.addAddressLabel);
	    frame.add(this.addAddressField);
	    frame.add(Box.createHorizontalStrut(85));
	    frame.add(this.addClientConfirmButton);
	    frame.add(this.back);
	    frame.setVisible(true);
	    frame.paint(frame.getGraphics());
	    frame.toFront();
	    frame.requestFocus();
	    
	    
	}
	
	private void addClient(String name, String address)
	{
		Client client;
		client = Warehouse.getInstance().addClient(name, address);
	}
	
	private void showProducts() 
	{
		clear();
    	JPanel textbox = new JPanel();
    	JPanel butttonBox = new JPanel();
    	frame = WarehouseContext.instance().getFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        textArea = new JTextArea(15, 30);
        textbox.add(new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));
        back = new JButton("Back");
        back.addActionListener(this);
        textArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        back.setAlignmentX(Component.CENTER_ALIGNMENT);
	    frame.add(Box.createHorizontalStrut(15));
        textbox.add(back);
        frame.add(textbox);
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
	
	private void QuerySystemAboutClients() {
		clear();
        (WarehouseContext.instance()).changeState(5);
	}
	
	private void showWaitlistFromProductMenu() {
    	clear();
	 	frame = WarehouseContext.instance().getFrame();
	 	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    pWaitLabel = new JLabel("Enter Product ID:");
	    pWaitIDField = new JTextField(10);
	    pWaitIDButton = new JButton("Enter");
	    back = new JButton("Back");
	    pWaitIDButton.addActionListener(this);
	    back.addActionListener(this);
	    frame.getContentPane().setLayout(new FlowLayout());
	    pWaitLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	    frame.add(pWaitLabel);
	    frame.add(pWaitIDField);
	    frame.add(Box.createHorizontalStrut(110));
	    frame.add(pWaitIDButton);
	    frame.add(back);
	    frame.setVisible(true);
	    frame.paint(frame.getGraphics());
	    frame.toFront();
	    frame.requestFocus();
	}
	private void showWaitlistFromProduct(String productId)
	{
		clear();
    	JPanel textbox = new JPanel();
    	JPanel backbutton = new JPanel();
    	frame = WarehouseContext.instance().getFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        textArea = new JTextArea(15, 32);
        textbox.add(new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));
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
		Product product = Warehouse.getInstance().getProduct(productId);
		
		if (product != null)
		{
			Iterator<WaitlistItem> waitlistItems = Warehouse.getInstance().getWaitlistedItemsFromProduct(productId);
			
			System.out.println("Waitlisted Orders for " + product.getName() + "(" + productId + ")");
			
			while (waitlistItems.hasNext())
			{
				WaitlistItem wItem = (WaitlistItem) waitlistItems.next();
				String clientId = wItem.getClientId();
				int itemQuantity = wItem.getQuantity();
				Client client = Warehouse.getInstance().getClient(clientId);
				
				System.out.println(String.format(WAITLIST_ITEM_FORMAT,
						client.getClientName(),
						clientId,
						itemQuantity
					)
				);
			}
		}
		else
		{
			System.out.println("Error! Unable to find product with id " + productId);
		}
	}
	
	private void shipProductFromSupplierMenu() {
		clear();
    	frame = WarehouseContext.instance().getFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel shipProdPanel = new JPanel();
		ShipProdLabel = new JLabel("Supplier ID:");
		shipeProdField = new JTextField(10);
		shipProdButton = new JButton("Enter");
		back = new JButton("Back");
		shipProdButton.addActionListener(this);
		back.addActionListener(this);
		shipProdPanel.add(ShipProdLabel);
		shipProdPanel.add(shipeProdField);
		shipProdPanel.add(shipProdButton);
		shipProdPanel.add(back);
		frame.add(shipProdPanel);
		frame.setVisible(true);
	    frame.paint(frame.getGraphics());
	    frame.toFront();
	    frame.requestFocus();
	}
	private void shipProductFromSupplier(String supplierId)
	{
    	JPanel textbox = new JPanel();
    	JPanel backbutton = new JPanel();
    	frame = WarehouseContext.instance().getFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        textArea = new JTextArea(15, 32);
        textbox.add(new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));
        
        shipPIDButton = new JButton("enter");
        shipPIDField = new JTextField(10);
        shipPIDLabel = new JLabel("Product ID:");
         
        shipPIDButton.addActionListener(this);
        backbutton.add(shipPIDLabel);
        backbutton.add(shipPIDField);
        backbutton.add(shipPIDButton);
        frame.add(textbox);
        frame.add(backbutton);
        frame.setVisible(true); 
        frame.paint(frame.getGraphics());
	    frame.toFront();
	    frame.requestFocus();
	    this.redirectSystemStreams();
		Supplier supplier = Warehouse.getInstance().getSupplier(supplierId);
		
		if (supplier != null)
		{
			String supplierName = supplier.getSupplierName();
			System.out.println("Selected Supplier " + supplierName + " can ship the following:");
			
			Iterator<SuppliedProduct> suppliedProducts = Warehouse.getInstance().getSuppliedProductsFromSupplier(supplierId);
			System.out.println(String.format(SUPPLIED_PRODUCT_FORMAT,
				"ID",
				"Name",
				"Price"
				)
			);
			
			List<String> productOptions = new LinkedList<String>();
			while (suppliedProducts.hasNext())
			{
				SuppliedProduct sp = (SuppliedProduct) suppliedProducts.next();
				int index = productOptions.size() + 1;
				System.out.println(index + ".\t" + String.format(SUPPLIED_PRODUCT_FORMAT,
					supplierId, 
					supplierName,
					sp.getPurchasePrice())
				);
				
				productOptions.add(sp.getProductId());
			}
		}
	}
	public void shipProd(String productId){	
	    JPanel textbox = new JPanel();
	    frame = WarehouseContext.instance().getFrame();
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    textArea = new JTextArea(15, 32);
	    textbox.add(new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
	        JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));
	    back = new JButton("Back");
	    back.addActionListener(this);
	    textbox.add(back);
	    frame.add(textbox);
	    frame.setVisible(true); 
	    frame.paint(frame.getGraphics());
		frame.toFront();
		frame.requestFocus();
		this.redirectSystemStreams();
		Product product = Warehouse.getInstance().getProduct(productId);
		int totalStock = 0, stock= 0;
		if (product != null)
		{
			stock = product.getCurrentStock();
			String quantityStr = "10";
			int quantity = Integer.parseInt(quantityStr);
			totalStock = quantity + stock;
			Iterator<WaitlistItem> waitlistItems = Warehouse.getInstance().getWaitlistedItemsFromProduct(productId);
			System.out.println("Processing waitlist items!");
			int itemNum = 1;
			while (waitlistItems.hasNext())
			{
				WaitlistItem wItem = (WaitlistItem) waitlistItems.next();
				String clientId = wItem.getClientId();
				Client client = Warehouse.getInstance().getClient(clientId);
				int itemQuantity = wItem.getQuantity();
				if (itemQuantity <= totalStock)
				{
					System.out.println("Waitlist Item " + itemNum + ": " + String.format(WAITLIST_ITEM_FORMAT,
								client.getClientName(),
								clientId,
								itemQuantity
							)
					);
						
					//need menu
					boolean results = Warehouse.getInstance().processWaitlistItem(clientId, productId, itemQuantity);
					if (results == true)
					{
						System.out.println("Waitlist Item was successfully processed!");
						totalStock -= itemQuantity;
					}
				}
			}
		} //end looping through waitlist items
		int currentStock = Warehouse.getInstance().addProductStock(productId, totalStock - stock);
		if (currentStock < 0)
		{
			System.out.println("An error has occured! Product stock has a value of " + currentStock);
		}
		else
		{
			System.out.println("Received shipment from supplier! Product " + product.getName() + " now has a stock of " + currentStock);
		}
	}
		
	public void getCIDTOBe() {
		clear();
	 	frame = WarehouseContext.instance().getFrame();
	 	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    CIDLabel = new JLabel("Enter Client ID:");
	    CIDField = new JTextField(10);
	    CIDButton = new JButton("Enter");
	    back = new JButton("Back");
	    CIDButton.addActionListener(this);
	    back.addActionListener(this);
	    frame.getContentPane().setLayout(new FlowLayout());
	    frame.add(CIDLabel);
	    frame.add(CIDField);
	    frame.add(CIDButton);
	    frame.add(back);
	    frame.setVisible(true);
	    frame.paint(frame.getGraphics());
	    frame.toFront();
	    frame.requestFocus();
	}
	public void becomeClient(String clientId)
	{
		 if (Warehouse.getInstance().getClient(clientId) != null)
        {
            (WarehouseContext.instance()).setCurrentUser(clientId);      
            (WarehouseContext.instance()).changeState(0); //switch to client state
        }
	}
	
	public void logout()
	{
		int wLogin = WarehouseContext.instance().getLoginUser();
		if (wLogin == WarehouseContext.IS_MANAGER)
		{
			WarehouseContext.instance().changeState(2); //switch to manager state
		}
		else if (wLogin == WarehouseContext.IS_CLERK)
		{
			WarehouseContext.instance().changeState(3); //switch to login state
		}
		else
		{
			//note this would occur as an error if client somehow became a clerk
			WarehouseContext.instance().changeState(1); //switch to error state
		}
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
