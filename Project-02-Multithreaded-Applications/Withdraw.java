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

public class Withdraw extends Thread {
    private BankAccount accountName;
    private int depositAmount;
    private static Random RANDOMVALUE = new Random();
      
    public Withdraw(BankAccount name){
        accountName = name;
    }//Withdraw

     /*
    withdraws a random amount of money
    does an even number check
    yields thread for next thread to run
    */   
    
    public void run() {
        while(true){
            depositAmount = RANDOMVALUE.nextInt(50);

            if (depositAmount == 0){
                depositAmount += 2;
            }//if
            if (depositAmount % 2 != 0){
                depositAmount ++;
            }//if

            accountName.Withdraw(depositAmount, Thread.currentThread().getName());
            Thread.yield();
        }//while
    }//run
}//Withdraw