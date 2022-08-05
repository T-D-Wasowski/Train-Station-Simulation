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
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Dawid
 */
public class TrainStation {

    public static final int WAITING_ROOM_CAPACITY = 30;
    
    //Capacity + 1 to account for pointer positions when queue is full.
    private static Passenger[] waitingRoom = new Passenger[WAITING_ROOM_CAPACITY];
    private static PassengerQueue trainQueue = new PassengerQueue();
    
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        
        String command;   
        
        printCommands();
        
        do {
            
            command = collectInputStr(scanner, "Enter command: ").toLowerCase();
            
            switch(command) {
                
                case "q":
                    System.out.println("You've quit the program.");
                    break;
                    
                case "a":
                    trainQueue.add(createPassenger(scanner));
                    break;
                    
                case "d":
                    trainQueue.remove();
                    break;
                    
                case "v":
                    trainQueue.view();
                    break;
                    
                case "s":
                    trainQueue.store(collectInputStr(scanner, "Please enter desired file name: "));
                    break;
                    
                case "l":
                    trainQueue.load(collectInputStr(scanner, "Please enter desired file name: "));
                    break;
                    
                case "r":
                    runSimulation();
                    break;
                    
                default:
                    System.out.println("Please enter a valid command.");
            
            }
            
        } while(!command.equals("q"));
     
    }
    
    //Method to print available commands
    private static void printCommands() {
        
        System.out.println("Available commands:\n"
            + "  A - Add passenger to queue\n"
            + "  D - Remove passenger from queue\n"
            + "  V - View train queue\n"
            + "  S - Store train queue\n"
            + "  L - Load train queue\n"
            + "  R - Run simulation\n"
            + "  Q - Quit program");
               
    }
    
    //Function to output text and collect user response as string
    private static String collectInputStr(Scanner scanner, String output) {  
        
        System.out.print(output);
        String input = scanner.nextLine();

        return input;
    }
       
    //Similar to above, but collects user response as int
    private static int collectInputInt(Scanner scanner, String output) {  
        
        System.out.print(output);
        int input = scanner.nextInt();
        scanner.nextLine();

        return input;       
    }
    
    //Method which creates a passenger for manual addition
    private static Passenger createPassenger(Scanner scanner) {
        
        String firstName = collectInputStr(scanner, "Please enter the passenger's first name: ");
        String lastName = collectInputStr(scanner, "Please enter the passenger's last name: ");
        
        Passenger passenger = new Passenger(firstName, lastName);
        
        return passenger;    
    }
    
// === SIMULATION METHODS ===
    
    //Runs the simulation
    private static void runSimulation() {
        
        //Reads passengers.dat into the waitingRoom
        populateWaitingRoom();
        
        //Creates clean queue for the simulation (Preserves trainQueue)
        PassengerQueue simQueue = new PassengerQueue();
        
        int waitingRoomCount = 0;
        int iterationCount = 1;
        
        do { 
            
            System.out.println("Iteration: " + iterationCount);
            
            //Adds a random amount of passengers to queue and updates counter. Prevents overflow.
            waitingRoomCount = addPassengersToQueue(simQueue, waitingRoomCount);
            
            //Adds a random delay to all passengers in the queue
            simQueue.addDelay(calculateDelay());
            
            //Removes one passenger
            simQueue.remove();
            
            //Prints queue statistist for this iteration
            printQueueStats(simQueue);
            
            //Increments iteration count
            iterationCount++;
     
        } while(!simQueue.isEmpty() || waitingRoomCount != 30);
        
        saveReport(simQueue);
        printReport();
        
    }
    
    //Reads passengers from passenger.dat into the waiting room
    private static void populateWaitingRoom() {
        
        try (BufferedReader reader = new BufferedReader(new FileReader("passengers.dat"))) {
  
            String lineWhole = reader.readLine();
            String[] lineSplit;
            
            for (int i = 0; i < WAITING_ROOM_CAPACITY; i++) {
                
                lineSplit = lineWhole.split(" ");
                
                //Creates passenger objects using above variables
                Passenger passenger = new Passenger(lineSplit[0], lineSplit[1]);
                
                //Inserts passenger into waiting room
                waitingRoom[i] = passenger;
                
                //Accesses next line from the file file
                lineWhole = reader.readLine();
                
            }
            
            System.out.println("Waiting room successfully populated with passengers.");
            
        } catch (IOException e) {
            System.out.println(e);
        }     
         
    }
    
    //Simulates a d6 die throw
    private static int dieThrow() {
        
        Random random = new Random();
           
        int die = random.nextInt(6) + 1;
        
        return die;
   
    }
    
    //Adds a random number of passengers to a queue, returns updated waiting room counts
    private static int addPassengersToQueue(PassengerQueue queue, int count) {
        
        int passToQueue = dieThrow();
            
        for (int i = 0; i < passToQueue; i++) {  
            if (count == 30) {
                break;
            } else {
                queue.add(waitingRoom[count]);  
                count++;
            }
        }
        
        return count;
    }
    
    //Calculates a random delay by throwing 3d6
    private static int calculateDelay() {
        int sum = 0;
        
        for (int i = 0; i < 3; i++) {
            sum += dieThrow();
        }
        
        return sum;     
    }
    
    //Prints queue statistics
    private static void printQueueStats(PassengerQueue queue) {
        
        System.out.println(" - Max Length: " + queue.getMaxLength() + "\n"
        + " - Max Stay: " + queue.getMaxStay() + "\n"
        + " - Min Stay: " + queue.getMinStay() + "\n"
        + " - Total Stay: " + queue.getTotalStay());
        
    }
    
    //Saves queue statistics into a report.dat file
    private static void saveReport(PassengerQueue queue) {
        
         //When written like this the writer is closed automatically, so no need for .close()
        try (PrintWriter writer = new PrintWriter("report.dat")) {
            
            writer.print(""); //Clears file before writing
            
            writer.println("Simulation Report:");
            writer.println(" - Max Length: " + queue.getMaxLength());
            writer.println(" - Max Stay: " + queue.getMaxStay());
            writer.println(" - Min Stay: " + queue.getMinStay());
            writer.println(" - Total Stay: " + queue.getTotalStay()); 
            writer.println(" - Average Stay: " + queue.getTotalStay()/WAITING_ROOM_CAPACITY);
            
            System.out.println("The report has been created.");
            
        } catch(IOException e) {           
            System.out.println(e);     
        }       
        
    }
    
    //Prints the report by reading it from the created file
    private static void printReport() {
        
        //Same for this reader with the .close()
        try (BufferedReader reader = new BufferedReader(new FileReader("report.dat"))) {
            
            String line = reader.readLine();
            
            while (line != null) {
                System.out.println(line);
                line = reader.readLine();
            }
            
        } catch (IOException e) {
            System.out.println(e);
        }     
        
    }
}
