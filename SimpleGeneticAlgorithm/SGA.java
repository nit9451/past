import java.util.*;
import java.io.*;

/*
 * Name: Philip Schwartz
 * ID: 2368864
 * CSCI 4401 Programming Assignment 1
 * Date: October 2013
 */

/*
 * SGA class: 
 * Simple Genetic Algorithm
 */
public class SGA {
    public static PrintWriter out;
    private static String fileName;
    private static int popSize;
    private static double crossoverRate;
    private static double mutationRate;
    private static double eliteRate;
    private static Random randomGen = new Random();
    final static int MAX_ITERATION = 2000;
    private static double totalFitness;
    private static int threads;
    private static int generations;

    /**
     * SGA Constructor: 
     * Default Parameters created: Threads: 10, Population 200, CrossOver Rate: 0.80, Mutation Rate: 0.10, Elite Rate: 0.05, Generations: 10.
     *
     */
    public SGA(String file){
        this(file, 10, 200, 0.80, 0.10, 0.05, 10);
    }

    /**
     * Custom SGA Constructor: 
     * Creates custom SGA with parameters specified by user.
     * @param thrds A parameter that saves the specified amount of threads
     * @param pop A parameter that saves the specified population size
     * @param cross A parameter that saves the specified crossover rate
     * @param mut A parameter that saves the specified mutation rate
     * @param elite A parameter that saves the specified mutation rate
     * @param gens A parameter that saves the # of generations to be processed
     */
    public SGA(String file, int thrds, int pop, double cross, double mut, double elite, int gens){
        fileName = file;
        threads = thrds;
        popSize = pop;
        crossoverRate = cross;
        mutationRate = mut;
        eliteRate = elite;
        generations = gens;
        
        try{
        out = new PrintWriter(fileName);
        }catch(Exception e){
            System.out.println("File not found. Restart Program!");
        
        }
}
    

    /**
     * Method chromosome: 
     * Returns chromosome within population at index i
     * @param population A parameter that consists of an array of Chromosomes (or population)
     * @param i A parameter used for indexing a specified chromosome in the population
     * @return Returns the chromosome in the population by index i
     */
    public Chromosome chromosome(Chromosome[] population, int i){
        return population[i];
    }

    /**
     * Method generatePop:
     * Randomly generates the population.
     * @param population A parameter of type Chromosome[] that is randomly generates to create the population.
     */
    public void generatePop(Chromosome[] population){
        for (int i = 0; i < popSize; i++)
            population[i] = new Chromosome(); 
    }

    /**
     * Method findBestFit: 
     * Finds the best fit chromosome of the population by sorting then returning the most fit chromosome.
     * @param population A parameter that consists of an array of Chromosomes (or population)
     * @return The return value is the best fit of the specified @param population.
     */
    public Chromosome findBestFit(Chromosome[] population){
        sort_Population(population);
        return population[0];
    }

    /**
     * Method totalFit: 
     * Returns the total fitness of the entire population. (Addition of each chromosome's fitness)
     * @param population A parameter that consists of an array of Chromosomes (or population)
     * @return The return value is the totalFitness of the population specified.
     */
    public double totalFit(Chromosome[] population){
        totalFitness = 0.0;
        for(int i = 0; i < popSize; i++){
            totalFitness += population[i].getFitness();
        }
        return totalFitness;
    }

    /**
     * Method fitness: 
     * Computes fitness of entire population, NOT using parrallel computation.
     * @param population A parameter that consists of an array of Chromosomes (or population)
     */
    public void fitness(Chromosome[] population){
        for(int i = 0; i < population.length; i++)
            population[i].calculateFitness();
    }

    /**
     * Method pFitComp: 
     * Parrallel Fitness Computation: Computes the fitness of the specified segment.  Used by created threads to process thread assigned segment of chromosomes within the population.
     * @param population A parameter that consists of an array of Chromosomes (or population)
     * @param lowB A parameter that sets the lower bound for fitness computation.
     * @param upB A parameter that sets the upper bound for fitness computation.
     */
    public void pFitComp(Chromosome[] population, int lowB, int upB){
        int lowerBound = lowB;
        int upperBound = upB;
        for(int i = lowerBound; i < upperBound; i++){
            population[i].calculateFitness();
        }
    }

    /**
     * Method sort_Population: 
     * Sorts the entire population based on fitness from greatest to least using Comparable interface and Arrays.sort
     * @param population A parameter that consists of an array of Chromosomes (or population)
     */
    public void sort_Population(Chromosome[] population){
        Arrays.sort(population);
    }

