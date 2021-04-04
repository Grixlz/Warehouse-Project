import java.util.*;
import java.io.*;

public class Supplier implements Serializable{
  private static final long serialVersionUID = 1L;
  private List<SuppliedProduct> suppliedProducts = new LinkedList<SuppliedProduct>();
  private String supplierID;
  private String supplierName; 
  private String supplierAddress;   

  public Supplier(String supplierName, String supplierAddress){
    this.supplierID = "S" + (IDServer.instance()).generateSID();
    this.supplierName = supplierName;
    this.supplierAddress = supplierAddress;
  }

//==================================================================
// Setters
//==================================================================
  public void setSupplierName(String supplierName){
    this.supplierName = supplierName;
  }

  public void setSupplierAddress(String supplierAddress){
    this.supplierAddress = supplierAddress;
  }
  
  public boolean addSuppliedProduct(SuppliedProduct sp)
  {
	//NOTE: Need to make this method.
	  suppliedProducts.add(sp);
	  return true;
  }

//==================================================================
// Getter
//==================================================================
  public String getSupplierID(){
    return supplierID;
  }

  public String getSupplierName(){
    return supplierName;
  }

  public String getSupplierAddress(){
    return supplierAddress;
  }
  
  public Iterator<SuppliedProduct> getSuppliedProducts()
  {
	  //NOTE: Return iterator from list of SuppliedProducts in supplier.
	  return suppliedProducts.iterator();
	  
	  //return (new LinkedList<SuppliedProduct>()).iterator();
  }

//==================================================================
// Misc
//==================================================================
	public String toString(){
		return ("\n ID: \t\t\t" + supplierID + 
    "\n Name: \t\t\t" + supplierName + 
    "\n Address: \t\t" + supplierAddress + "\n");
	}
}