// Paul
import java.util.*;
import java.io.*;

public class LoginState extends WarehouseState {
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));  
    private static LoginState instance;

	private LoginState()
	{
        super();
	}

    public static LoginState instance() {
      if (instance == null) {
        instance = new LoginState();
      }
      return instance;
    }
//====================================================================
// Options
//====================================================================
private enum Option
{ 
    CLIENT_LOGIN("Login as Client"),
    CLERK_LOGIN("Login as Clerk"),
    MANAGER_LOGIN("Login as Manager"),
    HELP("Display the help menu"),
    EXIT("Exit program");

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
            case CLIENT_LOGIN:      client();
                                    break;
            case CLERK_LOGIN:       clerk();
                                    break;
            case MANAGER_LOGIN:     manager();
                                    break;
            case HELP:              displayHelp();
                                    break;
            case EXIT:              
                                    break;   
        default:                System.out.println("Invalid choice");                               
        }  
    } while (command != Option.EXIT);
    (WarehouseContext.instance()).changeState(3);
  }
//====================================================================
// Login Methods
//====================================================================
//Remove after completing Context 
    private void client(){
        String id = getToken("Enter your client ID:");

        if( (Warehouse.getInstance()).getClient(id) != null) // NOTE: Uncomment this once you're done
        {
            (WarehouseContext.instance()).setLoginUser(WarehouseContext.IS_CLIENT);
            (WarehouseContext.instance()).setCurrentUser(id);      
            (WarehouseContext.instance()).changeState(0);
        }
        else
        {
            System.out.println("Error! Couldn't find client with ID " + id);
        }
    }

    private void clerk(){
        (WarehouseContext.instance()).setLoginUser(WarehouseContext.IS_CLERK);      
        (WarehouseContext.instance()).changeState(1);
    }

    private void manager(){
        (WarehouseContext.instance()).setLoginUser(WarehouseContext.IS_MANAGER);    
        (WarehouseContext.instance()).changeState(2);
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
        System.out.println("          Login Menu           ");
        System.out.println("===============================");
        process();
    }
}