// Tyler
import java.util.*;
import java.io.*;

public class ManagerState extends WarehouseState {
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));  
    private static ManagerState instance;
    private static Warehouse warehouse;             
    private final String SUPPLIER_FORMAT = "%-4s | %-20s | %-20s";
    private final String SUPPLIED_PRODUCT_FORMAT = "%-4s | %-20s ";
	private ManagerState()
	{
        super();
        warehouse = Warehouse.getInstance();        
	}

    public static ManagerState instance() {
      if (instance == null) {
        instance = new ManagerState();
      }
      return instance;
    }
//====================================================================
// Process
//====================================================================
private enum Option
{ 
    ADD_PRODUCT("Add Product"),
    ADD_SUPPLIER("Add Supplier"),
    SHOW_SUPPLIERLIST("Show Supplier List"),
    SHOW_PRODUCT_SUPPLIEDPRODUCTLIST("Show Supplier SuppliedProduct List"),
    SHOW_SUPPLIER_SUPPLIEDPRODUCTLIST("Show Supplier SuppliedProduct List"),
    UPDATE_PRODUCT("Update Product"),
    BECOME_CLERK("Become Clerk"),
    HELP("Display the help menu"),
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

public void process() {//started here!
    Option command; 
    do {
    	help(); 
        command = getCommand();
        switch (command) {
            case ADD_PRODUCT:             addProduct();
                                          break;
            case ADD_SUPPLIER:            addSupplier();
                                          break;
            case SHOW_SUPPLIERLIST :      showSupplierList();
                                          break;
            case SHOW_PRODUCT_SUPPLIEDPRODUCTLIST :             showProductSuppliedProductList();
                                          break;
            case SHOW_SUPPLIER_SUPPLIEDPRODUCTLIST :           showSupplierSuppliedProductList();
                                          break;
            case UPDATE_PRODUCT :         updateProduct();
                                          break;
            case BECOME_CLERK :            becomeClerk();
                                          break;
            case HELP:                    help();
                                          break;
            case LOGOUT:                  
                                          break;
        default:                System.out.println("Invalid choice");                               
        }  
    } while (command != Option.LOGOUT);
    (WarehouseContext.instance()).changeState(3);
  }
//====================================================================
// Client Methods
//====================================================================

private void addProduct(){
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

private void addSupplier(){
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


public void showSupplierList(){
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
	
public void showProductSuppliedProductList(){//to do

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

public void showSupplierSuppliedProductList(){//to do
		String supplierId = getToken("Enter the supplier ID you want information on.");
		
		Supplier supplier = warehouse.getSupplier(supplierId);
		if (supplier != null)
		{
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

public void updateProduct(){
  /*
    Update products and purchase prices for a particular supplier.
     Actor provides supplierID;
      system displays a list of productIDs and purchase prices.
       Actor can remove product, add product or change purchase price
  */
  //show all products and ids
  String supplierId = getToken("Enter the supplier ID you want products for.");
		
		Supplier supplier = warehouse.getSupplier(supplierId);
		if (supplier != null)
		{
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

    Scanner choiceIn = new Scanner(System.in);
    //menu for changing name or price or removeing
    System.out.println("-----------------------------");
    System.out.println("1 - Rename product");
    System.out.println("2 - Change Product Price");
    System.out.println("3 - Remove Product");
    System.out.println("4 - return to manager menu");
    System.out.println("-----------------------------");
    int choice = choiceIn.nextInt();
    if(choice == 1){
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
    else if(choice == 2){
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

    else if(choice == 3){
      String id = getToken("Enter the ID of the product you want removed.");
      Product product = warehouse.removeProduct(id); 
  }
}
public void becomeClerk	(){
  (WarehouseContext.instance()).changeState(3);
  }




//====================================================================
// Auxilary Methods
//====================================================================  
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

   private void help()
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

   public void logout(){//--------------------------
   
   //send help not sure what to put here
   
   }
          
  public void run() {
      help();
      process();
  }
}  