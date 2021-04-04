import java.util.*;
import java.io.*;

public class Product implements Serializable{
  	private static final long serialVersionUID = 1L;
  	private List<WaitlistItem> waitlistedProducts = new LinkedList<WaitlistItem>();
 	private List<SuppliedProduct> suppliedProducts = new LinkedList<SuppliedProduct>();
	private String productID;                           
	private String productName;                      
	private int quantity;
	private double buyPrice;
	private int currentStock;

	public Product(String name, double buyPrice, int currentStock){
    this.productID = "P" + (IDServer.instance()).generatePID();
		this.productName = name;
		this.buyPrice = buyPrice;
		this.currentStock = currentStock;
		this.quantity = 0;
	}

//==================================================================
// Setters
//==================================================================
	public void setProductName(String newName){
		this.productName = newName;
	}

	public void setCurrentStock(int currentStock) {
		this.currentStock = currentStock;
	}

	public void setBuyPrice(double buyPrice) {
		this.buyPrice = buyPrice;
	}

  public void setQuantity(int quantity){
		this.quantity = quantity;
	}
  
  public void addStock(int extraStock)
  {
	  this.quantity += extraStock;
  }
  
  public boolean addWaitlistItem(WaitlistItem wItem)
  {
	  //NOTE: Need to make this method.
	  waitlistedProducts.add(wItem);
	  return true;
  }
  
  public boolean hasWaitlistItem(String clientId, int quantity)
  {
	  //NOTE: Need to make this method.
	  //Search waitlisted items for one that matches clientId and quantity
	  //if found, return true
	  //else, return false
	  Iterator<WaitlistItem> itr = waitlistedProducts.iterator();
		String currID;
		while (itr.hasNext()) {
			WaitlistItem currWaitlistItem = itr.next();
      currID = currWaitlistItem.getClientId();
			if (currID == clientId) {
				return true;
			}
		}
		return false;
  }
  
  public boolean removeWaitlistItem(String clientId, int quantity)
  {
	  //NOTE: Need to make this method.
	  //Search waitlisted items for one that matches clientId and quantity
	  //then remove it.
	  //If found, return results of remove operation.
	  //Else, return false.
	  Iterator<WaitlistItem> itr = waitlistedProducts.iterator();
		String currID;
		while (itr.hasNext()) {
			WaitlistItem currWaitlistItem = itr.next();
      currID = currWaitlistItem.getClientId();
			if (currID == clientId) {
				itr.remove();
				return true;
			}
		}
	  return true;
  }
  
  public boolean addSuppliedProduct(SuppliedProduct sp)
  {
	//NOTE: Need to make this method.
	  suppliedProducts.add(sp);
	  return true;
  }

//==================================================================
// Getters
//==================================================================
	public String getProductID(){
		return productID;
	}
	
	public String getName(){
		return productName;
	}

	public int getQuantity(){
		return quantity;
	}

	public double getBuyPrice() {
		return buyPrice;
	}

	public int getCurrentStock() {
		return currentStock;
	}
	
	public Iterator<SuppliedProduct> getSuppliedProducts()
	  {
		  //NOTE: Return iterator from list of SuppliedProducts in products
		  
		  return suppliedProducts.iterator();
	  }
	
	public Iterator<WaitlistItem> getWaitlist()
	  {
		  //NOTE: Need to do this method
		  
		  return waitlistedProducts.iterator();
	  }

//==================================================================
// Misc
//==================================================================
	public String toString(){
		return ("\n ID: \t\t\t" + productID + 
    "\n Name: \t\t\t" + productName + 
    "\n Buy Price: \t$" + String.format("%.2f", buyPrice) + 
    "\n Current Stock: " + currentStock +"\n");
	}
}