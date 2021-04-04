import java.util.*;
import java.io.*;

public class SupplierList implements Serializable
{
	 private static final long serialVersionUID = 1L;
	 private List<Supplier> suppliers = new LinkedList<Supplier>();
	 private static SupplierList supplierList;
	 
	 private SupplierList(){	 
     //Nothing needs to be made here
	 }
	 
   // Makes a list For Warehouse, order, shipping if the those objecys are made
	 public static SupplierList instance(){
		 if(supplierList == null)
			 return (supplierList = new SupplierList());
		 else 
			 return supplierList;
	 }
	 
  // Searches for a Supplier in Supplier list
	public Supplier getSupplier(String supplierID){
		Supplier supplier = null;
		for (int i = 0; i < suppliers.size(); i++){
			Supplier s = suppliers.get(i);
			if (s.getSupplierID().equals(supplierID))
				supplier = suppliers.get(i);
		}//end for
		return supplier;
	}//end getSupplier
	 
	 // inserts a new Supplier into the list
	 public boolean insertSupplier(Supplier supplier){
		 suppliers.add(supplier);
		 return true;
	 }

  // Gets an iterator
  public Iterator<Supplier> getSuppliers(){
        return suppliers.iterator();
  }
	 
	 public String toString(){
    System.out.println("---------- Displaying Supplier List ----------");
    for (Supplier supplier : suppliers)
    {
      System.out.println(supplier);
    }
    return "---------------------------------------";
  }//end toString
  
}//end SupplierList