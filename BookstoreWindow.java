/*
Name:               Brian Batinchok
Course:             CNT4714 - Enterprise Computing
Assignment Title:   Program One - Event-Driven Programming
Date:               Sunday, January 24th, 2016

This program replicates an order form for a bookstore.  The inventory is
populated from a textfile and users can place orders using the form generated
by the program.  Orders can be processed and transactions are recorded to a
text output file.
*/

package bookstore;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

//this class defines the interface that the person interacts with

public class BookstoreWindow{
    
    //initial layout objects
    private static Bookstore bookstore;
    private static int currentOrderNumber;
    private static JFrame storeWindow;
    private static JPanel bookstorePanel;
    private static SpringLayout bookstoreLayout;
    private static BookstoreActions storeAction;
	
    //interface labels for text areas
    private static JLabel numberOfItemsLabel;
    private static JLabel bookIDLabel;
    private static JLabel bookQuantityLabel;
    private static JLabel bookInfoLabel;
    private static JLabel subTotalLabel;
    
    //interface text areas
    private static JTextField numberOfItemsField;
    private static JTextField bookIDField;
    private static JTextField bookQuantityField;
    private static JTextField bookInfoField;
    private static JTextField subTotalField;
	
    //buttons on the interface
    private static JButton processItemButton;
    private static JButton confirmItemButton;
    private static JButton viewOrderButton;
    private static JButton finishOrderButton;
    private static JButton newOrderButton;
    private static JButton exitButton;
    
    //initialize jframe and the storeaction
    public static void main(String[] args) {
	new BookstoreWindow().StartThread();
	currentOrderNumber = 1;
        InitWindow();
    }//main
   
	/**
	 * Sets many of the base attributes of the frame/panel and calls other methods to populate it.
	 */
    
    //call methods (panel,jframe, buttons) to populate main jframe
    private static void InitWindow(){
        storeWindow = new JFrame("The Bookstore");
        bookstorePanel  = new JPanel();
        bookstorePanel.setSize(700, 200);
        storeWindow.setSize(700, 200);
        
        InitButtons();    
        InitLabels();
        InitText();
        InitPlacement();
        
        storeWindow.add(bookstorePanel);
        storeWindow.setVisible(true);
    }//InitWindow
      
    //initiate labels to add to the panel
    private static void InitLabels(){
    	numberOfItemsLabel = new JLabel("Number of items in this order:");
    	bookIDLabel = new JLabel("Book ID for item #"+String.valueOf(currentOrderNumber)+":");
    	bookQuantityLabel = new JLabel("Quantity of item #"+String.valueOf(currentOrderNumber)+":");
    	bookInfoLabel = new JLabel("Item #"+String.valueOf(currentOrderNumber)+" info:");
    	subTotalLabel = new JLabel("Order subtotal for "+String.valueOf(currentOrderNumber-1)+" item(s):");
    	
        bookstorePanel.add(numberOfItemsLabel);
        bookstorePanel.add(bookIDLabel);
        bookstorePanel.add(bookQuantityLabel);
        bookstorePanel.add(bookInfoLabel);
        bookstorePanel.add(subTotalLabel);
    }//InitLabels
      
