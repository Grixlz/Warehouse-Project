import java.util.*;
import java.io.*;

public class ProductList implements Serializable
{
	 private static final long serialVersionUID = 1L;
	 private List<Product> products = new LinkedList<Product>();
	 private static ProductList productList;
	 
	 private ProductList(){	 
     //Nothing needs to be made here
	 }
	 
   // Makes a list For Warehouse, order, shipping if the those objecys are made
	 public static ProductList instance(){
		 if(productList == null)
			 return (productList = new ProductList());
		 else 
			 return productList;
	 }
	 
  // Searches for a product in product list
	public Product getProduct(String productID){
		for (int i = 0; i < products.size(); i++){
			if (products.get(i).getProductID().equals(productID))
				return products.get(i);
		}//end for
		return null;
	}//end getProduct
	 
	public Product removeProduct(String productID){
		for (int i = 0; i < products.size(); i++){
			if (products.get(i).getProductID().equals(productID))
				return products.remove(i);
		}//end for
		return null;
	}//end getProduct

	 // inserts a new product into the list
	 public boolean insertProduct(Product product){
		 products.add(product);
		 return true;
	 }

  // Gets an iterator
  public Iterator<Product> getProducts(){
        return products.iterator();
  }
	 
	 public String toString(){
    System.out.println("---------- Displaying Product List ----------");
    for (Product product : products)
    {
      System.out.println(product);
    }
    return "---------------------------------------";
  }//end toString
  
}//end ProductList