//Clare Miller

import java.util.*;
import java.io.*;

public class Warehouse implements Serializable
{
	private static final long serialVersionUID = 1L;
	private static Warehouse warehouse;
	SupplierList supplierList;
	ClientList clientList;
	ProductList productList;
	
	private Warehouse()
	{
		System.out.println("Initializing warehouse.");
		clientList = ClientList.instance();
		productList = ProductList.instance();
		supplierList = SupplierList.instance();
		IDServer.instance();
	}
	
	public static Warehouse getInstance()
	{
		if (warehouse == null)
		{
			warehouse = new Warehouse();
			return warehouse;
		}
		else
		{
			return warehouse;
		}
	}
	
	//==================================================================
	// Supplier Methods
	//==================================================================
	
	public Supplier addSupplier(String name, String address)
	{
		Supplier supplier = new Supplier(name, address);
		if (supplierList.insertSupplier(supplier))
		{
			return supplier;
		}
		
		return null;
	}
	
	public boolean changeSupplierName(String supplierId, String newName)
	{
		Supplier s1 = supplierList.getSupplier(supplierId);
		if (s1 != null)
		{
		    s1.setSupplierName(newName);
			return true;
		}
		
		return false;
	}
	
	public boolean changeSupplierAddress(String supplierId, String newAddress)
	{
		Supplier s1 = supplierList.getSupplier(supplierId);
		if (s1 != null)
		{
		    s1.setSupplierAddress(newAddress);
			return true;
		}
		
		return false;
	}
	
	public Supplier getSupplier(String supplierId)
	{
		Supplier supplier = supplierList.getSupplier(supplierId);
		return supplierList.getSupplier(supplierId);
	}
	
	public Iterator<Supplier> getSuppliers()
	{
		return supplierList.getSuppliers();
	}
	
	public Iterator<SuppliedProduct> getSuppliedProductsFromSupplier(String supplierId)
	{
		Supplier supplier = getSupplier(supplierId);
		if (supplier != null)
		{
			return supplier.getSuppliedProducts();
		}
		
		return (new LinkedList<SuppliedProduct>()).iterator();
	}
	
	//==================================================================
	// Product Methods
	//==================================================================
	
	public Product addProduct(String name, double price, int quantity)
	{
		Product product = new Product(name, price, quantity);
		if (productList.insertProduct(product))
		{
			return product;
		}
		
		return null;
	}
	
	public boolean changeProductName(String productId, String newName)
	{
		Product p = productList.getProduct(productId);
		if (p != null)
		{
			p.setProductName(newName);
			return true;
		}
		
		return false;
	}
	
	public boolean changeProductPrice(String productId, double newPrice)
	{
		Product p = productList.getProduct(productId);
		if (p != null)
		{
			p.setBuyPrice(newPrice);
			return true;
		}
		
		return false;
	}
   
   public Product removeProduct(String productId)
	{
		return productList.removeProduct(productId);
   }
	
	public Product getProduct(String productId)
	{
		return productList.getProduct(productId);
	}
	
	public double getProductCost(String productId)
	{
		Product p = productList.getProduct(productId);
		if (p != null)
		{
			return p.getBuyPrice();
		}
		
		return -999.99;
	}
	
	public Iterator<Product> getProducts()
	{
		return productList.getProducts();
	}
	
	public Iterator<WaitlistItem> getWaitlistedItemsFromProduct(String productId)
	{
		Product product = getProduct(productId);
		if (product != null)
		{
			return product.getWaitlist();
		}
		
		return (new LinkedList<WaitlistItem>()).iterator();
	}
	
	public int addProductStock(String productId, int extraStock)
	{
		Product product = getProduct(productId);
		
		if (product != null)
		{
			product.addStock(extraStock);
			
			return product.getCurrentStock();
		}
		
		return -1; //we should never have < 0 of a product in stock when adding to it, so it is considered an error result
	}
	
	public Iterator<SuppliedProduct> getSuppliedProductsFromProduct(String productId)
	{
		Product product = getProduct(productId);
		if (product != null)
		{
			return product.getSuppliedProducts();
		}
		
		return (new LinkedList<SuppliedProduct>()).iterator();
	}
	
	//==================================================================
	// Client Methods
	//==================================================================
	
