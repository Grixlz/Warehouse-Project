import java.util.*;
import java.io.*;
import javax.swing.*;
import java.text.*;
import java.awt.*;
import java.awt.event.*;


public class WarehouseContext {
    private static Warehouse warehouse;
    private static WarehouseContext instance;
    private int loginUser;
    private String currentUser; 
    private int currentState;
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    public static final int IS_CLIENT = 0;
    public static final int IS_CLERK = 1;
    public static final int IS_MANAGER = 2;
    private WarehouseState[] states;
    private int[][] nextState;

    private static JFrame wareFrame;
    
    
    
    private WarehouseContext()
	{
    	wareFrame = new JFrame("Warehouse");
        wareFrame.addWindowListener(new WindowAdapter()
        	{public void windowClosing(WindowEvent e){System.exit(0);}});
        wareFrame.setSize(400,400);
        wareFrame.setLocation(400, 400);
        wareFrame.setResizable(false);
        retrieve();
        // set up the FSM
        states = new WarehouseState[6];
        states[0] = ClientState.instance();       // Access to Clerk[1], Login[3], ModifyCart[4]
        states[1] = ClerkState.instance();        // Access to Client[0], Manager[2], Login[3], QueryClients[5]
        states[2] = ManagerState.instance();      // Access to Clerk[1], Login[3]
        states[3] = LoginState.instance();        // Access to Client[0], Clerk[1], Manager[2], Login[3]
        states[4] = ModifyCartState.instance();   // Access to Client[0]
        states[5] = QueryClientState.instance();  // Access to Clerk[1]

        // set up the transition table;
        nextState = new int[6][6];
        nextState[0][0] = -2;  nextState[0][1] =  1;  nextState[0][2] = -2;  nextState[0][3] =  3;  nextState[0][4] =  4;  nextState[0][5] = -2;
        nextState[1][0] =  0;  nextState[1][1] = -2;  nextState[1][2] =  2;  nextState[1][3] =  3;  nextState[1][4] = -2;  nextState[1][5] =  5;
        nextState[2][0] = -2;  nextState[2][1] =  1;  nextState[2][2] = -2;  nextState[2][3] =  3;  nextState[2][4] = -2;  nextState[2][5] = -2;
        nextState[3][0] =  0;  nextState[3][1] =  1;  nextState[3][2] =  2;  nextState[3][3] = -1;  nextState[3][4] = -2;  nextState[3][5] = -2;
        nextState[4][0] =  0;  nextState[4][1] = -2;  nextState[4][2] = -2;  nextState[4][3] = -2;  nextState[4][4] = -2;  nextState[4][5] = -2;
        nextState[5][0] = -2;  nextState[5][1] =  1;  nextState[5][2] = -2;  nextState[5][3] = -2;  nextState[5][4] = -2;  nextState[5][5] = -2;
        currentState = 3;
        
	}

    public static WarehouseContext instance() {
        if (instance == null) {
          instance = new WarehouseContext();
        }
        return instance;
      }

      public void process(){
        states[currentState].run();
      }

//====================================================================
// Primary Methods
//====================================================================
    public void changeState(int transition)
    {
        currentState = nextState[currentState][transition];
        if (currentState == -2) 
            {System.out.println("Error has occurred"); terminate();}
        if (currentState == -1) 
            terminate();
        states[currentState].run();
    }
    public void setCurrentUser(String code)
    {currentUser = code;}
  
    public void setLoginUser(int uID)
    { loginUser = uID;}
  
    public String getCurrentUser()
    { return currentUser;}
  
    public int getLoginUser()
    { return loginUser;}

//====================================================================
// Auxilary Methods
//====================================================================
public String getToken(String prompt) {
    do {
      try {
        System.out.println(prompt);
        String line = reader.readLine();
        StringTokenizer tokenizer = new StringTokenizer(line,"\n\r\f");
        if (tokenizer.hasMoreTokens()) {
          return tokenizer.nextToken();
        }
      } catch (IOException ioe) {
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

  private void retrieve()
  {
      try
      {
          Warehouse tempWarehouse = Warehouse.retrieve();
          if (tempWarehouse != null)
          {
              System.out.println("The warehouse data has been successfully retrieved from file WarehouseData.\n");
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
  
  private void terminate()
  {
   if (promptYesNo("Save data?")) {
      if (Warehouse.save()) {
         System.out.println(" The Warehouse has been successfully saved in the file WarehouseData \n" );
       } else {
         System.out.println(" There has been an error in saving \n" );
       }
     }
   System.out.println(" Goodbye \n "); System.exit(0);
  }
  
  public JFrame getFrame()
  { return wareFrame;}
  
  
  
  
 public static void main (String[] args)
{
	WarehouseContext.instance().process(); 
}

}