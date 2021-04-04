import java.io.Serializable;
import java.util.*;

public class Transaction implements Serializable {
	private static final long serialVersionUID = 1L;
	Date date;
	String title;
	String description;
	double money;
	
	public Transaction(String t, String d, double m)
	{
		date = new Date();
		title = t;
		description = d;
		money = m;
	}
	
	public void setTitle(String newTitle)
	{
		title = newTitle;
	}
	
	public void setDescription(String newDesc)
	{
		description = newDesc;
	}
	
	public void setMoney(double m)
	{
		money = m;
	}
	
	//GETTER METHODS
	
	public Date getDate()
	{
		return date;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public double getMoney()
	{
		return money;
	}
}