	public Client addClient(String cName, String cAddress){
		Client client = new Client(cName, cAddress);
		
		if (clientList.insertClient(client))
		{
			return client;
		}
		
	    return null;
	}
	
	public boolean changeClientName(String clientId, String newName)
	{
		Client c = clientList.getClient(clientId);
		if (c != null)
		{
			c.setClientName(newName);
			return true;
		}
		
		return false;
	}
	
	public boolean changeClientAddress(String clientId, String newAddress)
	{
		Client c = clientList.getClient(clientId);
		if (c != null)
		{
			c.setClientAddress(newAddress);
			return true;
		}
		
		return false;
	}
	
	public Client getClient(String clientId)
	{
		return clientList.getClient(clientId);
	}
	
	public Iterator<LineItem> getClientCart(String clientId)
	{
		Client c = clientList.getClient(clientId);
		if (c != null)
		{
			return c.getCartItems();
		}
		
		return null;
	}
	
	public Iterator<Client> getClients()
	{
		return clientList.getClients();
	}
	
	public Iterator<Transaction> getClientTransactions(String clientId)
	{
		Client client = getClient(clientId);
		
		if (client != null)
		{
			return client.getTransactions();
		}
		
		return null;
	}
	
	public Iterator<Invoice> getClientInvoices(String clientId)
	{
		Client client = getClient(clientId);
		
		if (client != null)
		{
			return client.getInvoices();
		}
		
		return null;
	}
	
	public Iterator<WaitlistItem> getWaitlistedItemsFromClient(String clientId)
	{
		Client client = getClient(clientId);
		if (client != null)
		{
			return client.getWaitlist();
		}
		
		return (new LinkedList<WaitlistItem>()).iterator();
	}
	
	//==================================================================
	// Misc Methods
	//==================================================================
	
	public boolean processOrder(String clientId)
	{
		Client client = clientList.getClient(clientId);
		if (client != null)
		{
			Iterator<LineItem> cart = client.getCartItems();
			int shippedItems = 0;
			int waitlistedItems = 0;
			double totalCost = 0;
			
			List<String> itemsToRemove = new LinkedList<String>();
			
			//NOTE: Maybe too complex? Could possibly create a separate, private method in warehouse to do this...
			while (cart.hasNext())
			{
				LineItem item = (LineItem) cart.next();
				String productId = item.getProductId();
				itemsToRemove.add(productId);
				int quantity = item.getQuantity();
				
				Product product = productList.getProduct(productId);
				if (product != null)
				{
					int stock = product.getCurrentStock();
					double price = product.getBuyPrice(); //NOTE: Should really be getSellPrice
					
					//NOTE: Should the totalCost be the sum of ALL items in the order, or only
					//		the items that were shipped (not waitlisted)?
					totalCost += price * (double) quantity;
					
					if (quantity <= stock)
					{
						shippedItems++;
						//totalCost += price;
						product.addStock(-quantity);
						
						double itemCost = price * (double) quantity;
						
						
						Invoice invoice = new Invoice(productId, quantity, itemCost);
						client.addInvoice(invoice);
						//InvoicePackage iPackage = new InvoicePackage(productId, quantity, itemCost);
						//orderInvoice.addInvoicePackage(iPackage);
					}
					else
					{
						waitlistedItems++;
						
						WaitlistItem wItem = new WaitlistItem(clientId, productId, quantity);
						client.addWaitlistItem(wItem);
						product.addWaitlistItem(wItem);
					}
				}
				
				//client.removeProductFromCart(productId);
			}
			
			client.addToBalance(totalCost);
			
			String desc = String.format("%03d items shipped and %03d items waitlisted.", shippedItems, waitlistedItems);
			Transaction t = new Transaction("PROCESSED ORDER", desc, totalCost);
			client.addTransaction(t);
			
			for (String pid : itemsToRemove)
			{
				client.removeProductFromCart(pid);
			}
		}
		
		
		
		//NOTE: What should we return? Should this method be void?
		return true;
	}
	
