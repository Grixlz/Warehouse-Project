//Clare

import java.io.Serializable;
import java.util.*;

public class Invoice implements Serializable {
	private static final long serialVersionUID = 1L;
	Date date;
	String productId;
	int quantity;
	double packageCost;
	
	public Invoice(String pid, int num, double cost)
	{
		date = new Date();
		productId = pid;
		quantity = num;
		packageCost = cost;
	}
	
	public Date getDate()
	{
		return date;
	}
	
	public String getProductId()
	{
		return productId;
	}
	
	public int getQuantity()
	{
		return quantity;
	}
	
	public double getPackageCost()
	{
		return packageCost;
	}
}
