import java.util.*;

/**
 * Implements First Fit, Next Fit, Best Fit, and Worst Fit for process memory allocation testing.
 * 
 * Name: Philip Schwartz
 * Date: October 2013 - November 2013
 * ID: 2368864
 */
public class MemoryAllocation{
    private Random randomGen;
    private int[] myMemory, myMemoryFF, myMemoryNF, myMemoryBF, myMemoryWF;
    private int memSize, processSize, memIndex, nextFitSavedIndex, processNum, numOfHoles;
    private double HoleOrAllocationPivot;
    private double holeOrAllocate;
    private static final int FIRSTFIT = -100;
    private static final int NEXTFIT = -101;
    private static final int BESTFIT = -102;
    private static final int WORSTFIT = -103;
    private static final int PROCESSMAX = 100; 
    private double[] FFosfn; //indexes in array will be operations,succeses,failures,newHoleSize
    private double[] NFosfn;
    private double[] BFosfn;
    private double[] WFosfn;

    /**
     * Constructor for objects of class MemoryAllocation
     */
    public MemoryAllocation(int MemorySize){
        this.memSize = MemorySize;
        numOfHoles = 0; //so holes array will start at index 0 when a hole is counted
        randomGen = new Random();
        HoleOrAllocationPivot = 0.5; //Pivot for random genertion of holes and processes
        this.myMemory = new int[MemorySize];
        initializeZero(this.myMemory);
        initializeHorA(this.myMemory);
        this.myMemoryFF = new int[MemorySize];
        this.myMemoryNF = new int[MemorySize];
        nextFitSavedIndex = 0;
        this.myMemoryBF = new int[MemorySize];
        this.myMemoryWF = new int[MemorySize];
        //Copy myMemory Array to 4 fit Arrays
        System.arraycopy(this.myMemory, 0, this.myMemoryFF, 0, MemorySize);
        System.arraycopy(this.myMemory, 0, this.myMemoryNF, 0, MemorySize);
        System.arraycopy(this.myMemory, 0, this.myMemoryBF, 0, MemorySize);
        System.arraycopy(this.myMemory, 0, this.myMemoryWF, 0, MemorySize);
        //System.out.println("Init Mem: " + MemoryToString(this.myMemory));

        //4 arrays to hold the stats of each fit algorithm
        this.FFosfn = new double[4];
        this.NFosfn = new double[4];
        this.BFosfn = new double[4];
        this.WFosfn = new double[4];
    }

    /**
     * Method FFMem
     *
     * @return this.myMemoryFF which is the array for First Fit memory.
     */
    public int[] FFMem(){
        return this.myMemoryFF;
    }

    /**
     * Method NFMem
     *
     * @return this.myMemoryNF which is the array for Next Fit memory.
     */
    public int[] NFMem(){
        return this.myMemoryNF;
    }

    /**
     * Method BFMem
     *
     * @return this.myMemoryBF which is the array for Best Fit memory.
     */
    public int[] BFMem(){
        return this.myMemoryBF;
    }

    /**
     * Method WFMem
     *
     * @return this.myMemoryWF which is the array for Worst Fit memory.
     */
    public int[] WFMem(){
        return this.myMemoryWF;
    }

    /**
     * Method AverageFFosfn
     *
     * @param index The index of the average stat you wish to retreive from First Fit.  (Index 0: operations, Index 1: Successes, Index 2: Failures, Index 3: New Hole Size)
     * @return The return value is the average stat specified by index.
     */
    public String AverageFFosfn(int index){
        return Double.toString(this.FFosfn[index]/this.memSize);
    }

    /**
     * Method AverageNFosfn
     *
     * @param index The index of the average stat you wish to retreive from Next Fit.  (Index 0: operations, Index 1: Successes, Index 2: Failures, Index 3: New Hole Size)
     * @return The return value is the average stat specified by index.
     */
    public String AverageNFosfn(int index){
        return Double.toString(this.NFosfn[index]/this.memSize);
    }

    /**
     * Method AverageBFosfn
     *
     * @param index The index of the average stat you wish to retreive from Best Fit.  (Index 0: operations, Index 1: Successes, Index 2: Failures, Index 3: New Hole Size)
     * @return The return value is the average stat specified by index.
     */
    public String AverageBFosfn(int index){
        return Double.toString(this.BFosfn[index]/this.memSize);
    }

