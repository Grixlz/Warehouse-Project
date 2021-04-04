import java.io.Serializable;
import java.util.*;

public class ShoppingCart implements Serializable
{
	private static final long serialVersionUID = 1L;
	List<LineItem> cart = new LinkedList<LineItem>();

	public ShoppingCart()
	{
		
	}
	
	public Iterator<LineItem> getCart()
	{
		return cart.iterator();
	}
	
	public boolean addProductToCart(LineItem item)
	{
		return cart.add(item);
	}
	
	public boolean isProductInCart(String productId)
	{
		boolean found = false;
		for (LineItem item : cart)
		{
			if (item.getProductId().equals(productId))
			{
				found = true;
				break;
			}
		}
		
		return found;
	}
	
	public boolean removeProductFromCart(String productId)
	{
		LineItem itemToRemove = null;
		for (LineItem item : cart)
		{
			if (item.getProductId().equals(productId))
			{
				itemToRemove = item;
				break;
			}
		}
		
		if (itemToRemove != null)
		{
			return cart.remove(itemToRemove);
		}
		
		return false;
	}
	
	public boolean updateProductInCart(String productId, int newQuantity)
	{
		LineItem itemToUpdate = null;
		for (LineItem item : cart)
		{
			if (item.getProductId().equals(productId))
			{
				itemToUpdate = item;
				break;
			}
		}
		
		if (itemToUpdate != null)
		{
			if(newQuantity == 0) 		// '0' means remove item
			{
				this.removeProductFromCart(productId);
			}
			else if(newQuantity < 0) 	// Can't have negative quantity
			{
				return false;
			}
			else						// default case: updates to new quantity
			{
				itemToUpdate.setQuantity(newQuantity);
			}
		}
		
		return itemToUpdate != null;
	}
}