/*
Name:               Brian Batinchok
Course:             CNT4714 - Enterprise Computing
Assignment Title:   Program Three - Two-Tier Client-Server Development MySQL/JDBC
Date:               Sunday, February 28th, 2016
*/

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;

public class UserInterface extends javax.swing.JFrame{
    //object declaration for database form interactions
    private JButton buttonClearCommand;
    private JButton buttonClearResults;
    private JButton buttonDatabaseConnection;
    private JButton buttonSQLExecution;
    private JComboBox boxForDriver;
    private JComboBox boxForDatabase;  
    private JLabel labelConnectionStatus;
    private JPanel panelContentPane;
    private JPasswordField fieldPassword;
    private JTable tableDatabaseOutput;
    private JTextField fieldUsername;
    private JTextPane jtextUserInput;

    Connection connectionDatabase;
    ResultSetMetaData resultMetaData;
    ResultSet resultSet;
    Statement statement;
    String stringDriver;
    String stringPassword;
    String stringQuery;
    String stringURL;
    String stringUsername;

    Vector<Vector<Object>> Data;

    public UserInterface() throws SQLException, ClassNotFoundException{
        buttonDatabaseConnection.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent eActionPerformed){
                //loads user input
                stringDriver = boxForDriver.getSelectedItem().toString();
                stringURL = boxForDatabase.getSelectedItem().toString();
                stringUsername = fieldUsername.getText();
                stringPassword = new String(fieldPassword.getPassword());

                //loads the string driver
                try{
                    Class.forName(stringDriver);
                    System.out.println("Driver Loaded");
                }//try
                
                catch(ClassNotFoundException eClassNotFound){
                    eClassNotFound.printStackTrace();
                }//catch

                /*
                attempts to establish a connection to the database
                --if a connection is made, the user is notified and the creation
                statement is made
                --if there is no connection then a popup error message is created
                */
                
                try{
                    connectionDatabase = DriverManager.getConnection(stringURL, 
                        stringUsername, stringPassword);
                    labelConnectionStatus.setText("Connected to " + stringDriver);
                    statement = connectionDatabase.createStatement();
                }//try
                
                catch(SQLException eSQLException){
                    eSQLException.printStackTrace();
                    JOptionPane.showMessageDialog(null, eSQLException.getMessage(),
                        "ERROR", JOptionPane.ERROR_MESSAGE);
                }//catch
            }//actionPerformed
        });//ActionListener
        
        buttonClearCommand.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                //input box message is cleared
                jtextUserInput.setText("");
            }//actionPerformed
        });//ActionListener
        
        buttonSQLExecution.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                
                /*user input is gathered and the input statement is checked
                --if there is a creation, deletion, or update, it is updated
                --if the command is an operation it is performed
                --info (metadata, names) gathered and changes the data in
                displayed table to match search
                --if the operation cannot be performed a popup message is made
                */
                
                stringQuery = jtextUserInput.getText();
 
                try{
                    //checks the input statement
                    if(stringQuery.contains("insert") || stringQuery.contains("delete") || 
                        stringQuery.contains("update") || stringQuery.contains("create") || 
                        stringQuery.contains("drop")){
                        statement.executeUpdate(stringQuery);
                    }//if
                    
                    else{
                        resultSet = statement.executeQuery(stringQuery);
                    }//else
                    
                    resultMetaData = resultSet.getMetaData();

                    Vector<String> ColumnNames = new Vector<String>();
                    int ColumnCount = resultMetaData.getColumnCount();
                    for (int i = 0; i < ColumnCount; i++){
                        ColumnNames.addElement(resultMetaData.getColumnLabel(i + 1));
                    }//for

                    Data = new Vector<Vector<Object>>();
                    while (resultSet.next()){
                        Vector<Object> Vector = new Vector<Object>();
                        for (int i = 0; i < ColumnCount; i++){
                            Vector.add(resultSet.getObject(i + 1));
                        }//for
                        Data.add(Vector);
                    }//while

                    tableDatabaseOutput.setModel(new DefaultTableModel(Data,ColumnNames));
                }//try
                
                catch (SQLException eSQLException1) {
                    eSQLException1.printStackTrace();
                    JOptionPane.showMessageDialog(null, eSQLException1.getMessage(), 
                        "ERROR", JOptionPane.ERROR_MESSAGE);
                }//catch
            }//actionPerformed
        });//ActionListener
        
        buttonClearResults.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //table is cleared
                tableDatabaseOutput.setModel(new DefaultTableModel());
            }//actionPerformed
        });//ActionListener
    }//UserInterface

    public static void main(String[] args) throws SQLException, ClassNotFoundException{
        //main runs the user interface
        JFrame frame = new JFrame("UserInterface");
        frame.setContentPane(new UserInterface().panelContentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }//main
}//UserInterface