	public boolean processWaitlistItem(String clientId, String productId, int quantity)
	{
		Client client = getClient(clientId);
		Product product = getProduct(productId);
		if (client != null && product != null)
		{
			//check if client waitlist has a match for productId and quantity
			//check if product waitlist has a match for clientId and quantity
			//if both are true, then remove waitlist from both, creating invoice, and return true
			//otherwise, return false
			
			//NOTE: For finding and removing waitlist items from client/product
			//perhaps we should just assign an ID to WaitlistItems?
			boolean clientHasWaitlistItem = client.hasWaitlistItem(productId, quantity);
			boolean productHasWaitlistItem = product.hasWaitlistItem(clientId, quantity);
			
			if (clientHasWaitlistItem && productHasWaitlistItem)
			{
				client.removeWaitlistItem(productId, quantity);
				product.removeWaitlistItem(clientId, quantity);
				
				double totalCost = product.getBuyPrice() * (double) quantity; //NOTE: Should really be getSellPrice()
				Invoice invoice = new Invoice(productId, quantity, totalCost);
				
				client.addInvoice(invoice);
				
				return true;
			}
		}
		
		return false;
	}
	
	public boolean addSuppliedProduct(String supplierId, String productId, double purchasePrice)
	{
		Supplier supplier = supplierList.getSupplier(supplierId);
		Product product = productList.getProduct(productId);
		
		SuppliedProduct sp = new SuppliedProduct(supplierId, productId, purchasePrice);
		
		boolean r1 = supplier.addSuppliedProduct(sp);
		boolean r2 = product.addSuppliedProduct(sp);
		
		return r1 && r2;
	}
	
	public boolean addToCart(String clientId, String productId, int quantity)
	{
		Client c = clientList.getClient(clientId);
		if (c != null)
		{
			Product p = productList.getProduct(productId);
			if (p != null)
			{
				LineItem item = new LineItem(productId, quantity);
				
				return c.addProductToCart(item);
			}
		}
		
		return false;
	}
	
	public boolean isProductInCart(String clientId, String productId)
	{
		//NOTE/WARNING: Returns true if product is in cart, false if client not found, and false if product is not in cart
		Client c = clientList.getClient(clientId);
		if (c != null)
		{
			return c.isProductInCart(productId);
		}
		
		return false;
	}
	
	public boolean removeFromCart(String clientId, String productId)
	{
		/*
		 * NOTE/WARNING: How can we differentiate between...
		 * 		1) Successfully removed item from cart
		 * 		2) Given item was not found in cart
		 * 		3) Invalid client id was given
		 */
		
		boolean isInCart = isProductInCart(clientId, productId);
		
		if (isInCart == true)
		{
			Client c = clientList.getClient(clientId);
			if (c != null)
			{
				
				return c.removeProductFromCart(productId);
			}
		}
		
		return false;
	}
	
	public boolean updateProductInCart(String clientId, String productId, int newQuantity)
	{
		/*
		 * NOTE/WARNING: How can we differentiate between...
		 * 		1) Successfully removed item from cart
		 * 		2) Given item was not found in cart
		 * 		3) Invalid client id was given
		 */
		
		boolean isInCart = isProductInCart(clientId, productId);
		
		if (isInCart == true)
		{
			Client c = clientList.getClient(clientId);
			if (c != null)
			{
				return c.updateProductInCart(productId, newQuantity);
			}
		}
		
		return false;
	}
	
	public static Warehouse retrieve()
	{
		try
		{
			FileInputStream file = new FileInputStream(new File("src/WarehouseData.txt"));
			ObjectInputStream input = new ObjectInputStream(file);
			input.readObject();
			IDServer.retrieve(input);
			
			return warehouse;
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
			return null;
		}
		catch (ClassNotFoundException cnfe)
		{
			cnfe.printStackTrace();
			return null;
		}
	}
	
	public static boolean save()
	{
		try
		{
			FileOutputStream file = new FileOutputStream(new File("WarehouseData.txt"));
			ObjectOutputStream output = new ObjectOutputStream(file);
			output.writeObject(warehouse);
			output.writeObject(IDServer.instance());
			output.close();
			
			return true;
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
			return false;
		}
	}
	
	private void writeObject(java.io.ObjectOutputStream output)
	{
		try
		{
			output.defaultWriteObject();
			output.writeObject(warehouse);
		}
		catch (IOException ioe)
		{
			System.out.println(ioe);
		}
	}
	
	private void readObject(java.io.ObjectInputStream input)
	{
		try
		{
			input.defaultReadObject();
			if (warehouse == null)
			{
				warehouse = (Warehouse) input.readObject();
			}
			else
			{
				input.readObject();
			}
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public String toString()
	{
		return "(DUMMY) Warehouse Data";
	}
}