// Tyler
import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

public class ModifyCartState extends WarehouseState {
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));  
    private static ModifyCartState instance;

    private final String LINE_ITEM_FORMAT = "%-4s   %-4s | %-20s | %-10s | %-10s | %-10s";

	private ModifyCartState()
	{
        super();
	}

    public static ModifyCartState instance() {
      if (instance == null) {
        instance = new ModifyCartState();
      }
      return instance;
    }
//====================================================================
// Options
//====================================================================
private enum Option
{ 
    SHOW_CART("Show cart"),
    ADD_CART("Add product to cart"),
    REMOVE_CART("Remove product from cart"),
    CHANGE_CART("Change product quantity in cart"),
    HELP("Display the help menu"),
    EXIT("Logout");

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

public void process() {
    Option command; 
    do {
    	displayHelp();
        command = getCommand();
        switch (command) {
            case SHOW_CART:                    showCart();
                                    break;
            case REMOVE_CART:                  removeCart();
                                    break;
            case ADD_CART:                     addCart();
                                    break;
            case CHANGE_CART:                  changeCart();
                                    break;
            case HELP:                         displayHelp();
                                    break;
            case EXIT:                         logout();
                                    break;   
        default:                System.out.println("Invalid choice");                               
        }  
    } while (command != Option.EXIT);
  }
//====================================================================
// Modify Cart Methods 
//====================================================================
    private void showCart(){
        Warehouse warehouse = Warehouse.getInstance();
        String clientId = WarehouseContext.instance().getCurrentUser();
		
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

    public void changeCart(){
        Warehouse warehouse = Warehouse.getInstance();
        String clientId = WarehouseContext.instance().getCurrentUser();
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

    public void addCart(){
        Warehouse warehouse = Warehouse.getInstance();
        String clientId = WarehouseContext.instance().getCurrentUser();
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

    public void removeCart(){
        Warehouse warehouse = Warehouse.getInstance();
        String clientId = WarehouseContext.instance().getCurrentUser();
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

    public void logout(){
        (WarehouseContext.instance()).changeState(0);
    }
//====================================================================
// Auxilary Methods
//====================================================================
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
    
    public void run() {
        System.out.println("===============================");
        System.out.println("   Modify Client Catr Menu     ");
        System.out.println("===============================");
        process();
    }
}