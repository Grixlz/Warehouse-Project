import java.util.*;
import java.io.*;

public class ClientList implements Serializable
{
	 private static final long serialVersionUID = 1L;
	 private List<Client> clients = new LinkedList<Client>();
	 private static ClientList clientList;
	 
	 private ClientList(){	 
     //Nothing needs to be made here
	 }
	 
   // Makes a list For Warehouse, order, shipping if the those objecys are made
	 public static ClientList instance(){
		 if(clientList == null)
			 return (clientList = new ClientList());
		 else 
			 return clientList;
	 }
	 
  // Searches for a Client in Client list
	public Client getClient(String clientID){
		for (int i = 0; i < clients.size(); i++){
			if (clients.get(i).getClientID().equals(clientID))
				return clients.get(i);
		}//end for
		return null;
	}//end Client
	 
	 // inserts a new Client into the list
	 public boolean insertClient(Client client){
		 clients.add(client);
		 return true;
	 }

  // Gets an iterator
  public Iterator<Client> getClients(){
        return clients.iterator();
  }
	 
	 public String toString(){
    System.out.println("---------- Displaying Client List ----------");
    for (Client client : clients)
    {
      System.out.println(client);
    }
    return "---------------------------------------";
  }//end toString
  
}//end ClientList