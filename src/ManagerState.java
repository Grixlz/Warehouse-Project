// Tyler
import java.util.*;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class ManagerState extends WarehouseState  implements ActionListener{
    private static ManagerState instance;
    private static Warehouse warehouse;             
    private final String SUPPLIER_FORMAT = "%-4s | %-20s | %-20s";
    private final String SUPPLIED_PRODUCT_FORMAT = "%-4s | %-20s| %-20s ";
    private String SUID, PUID;
    
    //GUI
    private JFrame frame;
    private AbstractButton productButton, supplierButton, productSuppliedProductsButton, supplierListButton, supplierSuppliedProductsButton,
    	updateProductButton, addPIDButton, addSIDButton, clerkButton, logoutButton, addPButton, addSButton, back, renamePButton, removePButton,
    	changePNameButton, updatePButton, renamePMButton, changePPButton;
    static JFrame cleintLogin;
    static JLabel managerLabel, clientLoginLabel, pNameLabel, pPriceLabel, pQuantLabel, sNameLabel, addSupplierLabel, sAddressLabel, SIDLabel,
    	PIDLabel, productSupplierLabel, sIDLabel, supplierSupplierLabel, renamePLabel, removePLabel, changePNameLabel, updateProductLabel, updateSIDLabel,
    	renamePMLabel, changePPLabel, productIDLabel;
    private JTextArea textArea;
    static JTextField clientIdField, pNameField, pPriceField, pQuantField,sNameField, sAddressField, PIDField,  SIDField, updateSIDField,
    	renamePMField, changePPField, productIDField;
    
    
	private ManagerState()
	{
        super();
        warehouse = Warehouse.getInstance();        
	}

    public static ManagerState instance() {
      if (instance == null) {
        instance = new ManagerState();
      }
      return instance;
    }
//====================================================================
// GUI
//====================================================================

public void process() {//started here!
	frame = WarehouseContext.instance().getFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JPanel LoginStatePanel = new JPanel();
    BoxLayout boxlayout = new BoxLayout(LoginStatePanel, BoxLayout.Y_AXIS);
    LoginStatePanel.setLayout(boxlayout);
    
    //button and listeners
    productButton = new JButton("Add Product");
    supplierButton =  new JButton("Add Supplier");
    supplierListButton = new JButton("Show Suppliers");
    supplierSuppliedProductsButton = new JButton("Show Supplier Supplier Products");
    productSuppliedProductsButton = new JButton("Show Product Supplier Products");
    updateProductButton = new JButton("Update Product");
    clerkButton = new JButton("Become Clerk");
    //supplierListButton = new JButton("Manager");
    logoutButton = new JButton("Logout");  
    
    productButton.addActionListener(this);
    supplierButton.addActionListener(this);
    supplierListButton.addActionListener(this);
    supplierSuppliedProductsButton.addActionListener(this);
    productSuppliedProductsButton.addActionListener(this);
    updateProductButton.addActionListener(this);
    clerkButton.addActionListener(this);
    logoutButton.addActionListener(this);
    
    //label
    managerLabel = new JLabel("Manager Menu");
    managerLabel.setFont(new Font("Helvetica", Font.PLAIN, 20));

    
    //center align
    managerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    productButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    supplierButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    supplierListButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    productSuppliedProductsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    supplierSuppliedProductsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    updateProductButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    clerkButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    
    
    //adding to pane
    LoginStatePanel.add(managerLabel);
    LoginStatePanel.add(this.productButton);
    LoginStatePanel.add(this.supplierButton);
    LoginStatePanel.add(this.supplierListButton);
    LoginStatePanel.add(this.productSuppliedProductsButton);
    LoginStatePanel.add(this.supplierSuppliedProductsButton);
    LoginStatePanel.add(this.updateProductButton);
    LoginStatePanel.add(this.clerkButton);
    LoginStatePanel.add(this.logoutButton);
    frame.getContentPane().add(LoginStatePanel);
    frame.setVisible(true);
    frame.paint(frame.getGraphics()); 
    frame.toFront();
    frame.requestFocus();
  }

public void actionPerformed(ActionEvent event) {
	if (event.getSource().equals(this.productButton)){
		this.addProductMenu();
	}
	else if(event.getSource().equals(this.addPButton)){
		clear();
		String name = pNameField.getText();
		Double price = Double.parseDouble(pPriceField.getText()); 
		int quantity = Integer.parseInt(pQuantField.getText()); 
		this.addProduct(name, price, quantity);
		this.process();
	}
	else if(event.getSource().equals(this.supplierButton)){
		this.addSupplierMenu();
	}
	else if(event.getSource().equals(this.addSButton)){
		clear();
		String name = sNameField.getText();
		String address = sAddressField.getText();
		this.addSupplier(name,address);
		this.process();
	}
	else if(event.getSource().equals(this.supplierListButton)){
		clear();
		this.showSupplierList();
	}
	else if(event.getSource().equals(this.productSuppliedProductsButton)){
		clear();
		this.productSuppliedMenu();
	}
	else if(event.getSource().equals(this.addPIDButton)){
		String id = PIDField.getText();
		clear();
		this.showProductSuppliedProductList(id);
	}
	else if(event.getSource().equals(this.supplierSuppliedProductsButton)){
		this.supplierSuppliedProductMenu();
	}
	else if(event.getSource().equals(this.addSIDButton)){
		String id = SIDField.getText();
		clear();
		this.showSupplierSuppliedProductList(id);
	}
	else if(event.getSource().equals(this.updateProductButton)){//--update menu
		clear();
		this.updateProductMenu();
	}
	else if(event.getSource().equals(this.updatePButton)){//--update product
		String id = updateSIDField.getText();
		SUID = id;
		clear();
		this.updateProduct(SUID);
	}
	else if(event.getSource().equals(this.renamePButton)){//--rename menu
		String id = productIDField.getText();
		PUID = id;
		clear();
		this.renameProductMenu();
	}
	else if(event.getSource().equals(this.renamePMButton)){//--rename
		String name = renamePMField.getText();
		clear();
		this.renameProduct(name, PUID);
		this.process();
	}
	else if(event.getSource().equals(this.changePNameButton)){//--change price menu
		String id = productIDField.getText();
		PUID = id;
		clear();
		this.changeProductPriceMenu();
	}
	else if(event.getSource().equals(this.changePPButton)){//--change price
		double price = Double.parseDouble(changePPField.getText());
		clear();
		this.changeProductPrice(price, PUID);
		this.process();
	}
	else if(event.getSource().equals(this.removePButton)){//--remove
		String id = productIDField.getText();
		PUID = id;
		clear();
		this.removeProduct(PUID);
		this.process();
	}
	else if(event.getSource().equals(this.clerkButton)){//---
		clear();
		this.becomeClerk();
	}
	else if(event.getSource().equals(this.back)){//---
		clear();
		this.process();
	}
	else if(event.getSource().equals(this.logoutButton)){
		clear();
		this.logout();
	}
}


//====================================================================
// Client Methods
//====================================================================

private void addProductMenu() {
	clear();
	frame = WarehouseContext.instance().getFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JPanel updateCartPanel = new JPanel();
    BoxLayout boxlayout = new BoxLayout(updateCartPanel, BoxLayout.Y_AXIS);
    updateCartPanel.setLayout(boxlayout);
    
    pNameLabel = new JLabel("Name:");
    pPriceLabel = new JLabel("Price:");
    pQuantLabel = new JLabel("Quantity:");
    pNameField = new JTextField(10);
    pPriceField = new JTextField(10);
    pQuantField = new JTextField(10);
    addPButton = new JButton("Add Product");
    back = new JButton("Back");
    addPButton.addActionListener(this);
    back.addActionListener(this);
    frame.getContentPane().setLayout(new FlowLayout());
    
    frame.add(this.pNameLabel);
    frame.add(this.pNameField);
    frame.add(Box.createHorizontalStrut(10));
    frame.add(this.pPriceLabel);
    frame.add(this.pPriceField);
    frame.add(Box.createHorizontalStrut(10));
    frame.add(this.pQuantLabel);
    frame.add(this.pQuantField);
    frame.add(this.addPButton);
    frame.add(this.back);
    frame.setVisible(true);
    frame.paint(frame.getGraphics());
    frame.toFront();
    frame.requestFocus();
}
private void addProduct(String name, Double price, int quantity){
	Product product;
	product = warehouse.addProduct(name, price, quantity);
  }

private void addSupplierMenu() {
	clear();
	frame = WarehouseContext.instance().getFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JPanel updateCartPanel = new JPanel();
    BoxLayout boxlayout = new BoxLayout(updateCartPanel, BoxLayout.Y_AXIS);
    updateCartPanel.setLayout(boxlayout);
    
    sNameLabel = new JLabel("Name:");
    sAddressLabel = new JLabel("Address:");
    sNameField = new JTextField(10);
    sAddressField = new JTextField(10);
    addSButton = new JButton("Add Supplier");
    addSButton.addActionListener(this);
    back = new JButton("Back");
    back.addActionListener(this);
    frame.getContentPane().setLayout(new FlowLayout());
    
    
    frame.add(this.sNameLabel);
    frame.add(this.sNameField);
    frame.add(Box.createHorizontalStrut(10));
    frame.add(this.sAddressLabel);
    frame.add(this.sAddressField);
    frame.add(Box.createHorizontalStrut(10));
    frame.add(this.addSButton);
    frame.add(this.back);
    frame.setVisible(true);
    frame.paint(frame.getGraphics());
    frame.toFront();
    frame.requestFocus();
}
private void addSupplier(String name, String address) {
	Supplier supplier;
	supplier = warehouse.addSupplier(name, address);
  }

public void showSupplierList(){
	JPanel textbox = new JPanel();
	JPanel backbutton = new JPanel();
	frame = WarehouseContext.instance().getFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    textArea = new JTextArea(15, 30);
    textbox.add(new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
    back = new JButton("Back");
    back.addActionListener(this);
    backbutton.add(back);
    frame.add(textbox);
    frame.add(backbutton);
    
    frame.paint(frame.getGraphics());
    frame.setVisible(true); 
    frame.toFront();
    frame.requestFocus();
    this.redirectSystemStreams();
    
    
    Iterator<Supplier> suppliers = warehouse.getSuppliers();
	System.out.println("SUPPLIERS IN SYSTEM");
	System.out.println(
		String.format(SUPPLIER_FORMAT,
		"ID", "Name", "Address"
		)
	);
	while (suppliers.hasNext())
	{
		Supplier s = (Supplier) suppliers.next();
		System.out.println(
			String.format(SUPPLIER_FORMAT,
				s.getSupplierID(),
				s.getSupplierName(),
				s.getSupplierAddress()
			)
		);	
	}
  }
	
public void productSuppliedMenu() {
	clear();
	frame = WarehouseContext.instance().getFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JPanel productSuppliedPanel = new JPanel();
    BoxLayout boxlayout = new BoxLayout(productSuppliedPanel, BoxLayout.Y_AXIS);
    productSuppliedPanel.setLayout(boxlayout);
    
    
    
    PIDLabel = new JLabel("Product Id:");
    PIDField = new JTextField(10);
    addPIDButton = new JButton("Show");
    addPIDButton.addActionListener(this);
    back = new JButton("Back");
    back.addActionListener(this);
    frame.getContentPane().setLayout(new FlowLayout());
    productSuppliedPanel.add(this.PIDLabel);
    productSuppliedPanel.add(this.PIDField);
    productSuppliedPanel.add(this.addPIDButton);
    productSuppliedPanel.add(this.back);
    frame.add(productSuppliedPanel);
    frame.setVisible(true);
    frame.paint(frame.getGraphics());
    frame.toFront();
    frame.requestFocus();
}
public void showProductSuppliedProductList(String productId){//to do
		clear();
		JPanel textbox = new JPanel();
		JPanel backbutton = new JPanel();
		frame = WarehouseContext.instance().getFrame();
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    textArea = new JTextArea(15, 30);
	    textbox.add(new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
	            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
	    back = new JButton("Back");
	    back.addActionListener(this);
	    backbutton.add(back);
	    frame.add(textbox);
	    frame.add(backbutton);
	    frame.setVisible(true); 
	    frame.paint(frame.getGraphics());
	    frame.toFront();
	    frame.requestFocus();
	    this.redirectSystemStreams();
		
		Product product = warehouse.getProduct(productId);
		if (product != null)
		{
			//ProductName (id)
			//	Current Stock = 0
			//	Sale Price = $0.00
			//  Supplied By:
			//		ID - SupplierName - PurchasePrice
			System.out.println(product.getName() + "(" + product.getProductID() + ")");
			System.out.println("\tSupplied By: ");
			
			Iterator<SuppliedProduct> suppliedProducts = warehouse.getSuppliedProductsFromProduct(productId);
			
			while (suppliedProducts.hasNext())
			{
				SuppliedProduct sp = (SuppliedProduct) suppliedProducts.next();
				
				String supplierId = sp.getSupplierId();
				double purchasePrice = sp.getPurchasePrice();
				Supplier supplier = warehouse.getSupplier(supplierId);
				String supplierName = "ERROR";
				if (supplier != null)
				{
					supplierName = supplier.getSupplierName();
				}
				System.out.println("\t\t" + String.format(
						SUPPLIED_PRODUCT_FORMAT,
						supplierId,
						supplierName,
						String.format("$%-9.02f", purchasePrice)
					)
				);
			}
		}
		else
		{
			System.out.println("Error! Unable to find product with id " + productId);
		}
	}

public void supplierSuppliedProductMenu() {
	clear();
	frame = WarehouseContext.instance().getFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JPanel supplierSuppliedPanel = new JPanel();
    BoxLayout boxlayout = new BoxLayout(supplierSuppliedPanel, BoxLayout.Y_AXIS);
    supplierSuppliedPanel.setLayout(boxlayout);
    
    
    
    
    SIDLabel = new JLabel("Suplier Id:");
    SIDField = new JTextField(10);
    addSIDButton = new JButton("Show");
    addSIDButton.addActionListener(this);
    back = new JButton("Back");
    back.addActionListener(this);
    frame.getContentPane().setLayout(new FlowLayout());
    supplierSuppliedPanel.add(this.SIDLabel);
    supplierSuppliedPanel.add(this.SIDField);
    supplierSuppliedPanel.add(this.addSIDButton);
    supplierSuppliedPanel.add(this.back);
    frame.getContentPane().add(supplierSuppliedPanel);
    frame.setVisible(true);
    frame.paint(frame.getGraphics());
    frame.toFront();
    frame.requestFocus();
}
public void showSupplierSuppliedProductList(String supplierId){
		JPanel textbox = new JPanel();
		JPanel backbutton = new JPanel();
		frame = WarehouseContext.instance().getFrame();
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    textArea = new JTextArea(15, 30);
	    textbox.add(new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
	            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
	    back = new JButton("Back");
	    back.addActionListener(this);
	    backbutton.add(back);
	    frame.add(textbox);
	    frame.add(backbutton);
	    frame.setVisible(true); 
	    frame.paint(frame.getGraphics());
	    frame.toFront();
	    frame.requestFocus();
	    this.redirectSystemStreams();
		
		Supplier supplier = warehouse.getSupplier(supplierId);
		if (supplier != null)
		{
			System.out.println("\tSupplies the following products: ");
			
			Iterator<SuppliedProduct> suppliedProducts = warehouse.getSuppliedProductsFromSupplier(supplierId);
			
			while (suppliedProducts.hasNext())
			{
				SuppliedProduct sp = (SuppliedProduct) suppliedProducts.next();
				
				String productId = sp.getProductId();
				double purchasePrice = sp.getPurchasePrice();
				
				Product product = warehouse.getProduct(productId);
				String productName = "ERROR";
				if (product != null)
				{
					productName = product.getName();
				}
				System.out.println("\t" + String.format(
						SUPPLIED_PRODUCT_FORMAT,
						productId,
						productName,
						String.format("$%-9.02f", purchasePrice)
					)
				);
			}
		}
		else
		{
			System.out.println("Error! Unable to find supplier with id " + supplierId);
		}
  }

public void updateProductMenu() {
	clear();
	frame = WarehouseContext.instance().getFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JPanel updateProductMenuPanel = new JPanel();
    BoxLayout boxlayout = new BoxLayout(updateProductMenuPanel, BoxLayout.Y_AXIS);
    updateProductMenuPanel.setLayout(boxlayout);
    
    
    
    updateProductLabel = new JLabel("Show Suplier Supplied Products");
    
    updateSIDLabel = new JLabel("Suplier Id:");
    updateSIDField = new JTextField(10);
    updatePButton = new JButton("Show");
    updatePButton.addActionListener(this);
    back = new JButton("Back");
    back.addActionListener(this);
    frame.getContentPane().setLayout(new FlowLayout());
    updateProductMenuPanel.add(this.updateProductLabel);
    updateProductMenuPanel.add(this.updateSIDLabel);
    updateProductMenuPanel.add(this.updateSIDField);
    updateProductMenuPanel.add(this.updatePButton);
    updateProductMenuPanel.add(this.back);
    frame.getContentPane().add(updateProductMenuPanel);
    frame.setVisible(true);
    frame.paint(frame.getGraphics());
    frame.toFront();
    frame.requestFocus();
}
public void updateProduct(String supplierId){
		
		//text box
		JPanel textbox = new JPanel();
		JPanel buttonPanel = new JPanel();
		JPanel PIDPanel = new JPanel();
		frame = WarehouseContext.instance().getFrame();
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    textArea = new JTextArea(15, 30);
	    textbox.add(new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
	            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
	    frame.add(textbox);
	    productIDField = new JTextField(10);
	    productIDLabel = new JLabel("Product ID:");
	    renamePButton = new JButton("Rename product"); 
	    changePNameButton = new JButton("Change Product Price"); 
	    removePButton = new JButton("Remove Product"); 
	    renamePButton.addActionListener(this);
	    changePNameButton.addActionListener(this);
	    removePButton.addActionListener(this);
	    PIDPanel.add(productIDLabel);
	    PIDPanel.add(productIDField);
	    buttonPanel.add(renamePButton);
	    buttonPanel.add(changePNameButton);
	    buttonPanel.add(removePButton);
	    buttonPanel.add(removePButton);
	    frame.add(PIDPanel);
	    frame.add(buttonPanel);
	    frame.setVisible(true); 
	    frame.paint(frame.getGraphics());
	    frame.toFront();
	    frame.requestFocus();
	    this.redirectSystemStreams();
		Supplier supplier = warehouse.getSupplier(supplierId);
		if (supplier != null)
		{
			System.out.println("\tSupplies the following products: ");
			
			Iterator<SuppliedProduct> suppliedProducts = warehouse.getSuppliedProductsFromSupplier(supplierId);
			
			while (suppliedProducts.hasNext())
			{
				SuppliedProduct sp = (SuppliedProduct) suppliedProducts.next();
				
				String productId = sp.getProductId();
				double purchasePrice = sp.getPurchasePrice();
				
				Product product = warehouse.getProduct(productId);
				String productName = "ERROR";
				if (product != null)
				{
					productName = product.getName();
				}
				System.out.println("\t" + String.format(
						SUPPLIED_PRODUCT_FORMAT,
						productId,
						productName,
						String.format("$%-9.02f", purchasePrice)
					)
				);
			}
		}
		else
		{
			System.out.println("Error! Unable to find supplier with id " + supplierId);
		}
		
	
	   
}
public void renameProductMenu() {
	clear();
	frame = WarehouseContext.instance().getFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JPanel renameProductMenuPanel = new JPanel();
    BoxLayout boxlayout = new BoxLayout(renameProductMenuPanel, BoxLayout.Y_AXIS);
    renameProductMenuPanel.setLayout(boxlayout);
  
    
    renamePMLabel = new JLabel("New Name:");
    renamePMField = new JTextField(10);
    renamePMButton = new JButton("rename");
    renamePMButton.addActionListener(this);
    back = new JButton("Back");
    back.addActionListener(this);
    frame.getContentPane().setLayout(new FlowLayout());
    renameProductMenuPanel.add(this.renamePMLabel);
    renameProductMenuPanel.add(this.renamePMField);
    renameProductMenuPanel.add(this.renamePMButton);
    renameProductMenuPanel.add(this.back);
    frame.getContentPane().add(renameProductMenuPanel);
    frame.setVisible(true);
    frame.paint(frame.getGraphics());
    frame.toFront();
    frame.requestFocus();
}
public void renameProduct(String name, String id) {
	warehouse.changeProductName(id, name);
}
public void changeProductPriceMenu() {
	clear();
	frame = WarehouseContext.instance().getFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JPanel changeProductPriceMenuPanel = new JPanel();
    BoxLayout boxlayout = new BoxLayout(changeProductPriceMenuPanel, BoxLayout.Y_AXIS);
    changeProductPriceMenuPanel.setLayout(boxlayout);
    
    
    
    updateProductLabel = new JLabel("Show Suplier Supplied Products");
    
    changePPLabel = new JLabel("New Price:");
    changePPField = new JTextField(10);
    changePPButton = new JButton("Change Price");
    changePPButton.addActionListener(this);
    back = new JButton("Back");
    back.addActionListener(this);
    frame.getContentPane().setLayout(new FlowLayout());
    changeProductPriceMenuPanel.add(this.changePPLabel);
    changeProductPriceMenuPanel.add(this.changePPField);
    changeProductPriceMenuPanel.add(this.changePPButton);
    changeProductPriceMenuPanel.add(this.back);
    frame.getContentPane().add(changeProductPriceMenuPanel);
    frame.setVisible(true);
    frame.paint(frame.getGraphics());
    frame.toFront();
    frame.requestFocus();
}
public void changeProductPrice(Double price, String id) {
	warehouse.changeProductPrice(id, price);
}
public void removeProduct(String id) {
	Product product = warehouse.removeProduct(id); 
}

public void becomeClerk	(){
	(WarehouseContext.instance()).setLoginUser(WarehouseContext.IS_CLERK);
	(WarehouseContext.instance()).changeState(1);
  }


//====================================================================
// Auxilary Methods
//====================================================================  

private void updateTextArea(final String text) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
    	  textArea.append(text);
      }
    });
  }

  private void redirectSystemStreams() {
    OutputStream out = new OutputStream() {
      @Override
      public void write(int b) throws IOException {
        updateTextArea(String.valueOf((char) b));
      }

      @Override
      public void write(byte[] b, int off, int len) throws IOException {
        updateTextArea(new String(b, off, len));
      }

      @Override
      public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
      }
    };

    System.setOut(new PrintStream(out, true));
    System.setErr(new PrintStream(out, true));
  }
   public void logout(){//--------------------------
	   (WarehouseContext.instance()).changeState(3);//why this do this hmmm
   
   }
   
   public void clear() { 
	   frame.getContentPane().removeAll();
	   frame.paint(frame.getGraphics());   
	   }   
 
		  public void run() {
		      process();
		  } 
}