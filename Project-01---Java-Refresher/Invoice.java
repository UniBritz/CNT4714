package bookstore;

//invoice class says how many orders or different books want to be purchased

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Invoice{
	
    private ArrayList<Order> Order;
    private int numberOfItemsInOrder = 0;
    private float subTotal = 0;
    private float grandTotal = 0;
    
    private String timeStamp;
    private String transactionStamp;
    
    //constructor sets up arraylist for orders
    public Invoice(){
    	this.Order = new ArrayList<Order>();
    }//Invoice
    
    //getters and setters for invoice parameters
    //parameters: subtotal, grandtotal, order, numberofitems
    public void setNumberOfItemsInOrder(int numberOfItemsInOrder){
    	this.numberOfItemsInOrder = numberOfItemsInOrder;
    }//setNumberOfItemsInOrder
    
    public void setSubTotal(float subTotal){
    	this.subTotal += subTotal;
    }//setSubTotal
    
    public void setGrandTotal(float grandTotal){
    	this.grandTotal = grandTotal;
    }//setGrandTotal
    
    public void setDate(){
    	Date date = new Date();
    	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss a z");
    	this.timeStamp = dateFormat.format(date);
    	dateFormat = new SimpleDateFormat("yyMMddHHmmss");
    	this.transactionStamp = dateFormat.format(date);
    }//setDate
    
    public void addOrder(Order mOrder){
    	this.Order.add(mOrder);
    }//addOrder
    
    public int getNumberOfItemsInOrder(){
    	return this.numberOfItemsInOrder;
    }//getNumberOfItemsInOrder
    
    public float getSubTotal(){
    	return this.subTotal;
    }//getSubTotal
    
    public float getGrandTotal(){
    	return this.grandTotal;
    }//getGrandTotal
    
    public ArrayList<Order> getOrders(){
    	return this.Order;
    }//getOrders
    
    public String getTimeStamp(){
    	return this.timeStamp;
    }//getTimeStamp
    
    public String getTransactionStamp(){
    	return this.transactionStamp;
    }//getTransactionStamp
    
}//Invoice
