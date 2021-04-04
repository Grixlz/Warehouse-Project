import java.io.Serializable;

public class LineItem  implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String productId;
	private int quantity;
	
	public LineItem(String id, int q)
	{
		productId = id;
		quantity = q;
	}
	
	public String getProductId()
	{
		return productId;
	}
	
	public int getQuantity()
	{
		return quantity;
	}
	
	public void setQuantity(int q)
	{
		quantity = q;
	}
}