    /**
     * Method AverageWFosfn
     *
     * @param index The index of the average stat you wish to retreive from Worst Fit.  (Index 0: operations, Index 1: Successes, Index 2: Failures, Index 3: New Hole Size)
     * @return The return value is the average stat specified by index.
     */
    public String AverageWFosfn(int index){
        return Double.toString(this.WFosfn[index]/this.memSize);
    }

    /**
     * Method randomProcessSize
     *
     * @return Returns a random int between 1 and PROCESSMAX.
     */
    public int randomProcessSize(){
        return randomGen.nextInt(PROCESSMAX) + 1;
    }

    /**
     * Method initializeZero
     * Initializes memory array to all 0's.
     *
     * @param mem Is the memory array to be initialized.
     */
    public void initializeZero(int[] mem){
        for(int i = 0; i < mem.length; i++){
            mem[i] = 0;
        }   
    }

    /**
     * Method initializeHorA
     * Goes through memory array randomly allocating processes or skipping indexes for holes.
     *
     * @param mem The memory array to be randomly allocated or remain holes.
     */
    public void initializeHorA(int [] mem){
        numOfHoles = 0;
        memIndex = 0;
        processNum = 1;
        while(memIndex < mem.length){
            processSize = randomProcessSize();
            holeOrAllocate = randomGen.nextDouble();
            if(holeOrAllocate <= HoleOrAllocationPivot){//allocate
                for(int i = 0; i < processSize && memIndex < mem.length; i++){
                    mem[memIndex] = processNum;
                    memIndex++;
                }
                processNum += 1;
            }
            else{//hole
                for( int i = 0; i < processSize && memIndex < mem.length; i++){
                    memIndex++; //skip arrayIndexes
                }
                numOfHoles = numOfHoles + 1;
            }     
        }

    }

    /**
     * Method firstFit
     * Finds the first available hole to fit the process, and fails if no hole is found.
     * @param procSize The process sized attemping to be fit in a hole in FFmemory.
     */
    public void firstFit(int procSize){
        boolean fit = false;
        int holeSize;
        memIndex = 0;
        int startHoleIndex = 0, endHoleIndex = 0, offset = 0;

        while(fit == false && memIndex < this.myMemoryFF.length){

            holeSize = 0;
            if(this.myMemoryFF[memIndex] == 0 || this.myMemoryFF[memIndex] == -1){
                startHoleIndex = memIndex;
                offset = memIndex;
                do{
                    holeSize++;
                    memIndex++; this.FFosfn[0]++; //inc operations
                }while(memIndex < this.myMemoryFF.length && (this.myMemoryFF[memIndex] == 0 || this.myMemoryFF[memIndex] == -1));
                endHoleIndex = memIndex - 1;
            }
            else{
                memIndex++; this.FFosfn[0]++;//inc operations
            }

            if(holeSize >= procSize){
                fit = true; this.FFosfn[1]++;//inc success
                for(; startHoleIndex < procSize+offset; startHoleIndex++)//alocate first part of hole for process as big as process size
                    this.myMemoryFF[startHoleIndex] = FIRSTFIT;
                for(; startHoleIndex <= endHoleIndex; startHoleIndex++){//set rest of hole to a new hole
                    this.myMemoryFF[startHoleIndex] = -1; 
                    this.FFosfn[3]++; //inc newHoleSize
                }
            }

        }

        if(fit == false)
            this.FFosfn[2]++; //inc failure

    }