    //initialize text fields and uses listeners
    private static void InitText(){
    	numberOfItemsField = new JTextField(13);
    	bookIDField = new JTextField(13);
    	bookQuantityField = new JTextField(13);
    	bookInfoField = new JTextField(40);
    	subTotalField = new JTextField(13);
    	
    	bookIDField.addKeyListener(new KeyListener(){
            @Override
            public void keyPressed(KeyEvent arg0){
            }//keyPressed

            @Override
            public void keyReleased(KeyEvent arg0){
            }//keyReleased

            @Override
            public void keyTyped(KeyEvent arg0){
		if((arg0.getKeyCode() == KeyEvent.VK_ENTER) && !bookIDField.getText().isEmpty()){
                    bookstore = storeAction.FindBook(Integer.valueOf(bookIDField.getText()));
                    
                    if(!bookQuantityField.getText().isEmpty())
                        storeAction.BookInfoSetter(bookstore, Integer.valueOf(bookQuantityField.getText()));
                
                    else
                        storeAction.BookInfoSetter(bookstore, 1);
                
                    AutoComplete(bookstore);
                }//if  		
            }//keyTyped
        });//KeyListener
        
    	bookIDField.addFocusListener(new FocusListener(){
            @Override
            public void focusGained(FocusEvent e){
            }//focusGained

            @Override
            public void focusLost(FocusEvent e){
		if(!bookIDField.getText().isEmpty()){
                    bookstore = storeAction.FindBook(Integer.valueOf(bookIDField.getText()));
					
                    if(!bookQuantityField.getText().isEmpty())
                        storeAction.BookInfoSetter(bookstore, Integer.valueOf(bookQuantityField.getText()));
                    
                    else
                        storeAction.BookInfoSetter(bookstore, 1);
					
                    AutoComplete(bookstore);
		}//if
            }//focusLost
    	});//FocusListener
        
    	bookQuantityField.addFocusListener(new FocusListener(){
            @Override
            public void focusGained(FocusEvent arg0){
            }//focusGained

            @Override
            public void focusLost(FocusEvent arg0){
		if(!bookQuantityField.getText().isEmpty() && !bookIDField.getText().isEmpty()){
                    bookstore = storeAction.FindBook(Integer.valueOf(bookIDField.getText()));
                    storeAction.BookInfoSetter(bookstore, Integer.valueOf(bookQuantityField.getText()));			
                    AutoComplete(bookstore);
		}//if		
            }//focusLost
        });//FocusListener
        
    	bookQuantityField.addKeyListener(new KeyListener(){
            @Override
            public void keyPressed(KeyEvent arg0) {	
            }//keyPressed

            @Override
            public void keyReleased(KeyEvent arg0) {
		if(Character.isDigit(arg0.getKeyChar())){
                    bookstore = storeAction.FindBook(Integer.valueOf(bookIDField.getText()));
                    storeAction.BookInfoSetter(bookstore, Integer.valueOf(bookQuantityField.getText()));		
                    AutoComplete(bookstore);
		}//if
            }//keyReleased

            @Override
            public void keyTyped(KeyEvent arg0) {
            }//keyTyped
        });//KeyListener
    	
    	bookInfoField.setEnabled(false);
    	subTotalField.setEditable(false);
    	
        bookstorePanel.add(numberOfItemsField);
        bookstorePanel.add(bookIDField);
        bookstorePanel.add(bookQuantityField);
        bookstorePanel.add(bookInfoField);
        bookstorePanel.add(subTotalField);
    }//InitText
    
