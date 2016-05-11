/* 
 Name:              Brian Batinchok
 Course:            CNT 4714 Spring 2016
 Assignment Title:  Project 2 – Synchronized, Cooperating Threads Under Locking
 Due Date:          February 14, 2016
*/

package BankSimulation;

import java.util.Random;

/*
defines bank account and needed variables
RANDOMVALUE used as a static random number
*/
public class Deposit extends Thread {
    private BankAccount accountName;
    private int depositAmount;
    private static Random RANDOMVALUE = new Random();
    
    public Deposit(BankAccount name){
        accountName = name;
    }//Deposit

    /*
    deposits a random amount of money
    does a random number check
    sleeps thread for random time
    */
    public void run() {
        while(true){
            depositAmount = RANDOMVALUE.nextInt(200);

            if (depositAmount == 0){
                depositAmount += 2;
            }//if
            if (depositAmount % 2 != 0){
                depositAmount ++;
            }//if

            accountName.Deposit(depositAmount, Thread.currentThread().getName());

            try{
                Thread.sleep(RANDOMVALUE.nextInt(300));
            }//try
            catch(InterruptedException e){
                e.printStackTrace();
            }//catch
        }//while
    }//run
}//Deposit