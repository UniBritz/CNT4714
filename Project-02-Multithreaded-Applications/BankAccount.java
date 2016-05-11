/* 
 Name:              Brian Batinchok
 Course:            CNT 4714 Spring 2016
 Assignment Title:  Project 2 – Synchronized, Cooperating Threads Under Locking
 Due Date:          February 14, 2016
*/

package BankSimulation;

public interface BankAccount{
    public void Deposit(int depositAmount, String thread);
    public void Withdraw(int withdrawAmount, String thread);
}//BankAccount