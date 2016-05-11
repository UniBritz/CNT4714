/* 
 Name:              Brian Batinchok
 Course:            CNT 4714 Spring 2016
 Assignment Title:  Project 2 – Synchronized, Cooperating Threads Under Locking
 Due Date:          February 14, 2016
*/

package BankSimulation;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Bank implements BankAccount{
    private Lock threadLock = new ReentrantLock();
    private Condition depositPossible = threadLock.newCondition();
    private Condition withdrawPossible = threadLock.newCondition();

    int balanceTotal = 0;

    /*
    uses a lock to prevent other threads from running
    performs a balance check before a withdraw
    unlocks withdraw thread upon completion
    */
    public void Withdraw(int withdrawAmount, String thread){
        threadLock.lock();

        if (balanceTotal >= withdrawAmount){
            balanceTotal -= withdrawAmount;
            System.out.printf("\t\t\t\t\t%s withdraws $%s\t\t$%d\n", thread, 
                withdrawAmount, balanceTotal);
        }//if
        else{
            System.out.println("\t\t\t\t\t" + thread + " withdraws $" 
                + withdrawAmount + " - BLOCKED - Insufficient Funds");

            try{
                depositPossible.signalAll();
                withdrawPossible.await();
            }//try            
            catch (InterruptedException e){
                e.printStackTrace();
            }//catch
        }//else
        threadLock.unlock();
    }//Withdraw

    /*
    uses a lock to prevent other threads from running
    adds deposit to the total balance
    */
    public void Deposit(int depositAmount, String thread){
        threadLock.lock();

        balanceTotal +=depositAmount;
        System.out.printf("%s deposits $%s\t\t\t\t\t\t\t$%d\n", thread, 
            depositAmount, balanceTotal);

        try {
            withdrawPossible.signalAll();
            depositPossible.await();
        }//try       
        catch (InterruptedException e) {
            e.printStackTrace();
        }//catch
        threadLock.unlock();
    }//Deposit

    /*
    defines the deposit and withdraw threads
    starts the action of the objects to run
    */
    public static void main (String[] args){
        System.out.println("Deposit Threads \t Withdrawal Threads \t Balance");
        System.out.println("--------------- \t ------------------ \t -----------");
        Bank BankAction = new Bank();
        
        Deposit depositThread1 = new Deposit(BankAction);
        depositThread1.setName("Thread 1");
        Deposit depositThread2 = new Deposit(BankAction);
        depositThread2.setName("Thread 2");
        Deposit depositThread3 = new Deposit(BankAction);
        depositThread3.setName("Thread 3");
        
        Withdraw withdrawThread1 = new Withdraw(BankAction);
        withdrawThread1.setName("Thread 4");
        Withdraw withdrawThread2 = new Withdraw(BankAction);
        withdrawThread2.setName("Thread 5");
        Withdraw withdrawThread3 = new Withdraw(BankAction);
        withdrawThread3.setName("Thread 6");
        Withdraw withdrawThread4 = new Withdraw(BankAction);
        withdrawThread4.setName("Thread 7");
        
        depositThread1.start();
        depositThread2.start();
        depositThread3.start();
        withdrawThread1.start();
        withdrawThread2.start();
        withdrawThread3.start();
        withdrawThread4.start();
    }//main
}//Bank