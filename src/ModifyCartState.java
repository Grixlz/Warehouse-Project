import java.util.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
import java.text.*;
import java.awt.*;
import java.awt.event.*;

public class ModifyCartState extends WarehouseState  implements ActionListener{
    private static ModifyCartState instance;

    private final String LINE_ITEM_FORMAT = "%-4s   %-4s | %-20s | %-10s | %-10s | %-10s";

    //GUI
    private JFrame frame;
    private AbstractButton showCartButton, changeCartButton, addCartButton, removeCartButton, Logout, back, addToCart, continueProcessing, removeFromCart;
    static JFrame clientMenu;
    static JLabel clientMenuLabel, updateCartLabel, itemNumberLabel, quantityLabel, ProductIdLabel, addItemPIDLabel, addItemQuantLabel, removeItemPIDLabel;
    static JTextField productIdField, quantityField, addItemPIDField, addItemQuantField,removeItemPIDField;
    private JTextArea textArea;
    
    
    
    
	private ModifyCartState()
	{
        super();
	}

    public static ModifyCartState instance() {
      if (instance == null) {
        instance = new ModifyCartState();
      }
      return instance;
    }
//====================================================================
// GUI
//====================================================================
	public void process() {
		frame = WarehouseContext.instance().getFrame();
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    JPanel cartPane = new JPanel();
	    BoxLayout boxlayout = new BoxLayout(cartPane, BoxLayout.Y_AXIS);
	    cartPane.setLayout(boxlayout);
	    
	    //button and listeners
	    showCartButton = new JButton("Show Cart"); 
	    changeCartButton = new JButton("Change Quantity of Product in Cart"); 
	    addCartButton = new JButton("Add to Cart"); 
	    removeCartButton = new JButton("Remove an Product from the Cart");  
	    Logout  = new JButton("Logout"); 
	    showCartButton.addActionListener(this);
	    changeCartButton.addActionListener(this);
	    addCartButton.addActionListener(this);
	    removeCartButton.addActionListener(this);
	    Logout.addActionListener(this);
	    
	    //label
	    clientMenuLabel = new JLabel("Modify Cart");
	    clientMenuLabel.setFont(new Font("Helvetica", Font.PLAIN, 20));
	    
	    //center align
	    clientMenuLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	    showCartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
	    changeCartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
	    addCartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
	    removeCartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
	    Logout.setAlignmentX(Component.CENTER_ALIGNMENT);
	    
	    //adding to pane
	    cartPane.add(clientMenuLabel);
	    cartPane.add(this.showCartButton);
	    cartPane.add(this.changeCartButton);
	    cartPane.add(this.addCartButton);
	    cartPane.add(this.removeCartButton);
	    cartPane.add(this.Logout);
	    frame.getContentPane().add(cartPane);
	    frame.setVisible(true);
	    frame.paint(frame.getGraphics()); 
	    frame.toFront();
	    frame.requestFocus();
  }

	public void actionPerformed(ActionEvent event) {
		if (event.getSource().equals(this.showCartButton)){
			this.showCart();
		}
		else if(event.getSource().equals(this.changeCartButton)){
			this.changeCart();
		}
		else if(event.getSource().equals(this.addCartButton)){
			this.addToCartMenu();
		}
		else if(event.getSource().equals(this.removeCartButton)){
			this.removeCartMenu();
		}
		else if(event.getSource().equals(this.continueProcessing)){
			String id = productIdField.getText();
			int quant = Integer.parseInt(quantityField.getText()); 
			this.updateItemInCart(id,quant);
			clear();
			this.process();
		}
		else if(event.getSource().equals(this.addToCart)){
			String id = addItemPIDField.getText();
			int quant = Integer.parseInt(addItemQuantField.getText()); 
			this.addCart(id, quant);
			clear();
			this.process();
		}
		else if(event.getSource().equals(this.removeFromCart)){
			String id = removeItemPIDField.getText();
			this.removeCart(id);
			clear();
			this.process();
		}
		else if(event.getSource().equals(this.back)){
			clear();
			this.process();
		}
		else {
			clear();
			this.logout();
			
		}
	}
//====================================================================
// Modify Cart Methods 
//====================================================================
    private void showCart(){
    	clear();
    	JPanel textbox = new JPanel();
    	JPanel backbutton = new JPanel();
    	frame = WarehouseContext.instance().getFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        textArea = new JTextArea(15, 32);
        textbox.add(new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS));
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
        Warehouse warehouse = Warehouse.getInstance();
        String clientId = WarehouseContext.instance().getCurrentUser();
		
		Iterator<LineItem> cartItems = warehouse.getClientCart(clientId);
		
		if (cartItems == null)
		{
			System.out.println("Client " + clientId + " was not found!");
			return;
		}
		
		System.out.println("SHOPPING CART FOR CLIENT " + clientId);
		System.out.println(
			String.format(LINE_ITEM_FORMAT,
				"Num", "ID", "Name", "Quantity", "Cost", "Total Cost"
			)
		);
		
