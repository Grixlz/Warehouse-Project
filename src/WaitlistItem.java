import java.io.Serializable;

public class WaitlistItem implements Serializable {
	private static final long serialVersionUID = 1L;
	String clientId;
	String productId;
	int quantity;
	
	public WaitlistItem(String cid, String pid, int q)
	{
		clientId = cid;
		productId = pid;
		quantity = q;
	}
	
	public String getClientId()
	{
		return clientId;
	}
	
	public String getProductId()
	{
		return productId;
	}
	
	public int getQuantity()
	{
		return quantity;
	}

	public String toString() {
		return ("Client: " + clientId + " Product: " + productId + " Amount: " + quantity);
	}
}
