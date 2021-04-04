import java.io.Serializable;

public class SuppliedProduct implements Serializable {
	private static final long serialVersionUID = 1L;
	String productId;
	String supplierId;
	double purchasePrice;
	
	public SuppliedProduct(String sid, String pid, double price)
	{
		productId = pid;
		supplierId = sid;
		purchasePrice = price;
	}
	
	public String getProductId()
	{
		return productId;
	}
	
	public String getSupplierId()
	{
		return supplierId;
	}
	
	public double getPurchasePrice()
	{
		return purchasePrice;
	}
}
