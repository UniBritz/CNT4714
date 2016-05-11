 /* 
Name: Brian Batinchok
Course: CNT 4714 â€“ Spring 2016 â€“ Project Four
Assignment Title: Developing A Three-Tier Distributed Web-Based Application
Date: March 27, 2016
*/

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.lang.Boolean;
import java.lang.ClassNotFoundException;
import java.sql.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Project4 extends HttpServlet{
    //initialize variables
    static final String DATABASE = "jdbc:mysql://localhost:3306/project4";
    Boolean connect = false;
    Boolean driverLoaded = false;
    Boolean start = false;
    Connection connection;
    int counter = 0;
    int numberEffected;
    List listSuppliers = new ArrayList();
    ResultSet setResults;
    ResultSetMetaData metaData;
    Statement SQLStatement;
    String stringError;
    String stringQuery;
    String stringSQL;

    //runs on page start and tries to open a connection
    public void init(ServletConfig config) throws ServletException{
        start = true;
        
        try{
            Class.forName("com.mysql.jdbc.Driver");
            driverLoaded = true;
        }//try
        
        catch(ClassNotFoundException e){
            stringError = e.getMessage();
        }//catch
        
        try{
            connection = DriverManager.getConnection(DATABASE, "CNT4714", "CNT4714");
            SQLStatement = connection.createStatement();
            connect = true;
        }//try
        
        catch(SQLException e1){
            e1.printStackTrace();
        }//catch
    }//init
    
    //runs on page close and closes any connections
    public void closeConnection(){
        try{
            connection.close();
        }//try
        
        catch(SQLException e1){
            e1.printStackTrace();
        }//catch
    }//connectionClose


    protected void doPost(HttpServletRequest request, 
        HttpServletResponse response) throws ServletException, IOException{
        
        //pulls user input or runs a default comment with no user input
        stringQuery = request.getParameter("text");

        if(stringQuery == ""){
            stringQuery = "select * from suppliers";
        }//if
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        //prints the html document, organized by header/body/form
        
        //html document start
        out.println("<!DOCTYPE html>" );
        out.println("<html lang=\"en\">");

        //print header
        out.println( "<head>" );
        out.println( "<title>Project #4</title>" );
        out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\">");
        out.println( "</head>" );

        //print body
        out.println( "<body>" );
        out.println( "<h1>Project 4 Remote Database Management System </h1>");
        out.println("<hr>");
        out.println("<p>Connected to the Project4 database.</p>");
        out.println("<p>Enter your valid SQL query or update statement.</p>");
        out.println("<p>If no query/update command is given the Execute button "
                + "will display all supplier information in the database.</p>");
        out.println("<p>All Execution results below.</p>");

        //print form
        out.println("<form action=\"/Project4/Project4\" method=\"post\">");
        out.println("<textarea name=\"text\" rows=\"5\" cols=\"80\" id=\"text\">select * from suppliers;</textarea><br>");
        out.println("<input type = \"submit\" name = \"submit\" value = \"Submit\" />");
        out.println("<button type = \"submit\" formmethod = \"get\" formaction=\"/Project4/Project4\">Clear</button>");
        out.println("</form>");
        out.println("<hr>");

        out.println("<h2>Database Results:</h2>");
        out.println("<table>");

        /*
        checks the kind of SQL statement and tries to run logic
            -checks affected rows, finds modifications, removes duplicates
            -counts number of affected changes, prints changes
        */
        try{
            if (stringQuery.contains("insert") || stringQuery.contains("delete") 
                || stringQuery.contains("update") || stringQuery.contains("create") 
                || stringQuery.contains("drop")){
                try{
                    out.println("<tr>");

                    numberEffected = SQLStatement.executeUpdate(stringQuery);
                    out.println("<th> The statement executed successfully. <br>" + 
                            numberEffected + " row(s) effected.</th>");
                    out.println("</tr>");
                    out.println("<tr><td>Business Logic Detected! - Updating Supplier "
                            + "Status</td></tr>");

                    setResults = SQLStatement.executeQuery("select snum from "
                            + "shipments\n" + "where quantity >= 100 ");

                    int i = 0;
                    while (setResults.next()){
                        if(!listSuppliers.contains(setResults.getObject(1))){
                            listSuppliers.add(setResults.getObject(1));
                        }//if
                    }//while
                    
                     counter = 0;
                     for(i = 0; i < listSuppliers.size(); i++){
                      SQLStatement.executeUpdate("update suppliers\n" + "set "
                              + "status = status + 5\n" + "where snum = \'" + 
                              listSuppliers.get(i) + "\'");
                     counter++;
                     }//for

                     out.println("<tr>");
                     out.println("<td>Business Logic updated " + counter + 
                             " supplier status marks.</td>");
                     out.println("</tr>");
                }//try//try
                
                //prints SQL string errors to the user
                catch(SQLException e1){
                    out.println("<tr>");
                    out.println("<th>Statement failed to execute <br>" 
                            + e1.getMessage() + "</th>");
                    out.println("</tr>");
                }//catch
            }//if
            
            //pulls metadata table structure, and table data
            else{
                setResults = SQLStatement.executeQuery(stringQuery);
                metaData = setResults.getMetaData();
                out.println("<tr>");
                int columnCounter = metaData.getColumnCount();
                
                for(int i = 0; i < columnCounter; i++){
                    out.println("<th>" + metaData.getColumnName(i + 1));
                }//for

                out.println("</tr>");

                while (setResults.next()){
                    out.println("<tr>");
                    for (int i = 0; i < columnCounter; i++)
                        out.println("<td>" + setResults.getString(i + 1) + "</td>");
                    out.println("</tr>");
                }//while
            }//else
        }//try//try
        
        //prints errors to the user
        catch(SQLException e1){
            out.println("<th> Error in the SQL statement:<br>" + e1.getMessage() + "</th>");
        }//catch

        out.println("</table>");
        out.println( "</body>" );
        out.println( "</html>" );
        out.close();
    }//doPost

//This is called if the clear button is clicked. All it does is repeat the origional HTML.
    protected void doGet (HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException{
        
        stringQuery = request.getParameter("text");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        //html document start
        out.println("<!DOCTYPE html>" );
        out.println("<html lang=\"en\">");

        //print header
        out.println("<head>");
        out.println("<title>Project #4</title>");
        out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\">");
        out.println("</head>");

        //print body
        out.println("<body>");
        out.println("<h1>Project 4 Remote Database Management System </h1>");
        out.println("<hr>");
        out.println("<p>Connected to the Project4 database.</p>");
        out.println("<p>Enter your valid SQL query or update statement.</p>");
        out.println("<p>If no query/update command is given the Execute button "
                + "will display all supplier information in the database.</p>");
        out.println("<p>All Execution results below.</p>");

        //print form
        out.println("<form action=\"/Project4/Project4\" method=\"post\">");
        out.println("<textarea name=\"text\" rows=\"5\" cols=\"80\" id=\"text\">select * from suppliers;</textarea><br>");
        out.println("<input type = \"submit\" name = \"submit\" value = \"Submit\" />");
        out.println("<button type = \"submit\" formmethod = \"get\" formaction=\"/Project4/Project4\">Clear</button>");
        out.println("</form>");
        out.println("<hr>");

        out.println( "</body>" );
        out.println( "</html>" );
        out.close();
    }//doGet
}//Project4