    /**
     * Method nextFit
     * Find the next hole from the previously allocated hole to try and fit the next process.  
     * This algorithm will wrap around to the beginning of memory if the end of memory is reached and a hole is not found.
     * This algorithm keeps track of a global variable nextFitSavedIndex, once a hole is allocated the index is set to the index following the last process allocated index.
     * 
     *
     * @param procSize The size of the process to fit.
     */
    public void nextFit(int procSize){
        boolean fit = false;
        boolean wrapAround = false;
        int holeSize;
        int startHoleIndex = 0, endHoleIndex = 0, offset = 0;
        int i = nextFitSavedIndex;
        int wrapAroundLastIndex = nextFitSavedIndex;

        while(fit == false && i < this.myMemoryNF.length){

            holeSize = 0;
            if(this.myMemoryNF[i] == 0 || this.myMemoryNF[i] == -1){
                startHoleIndex = i;
                offset = i;
                do{
                    holeSize++;
                    i++; this.NFosfn[0]++; //inc operations
                }while(i < this.myMemoryNF.length && (this.myMemoryNF[i] == 0 || this.myMemoryNF[i] == -1));
                endHoleIndex = i - 1;

            }
            else{
                i++; this.NFosfn[0]++; //inc operations
            }

            //If the process can fit in the hole, allocate the process
            if(holeSize >= procSize){
                fit = true; this.NFosfn[1]++; //inc success
                for(; startHoleIndex < procSize + offset; startHoleIndex++)//allocate first part of hole for process size
                    this.myMemoryNF[startHoleIndex] = NEXTFIT;

                nextFitSavedIndex = startHoleIndex; //set nextFitSavedIndex to end of allocated space for next iteration, so that new hole added at end is accounted for.    

                for(; startHoleIndex <= endHoleIndex; startHoleIndex++){//set rest of hole to a new hole
                    this.myMemoryNF[startHoleIndex] = -1;
                    this.NFosfn[3]++; //inc new Hole size
                }
            }

        }
        if(fit == false){
            wrapAround = true;
            i = 0; //reset to searching to start
        }

        //Second while loop for wrapAround index if fit = false still,
        while(fit == false && i < wrapAroundLastIndex){

            holeSize = 0;
            if(this.myMemoryNF[i] == 0 || this.myMemoryNF[i] == -1){
                startHoleIndex = i;
                offset = i;
                do{
                    holeSize++;
                    i++; this.NFosfn[0]++; //inc operations
                }while(i < wrapAroundLastIndex && (this.myMemoryNF[i] == 0 || this.myMemoryNF[i] == -1));
                endHoleIndex = i - 1;

            }
            else{
                i++; this.NFosfn[0]++; //inc operations
            }

            if(holeSize >= procSize){
                fit = true; this.NFosfn[1]++; //inc success
                for(; startHoleIndex < procSize + offset; startHoleIndex++)//allocate first part of hole for process size
                    this.myMemoryNF[startHoleIndex] = NEXTFIT;

                nextFitSavedIndex = startHoleIndex; //set nextFitSavedIndex to end of allocated space for next iteration, so that new hole added at end is accounted for.    

                for(; startHoleIndex <= endHoleIndex; startHoleIndex++){//set rest of hole to a new hole
                    this.myMemoryNF[startHoleIndex] = -1;
                    this.NFosfn[3]++; //inc new Hole size
                }
            }

        }

        //If even after wrapAround fit is not found, increment failure
        if(fit == false)
            this.NFosfn[2]++; //inc failure

        
    }

    /**
     * Class Hole
     * Used for bestFit and worstFit for ordering of holes as related to space leftOver after allocating a process.
     */
    private class Hole implements Comparable{
        private int size;
        private int index;
        private int processSize;
        Hole(int index, int size, int processSize){
            this.index = index;
            this.size = size;
            this.processSize = processSize;
        }

        /**
         * Method compareTo
         * Optimized to order from Best Fit to Worst Fit based on (Size of the Hole)-(Process Size).  
         * After ordering the hole for Best Fit will be at index 1, and Worst Fit will be at the last index.
         *
         * @param o A hole to compare to.
         * @return :-1 the hole left over after allocation is smaller than the Object o's. :0 if they are equal in hole size and process size. :1 if the hole left over after allocation is larger than Object o's.
         */
        public int compareTo(Object o){
            if(o != null){
                Hole c = (Hole)o;
                return (this.size()-this.processSize()) - (c.size()-c.processSize());
            }
            else
                return 0;
        }

        private int index(){
            return this.index;
        }

        private int size(){
            return this.size;
        }

        private int processSize(){
            return this.processSize;
        }
    }

