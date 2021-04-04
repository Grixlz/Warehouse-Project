//Clare Miller

import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

public class UserInterface
{
	//NOTE/WARNING - Should these be changed to the toString() method for each respective class?
	//Pros: UI doesn't need to know methods of C/P/S/LI
	//Cons: UI is in charge of formatting, so it should be the one to handle it
	
	//id, name, address, balance
	private final String CLIENT_FORMAT = "%-4s | %-20s | %-20s | %10s";
	//id, name, stock, price
	private final String PRODUCT_FORMAT = "%-4s | %-20s | %-10s | %-10s";
	//id, name, address
	private final String SUPPLIER_FORMAT = "%-4s | %-20s | %-20s";
	//?, productId, ? / ? / ? / ?
	private final String LINE_ITEM_FORMAT = "%-4s   %-4s | %-20s | %-10s | %-10s | %-10s";
	//productId, productName, purchasePrice
	private final String SUPPLIED_PRODUCT_FORMAT = "%-4s | %-20s | %-10s";
	//clientName, clientId, quantity
	private final String WAITLIST_ITEM_FORMAT = "%-20s (%4s) - x%-10s";
	//date, transactionTitle, transactionDescription, money amount
	private final String TRANSACTION_FORMAT = "%-21s | %-20s | %-60s | %-10s";
	//date, productName, productId, quantity, totalCost
	private final String INVOICE_FORMAT = "%-21s | %-20s (%-4s) | %-10s | %-10s";
	
	private static UserInterface userInterface;
	private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	private static Warehouse warehouse;
	
	private enum Option
	{
		
		ADD_CLIENT("Adds client to system"),
		ADD_PRODUCT("Adds product to system"),
		ADD_SUPPLIER("Adds supplier to system"),
		
		CHANGE_CLIENT_NAME("Changes the name of a client in the system"),
		CHANGE_CLIENT_ADDRESS("Changes the address of a client in the system"),
		CHANGE_PRODUCT_NAME("Changes the name of a product in the system"),
		CHANGE_PRODUCT_PRICE("Changes the price of a product in the system"),
		CHANGE_SUPPLIER_NAME("Changes the name of a supplier in the system"),
		CHANGE_SUPPLIER_ADDRESS("Changes the address of a supplier in the system"),
		
		ADD_TO_CART("Adds product w/ quantity to cart"),
		REMOVE_FROM_CART("Removes an item from the cart"),
		UPDATE_PRODUCT_IN_CART("Updates a line item in the cart"),
		
		PROCESS_ORDER("Processes a clients current order"),
		ADD_SUPPLIED_PRODUCT("Connects a product and a supplier via a Supplied-Product"),
		SHIP_PRODUCT("Adds to a product's stock by receiving a shipment from a given supplier"),
		
		SHOW_CLIENT_TRANSACTIONS("Shows a list of transactions for a client"),
		SHOW_CLIENT_INVOICES("Shows a list of invoices for a client"),
		GET_PRODUCT_INFO("Get info about a product"),
		GET_SUPPLIER_INFO("Get info about a supplier"),
		SHOW_OUTSTANDING_CLIENTS("Shows a list of all clients in system with a balance above 0"),
		SHOW_WAIT_LIST_PRODUCTS("Shows all clients who have waitlisted a product"),
		SHOW_WAIT_LIST_CLIENTS("Shows all waitlisted products under a client"),
		
		SHOW_CLIENT_CART("Shows list of line items in a client's shopping cart"),
		SHOW_CLIENTS("Shows list of clients in database"),
		SHOW_PRODUCTS("Shows list of products in database"),
		SHOW_SUPPLIERS("Shows list of suppliers in database"),
		
		SAVE("Saves data to file"),
		RETRIEVE("Loads data from file"),
		HELP("Displays the help menu"),
		EXIT("Exits the program");
		
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
	
	private UserInterface()
	{
		boolean option = promptYesNo("Do you wish to load user data?");
		if (option)
		{
			retrieve();
		}
		else
		{
			warehouse = Warehouse.getInstance();
		}
	}
	
