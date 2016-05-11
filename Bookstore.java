package bookstore;

public class Bookstore{
    
    //variables for bookstore
    private int bookID;
    private float bookPrice;
    private String bookName;
    private String bookInfo;
    
    //constructor
    public Bookstore(){
    	setInfo("No book found with that ID.");
    }//Bookstore
    
    //attribute getters and setters
    public float getPrice(){
    	return this.bookPrice;
    }//getPrice
    
    public int getID(){
    	return this.bookID;
    }//getID
  
    public String getInfo(){
    	return this.bookInfo;
    }//getInfo
    
    public String getName(){
    	return this.bookName;
    }//getName
    
    public void setID(int bookID){
    	this.bookID = bookID;
    }//setID
    
    public void setName(String bookName){
    	this.bookName = bookName;
    }//setName
    
    public void setPrice(float bookPrice){
    	this.bookPrice = bookPrice;
    }//setPrice
    
    public void setInfo(String bookInfo){
    	this.bookInfo = bookInfo;
    }//setInfo
    
}//Bookstore