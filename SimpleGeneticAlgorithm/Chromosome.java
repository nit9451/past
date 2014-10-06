import java.util.*;

/*
 * Name: Philip Schwartz
 * ID: 2368864
 * CSCI 4401 Programming Assignment 1
 * Date: October 2013
 */

/*
 * Chromosome class:
 * Data structure used as Individuals in a population for computation in SGA.
 */

public class Chromosome implements Comparable{
    private int chromo[];
    private double fitness;
    Random rand = new Random();
    private int decArray[];
    public static final int BITS = 160;
    public static final int SEGMENTS = 10;

    /**
     * Chromosome Constructor:
     * Constructs a chromosome with 0 initial fitness.
     * Randomly generates the binary 160 bit chromosome.
     * Also stores a decimal array of the chromsome's 10 segments for fitness computation.
     *
     */
    public Chromosome(){
        this.fitness = 0;
        this.chromo = new int[BITS];
        generate();
        this.decArray = new int[SEGMENTS];
        binaryToDecimalArray();
    }

    /**
     * Method returnChromo:  
     * Returns the 160 bit array.
     *
     * @return The return value is the array of bits the chromosome consists of.
     */
    public int[] returnChromo(){
        return chromo;
    }

    /**
     * Method returnDec:
     * Returns the 10 variable decimal array to be used for fitness computation.
     *
     * @return The return value is this chromosome's decimal array.
     */
    public int[] returnDec(){
        return this.decArray;
    }

    /**
     * Method setBit:
     * Sets the chromosome's bit at index to the bit specified
     * @param index A parameter that corresponds to the specified bit in the chromosome
     * @param bit A parameter that is used to change the chromosomes bit to this one
     */
    public void setBit(int index, int bit){
        this.chromo[index] = bit;
    }

    /**
     * Method getBit: 
     * Returns the chromosome's bit at the specified index.
     * @param index A parameter that is the index of the chromosome.
     * @return The return value is the bit at this index.
     */
    public int getBit(int index){
        return chromo[index];
    }

    /**
     * Method setFitnessValue:
     * Sets the chromosome's fitness to the specified fitnessValue
     *
     * @param fitnessValue A parameter that is used to set the chromosome's fitness to this value.
     */
    public void setFitnessValue(int fitnessValue){
        this.fitness = fitnessValue;
    }

    /**
     * Method mutate: 
     * Randomly chooses a bit to mutate(flip)
     *
     */
    public void mutate(){
        int index = rand.nextInt(BITS);
        this.setBit(index, 1 - this.getBit(index)); //Flips bit
    }

    /**
     * Method getFitness:
     * Returns this chromosome's fitness
     *
     * @return The return value is this chromosome's fitness.
     */
    public double getFitness(){
        return this.fitness;
    }

    /**
     * Method calculateFitness:
     * Calculates this chromosome's fitness using the equation.
     *
     */
    public void calculateFitness(){
        double val1 = 0;
        double val2 = 1;
        for(int i = 0; i < 10; i++){
            val1 += Math.pow(this.decArray[i], 2)/4000.0;
            val2 *= Math.cos(this.decArray[i]/Math.sqrt(i+1));
        }
        this.fitness = 1 + val1 - val2;
    }

    /**
     * Method binaryToDecimalArray: 
     * Converts the 160 bit chromosome array into 10 segments (10 variable X's) to be used in calculation of the fitness. Creates an array of size 10 with these values.
     *
     * Note: there probably is a better way to implement this, was having trouble when I tried to make it more efficient.  
     * Probably a trivial way to improve efficiency I just could not think of it at the time.  Was trying to do a nested loop, but in reality it would be the same efficiecy.
     * Would still require 10 for loops whether nested or not.
     */
    public void binaryToDecimalArray(){
        int index = 0;
        int value = 0;
        for(int i = 0, j = 0; i < 16; i++, j++){
            if(chromo[i] == 1)
                value = value + (int)Math.pow(2, j);
        }
        decArray[index] = value;
        value = 0;
        index++;

        for(int i = 16, j = 0; i < 32; i++, j++){
            if(chromo[i] == 1)
                value = value + (int)Math.pow(2, j);
        }
        decArray[index] = value;
        value = 0;
        index++;

        for(int i = 32, j = 0; i < 48; i++, j++){
            if(chromo[i] == 1)
                value = value + (int)Math.pow(2, j);
        }
        decArray[index] = value;
        value = 0;
        index++;

        for(int i = 48, j = 0; i < 64; i++, j++){
            if(chromo[i] == 1)
                value = value + (int)Math.pow(2, j);
        }
        decArray[index] = value;
        value = 0;
        index++;

        for(int i = 64, j = 0; i < 80; i++, j++){
            if(chromo[i] == 1)
                value = value + (int)Math.pow(2, j);
        }
        decArray[index] = value;
        value = 0;
        index++;

        for(int i = 80, j = 0; i < 96; i++, j++){
            if(chromo[i] == 1)
                value = value + (int)Math.pow(2, j);
        }
        decArray[index] = value;
        value = 0;
        index++;

        for(int i = 96, j = 0; i < 112; i++, j++){
            if(chromo[i] == 1)
                value = value + (int)Math.pow(2, j);
        }
        decArray[index] = value;
        value = 0;
        index++;

        for(int i = 112, j = 0; i < 128; i++, j++){
            if(chromo[i] == 1)
                value = value + (int)Math.pow(2, j);
        }
        decArray[index] = value;
        value = 0;
        index++;

        for(int i = 128, j = 0; i < 144; i++, j++){
            if(chromo[i] == 1)
                value = value + (int)Math.pow(2, j);
        }
        decArray[index] = value;
        value = 0;
        index++;

        for(int i = 144, j = 0; i < 160; i++, j++){
            if(chromo[i] == 1)
                value = value + (int)Math.pow(2, j);
        }
        decArray[index] = value;
        value = 0;
        index++;

        //Converts decimal down to 11 bit (-1023 <= x <= 1024)
        for(int i = 0; i < 10; i++)
            decArray[i] = (decArray[i]/32) - 1024;

    }

    /**
     * Method generate: 
     * Randomly generates the binary 160 bit chromosome with random 0s and 1s
     *
     */
    public void generate(){
        for (int i = 0; i < chromo.length; i++){
            chromo[i] = rand.nextInt(2);
        }
    }

    /**
     * Method compareTo: 
     * Overwritten to sort population by fitness from greatest to least.
     *
     */
    public int compareTo(Object o){
        Chromosome c = (Chromosome) o;
        int num = (int)(c.getFitness() - this.getFitness());
        return num;
    }

    /**
     * Method equals
     */
    public boolean equals(Object o){
        boolean result = false;
        if (o instanceof Chromosome){
            Chromosome c = (Chromosome) o;
            result = c.returnChromo().equals(chromo);
        }
        return result;
    }

    /**
     * Method toString
     */
    public String toString(){
        String s = "[";
        for(int i = 0; i < chromo.length; i++)
            s = s + Integer.toString(chromo[i]);
        s = s + "]";
        return s;
    }

}