    //initialize buttons with listeners
    private static void InitButtons(){
    	processItemButton = new JButton("Process Item #"+String.valueOf(currentOrderNumber)+"");
    	confirmItemButton = new JButton("Confirm Item #"+String.valueOf(currentOrderNumber)+"");
    	viewOrderButton = new JButton("View Order");
    	finishOrderButton = new JButton("Finish Order");
    	newOrderButton = new JButton("New Order");
    	exitButton = new JButton("Exit");
    	
    	processItemButton.setEnabled(false);
    	
    	processItemButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent arg0){
		storeAction.OrderProcessing(bookstore, Integer.valueOf(bookQuantityField.getText()), Integer.valueOf(numberOfItemsField.getText()));
		if(currentOrderNumber < Integer.valueOf(numberOfItemsField.getText())){
                    numberOfItemsField.setEditable(false);
                    numberOfItemsField.setEditable(false);
                    confirmItemButton.setEnabled(true);
                    processItemButton.setEnabled(false);
					
                    bookIDField.setText("");
                    bookQuantityField.setText("");
                    currentOrderNumber++;
                    LabelRedraw();
		}//if
                
                else{
                    confirmItemButton.setEnabled(false);
                    processItemButton.setEnabled(false);
		}//else
            }//actionPerformed
        });//ActionListener
        
        confirmItemButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent arg0){
                JOptionPane.showMessageDialog(bookstorePanel,"Item #"+String.valueOf(currentOrderNumber)+" accepted");
                numberOfItemsField.setEditable(false);
                processItemButton.setEnabled(true);
                confirmItemButton.setEnabled(false);
            }//actionPerformed
        });//ActionListener
        
    	viewOrderButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent arg0){
		JOptionPane.showMessageDialog(bookstorePanel, storeAction.ViewOrderDisplay());
            }//actionPerformed
        });//ActionListener
        
    	finishOrderButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent arg0){
                JOptionPane.showMessageDialog(bookstorePanel, storeAction.InvoiceDisplay());
		storeAction.InvoiceWriter();
		ClickNewOrder();
            }//actionPerformed
        });//ActionListener
        
    	newOrderButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent arg0){
		ClickNewOrder();
            }//actionPerformed
        });//ActionListener
        
    	exitButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent arg0){
		storeWindow.dispose();
            }//actionPerformed
        });//ActionListener
    	
    	bookstorePanel.add(processItemButton);
        bookstorePanel.add(confirmItemButton);
        bookstorePanel.add(viewOrderButton);
        bookstorePanel.add(finishOrderButton);
        bookstorePanel.add(newOrderButton);
        bookstorePanel.add(exitButton);
    }//InitButtons
	
    //use spring layout for object positioning
    private static void InitPlacement(){
    	bookstoreLayout = new SpringLayout();
    	bookstoreLayout.putConstraint(SpringLayout.NORTH,numberOfItemsLabel, 5, SpringLayout.NORTH,bookstorePanel);
    	bookstoreLayout.putConstraint(SpringLayout.NORTH,numberOfItemsField, 5, SpringLayout.NORTH,bookstorePanel);
    	bookstoreLayout.putConstraint(SpringLayout.WEST, numberOfItemsLabel, 5, SpringLayout.WEST, bookstorePanel);
    	bookstoreLayout.putConstraint(SpringLayout.WEST, numberOfItemsField, 5, SpringLayout.EAST, numberOfItemsLabel);
    	
    	bookstoreLayout.putConstraint(SpringLayout.NORTH,bookIDLabel, 7, SpringLayout.SOUTH,numberOfItemsLabel);
    	bookstoreLayout.putConstraint(SpringLayout.WEST, bookIDLabel, 5, SpringLayout.WEST, bookstorePanel);
    	bookstoreLayout.putConstraint(SpringLayout.NORTH, bookIDField, 5, SpringLayout.SOUTH, numberOfItemsField);
    	bookstoreLayout.putConstraint(SpringLayout.WEST, bookIDField, 5, SpringLayout.EAST, bookIDLabel);
    	
    	bookstoreLayout.putConstraint(SpringLayout.NORTH,bookQuantityLabel, 10, SpringLayout.SOUTH,bookIDLabel);
    	bookstoreLayout.putConstraint(SpringLayout.WEST, bookQuantityLabel, 5, SpringLayout.WEST, bookstorePanel);
    	bookstoreLayout.putConstraint(SpringLayout.NORTH, bookQuantityField, 5, SpringLayout.SOUTH, bookIDField);
    	bookstoreLayout.putConstraint(SpringLayout.WEST, bookQuantityField, 5, SpringLayout.EAST, bookQuantityLabel);
    	
    	bookstoreLayout.putConstraint(SpringLayout.NORTH,bookInfoLabel, 10, SpringLayout.SOUTH,bookQuantityLabel);
    	bookstoreLayout.putConstraint(SpringLayout.WEST, bookInfoLabel, 5, SpringLayout.WEST, bookstorePanel);
    	bookstoreLayout.putConstraint(SpringLayout.NORTH, bookInfoField, 5, SpringLayout.SOUTH, bookQuantityField);
    	bookstoreLayout.putConstraint(SpringLayout.WEST, bookInfoField, 5, SpringLayout.EAST, bookInfoLabel);
    	
    	bookstoreLayout.putConstraint(SpringLayout.NORTH,subTotalLabel, 10, SpringLayout.SOUTH,bookInfoLabel);
    	bookstoreLayout.putConstraint(SpringLayout.WEST, subTotalLabel, 5, SpringLayout.WEST, bookstorePanel);
    	bookstoreLayout.putConstraint(SpringLayout.NORTH, subTotalField, 5, SpringLayout.SOUTH, bookInfoField);
    	bookstoreLayout.putConstraint(SpringLayout.WEST, subTotalField, 5, SpringLayout.EAST, subTotalLabel);
    	
    	bookstoreLayout.putConstraint(SpringLayout.NORTH, processItemButton, 10, SpringLayout.SOUTH, subTotalLabel);
    	bookstoreLayout.putConstraint(SpringLayout.NORTH, confirmItemButton, 10, SpringLayout.SOUTH, subTotalLabel);
    	bookstoreLayout.putConstraint(SpringLayout.NORTH, viewOrderButton, 10, SpringLayout.SOUTH, subTotalLabel);
    	bookstoreLayout.putConstraint(SpringLayout.NORTH, finishOrderButton, 10, SpringLayout.SOUTH, subTotalLabel);
    	bookstoreLayout.putConstraint(SpringLayout.NORTH, newOrderButton, 10, SpringLayout.SOUTH, subTotalLabel);
    	bookstoreLayout.putConstraint(SpringLayout.NORTH, exitButton, 10, SpringLayout.SOUTH, subTotalLabel);
    	
    	bookstoreLayout.putConstraint(SpringLayout.WEST, processItemButton, 10, SpringLayout.WEST, bookstorePanel);
    	bookstoreLayout.putConstraint(SpringLayout.WEST, confirmItemButton, 10, SpringLayout.EAST, processItemButton);
    	bookstoreLayout.putConstraint(SpringLayout.WEST, viewOrderButton, 10, SpringLayout.EAST, confirmItemButton);
    	bookstoreLayout.putConstraint(SpringLayout.WEST, finishOrderButton, 10, SpringLayout.EAST, viewOrderButton);
    	bookstoreLayout.putConstraint(SpringLayout.WEST, newOrderButton, 10, SpringLayout.EAST, finishOrderButton);
    	bookstoreLayout.putConstraint(SpringLayout.WEST, exitButton, 10, SpringLayout.EAST, newOrderButton);
    	
    	bookstorePanel.setLayout(bookstoreLayout);
    }//InitPlacement
	
    //fills in information for info, subtotal, and quantity
    private static void AutoComplete(Bookstore Bookstore){
    	bookInfoField.setText(Bookstore.getInfo());
        
    	if(!bookQuantityField.getText().isEmpty())
    		subTotalField.setText(String.valueOf(storeAction.DisplaySubTotal(Bookstore, Integer.valueOf(bookQuantityField.getText()))));
    	else{
    		bookQuantityField.setText("1");
    		subTotalField.setText(String.valueOf(storeAction.DisplaySubTotal(Bookstore,1)));
    	}//else
    }//AutoComplete

    //update item numbers of labels based on currentordernumber
    private static void LabelRedraw(){
    	processItemButton.setText("Process Item #"+String.valueOf(currentOrderNumber)+"");
    	confirmItemButton.setText("Confirm Item #"+String.valueOf(currentOrderNumber)+"");
    	bookIDLabel.setText("Enter Book ID for item #"+String.valueOf(currentOrderNumber)+":");
    	bookQuantityLabel.setText("Enter quantity for item #"+String.valueOf(currentOrderNumber)+":");
    	bookInfoLabel.setText("Item #"+String.valueOf(currentOrderNumber)+" info:");
    	subTotalLabel.setText("Order subtotal for "+String.valueOf(currentOrderNumber-1)+" item(s):");
    }//LabelRedraw
    
    //clears fields, redraw labels, new storeaction object
    private static void ClickNewOrder(){
	new BookstoreWindow().StartThread();
    	currentOrderNumber = 1;	
    	bookIDField.setText("");
	bookQuantityField.setText("");
	bookInfoField.setText("");
    	subTotalField.setText("");
	numberOfItemsField.setText("");
		
	LabelRedraw();
		
	numberOfItemsField.setEditable(true);
	confirmItemButton.setEnabled(true);
	processItemButton.setEnabled(false);
    }//ClickNewOrder
  
    //storeaction in its own thread
    class NewThread extends Thread{
	@Override
	public void run(){
            storeAction = new BookstoreActions();
	}//run
    }//Thread
	
    private void StartThread(){
	new NewThread().start();
    }//StartThread
}//BookstoreWindow