    /**
     * Method bestFit_SegmentPop:
     * Finds the best fitness of the population segment designated to each thread when using threads for fitness computation.
     * @param population A parameter that consists of an array of Chromosomes (or population)
     * @param lowerB A parameter that sets the lower bound for generating the population segment
     * @param upperB A parameter that sets the upper bound for generating the population segment
     * @return The return value is the best fitness of the segment's population
     */
    public double bestFit_SegmentPop(Chromosome[] population, int lowerB, int upperB){
        Chromosome[] segment = Arrays.copyOfRange(population, lowerB, upperB);
        return findBestFit(segment).getFitness();    
    }

    public Chromosome[] swapPop(Chromosome[] newPop ,Chromosome[] pop){
        System.arraycopy(newPop, 0, pop, 0, popSize);
        return pop;
    }

    /**
     * Method rouletteSelection: 
     * Higher Fitnesses are more likely to be selected using this method. 
     * Depending on randomGen.nextDouble(), determines the higher fitnesses between already high fitnesses being chosen. 
     * @param population A parameter that consists of an array of Chromosomes (or population)
     * @return Returns the randomly selected chromosome from the population using Roulette Selection method
     */    
    public Chromosome rouletteSelection(Chromosome[] population){
        totalFitness = totalFit(population);
        double rand = ((double)randomGen.nextDouble() * totalFitness);
        double sum = 0;
        int i = 0;
        for(; i < popSize; i++){
            sum += population[i].getFitness();
            if (rand < sum){
                break;
            }
        }

        return population[i];
    }

    /**
     * Method crossover: 
     * Chooses a random pivot (crossover) point within the population to swap segment of bits between chromosomes.
     * @param c1 A parameter Chromosome from the population, that is going to be used for crossover.
     * @param c2 A parameter Chromosome from the population, that is going to be used for crossover.
     * @return The return value consists of the two new Chromosomes after random crossover between the two.
     */
    public static Chromosome crossover(Chromosome c1, Chromosome c2){
        Chromosome[] newChrom = new Chromosome[2];
        newChrom[0] = new Chromosome();
        //newChrom[1] = new Chromosome();

        int crossOverPoint = randomGen.nextInt(Chromosome.BITS);
        int i;
        for(i = 0; i < crossOverPoint; i++){
            newChrom[0].setBit(i, c1.getBit(i));
            //newChrom[1].setBit(i, c2.getBit(i));
        }

        for(; i < Chromosome.BITS; i++){
            newChrom[0].setBit(i, c2.getBit(i));
            //newChrom[1].setBit(i, c1.getBit(i));
        }

        return newChrom[0];
    }

