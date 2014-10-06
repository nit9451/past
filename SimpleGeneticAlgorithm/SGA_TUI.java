import java.util.*;
import java.lang.*;
import java.io.*;


/*
 * Name: Philip Schwartz
 * ID: 2368864
 * CSCI 4401 Programming Assignment 1
 * Date: October 2013
 */

/*
 * SGA TUI class: 
 * Used to run the SGA.
 * User can enter custom parameters or compute using default parameters.
 */
public class SGA_TUI{
    private Scanner input;
    private SGA sga;
    private PrintWriter out;
    String fileName;

    private static final int DEFAULT = 1;
    private static final int CUSTOM = 2;
    private static final int EXIT = 0;

    /**
     * SGA_TUI Constructor:
     * Constructs a Scanner object to read user input.
     */
    public SGA_TUI(){
        input = new Scanner(System.in);
    }

    /**
     * Method main:
     * Starts the TUI.
     */
    public static void main(String [] args){
        (new SGA_TUI()).start();
    }

    /**
     * Method start:
     * Prints Menu to User, while choice != EXIT.  
     * Executes choice if choice != EXIT.
     */
    private void start(){
        System.out.println("Welcome to a Simple Genetic Algorithm");
        getFileName();
        //System.out.println("Please choose an option from the menu to compute: ");
        int choice = -1;
        while(choice != EXIT){
            printMenu();
            choice = readIntWithPrompt("Enter an option from the menu: ");
            executeChoice(choice);
        }
    }

    /**
     * Method: getFileName
     * Gets the file entered from user and ensures the file exists before proceeding.
     */
    private void getFileName(){
        fileName = readMessageWithPrompt("Enter a file path to write output to: ");
        File file = new File(fileName);
        if(file.exists() != true){
            System.out.println("File does not exist!");
            System.out.println("RETRY!");
            start();
        }
    }
        

    /**
     * Method printMenu:
     * Prints default menu to user.
     */
    private void printMenu(){
        System.out.println("MENU:");
        System.out.println(DEFAULT + " - SGA with default parameters: ");
        System.out.println("    (Threads: 10, Pop: 200, Crossover: 0.80, Mutation: 0.10, Elite: 0.05, Generations: 10)");
        System.out.println(CUSTOM + " - Enter your own parameters for the SGA.");
        System.out.println(EXIT + " - Exit.");
    }


    /**
     * Method readMessageWithPrompt
     *
     * @param prompt 
     * contains the message asked to the user, to retrieve input.
     * @return String message
     * returns message entered by user.
     */
    private String readMessageWithPrompt(String prompt){
        System.out.print(prompt); System.out.flush();
        while(!input.hasNext()){
            input.nextLine();
            System.out.println("Invalid entry.");
            System.out.println(prompt); System.out.flush();
        }
        String message = input.next();
        input.nextLine();
        return message;
    }
    
    /**
     * Method readIntWithPrompt:
     *
     * @param String $prompt
     *  contains the message asked to the user, to retrieve input.
     * @return int $in
     *  returns int entered by user.
     */
    private int readIntWithPrompt(String prompt){
        System.out.print(prompt); System.out.flush();
        while(!input.hasNextInt()){
            input.nextLine();
            System.out.println("Invalid entry.");
            System.out.println(prompt); System.out.flush();
        }

        int in = input.nextInt();
        input.nextLine();
        return in;
    }

    /**
     * Method readDoubleWithPrompt:
     *
     * @param String $prompt
     *  contains the message asked to the user, to retrieve input.
     * @return double $in
     *  returns double entered by user.
     */
    private double readDoubleWithPrompt(String prompt){
        System.out.print(prompt); System.out.flush();
        while(!input.hasNextDouble()){
            input.nextLine();
            System.out.println("Invalid entry.");
            System.out.println(prompt); System.out.flush();
        }

        double in = input.nextDouble();
        input.nextLine();
        return in;
    }

    /**
     * Method executeChoice
     * Executes choice recieved from user.
     * @param int $choice
     *  contains choice received from user, used to execute further through TUI
     */
    private void executeChoice(int choice){
        if (choice == EXIT){
            System.out.println();
            System.out.println("Good-bye.\n");
        }
        else if (choice == DEFAULT){
            //Create default SGA
            sga = new SGA(fileName);
            sga.start();
        }
        else if (choice == CUSTOM){
            //Get custom input from user
            custom();
        }
        else {
            System.out.println("Invalid option.");
            System.out.println();
            System.out.println();
        }
    }

    /**
     * Method custom:
     * Gets user's custom input parameters. 
     * Enforces range of certain input which is specified.
     *
     */
    private void custom(){
        //DO, WHILE LOOP FOR EXITING IF 0 is entered
        System.out.println("Recommended GA parameters enforced: ");
        System.out.println("Threads >= 0 and < Population Size.");
        System.out.println("Population Size = Positive EVEN Integer (Greater size, better chance for higher fitness)");
        System.out.println("Crossover rate = 80 to 90% (.80 - .90), at least 60% required (.60 or >)");
        System.out.println("Mutation rate = 0.5 to 10% (.005 - .10)");
        System.out.println("Elite rate = 5 - 10% (.05 - 0.10)");
        System.out.println("Generations = 1 - 2000 (The amount of generations to process.)");
        int threads = readIntWithPrompt("Enter the amount of threads to use for fitness computation: ");
        int pop = readIntWithPrompt("Enter a population size: ");
        double cross = readDoubleWithPrompt("Enter a crossover-rate: ");
        double mut = readDoubleWithPrompt("Enter a mutation-rate: ");
        double elite = readDoubleWithPrompt("Enter an elite-rate: ");
        int gens = readIntWithPrompt("Enter amount of generations: ");

        boolean choices = true;
        if (threads < 0 ||  threads > pop){
            System.out.println();
            System.out.println("-->Invalid # of threads entered. Retry.");
            System.out.println();
            choices = false;
        }
        if (pop < 0 || pop%2 != 0){
            System.out.println();
            System.out.println("-->Invalid population size entered. Retry.");
            System.out.println();
            choices = false;
        }
        if (cross < .60 || cross > 1.0){
            System.out.println();
            System.out.println("-->Invalid cross-over rate entered. Retry.");
            System.out.println();
            choices = false;
        }
        if (mut < 0.005 || mut > 0.10){
            System.out.println();
            System.out.println("-->Invalid mutation rate entered. Retry.");
            System.out.println();
            choices = false;
        }
        if (elite < 0.05 || elite > 0.10){
            System.out.println();
            System.out.println("-->Invalid elite rate entered. Retry.");
            System.out.println();
            choices = false;
        }
        if (gens < 1 || gens > 2000){
            System.out.println();
            System.out.println("-->Invalid # of generations entered. Retry.");
            System.out.println();
            choices = false;
        }
        if(choices == true){
            //Choices are acceptable. Create custom SGA
            sga = new SGA(fileName, threads, pop, cross, mut, elite, gens);
            sga.start();
        }
        else
            custom();

    }
}

  