    /**
     * Method bestFit
     * Finds the Best Fit(Most snug, least amount index holes left over after allocating the process)
     * Uses Hole objects to order all holes after iterating through entire memory to sort the Holes from Best Fit to Worst Fit.
     * @param procSize The size of the process to fit.
     */
    public void bestFit(int procSize){
        boolean fit = false;
        Hole[] tempHoles = new Hole[numOfHoles];
        boolean newHole;
        int index = 0;
        int holeIndex = 0;
        int startHoleIndex = 0, endHoleIndex = 0, offset = 0;
        int holeSize;

        while(index < this.myMemoryBF.length){

            holeSize = 0;
            newHole = false;
            if(this.myMemoryBF[index] == 0 || this.myMemoryBF[index] == -1){
                startHoleIndex = index;
                newHole = true;
                do{
                    holeSize++;
                    index++; this.BFosfn[0]++; //inc operations
                }while(index < this.myMemoryBF.length && (this.myMemoryBF[index] == 0 || this.myMemoryBF[index] == -1));
                endHoleIndex = index - 1;
            }
            else{
                index++; this.BFosfn[0]++; //inc operations
            }

            if(newHole == true && holeSize >= procSize){//Don't add holes that aren't big enough to fit process
                tempHoles[holeIndex] = new Hole(startHoleIndex, holeSize, procSize);
                holeIndex++;
            }
            //index++; 
        }

        Hole[] holes = new Hole[holeIndex];
        //copy tempHoles array to holes, for sorting(no null holes for comparator)
        for(int i = 0; i < holes.length; i++)
            holes[i] = tempHoles[i];

        Arrays.sort(holes);//sort holes based on amount of space leftover after adding the process, less space left comes before more space left
        boolean holeInHoleArray = false;
        if(holes.length > 0){ //if hole Array contains a Hole then get the first index for best fit
            holeIndex = 0;//Largest Hole after sort
            holeInHoleArray = true;
        }

        if(holeInHoleArray == true && holes[holeIndex] != null){//best Fit found
            this.BFosfn[1]++; //inc success
            offset = holes[holeIndex].index();
            int in = holes[holeIndex].index();
            int size = holes[holeIndex].size();
            for(; in < procSize+offset; in++)
                this.myMemoryBF[in] = BESTFIT;

            for(; in < size+offset; in++){
                this.myMemoryBF[in] = -1;
                this.BFosfn[3]++; //inc new Hole Size
            }
        }
        else{
            //Hole large enough not found, FAIL
            this.BFosfn[2]++; //inc failure
        }

    }

    /**
     * Method worstFit
     * Finds the Worst Fit(Least snug, most amount index holes left over after allocating the process)
     * Uses Hole objects to order all holes after iterating through entire memory to sort the Holes from Best Fit to Worst Fit.  The worst fit hole will be at the end of the Hole Arrray.
     * @param procSize The size of the process to fit.
     */
    public void worstFit(int procSize){
        boolean fit = false;
        Hole[] tempHoles = new Hole[numOfHoles];
        boolean newHole;
        int index = 0;
        int holeIndex = 0;
        int startHoleIndex = 0, endHoleIndex = 0, offset = 0;
        int holeSize;

        while(index < this.myMemoryWF.length){

            holeSize = 0;
            newHole = false;
            if(this.myMemoryWF[index] == 0 || this.myMemoryWF[index] == -1){
                startHoleIndex = index;
                newHole = true;
                do{
                    holeSize++;
                    index++; this.WFosfn[0]++;//inc operations
                }while(index < this.myMemoryWF.length && (this.myMemoryWF[index] == 0 || this.myMemoryWF[index] == -1));
                endHoleIndex = index - 1;
            }
            else{
                index++; this.WFosfn[0]++; //inc operations
            }

            if(newHole == true && holeSize >= procSize){//Don't add holes that aren't big enough to fit process
                tempHoles[holeIndex] = new Hole(startHoleIndex, holeSize, procSize);
                holeIndex++;
            }

        }

        Hole[] holes = new Hole[holeIndex];
        //copy tempHoles array to holes, for sorting(no null holes for comparator)
        for(int i = 0; i < holes.length; i++)
            holes[i] = tempHoles[i];

        Arrays.sort(holes);//sort holes based on amount of space leftover after adding the process, less space left comes before more space left
        //find largest hole that will fit process
        boolean holeInHoleArray = false;
        if(holes.length > 0){ //if hole Array contains a Hole then get the last index for worst fit
            holeIndex = holes.length - 1;//Largest Hole after sort
            holeInHoleArray = true;
        }

        //If there is a hole big enough and the hole != null
        if(holeInHoleArray == true && holes[holeIndex] != null){//worst Fit found (Will be in last index after sorting)
            this.WFosfn[1]++; //inc success
            offset = holes[holeIndex].index();
            int in = holes[holeIndex].index();
            int size = holes[holeIndex].size();
            for(; in < procSize+offset; in++)
                this.myMemoryWF[in] = WORSTFIT;

            for(; in < size+offset; in++){
                this.myMemoryWF[in] = -1;
                this.WFosfn[3]++; //inc new Hole size
            }
        }
        else{
            //Hole large enough not found, FAIL
            this.WFosfn[2]++; //inc failure
        }
    }

    /**
     * Method MemoryToString
     *
     * @param mem The memory array specified.
     * @return The array of Memory to string [1, 2, 3, ...] etc.
     */
    public String MemoryToString(int[] mem){
        String s = "[";
        for( int i = 0; i < mem.length; i++ ){
            s += Integer.toString(mem[i]) + ", ";
        }
        s += "]";
        return s;
    }

}
