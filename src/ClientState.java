// Paul
import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

public class ClientState extends WarehouseState {
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));  
    private static ClientState instance;

    private final String LINE_ITEM_FORMAT = "%-4s   %-4s | %-20s | %-10s | %-10s | %-10s";
	//id, name, stock, price
	private final String PRODUCT_FORMAT = "%-4s | %-20s | %-10s | %-10s";
    //date, transactionTitle, transactionDescription, money amount
    private final String TRANSACTION_FORMAT = "%-21s | %-20s | %-60s | %-10s";
    //clientName, clientId, quantity
	private final String WAITLIST_ITEM_FORMAT = "%-20s (%4s) - x%-10s";

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
//====================================================================
// Options
//====================================================================
private enum Option
{ 
    SHOW_CLIENT_DETAILS("Show client details"),
    SHOW_PRODUCTS("Show Products"),
    SHOW_CLIENT_TRANSACTIONS("Show client transactions"),
    MODIFY_CLIENT_CART("Modify Client Cart"),
    DISPLAY_WAITLIST("Display waitlist"),
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
            case SHOW_CLIENT_DETAILS:          showClientDetails();
                                    break;
            case SHOW_PRODUCTS:                showProducts();
                                    break;
            case SHOW_CLIENT_TRANSACTIONS:     showClientTrans();
                                    break;
            case MODIFY_CLIENT_CART:           modifyClientCart();
            						break;
            case DISPLAY_WAITLIST:             displayWaitlist();
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
// Client Methods
//====================================================================
    private void showClientDetails(){
        String ClientID = WarehouseContext.instance().getCurrentUser();
        System.out.println(Warehouse.getInstance().getClient(ClientID));
    }

    private void showProducts(){
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
        System.out.println("          Client Menu          ");
        System.out.println("===============================");
        process();
    }
}