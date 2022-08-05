/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trainstation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author Dawid
 */
public class PassengerQueue {
    
    //Capacity + 1 to account for pointer positions when queue is full.
    private Passenger[] queueArray = new Passenger[TrainStation.WAITING_ROOM_CAPACITY + 1];
    
    //Pointers
    private int first = 0;
    private int last = 0;
    
    //Statistics
    private int maxLength = 0; //Maximum length reached by the queue
    private int maxStayInQueue = 0; //Number of seconds that the passenger who stayed longest in queue
    private int minStayInQueue = 999; //Number of sec that the passenger who stayed the shortest in queue 
    private int totalStayInQueue = 0; //Total amount of time spent in queue by all passengers
    
   
// === GET FUNCTIONS ===  
    
    public int getMaxLength() {        
        return maxLength;       
    }
    
    public int getMaxStay() {    
        return maxStayInQueue; 
    }
    
    public int getTotalStay() {
        return totalStayInQueue;
    }
    
    public int getMinStay() {
        return minStayInQueue;
    }
    
// === PUBLIC FUNCTIONS ===
    
    public void add(Passenger next) {

        //Condition below checks if the queue is full
        if (isFull()) {
            
            System.out.println("The queue is full; cannot add another passenger.");       
            
        } else {
        
            //Adds passenger to queue
            queueArray[last] = next;
            
            //Works out the next empty space pointer
            last = pointerIncrement(last);
            
            //Prints out confirmation of passenger addition
            System.out.println(next.getName() + " has been added to the queue and"
                    + " is currently #" + calcQueueLength() + " in the queue.");
        }  
        
        //Checks and updates max queue length
        if (calcQueueLength() > maxLength) {
            maxLength = calcQueueLength();
        }       
        
    }
    
    public void remove() {
        
        if (isEmpty()) {
            System.out.println("The queue is empty; there are no passengers to remove.");
        } else {
            //Preserves passenger object for duration of the remove() method
            Passenger passenger = queueArray[first];
      
            //Removes passenger from queue
            queueArray[first] = null;           

            //Increments the first pointer in queue
            first = pointerIncrement(first);
            
            //Checks and updates maxStayInQueue
            if (passenger.getSeconds() > maxStayInQueue) {
                maxStayInQueue = passenger.getSeconds();
            }
            
            //Checks and updates minStayInQueue
            if (passenger.getSeconds() < minStayInQueue) {
                minStayInQueue = passenger.getSeconds();
            }
            
            totalStayInQueue += passenger.getSeconds();

            //Prints out confirtmation of passenger removal
            System.out.println(passenger.getName() + " has been removed from the queue, with "
                    + calcQueueLength() + " passengers still waiting.");
            
            
        }
        
        
    }
    
    public void view() {
        int pos = 1;
        if (first < last) {
            for (int i = first; i < last; i++) {
                System.out.println("#" + pos + ": " + queueArray[i].getName());
                pos++;
                } 
        } else if (last < first) {
            for (int i = first; i < queueArray.length; i++) {
                System.out.println("#" + pos + ": " + queueArray[i].getName());
                pos++;
            } 
            
            for (int i = 0; i < last; i++) {   
                System.out.println("#" + pos + ": " + queueArray[i].getName());
                pos++;
            }
        } else {
            System.out.println("The queue is empty.");
        }   
    }
    
    public void store(String fileName) {
        
         //When written like this the writer is closed automatically, so no need for .close()
        try (PrintWriter writer = new PrintWriter(fileName + ".txt")) {
            
            writer.print(""); //Clears file before writing
            
            if (first < last) {
                for (int i = first; i < last; i++) {
                    writer.println(queueArray[i].getName() + " " + queueArray[i].getSeconds());
                } 
                System.out.println("The train data has been saved to file.");
            } else if (last < first) {
                for (int i = first; i < queueArray.length; i++) {
                    writer.println(queueArray[i].getName() + " " + queueArray[i].getSeconds());
                } 
                for (int i = 0; i < last; i++) {   
                    writer.println(queueArray[i].getName() + " " + queueArray[i].getSeconds());
                }   
                System.out.println("The train data has been saved to file.");
            } else {
                System.out.println("The queue is empty; there is nothing to save.");
            }    
            
        } catch(IOException e) {           
            System.out.println(e);     
        }       
        
    }
    
    public void load(String fileName) {
        
         //Same for this reader with the .close()
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName + ".txt"))) {
  
            int i = 0;
            String lineWhole = reader.readLine();
            String[] lineSplit;
            
            while (lineWhole != null) {

                //Splits line into name, surname and secondsInQueue
                lineSplit = lineWhole.split(" ");
                
                //Creates passenger objects using above variables
                Passenger passenger = new Passenger(lineSplit[0], lineSplit[1]);
                passenger.setSeconds(Integer.parseInt(lineSplit[2]));
                
                //Inserts passenger into queue
                queueArray[i] = passenger;
                
                //Increases counter
                i++;  
                
                //Accesses next line from text file
                lineWhole = reader.readLine();
                
            }
            
            //Sets first and last parameters
            first = 0;
            last = i;
            
            System.out.println("The train data has been loaded from file.");
            
        } catch (IOException e) {
            System.out.println(e);
        }     
        
    }
    
    //Checks if the queue is full
    public boolean isFull() { 
        return ((first == 0 && last == TrainStation.WAITING_ROOM_CAPACITY) || ((last + 1) == first));
    }
    
    //Checks if the queue is empty
    public boolean isEmpty() {
        return (first == last);
    }
    
    public void addDelay(int seconds) {
        
        if (first < last) {
            for (int i = first; i < last; i++) {
                queueArray[i].addSeconds(seconds);
                } 
        } else if (last < first) {
            for (int i = first; i < queueArray.length; i++) {
                queueArray[i].addSeconds(seconds);
            } 
            
            for (int i = 0; i < last; i++) {   
                queueArray[i].addSeconds(seconds);
            }
        } else {
            System.out.println("The queue is empty; cannot add delay.");
        }   
        
    }
    
    public void display() {
        
        System.out.println(queueArray + " " + first + " " + last + " " + maxStayInQueue + " "
                + maxLength);
        
    }
    
// === PRIVATE FUNCTIONS ===
    
    //Increments the pointer after adding/removing a passenger
    private int pointerIncrement(int pointer) {        
        if (pointer == TrainStation.WAITING_ROOM_CAPACITY) {
            pointer = 0;
        } else {
            pointer++;
        }
        return pointer;
    }
    
    //Calculates the current length of the queue
    private int calcQueueLength() {
        int length;
        
        if (first > last) {
            length = ((TrainStation.WAITING_ROOM_CAPACITY + 1) - (first - last));    
        } else {
            length = (last - first);
        }    
        
        return length;    
    }
    
}
