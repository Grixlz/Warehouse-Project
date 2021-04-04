// Tyler
import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

public class QueryClientState extends WarehouseState {
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));  
    private static QueryClientState instance;
    private final String CLIENTTrans_FORMAT = "%-4s | %-20s | %-20s";
    private final String CLIENT_FORMAT = "%-4s | %-20s | %-20s | %10s";

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
private enum Option
{ 
    SHOW_ALL_CLIENTS("List of all clients"),
    LIST_CLIENTS_WITH_OUTSTANDING_BALANCE("List of clients with outstanding balances"),
    CLIENTS_WITH_NO_TRANSACTIONS("List of all clients with no transactions"),
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
            case SHOW_ALL_CLIENTS:              			showAllClients();
                                    break;
            case LIST_CLIENTS_WITH_OUTSTANDING_BALANCE:     showClientsWithOutstanding();
                                    break;
            case CLIENTS_WITH_NO_TRANSACTIONS:				showClientsNoTransactions();
                                    break;
            case HELP:                         				displayHelp();
                                    break;
            case EXIT:                         				logout();
                                    break;   
        default:                System.out.println("Invalid choice");                               
        }  
    } while (command != Option.EXIT);
  }
//====================================================================
// QueryClientState Methods
//====================================================================
public void showAllClients() {
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
        System.out.println("      Query Client Menu        ");
        System.out.println("===============================");
        process();
    }
}