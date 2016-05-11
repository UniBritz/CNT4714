package bookstore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

//backend work for the window class

public class BookstoreActions{
    private ArrayList<Bookstore> Books;
    private final Invoice Invoice; 
    private boolean Read = false;
    private final float SalesTax = 0.06f;
	
    private final String FileName = "src/inventory.txt";
    private final String FileOutput = "transaction.txt";
    private final String DisplayTax = "6%";

//Invoice initialized, reads in inventory
    public BookstoreActions(){
    this.Invoice = new Invoice();
	if(!Read){
            InventoryReader();
            Read = true;
	}//if
    }//StoreAction
    
    //adds order to invoices
    public void OrderProcessing(Bookstore Bookstore,int BookQuantity, int numberofItemsInOrder){
        Order NewOrder = new Order();
	NewOrder.setBook(Bookstore);
	NewOrder.setItemQuantity(BookQuantity);
	NewOrder.setDiscount(Float.valueOf(getDisplayDiscount(BookQuantity)));
	NewOrder.setSubtotal(CalculateDiscount(Bookstore,BookQuantity));
		
	this.Invoice.addOrder(NewOrder);
	this.Invoice.setSubTotal(CalculateDiscount(Bookstore,BookQuantity));
	this.Invoice.setGrandTotal(this.Invoice.getSubTotal() + (this.Invoice.getSubTotal() * SalesTax));
	this.Invoice.setNumberOfItemsInOrder(numberofItemsInOrder);	
	}//OrderProcessing

        //find a book based on its ID number
	public Bookstore FindBook(int bookID){
            for(Bookstore book:Books){
		if(book.getID() == bookID){
                    return book;
		}//if
            }//for
		
            return new Bookstore();
	}//FindBook

    //set book info field
    public void BookInfoSetter(Bookstore Book, int BookQuantity){
    	Book.setInfo(String.valueOf(Book.getID()) +" "+Book.getName()+" "+String.valueOf(Book.getPrice() + 
            " " + String.valueOf(BookQuantity) + " " + String.valueOf(getDisplayDiscount(BookQuantity))+ 
            "% "+String.valueOf(CalculateDiscount(Book,BookQuantity))));
    }//BookInfoSetter
    
    //calculate the subtotal
    public float DisplaySubTotal(Bookstore Book, int BookQuantity){
    	return Invoice.getSubTotal() + CalculateDiscount(Book,BookQuantity);
    }//DisplaySubTotal
     
    //string that contains info on processed orders
    public String ViewOrderDisplay(){
    	String ViewOrders = "";
    	int index = 1;
    	for(Order BookOrder: Invoice.getOrders()){
            ViewOrders = ViewOrders + String.valueOf(index)+". "
                +BookOrder.getBookItems().getInfo()+"\n";
            index++;
    	}//for
        
    	return ViewOrders;
    }//ViewOrderDisplay

    //invoice writer, writes to a file
    public void InvoiceWriter(){
	FileWriter FileWriter = null;
	BufferedWriter BufferedWriter = null;
                
	try{
            FileWriter = new FileWriter(FileOutput,true);
	}//try
                
        catch (IOException e){
            System.out.println("Could not open the file for writing.");
            e.printStackTrace();
	}//catch
                
	BufferedWriter = new BufferedWriter(FileWriter);
		
	for(Order BookOrder: this.Invoice.getOrders()){
            try{
		BufferedWriter.write(this.Invoice.getTransactionStamp()+", "+BookOrder.getBookItems().getID()+", "
                    +BookOrder.getBookItems().getName()+", "+BookOrder.getBookItems().getPrice()+", "
                    +BookOrder.getItemQuantity()+", "+BookOrder.getDiscount()+", "+BookOrder.getSubtotal()+", "
                    +this.Invoice.getTimeStamp());
                        
                BufferedWriter.newLine();
                }//try
                    
                catch(IOException e){
                    System.out.println("Could not write the file.");
                    e.printStackTrace();
                }//catch
	}//for
		
        try{
            BufferedWriter.close();
            FileWriter.close();
	}//try
                
        catch(IOException e){
            e.printStackTrace();
	}//catch
    }//InvoiceWriter   
  
    //invoice displayed in a dialog message
    public String InvoiceDisplay(){
    	this.Invoice.setDate();
    	String Message = "Date: ";
    	
    	Message = Message + this.Invoice.getTimeStamp() + "\n\n";
    	Message = Message + "Number of Line items: " + this.Invoice.getNumberOfItemsInOrder() + "\n\n";
    	Message = Message + "Item # / ID / Title / Price / Qty / Disc % / Subtotal:\n\n";
    	Message = Message + ViewOrderDisplay() + "\n\n";
    	Message = Message + "Order subtotal: " + this.Invoice.getSubTotal() + "\n\n";
    	Message = Message + "Tax Rate:    " + DisplayTax + "\n\n";
    	Message = Message + "Tax Amount:    $" + (this.Invoice.getSubTotal() * SalesTax) + "\n\n";
    	Message = Message + "Order Total:    " + this.Invoice.getGrandTotal() + "\n\n";
    	Message = Message + "Thank you for shopping at The Bookstore! \n\n";
    	
    	return Message;
    }//Invoice Display

    //discount amount based on number of items ordered
    private int getDisplayDiscount(int Quantity){
    	if(Quantity < 5){
            return 0;
	}//if
        
        else if(Quantity < 10){
            return 10;
	}//elseif
        
        else if(Quantity < 15){
            return 15;
	}//elseif
        
        else if(Quantity >= 15){
            return 20;
	}//else if
        
        else 
            return 0;
    }//DisplayDiscount
	  
    //discount calculator if necessary
    private float CalculateDiscount(Bookstore Bookstore, int Quantity){
	float subTotal = Bookstore.getPrice() * (int) Quantity;
		
	if(Quantity < 5){
            return subTotal;
	}//if
        
        else if(Quantity < 10){
            return subTotal - (subTotal * 0.1f);
	}//elseif
        
        else if(Quantity < 15){
            return subTotal - (subTotal * 0.15f);
	}//elseif
        
        else if(Quantity >= 15){
            return subTotal - (subTotal * 0.2f);
	}//elseif
        
        else 
            return 0f;
    }//CalculateDiscount

    //reads inventory from a file into an arraylist
    private void InventoryReader(){
        
		//Read in the inventory to the book object
		FileReader FileReader = null;
		BufferedReader BufferedReader = null;
		Books = new ArrayList<Bookstore>();
                
		try{
                    FileReader = new FileReader(FileName);
			
		}//try
                
                catch (FileNotFoundException e){
                    JOptionPane MessageBox = new JOptionPane("Could not open file for reading.",JOptionPane.ERROR_MESSAGE);
                    MessageBox.setVisible(true);
                    e.printStackTrace();
		}//catch	
		
		try {
                    BufferedReader = new BufferedReader(FileReader);
			
                    String Line;
                    while((Line = BufferedReader.readLine()) != null){
                        String[] CurrentBook = new String[3];
			Bookstore Book = new Bookstore();
				
			CurrentBook = Line.split(",", 3);
			Book.setID(Integer.valueOf(CurrentBook[0]));
			Book.setName(CurrentBook[1]);
			Book.setPrice(Float.valueOf(CurrentBook[2]));
				
			Books.add(Book);
			}//while   
		}//try
                
                catch(IOException e){
                    e.printStackTrace();
		}//catch
    }//InventoryReader
}//BookStoreActions