package bookstore;

//order object determines the amount of a given book a person wants

public class Order{
    
    //variables for order
    private Bookstore book;
    private int itemQuantity;
    private float discount;
    private float subTotal;

    //getters and setters for attributes in order
    public Bookstore getBookItems(){
    	return this.book;
    }//getBookItems
    
    public float getDiscount(){
    	return this.discount;
    }//getDiscount
    
    public float getSubtotal(){
    	return this.subTotal;
    }//getSubtotal
    
    public int getItemQuantity(){
    	return this.itemQuantity;
    }//getItemQuantity

    public void setItemQuantity(int itemQuantity){
    	this.itemQuantity = itemQuantity;
    }//setItemQuantity
    
    public void setBook(Bookstore book){
    	this.book = book;
    }//setBook
    
    public void setDiscount(float discount){
    	this.discount = discount;
    }//setDiscount
    
    public void setSubtotal(float subTotal){
    	this.subTotal = subTotal;
    }//setSubtotal
    
}//Order