	public static UserInterface instance()
	{
		if (userInterface == null)
		{
			userInterface = new UserInterface();
			return userInterface;
		}
		else
		{
			return userInterface;
		}
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
	
	private boolean promptYesNo(String prompt)
	{
		prompt += " (Y/y) for YES, anything else for NO.";
		char result = getToken(prompt).charAt(0);
		if (result == 'Y' || result == 'y')
		{
			return true;
		}
		return false;
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
	
	private void addProduct()
	{
		String name = getToken("Enter name of new product.");
		String priceStr = getToken("Enter price the new product is sold for.");
		String quantityStr = getToken("Enter current stock of new product, if any.");
		
		double price = Double.parseDouble(priceStr);
		int quantity = Integer.parseInt(quantityStr);
		
		Product product;
		product = warehouse.addProduct(name, price, quantity);
		if (product == null)
		{
			System.out.println("Error! Failed to add product to warehouse!");
		}
		System.out.println(product);
	}
	
	private void addSupplier()
	{
		String name = getToken("Enter name of new supplier.");
		String address = getToken("Enter address of new supplier.");
		
		Supplier supplier;
		supplier = warehouse.addSupplier(name, address);
		if (supplier == null)
		{
			System.out.println("Error! Failed to add supplier to warehouse!");
		}
		System.out.println(supplier);
	}
	
	private void changeClientName()
	{
		String id = getToken("Enter the ID of the client whose name you wish to modify.");
		
		Client client = warehouse.getClient(id); 
		
		if (client != null)
		{
			System.out.println("Selected Client: " + client.getClientName());
			String name = getToken("Enter the new name for the client.");
			
			warehouse.changeClientName(id, name);
		}
		else
		{
			System.out.println("Error! Couldn't find client with ID " + id);
		}
		
	}
	
	private void changeClientAddress()
	{
		String id = getToken("Enter the ID of the client whose address you wish to modify.");
		
		Client client = warehouse.getClient(id); 
		
		if (client != null)
		{
			System.out.println("Selected Client: " + client.getClientName());
			String address = getToken("Enter the new address for the client.");
		
			warehouse.changeClientAddress(id, address);
		}
		else
		{
			System.out.println("Error! Couldn't find client with ID " + id);
		}
	}
	
	private void changeProductName()
	{
		String id = getToken("Enter the ID of the product whose name you wish to modify.");
		
		Product product = warehouse.getProduct(id); 
		
		if (product != null)
		{
			System.out.println("Selected Product: " + product.getName());
			String name = getToken("Enter the new name for the product.");
			
			warehouse.changeProductName(id, name);
		}
		else
		{
			System.out.println("Error! Couldn't find product with ID " + id);
		}
	}
	
	private void changeProductPrice()
	{
		String id = getToken("Enter the ID of the product whose price you wish to modify.");
		
		Product product = warehouse.getProduct(id); 
		
		if (product != null)
		{
			System.out.println("Selected Product: " + product.getName());
			String priceStr = getToken("Enter the new price for the product.");
			double price = Double.parseDouble(priceStr);
		
			warehouse.changeProductPrice(id, price);
		}
		else
		{
			System.out.println("Error! Couldn't find product with ID " + id);
		}
	}
	
	private void changeSupplierName()
	{
		String id = getToken("Enter the ID of the supplier whose address you wish to modify.");
		
		Supplier supplier = warehouse.getSupplier(id); 
		
		if (supplier != null)
		{
			System.out.println("Selected Supplier: " + supplier.getSupplierName());
			String name = getToken("Enter the new name for the supplier.");
			
			warehouse.changeSupplierName(id, name);
		}
		else
		{
			System.out.println("Error! Could not find supplier with ID " + id);
		}
	}
	
	private void changeSupplierAddress()
	{
		String id = getToken("Enter the ID of the supplier whose address you wish to modify.");
		
		Supplier supplier = warehouse.getSupplier(id); 
		
		if (supplier != null)
		{
			System.out.println("Selected Supplier: " + supplier.getSupplierName());
			String address = getToken("Enter the new address for the supplier.");
			
			warehouse.changeSupplierAddress(id, address);
		}
		else
		{
			System.out.println("Error! Could not find supplier with ID " + id);
		}
	}
	
	private void processOrder()
	{
		String clientId = getToken("Enter the client ID to process their order.");
		
		boolean result = warehouse.processOrder(clientId);
		if (result == true)
		{
			System.out.println("Successfully processed order!");
		}
		else
		{
			System.out.println("Failed to process order!");
		}
	}
	
	private void addSuppliedProduct()
	{
		String supplierId = getToken("Enter the supplier ID for the new Supplied-Product pair.");
		
		//NOTE: Validate supplierId?
		Supplier supplier = warehouse.getSupplier(supplierId);
		if (supplier != null)
		{
			String supplierName = supplier.getSupplierName();
			System.out.println("Selected supplier: " + supplierName + "(" + supplier.getSupplierID() + ")");
			
			String productId = getToken("Enter the product ID to be supplied by " + supplierName);
			//NOTE: Validate productId?
			Product product = warehouse.getProduct(productId);
			if (product != null)
			{
				String productName = product.getName();
				String priceStr = getToken("Enter the purchase price for product " + productName + " supplied by " + supplierName);
				double price = Double.parseDouble(priceStr);
				
				boolean results = warehouse.addSuppliedProduct(supplierId, productId, price);
				if (results == true)
				{
					System.out.println("Successfully added a SuppliedProduct relationship between " + productId + " and " + supplierId);
				}
				else
				{
					System.out.println("Failed to add a SuppliedProduct relationship between " + productId + " and " + supplierId);
				}
			}
			
		}
		else
		{
			//print warning message
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
				//SuppliedProduct sp = (SuppliedProduct) suppliedProducts.next();
				int index = productOptions.size() + 1;
				System.out.println(index + ".\t" + String.format(SUPPLIED_PRODUCT_FORMAT,
					supplierId, 
					supplierName//,
					//sp.getPurchasePrice();
					)
				);
				
				//productOptions.add(sp.getProductId());
			}
			int numOptions = productOptions.size() + 1;
			
			int choice = -1;
			while (choice >= 1 && choice <= numOptions)
			{
				String productChoice = getToken("Enter the product you wish to be shipped (using line number, not ID)");
				choice = Integer.parseInt(productChoice);
				
				if (choice < 1 || choice > numOptions)
				{
					System.out.println("ERROR! Please enter an option between 1 and " + numOptions);
				}
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
	
	
	
	private void addToCart()
	{
		String clientId = getToken("Enter the client ID for adding an item to the cart.");
		String productId = getToken("Enter the product ID of the item being added to the cart.");
		String quantityStr = getToken("Enter quantity of item being added to cart.");
		
		int quantity = Integer.parseInt(quantityStr);
		if (quantity <= 0)
		{
			System.out.println("Quantity of item added to cart cannot be below 0!");
		}
		else
		{
			boolean result = warehouse.addToCart(clientId, productId, quantity);
			if (result == false)
			{
				System.out.println("Failed to add " + productId + " (x" + quantity +") to the cart of " + clientId);
			}
			else
			{
				System.out.println("Successfully added " + productId + " (x" + quantity + ") to the cart of " + clientId);
			}
		}
	}
	
	private void removeFromCart()
	{
		String clientId = getToken("Enter the client ID for removing an item to the cart.");
		String productId = getToken("Enter the product ID of the item being removed from the cart.");
		
		boolean itemInCart = warehouse.isProductInCart(clientId, productId);
		
		if (itemInCart == true)
		{
			boolean result = warehouse.removeFromCart(clientId, productId);
			if (result == false)
			{
				System.out.println("Failed to remove " + productId + " from the cart of " + clientId);
			}
			else
			{
				System.out.println("Successfully removed " + productId + " from the cart of " + clientId);
			}
		}
		else
		{
			System.out.println("Product " + productId + " not found in the cart of " + clientId);
		}
	}
	
	private void updateProductInCart()
	{
		String clientId = getToken("Enter the client ID whose cart you want to modify.");
		String productId = getToken("Enter the product ID of the item being modified in the cart.");
		
		boolean itemInCart = warehouse.isProductInCart(clientId, productId);
		
		if (itemInCart == true)
		{
			String quantityStr = getToken("Enter the new quantity of item in cart.");
			int quantity = Integer.parseInt(quantityStr);
			
			boolean result = warehouse.updateProductInCart(clientId, productId, quantity);
			if (result == false)
			{
				System.out.println("Failed to update " + productId + " in the cart of " + clientId);
			}
			else
			{
				System.out.println("Successfully updated quantity of " + productId + " to " + quantity + " from the cart of " + clientId);
			}
		}
		else
		{
			System.out.println("Product " + productId + " not found in the cart of " + clientId);
		}
	}
	
	
	
	private void showClientTransactions()
	{
		String clientId = getToken("Enter the client ID whose transactions you wish to show.");
		
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
	
	private void showClientInvoices()
	{
		String clientId = getToken("Enter the client ID whose invoices you wish to show.");
		
		Client client = warehouse.getClient(clientId);
		
		if (client != null)
		{
			
			Iterator<Invoice> invoices = warehouse.getClientInvoices(clientId);
			System.out.println("Invoices for " + client.getClientName() + " (" + clientId + ")");
			System.out.println("-----");
			System.out.println(String.format(
					INVOICE_FORMAT,
					"DATE",
					"PRODUCT NAME",
					"PID",
					"QUANTITY",
					"TOTAL COST"
				)
			);
			
			while (invoices.hasNext())
			{
				Invoice invoice = (Invoice) invoices.next();
				Date date = invoice.getDate();
				String productId = invoice.getProductId();
				int quantity = invoice.getQuantity();
				double cost = invoice.getPackageCost();
				
				String formattedDate = new SimpleDateFormat("MM/dd/yyyy, hh:mm aaa").format(date);
				Product product = warehouse.getProduct(productId);
				String productName = "ERROR";
				if (product != null)
				{
					productName = product.getName();
				}
				
				System.out.println(String.format(
						INVOICE_FORMAT,
						formattedDate,
						productName,
						productId,
						quantity,
						String.format("$%-9.02f", cost)
					)
				);
			}
			
		}
		else
		{
			System.out.println("Error! Client with id " + clientId + " was not found!");
		}
		
	}
	
	private void getProductInfo()
	{
		String productId = getToken("Enter the product ID you want information on.");
		
		Product product = warehouse.getProduct(productId);
		if (product != null)
		{
			//ProductName (id)
			//	Current Stock = 0
			//	Sale Price = $0.00
			//  Supplied By:
			//		ID - SupplierName - PurchasePrice
			System.out.println(product.getName() + "(" + product.getProductID() + ")");
			System.out.println("\tCurrent Stock = x" + product.getCurrentStock());
			System.out.println("\tSale Price = " + String.format("$%-9.02f", product.getBuyPrice()));
			System.out.println("\tSupplied By: ");
			
			Iterator<SuppliedProduct> suppliedProducts = warehouse.getSuppliedProductsFromProduct(productId);
			
			while (suppliedProducts.hasNext())
			{
				SuppliedProduct sp = (SuppliedProduct) suppliedProducts.next();
				
				String supplierId = sp.getSupplierId();
				double purchasePrice = sp.getPurchasePrice();
				Supplier supplier = warehouse.getSupplier(supplierId);
				String supplierName = "ERROR";
				if (supplier != null)
				{
					supplierName = supplier.getSupplierName();
				}
				System.out.println("\t\t" + String.format(
						SUPPLIED_PRODUCT_FORMAT,
						supplierId,
						supplierName,
						String.format("$%-9.02f", purchasePrice)
					)
				);
			}
		}
		else
		{
			System.out.println("Error! Unable to find product with id " + productId);
		}
	}
	
	private void getSupplierInfo()
	{
		String supplierId = getToken("Enter the supplier ID you want information on.");
		
		Supplier supplier = warehouse.getSupplier(supplierId);
		if (supplier != null)
		{
			System.out.println(supplier.getSupplierName() + "(" + supplier.getSupplierID() + ")");
			System.out.println("\tAddress = " + supplier.getSupplierAddress());
			System.out.println("\tSupplies the following products: ");
			
			Iterator<SuppliedProduct> suppliedProducts = warehouse.getSuppliedProductsFromSupplier(supplierId);
			
			while (suppliedProducts.hasNext())
			{
				SuppliedProduct sp = (SuppliedProduct) suppliedProducts.next();
				
				String productId = sp.getProductId();
				double purchasePrice = sp.getPurchasePrice();
				
				Product product = warehouse.getProduct(productId);
				String productName = "ERROR";
				if (product != null)
				{
					productName = product.getName();
				}
				System.out.println("\t" + String.format(
						SUPPLIED_PRODUCT_FORMAT,
						productId,
						productName,
						String.format("$%-9.02f", purchasePrice)
					)
				);
			}
		}
		else
		{
			System.out.println("Error! Unable to find supplier with id " + supplierId);
		}
	}
	
	private void showOutstandingClients()
	{
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
	
	private void showWaitlistFromClient()
	{
		String clientId = getToken("What client do you wish to see the waitlist for?");
		Client client = warehouse.getClient(clientId);
		
		if (client != null)
		{
			Iterator<WaitlistItem> waitlistItems = warehouse.getWaitlistedItemsFromClient(clientId);
			
			System.out.println("Waitlisted Orders for " + client.getClientName() + "(" + clientId + ")");
			
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
	
	
	private void showClientCart()
	{
		String clientId = getToken("Enter the client ID whose cart you wish to view.");
		
		Iterator<LineItem> cartItems = warehouse.getClientCart(clientId);
		
		if (cartItems == null)
		{
			System.out.println("Client " + clientId + " was not found!");
			return;
		}
		
		System.out.println("SHOPPING CART FOR CLIENT " + clientId);
		System.out.println(
			String.format(LINE_ITEM_FORMAT,
				"Num", "ID", "Name", "Quantity", "Cost", "Total Cost"
			)
		);
		
		int index = 1;
		//double totalCost = 0.0;
		while (cartItems.hasNext())
		{
			LineItem item = (LineItem) cartItems.next();
			Product product = warehouse.getProduct(item.getProductId());
			
			if (product != null)
			{
				double productCost = product.getBuyPrice();
				double lineItemCost = productCost * (double) item.getQuantity();
				
				System.out.println(
					String.format(LINE_ITEM_FORMAT,
						String.format("%3d.", index),
						product.getProductID(),
						product.getName(),
						"x" + item.getQuantity(),
						String.format("$%-9.02f", productCost),
						String.format("$%-9.02f", lineItemCost)
					)
				);
			}
			
			
			//totalCost += lineItemCost;
			index++;
		}
		
		if (index == 1)
		{
			System.out.println("Cart is empty.");
		}
	}
	
	private void showClients()
	{
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
	
	private void showSuppliers()
	{
		Iterator<Supplier> suppliers = warehouse.getSuppliers();
		System.out.println("SUPPLIERS IN SYSTEM");
		System.out.println(
			String.format(SUPPLIER_FORMAT,
				"ID", "Name", "Address"
			)
		);
		while (suppliers.hasNext())
		{
			Supplier s = (Supplier) suppliers.next();
			System.out.println(
				String.format(SUPPLIER_FORMAT,
					s.getSupplierID(),
					s.getSupplierName(),
					s.getSupplierAddress()
				)
			);
			
		}
	}
	
	private void save()
	{
		if (warehouse.save())
		{
			System.out.println("The warehouse data has been saved to the file WarehouseData.\n");
		}
		else
		{
			System.out.println("Failed to save warehouse data.");
		}
	}
	
	private void retrieve()
	{
		try
		{
			Warehouse tempWarehouse = Warehouse.retrieve();
			if (tempWarehouse != null)
			{
				System.out.println("The warehouse data has been retrieved from file WarehouseData.\n");
				warehouse = tempWarehouse;
			}
			else
			{
				System.out.println("Warehouse file not found. Creating new warehouse data.");
				warehouse = Warehouse.getInstance();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void process()
	{
		Option command;
		displayHelp();
		do
		{
			command = getCommand();
			switch (command)
			{
				case ADD_CLIENT:
					addClient();
					break;
				case ADD_PRODUCT:
					addProduct();
					break;
				case ADD_SUPPLIER:
					addSupplier();
					break;
				///////
				///////
				case CHANGE_CLIENT_NAME:
					changeClientName();
					break;
				case CHANGE_CLIENT_ADDRESS:
					changeClientAddress();
					break;
				case CHANGE_PRODUCT_NAME:
					changeProductName();
					break;
				case CHANGE_PRODUCT_PRICE:
					changeProductPrice();
					break;
				case CHANGE_SUPPLIER_NAME:
					changeSupplierName();
					break;
				case CHANGE_SUPPLIER_ADDRESS:
					changeSupplierAddress();
					break;
				///////
				///////
				case ADD_TO_CART:
					addToCart();
					break;
				case REMOVE_FROM_CART:
					removeFromCart();
					break;
				case UPDATE_PRODUCT_IN_CART:
					updateProductInCart();
					break;
				case ADD_SUPPLIED_PRODUCT:
					addSuppliedProduct();
					break;
				
				case PROCESS_ORDER:
					processOrder();
					break;
				case SHIP_PRODUCT:
					shipProductFromSupplier();
					break;
				///////
				///////
				case SHOW_CLIENT_TRANSACTIONS:
					showClientTransactions();
					break;
				case SHOW_CLIENT_INVOICES:
					showClientInvoices();
					break;
				case GET_PRODUCT_INFO:
					getProductInfo();
					break;
				case GET_SUPPLIER_INFO:
					getSupplierInfo();
					break;
				case SHOW_OUTSTANDING_CLIENTS:
					showOutstandingClients();
					break;
				case SHOW_WAIT_LIST_PRODUCTS:
					showWaitlistFromProduct();
					break;
				case SHOW_WAIT_LIST_CLIENTS:
					showWaitlistFromClient();
					break;
				
				///////
				///////
				case SHOW_CLIENT_CART:
					showClientCart();
					break;
				case SHOW_CLIENTS:
					showClients();
					break;
				case SHOW_PRODUCTS:
					showProducts();
					break;
				case SHOW_SUPPLIERS:
					showSuppliers();
					break;
				case SAVE:
					save();
					break;
				case RETRIEVE:
					retrieve();
					break;
				case HELP:
					displayHelp();
					break;
				case EXIT:
					break;
			}
		} while (command != Option.EXIT);
	}
	
	//Comment out for repl.it
	public static void main(String[] args)
	{
		UserInterface.instance().process();
	}
}
