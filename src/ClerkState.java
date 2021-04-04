//Clare

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

public class ClerkState extends WarehouseState {
	private final String CLIENT_FORMAT = "%-4s | %-20s | %-20s | %10s";
	//id, name, stock, price
	private final String PRODUCT_FORMAT = "%-4s | %-20s | %-10s | %-10s";
	//productId, productName, purchasePrice
	private final String SUPPLIED_PRODUCT_FORMAT = "%-4s | %-20s | %-10s";
	//clientName, clientId, quantity
	private final String WAITLIST_ITEM_FORMAT = "%-20s (%4s) - x%-10s";
	
	private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));  
	private static Warehouse warehouse;
	public static ClerkState instance;
	
	private enum Option
	{
		ADD_CLIENT("Adds client to system"),
		SHOW_PRODUCTS("Shows list of products in database"),
		QUERY_SYSTEM_ABOUT_CLIENTS("Query system about clients"),
		BECOME_CLIENT("Log in as Client"),
		SHOW_WAIT_LIST_PRODUCTS("Shows all clients who have waitlisted a product"),
		SHIP_PRODUCT("Adds to a product's stock by receiving a shipment from a given supplier"),
		HELP("Displays the help menu"),
		LOGOUT("Logout");
		
		private String description;
		private static int LENGTH = Option.values().length;
		
		private Option(String str)
		{
			description = str;
		}
		
		public String getDescription()
		{
			return description;
		}
	}
	
	private ClerkState()
	{
		warehouse = Warehouse.getInstance(); //get the facade
	}
	
	public static ClerkState instance()
	{
		if (instance == null)
		{
			instance = new ClerkState();
		}
		return instance;
	}
	
	public void process() {
		Option command; 
		do {
			displayHelp(); 
			command = getCommand();
			switch (command) {
				case ADD_CLIENT:
					addClient();
					break;
				case SHOW_PRODUCTS:
					showProducts();
					break;
				case QUERY_SYSTEM_ABOUT_CLIENTS:
					QuerySystemAboutClients();
					break;
				case BECOME_CLIENT:
					becomeClient();
					break;
				case SHOW_WAIT_LIST_PRODUCTS:
					showWaitlistFromProduct();
					break;
				case SHIP_PRODUCT:
					shipProductFromSupplier();
					break;
				case HELP:
					displayHelp();
					break;
				case LOGOUT:
					break;
				default:
					System.out.println("Invalid choice");    
					break;
			}  
		} while (command != Option.LOGOUT);
		
		(WarehouseContext.instance()).changeState(3);
	}
	
	private String getToken(String prompt)
	{
	    do
	    {
	        try
	        {
	            System.out.println(prompt);
	            String line = reader.readLine();
	            StringTokenizer tokenizer = new StringTokenizer(line, "\n\r\f");
	            if (tokenizer.hasMoreTokens())
	            {
	                return tokenizer.nextToken();
	            }
	        }
	        catch (IOException ioe)
	        {
	            System.exit(0);
	        }
	    } while (true);
	}

	private Option getCommand()
	{
	    do
	    {
	        try
	        {
	            String token = getToken("Enter a command. Use " + Option.HELP.ordinal() + " to display the menu.");
	            int value = Integer.parseInt(token);
	            if (value >= 0 && value <= Option.LENGTH)
	            {
	                return Option.values()[value];
	            }
	            else
	            {
	                System.out.println("Input command out of range!");
	            }
	        }
	        catch (NumberFormatException nfe)
	        {
	            System.out.println("Invalid input - Please enter a valid number!");
	        }
	    } while (true);
	}
	
	private void displayHelp()
	{
	    System.out.println("Enter a number associated with a command seen below");
	    System.out.println("---------------------------------------------------");
	    Option options[] = Option.values();
	    
	    for (Option opt : options)
	    {
	        System.out.println(opt.ordinal() + " - " + opt.getDescription());
	    }
	    System.out.println("---------------------------------------------------");
	}
	
	private void addClient()
	{
		String name = getToken("Enter name of new client.");
		String address = getToken("Enter address of new client.");
		
		Client client;
		client = warehouse.addClient(name, address);
		if (client == null)
		{
			System.out.println("Error! Failed to add client to warehouse!");
		}
		System.out.println(client);
	}
	
	private void showProducts()
	{
		Iterator<Product> products = warehouse.getProducts();
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
        (WarehouseContext.instance()).changeState(5);
	}
	
	private void showWaitlistFromProduct()
	{
		String productId = getToken("What product do you wish to see the waitlist for?");
		Product product = warehouse.getProduct(productId);
		
		if (product != null)
		{
			Iterator<WaitlistItem> waitlistItems = warehouse.getWaitlistedItemsFromProduct(productId);
			
			System.out.println("Waitlisted Orders for " + product.getName() + "(" + productId + ")");
			
			while (waitlistItems.hasNext())
			{
				WaitlistItem wItem = (WaitlistItem) waitlistItems.next();
				String clientId = wItem.getClientId();
				int itemQuantity = wItem.getQuantity();
				Client client = warehouse.getClient(clientId);
				
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
	
	private void shipProductFromSupplier()
	{
		String supplierId = getToken("Enter the ID of supplier to ship products.");
		
		Supplier supplier = warehouse.getSupplier(supplierId);
		
		if (supplier != null)
		{
			String supplierName = supplier.getSupplierName();
			System.out.println("Selected Supplier " + supplierName + " can ship the following:");
			
			Iterator<SuppliedProduct> suppliedProducts = warehouse.getSuppliedProductsFromSupplier(supplierId);
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
			int numOptions = productOptions.size() + 1;
			
			int choice = -1;
			String productChoice = getToken("Enter the product you wish to be shipped (using line number, not ID)");
			choice = Integer.parseInt(productChoice);
			if (choice < 1 || choice > numOptions)
			{
				System.out.println("ERROR! Please enter an option between 1 and " + numOptions);
			}
			
			String productId = productOptions.get(choice);
			
			Product product = warehouse.getProduct(productId);
			
			if (product != null)
			{
				int stock = product.getCurrentStock();
				
				String quantityStr = getToken("Enter the quantity of the product to ship to warehouse.");
				int quantity = Integer.parseInt(quantityStr);
				int totalStock = quantity + stock;
				
				Iterator<WaitlistItem> waitlistItems = warehouse.getWaitlistedItemsFromProduct(productId);
				
				System.out.println("Processing waitlist items!");
				int itemNum = 1;
				while (waitlistItems.hasNext())
				{
					WaitlistItem wItem = (WaitlistItem) waitlistItems.next();
					String clientId = wItem.getClientId();
					Client client = warehouse.getClient(clientId);
					int itemQuantity = wItem.getQuantity();
					
					if (itemQuantity <= totalStock)
					{
						System.out.println("Waitlist Item " + itemNum + ": " + String.format(WAITLIST_ITEM_FORMAT,
									client.getClientName(),
									clientId,
									itemQuantity
								)
						);
						
						String response = getToken("Process above waitlist item (Y/N)?");
						
						while (response.toLowerCase() == "y" || response.toLowerCase() == "n")
						{
							System.out.println("Input error! Please input either Y for yes, or N for no.");
							response = getToken("Process above waitlist item (Y/N)?");
						}
						
						if (response.toLowerCase() == "y")
						{
							boolean results = warehouse.processWaitlistItem(clientId, productId, itemQuantity);
							
							if (results == true)
							{
								System.out.println("Waitlist Item was successfully processed!");
								totalStock -= itemQuantity;
							}
							else
							{
								System.out.println("Failed to process waitlist item!");
							}
						}
					}
				} //end looping through waitlist items
				
				int currentStock = warehouse.addProductStock(productId, totalStock - stock);
				
				if (currentStock < 0)
				{
					System.out.println("An error has occured! Product stock has a value of " + currentStock);
				}
				else
				{
					System.out.println("Received shipment from supplier! Product " + product.getName() + " now has a stock of " + currentStock);
				}
			}
		}
		
	}
	
	public void becomeClient()
	{
		String clientId = getToken("Enter your clientID: ");
		
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
	
	
	
	public void run() {
        process();
    }
}