		int index = 1;
		//double totalCost = 0.0;
		while (cartItems.hasNext())
		{
			LineItem item = (LineItem) cartItems.next();
			Product product = warehouse.getProduct(item.getProductId());
			
			if (product != null)
			{
				double productCost = product.getBuyPrice();
				double lineItemCost = productCost * (double) item.getQuantity();
				
				System.out.println(
					String.format(LINE_ITEM_FORMAT,
						String.format("%3d.", index),
						product.getProductID(),
						product.getName(),
						"x" + item.getQuantity(),
						String.format("$%-9.02f", productCost),
						String.format("$%-9.02f", lineItemCost)
					)
				);
			}
			
			
			//totalCost += lineItemCost;
			index++;
		}
		
		if (index == 1)
		{
			System.out.println("Cart is empty.");
		}
    }

    public void changeCart(){
    	clear();
    	frame = WarehouseContext.instance().getFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel updateCartPanel = new JPanel();
        JPanel updatePICartPanel = new JPanel();
        BoxLayout boxlayout = new BoxLayout(updateCartPanel, BoxLayout.Y_AXIS);
        updateCartPanel.setLayout(boxlayout);
	    continueProcessing = new JButton("Change");
	    continueProcessing.addActionListener(this);
	    productIdField = new JTextField(10);
	    frame.getContentPane().setLayout(new FlowLayout());
	    quantityField = new JTextField(10);
	    quantityLabel = new JLabel("Quantity:");
	    ProductIdLabel = new JLabel("Product ID:");
	    back = new JButton("Back");
	    back.addActionListener(this);
	    frame.add(this.ProductIdLabel);
	    frame.add(this.productIdField);
	    frame.add(Box.createHorizontalStrut(10));
	    frame.add(this.quantityLabel);
	    frame.add(this.quantityField);
	    frame.add(Box.createHorizontalStrut(10));
	    frame.add(this.continueProcessing);
	    frame.add(this.back);
	    frame.setVisible(true);
	    frame.paint(frame.getGraphics());
	    frame.toFront();
	    frame.requestFocus();
	    
    }
    
    public void updateItemInCart(String pid, int quantity) {
        Warehouse warehouse = Warehouse.getInstance();
        String clientId = WarehouseContext.instance().getCurrentUser();
		boolean itemInCart = warehouse.isProductInCart(clientId, pid);
		if (itemInCart == true)
		{
			boolean result = warehouse.updateProductInCart(clientId, pid, quantity);
		}
    }

    public void addToCartMenu() {
    	clear();
    	frame = WarehouseContext.instance().getFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel updateCartPanel = new JPanel();
        BoxLayout boxlayout = new BoxLayout(updateCartPanel, BoxLayout.Y_AXIS);
        updateCartPanel.setLayout(boxlayout);
        
        addItemPIDLabel = new JLabel("Poduct Id of item you want to add.");
        addItemQuantLabel = new JLabel("Quantity of item you want to add.");
        addToCart = new JButton("Add to Cart");
        addItemPIDField = new JTextField(10);
        addItemQuantField = new JTextField(10);
	    frame.getContentPane().setLayout(new FlowLayout());
	    back = new JButton("Back");
	    back.addActionListener(this);
	    frame.add(this.addItemPIDLabel);
	    frame.add(this.addItemPIDField);
	    frame.add(this.addItemQuantLabel);
	    frame.add(this.addItemQuantField);
	    frame.add(this.addToCart);
	    frame.add(this.back);
	    frame.setVisible(true);
	    frame.paint(frame.getGraphics());
	    frame.toFront();
	    frame.requestFocus();
	    
	    addToCart.addActionListener(this);
    }
    
    public void addCart(String productId, int quantity){
        Warehouse warehouse = Warehouse.getInstance();
        String clientId = WarehouseContext.instance().getCurrentUser();
		boolean result = warehouse.addToCart(clientId, productId, quantity);
    }

    public void removeCartMenu() {
    	clear();
    	frame = WarehouseContext.instance().getFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel updateCartPanel = new JPanel();
        BoxLayout boxlayout = new BoxLayout(updateCartPanel, BoxLayout.Y_AXIS);
        updateCartPanel.setLayout(boxlayout);
        
        removeItemPIDLabel = new JLabel("Poduct Id:");
        removeFromCart = new JButton("Remove from cart");
        removeItemPIDField = new JTextField(10);
        back = new JButton("Back");
	    back.addActionListener(this);
	    updateCartPanel.add(this.removeItemPIDLabel);
	    updateCartPanel.add(this.removeItemPIDField);
	    frame.add(updateCartPanel);
	    frame.add(this.removeFromCart);
	    frame.add(this.back);
	    frame.setVisible(true);
	    frame.paint(frame.getGraphics());
	    frame.toFront();
	    frame.requestFocus();
	    
	    removeFromCart.addActionListener(this);
    }
    
    public void removeCart(String productId){
        Warehouse warehouse = Warehouse.getInstance();
        String clientId = WarehouseContext.instance().getCurrentUser();
		boolean result = warehouse.removeFromCart(clientId, productId);
    }

    public void logout(){
        (WarehouseContext.instance()).changeState(0);
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

	public void clear() { 
	    frame.getContentPane().removeAll();
	    frame.paint(frame.getGraphics());   
	  }  
    
    public void run() {
        process();
    }
}