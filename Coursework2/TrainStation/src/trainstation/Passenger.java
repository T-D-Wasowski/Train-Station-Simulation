/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trainstation;

/**
 *
 * @author Dawid
 */
public class Passenger {
    
    private String firstName;
    private String lastName;
    private int secondsInQueue;
    
    //Constructor method
    Passenger(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        secondsInQueue = 0;
    }
    
    public String getName() {
        return firstName + " " + lastName;       
    }
    
    public void setName(String firstName, String lastName) {    
        this.firstName = firstName;
        this.lastName = lastName;    
    }
    
    public int getSeconds() {
        return secondsInQueue;   
    }
    
    public void setSeconds(int seconds) {
        secondsInQueue = seconds;
    }
    
    public void addSeconds(int seconds) {
        secondsInQueue += seconds;
    }
    
    public void display() {
        System.out.println(firstName + " " + lastName + " " + secondsInQueue);      
    }
}