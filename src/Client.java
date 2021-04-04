import java.util.*;
import java.io.*;

public class Client implements Serializable{
   private static final long serialVersionUID = 1L;
   private String clientID;
   private String clientName; 
   private String clientAddress;
   private double balance;
   private ShoppingCart cart = new ShoppingCart();
   private List<WaitlistItem> waitlistedProducts = new LinkedList<WaitlistItem>();
   private List<Invoice> invoiceList = new LinkedList<Invoice>();
   private List<Transaction> transactionList = new LinkedList<Transaction>();

  public Client(String clientName, String clientAddress){
    this.clientID = "C" + (IDServer.instance()).generateCID();
    this.clientName = clientName;
    this.clientAddress = clientAddress;
    this.balance = 0;
  }

//==================================================================
// Setters
//==================================================================
  public void setClientName(String clientName){
    this.clientName = clientName;
  }

  public void setClientBalance(double balance){
    this.balance = balance;
  }

  public void setClientAddress(String clientAddress){
    this.clientAddress = clientAddress;
  }
  
  public boolean addProductToCart(LineItem item)
  {
	  return cart.addProductToCart(item);
  }
  
  public boolean removeProductFromCart(String productId)
  {
	  return cart.removeProductFromCart(productId);
  }
  
  public boolean updateProductInCart(String productId, int newQuantity)
  {
	  return cart.updateProductInCart(productId, newQuantity);
  }
  
  public void addToBalance(double amount)
  {
	  this.balance += amount;
  }
  
  public boolean addWaitlistItem(WaitlistItem wItem)
  {
	  waitlistedProducts.add(wItem);
	  return true;
  }
  
  public boolean addTransaction(Transaction trans)
  {
	  transactionList.add(trans);
	  return true;
  }
  
  public boolean addInvoice(Invoice invoice)
  {
	  invoiceList.add(invoice);
	  return true;
  }

//==================================================================
// Getter
//==================================================================
  public String getClientID(){
    return clientID;
  }

  public String getClientName(){
    return clientName;
  }

  public double getClientBalance(){
    return balance;
  }

  public String getClientAddress(){
    return clientAddress;
  }
  
  public boolean isProductInCart(String productId)
  {
	  return cart.isProductInCart(productId);
  }
  
  public Iterator<LineItem> getCartItems()
  {
	  return cart.getCart();
  }
  
  public Iterator<Transaction> getTransactions()
  {
    return transactionList.iterator();
  }
  
  public Iterator<Invoice> getInvoices()
  {
    return invoiceList.iterator();
  }
  
  public Iterator<WaitlistItem> getWaitlist()
  {
    return waitlistedProducts.iterator();
  }

//==================================================================
// Methods
//==================================================================
public boolean getCartItem(String productId, int quantity)
{
  Iterator<WaitlistItem> itr = waitlistedProducts.iterator();
  String currID;
  while (itr.hasNext()) {
    WaitlistItem currWaitlistItem = itr.next();
    currID = currWaitlistItem.getProductId();
    if (currID == productId) {
      itr.remove();
      return true;
    }
  }
  return false;
}

public boolean removeWaitlistItem(String productId, int quantity)
{
  Iterator<WaitlistItem> itr = waitlistedProducts.iterator();
  String currID;
  while (itr.hasNext()) {
    WaitlistItem currWaitlistItem = itr.next();
    currID = currWaitlistItem.getProductId();
    if (currID == productId) {
      itr.remove();
      return true;
    }
  }
  return false;
}

public boolean hasWaitlistItem(String productId, int quantity)
{
  Iterator<WaitlistItem> itr = waitlistedProducts.iterator();
  String currID;
  while (itr.hasNext()) {
    WaitlistItem currWaitlistItem = itr.next();
    currID = currWaitlistItem.getProductId();
    if (currID == productId) {
      return true;
    }
  }
  return false;
}

public boolean AcceptPayment(double paymentAmount)
{
  if(paymentAmount < 0 || balance < paymentAmount)
  {
    return false;
  }
  balance -= paymentAmount;  
  return true;
}

public boolean EditCart(String productId, int quantity)
{
  cart.updateProductInCart(productId, quantity);
  return true;
}

	public String toString(){
		return ("\n ID: \t\t\t" + clientID + 
    "\n Name: \t\t\t" + clientName + 
    "\n Balance: \t\t$" + String.format("%.2f", balance) + 
    "\n Address: \t\t" + clientAddress + "\n");
	}
}