    /**
     * Method start: 
     * Starts the SGA. 
     * Loops through each generation of the population.
     * Processing the population's fitness using threads, sorting, and then creating a new population to process next using elitism, mutation, and crossover.
     *
     */
    public void start(){
        out.println("SGA Parameters; Threads:"+threads+"; Population Size:"+popSize+"; CrossOver Rate:"+crossoverRate+"; Mutation Rate:"+mutationRate+"; Elite Rate:"+eliteRate+"; Generations:"+generations+".");
        Chromosome[] population = new Chromosome[popSize];
        generatePop(population);
        Chromosome[] newPopulation = new Chromosome[popSize];

        //Chromosome[] chromo = new Chromosome[2];

        int generation = 1; //set at 1 so as to skip generation 0.

        //loop

        for(int it = 0; it <= MAX_ITERATION && it < generations; it++){
            int c = 0; //index counter for newPop

            //Thread setup
            int R = popSize/threads;
            ThreadedFitness[] threadsNum = new ThreadedFitness[threads+1];

            for(int i = 1; i <= threads; i++){
                int upperBound = i*R;
                int lowerBound = (i-1)*R+1;
                threadsNum[i] = new ThreadedFitness(i, lowerBound, upperBound, this, population);
                threadsNum[i].start();
            }

            try{
                for(int i = 1; i<= threads; i++){
                    threadsNum[i].join();
                }
            }catch(InterruptedException e){
                //doNothing
            }
            System.out.println("  >Back to sequential execution.");

            //Sort Pop
            sort_Population(population);

            //Print Generation and BestFit of current population.
            System.out.print("Generation: " + generation);
            out.print("Generation: " + generation);
            System.out.println(";  Best Fitness = " + findBestFit(population).getFitness());
            out.println("; Best Fitness = " + findBestFit(population).getFitness());
            generation++;

            //Elitism
            for(int i = 0; i < (eliteRate*popSize); i++){
                newPopulation[c] = population[i];
                c++;
            }

            //The lower bound used to roulleteSelect and add to newPop without overwritting Elite
            int eliteOffsetLowerBound = c;
            //The upper bound (based on crossoverRate) used to roulleteSelect until, and used to crossOver this set of the population
            //Leaving room for the last of the original population to be copied to newPop untouched
            int eliteOffsetUpperBound = (int)(crossoverRate*popSize) + eliteOffsetLowerBound;           
            //generate new Pop, starting with index after elites have been added.
            while(c < eliteOffsetUpperBound){
                newPopulation[c] = rouletteSelection(population);
                c++;
            }

            //Add rest of pop segment to newPop UNTOUCHED, and free from crossover and mutation
            for(int i = eliteOffsetUpperBound; i < popSize; i++){
                newPopulation[i] = population[i];
            }

            Chromosome[] children = new Chromosome[popSize];
            //Cross-over
            for(int i = eliteOffsetLowerBound; i < eliteOffsetUpperBound; i++){
                Chromosome chromo1;
                //Gets 2 random indexes for crossover from the newPopulation (which consists of Elite and Roullette Selected Pop
                int cIndex1 = randomGen.nextInt((int)(crossoverRate*popSize)) + 1;
                int cIndex2 = randomGen.nextInt((int)(crossoverRate*popSize)) + 1;
                chromo1 = crossover(newPopulation[cIndex1], newPopulation[cIndex2]);
                children[i] = chromo1;
                //newPopulation[cIndex2] = chromo[1];
            }
            //add children to newPop
            for(int i = eliteOffsetLowerBound; i < eliteOffsetUpperBound; i++){
                newPopulation[i] = children[i];
            }

            int mutationBound = (int)(mutationRate*popSize);
            //Mutation
            for(int i = 0; i < mutationBound; i++){
                int index = randomGen.nextInt((int)(crossoverRate*popSize)) + eliteOffsetLowerBound;
                newPopulation[index].mutate();
            }

            //sets newPopulation to population for processing of next generation
            population = swapPop(newPopulation, population);
        }

        //Best Fit Overall after Iterations
        Chromosome bestFit = findBestFit(population);
        System.out.println(); 
        out.println();
        System.out.println("Best Fitness Achieved --> " + bestFit.getFitness());
        out.println("Best Fitness Achieved --> " + bestFit.getFitness());
        System.out.println();
        System.out.println();
        out.println();
        out.println();
        System.out.println("WELCOME BACK! Would you like to compute using different parameters? If not enter 0 to Exit.");
        System.out.println("NOTE: Continuing with another SGA computation will overwrite previous data in file.");
        out.close();
    }
}

/*
 * Threaded Fitness class: 
 * Used to compute fitness in parallel using thread(s).
 */
class ThreadedFitness extends Thread {
    private int tID, lowerB, upperB;
    private SGA sga;
    private Chromosome[] pop;
    PrintStream out;

    public ThreadedFitness(int id, int lb, int ub, SGA sGa, Chromosome[] population){
        tID = id;
        lowerB = lb;
        upperB = ub; 
        sga = sGa;
        pop = population;
        out = out;
    }  

    /**
     * Method run:
     * Runs the current thread computing the fitness for the thread's specified population segment
     * Prints thread info, thread segment's best fitness, and when the thread has completed.
     *
     */
    public void run(){
        try{
        System.out.println("    -I am Thread "+tID+ ", processing chromosome #s: " + lowerB + " - " + upperB);
        sga.out.println("    -I am Thread "+tID+ ", processing chromosome #s: " + lowerB + " - " + upperB);
        sga.pFitComp(pop, lowerB - 1, upperB - 1); //this way population[0] is accounted for, and population[popSize]
        System.out.println("    -Thread #" + tID +": Population Segment Best Fitness: "+ sga.bestFit_SegmentPop(pop, lowerB - 1, upperB - 1));
        sga.out.println("    -Thread #" + tID +": Population Segment Best Fitness: "+ sga.bestFit_SegmentPop(pop, lowerB - 1, upperB - 1));
        System.out.println("    -Thread #" + tID + " is done!");
        sga.out.println("    -Thread #" + tID + " is done!");
    }catch(Exception e){
        System.out.println("An exception occured in Threads.");
    }
    }
}
