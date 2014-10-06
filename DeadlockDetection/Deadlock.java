
import java.util.*;
import java.io.*;

/**
 * Name: Philip Schwartz 
 * CSCI 4401 Programming Assignment 3
 * Date: 11/2013
 * 
 * Deadlock class: Detects whether deadlock occurs in a given Resource Allocation Graph, the input for the RAG is retrieved from user's input file.
 * 
 * <> Sample command line running of program:
 *  - open command line
 *  - ensure you are in same directory as program, input file, and output file.
 *  - running program on command line: $ java Deadlock InputFile.txt > OutputFile.txt
 *  
 *  Sample Formatting of Input File:
 *  1 N 1
 *  2 N 2
 *  3 N 6
 *  5 N 1
 *  1 R 1
 *  etc...
 */
public class Deadlock{
    
    /**
     * Deadlock Constructor
     */
    private Deadlock(){
    }
        
    
    /**
     * Method main
     * @param args A parameter of command line arguments. Need an input file as first command line argument.
     */
    public static void main(String[] args){
        (new Deadlock()).run(args[0]);
    }


    /**
     * Method run
     *
     * @param input must include the first argument on the command line, which should be the input file.
     * Goes through inputFile simulating a resource allocation graph looking for deadlock after each line of input parsed.
     * If deadlock detected the program quits parsing of input file and writes deadlock detected output to output file.
     * Else deadlock was not detected and the program writes completed to output file.
     */
    
    public void run(String input){
        try{
            String inFileName = input; //first argument on command line (input file)
            File inFile = new File(inFileName);
            Scanner in = new Scanner(inFile);
            RAG rag = new RAG();
            int process = 0;
            int resource = 0;
            String NorR = " ";
            boolean cycleDetected = false;
            boolean errorDetected = false;
            int i = 0;
            while(cycleDetected == false && errorDetected == false && in.hasNext()){
                process = Integer.parseInt(in.next().trim());
                NorR = in.next();
                resource = -1*(Integer.parseInt(in.next().trim()));
                if(NorR.equalsIgnoreCase("N")){
                    rag.needsResource(resource, process);
                    if(i != 0 || i != 2)//Only 2 lines of input cannot have deadlock no need to check
                        cycleDetected = rag.checkForCycleDFS();
                }
                else if(NorR.equalsIgnoreCase("R")){
                    rag.releaseResource(resource, process);
                    //no need to check for cycle
                }
                else{
                    rag.errorOccuredInFormatting();
                    errorDetected = true;
                }
                i++;
            }

            //If no cycle detected 
            if(cycleDetected == false){
                rag.noDeadlock();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * Class RAG: Simulates a Resource Allocation Graph using a linked list of Edges, Edges consistings of a pair of resource and process.
     * The LinkedList paired with the Edge simulate the two column table for keeping track of the Resource Allocation Graph (Where Processes are + and Resources are -)
     * Ex: 
     * | -1  |  1  | <- Process 1 is allocated to resource 1
     * | -2  |  2  | <- Process 2 is allocated to resource 2
     * |  3  | -2  | <- Switched columns indicates Process 3 is waiting on resource 2
     * etc.
     */
    class RAG{
        List<Edge> table;
        boolean hasDeadlock;
        boolean hasCycle;
        boolean cycle;

        /**
         * RAG Constructor
         *
         */
        private RAG(){ 
            table = new LinkedList<Edge>();
            hasDeadlock = false;
            hasCycle = false;
            cycle = false;
        }

        /**
         * Class Edge: Used to hold pairs of a Resource and Process.  (Left is the left side of the simulated table, and Right is the right side of the simulated table)
         */
        class Edge{
            int left;
            int right;
            boolean visited;

            
            /**
             * Edge Constructor
             *
             * @param l A parameter for the left side of the table pair
             * @param r A parameter for the right side of the table pair
             */
            private Edge(int l, int r){
                this.left = l;
                this.right = r;
                visited = false;

            }

            @Override
            public boolean equals(Object o){
                Edge c = (Edge) o;
                return (c.left == this.left && c.right == this.right);
            }

        }

        
        /**
         * Method noDeadlock
         * Simple print out method if processing of input completed with no deadlock.
         */
        public void noDeadlock(){
            System.out.println("EXECUTION COMPLETED: No deadlock encountered.");
        }

        /**
         * Method errorOccuredInFormatting
         * Simple print out method if an error in formatting may occured.
         */
        public void errorOccuredInFormatting(){
            System.out.println("ERROR: Improper formatting in Input File, error occured while reading.");
        }

        /**
         * Method checkForCycleDFS
         * Checks for a cycle(deadlock) using Depth First Search, using visited nodes in Edge class.
         * @return The return value is true if a deadlock occured, and false if a deadlock is not present.
         */
        public boolean checkForCycleDFS(){
            //uses global variable hasDeadlock
            int i = 0;
            for(; i < table.size(); i++){ //set all Edges visited to false
                table.get(i).visited = false;
            }
            i = 0;
            for(; hasDeadlock == false && i < table.size(); i++){
                if(table.get(i).visited == false){
                    List<Integer> cycleList = new ArrayList<Integer>(); //keep list of cycles resources and processes
                    hasDeadlock = hasDeadlock(table.get(i), cycleList);
                }
            }
            return hasDeadlock;
        }

        
        /**
         * Method hasDeadlock
         * Recursively searches for a cycle in the graph as long as a next Edge is found and hasCycle method returns false.
         * Uses a dummy Edge to determine the end of a traversal, where a next Edge cannot be found (Edge(0, 0) where Edge.left == 0, and Edge.right == 0).
         *
         * @param e A parameter that represents the current Edge being traversed for deadlock detection.
         * @param cycleList A parameter the current traversal list being kept incase a deadlock occurs.
         * @return The return value is true if a cycle is detected
         */
        public boolean hasDeadlock(Edge e, List<Integer> cycleList){
            //uses global variable hasCycle
            e.visited = true;
            Edge nextEdge = getNext(e); //get the next edge to search for a cycle
            cycleList.add(e.left);

            if(hasCycle(cycleList)){
                hasCycle = true;
                Collections.sort(cycleList); //To get processes and resources from least to greatest
                System.out.println("DEADLOCK DETECTED: Processes "+processesInCycle(cycleList)+"and Resources "+resourcesInCycle(cycleList)+"are found in a cycle.");
            }
            else{ //if no next edge found break recursion here
                if(!(nextEdge.right == 0 && nextEdge.left == 0))
                    hasDeadlock(nextEdge, cycleList); 
                else
                    hasCycle = false;
            }
            return hasCycle;

        }

        /**
         * Method processesInCycle
         * If a cycle is detected in hasDeadlock, this method is called to pull out the processes in the cycleList, 
         * and order them from least to greatest, and return a string of these processes for print out.
         * @param cycleList A parameter that contains the cycleList where a cycle was detected.
         * @return The return value is a String of processes contained in the cycle, ordered from least to greatest.
         */
        public String processesInCycle(List<Integer> cycleList){
            String s = "";
            for(int i = 0; i < cycleList.size(); i++){
                if(cycleList.get(i) > 0)
                    s+= (Integer.toString(cycleList.get(i)) + ", ");
            }
            return s;
        }

        /**
         * Method resourcesInCycle
         * If a cycle is detected in hasDeadlock, this method is called to pull out the resources in the cycleList, convert them to positive numbers, 
         * and order them from least to greatest, and return a string of these resources for print out.
         * @param cycleList A parameter that contains the cycleList where a cycle was detected.
         * @return The return value is a String of resources contained in the cycle, ordered from least to greatest.
         */
        public String resourcesInCycle(List<Integer> cycleList){
            String s = "";
            List<Integer> tempList = new LinkedList<Integer>();
            for(int i = 0; i < cycleList.size(); i++){
                if(cycleList.get(i) < 0)
                    tempList.add(cycleList.get(i)*-1);
            }

            Collections.sort(tempList); //sort the positive list of resources  
            for(int i = 0; i < tempList.size(); i++)
                s+= (Integer.toString(tempList.get(i)) + ", ");

            return s;
        }
        
        /**
         * Method hasCycle
         * Checks for a repeated process # in the cycleList to determine whether a cycle exists
         * @param cycleList A parameter that consists of the current cycleList from traversing the Edges.
         * @return The return value returns true if the last element in the List is repeated at any point earlier in the list, then a cycle has occured.  Returns false otherwise.
         */
        public boolean hasCycle(List<Integer> cycleList){

            int last = cycleList.get(cycleList.size()-1); //get the last added element of cycleList for comparison
            for(int i = 0; cycle == false && i < cycleList.size()-1; i++){
                if(cycleList.get(i).equals(last)){
                    cycle = true;
                    cycleList.remove(i);//remove last added element from cycleList for printout of process/resource in cycle for in proccessesInCycle method and resourcesInCycle method.
                }
            }
            return cycle;
        }

        //Get next Edge from previous edge
        /**
         * Method getNext
         * Gets the next Edge of traversal by checking if the right side of the edge is equal to any edge's left side in the entire table.
         * If an edge is found, then it is the next Edge to continue traversing for the checking of a cycle.
         * @param e A parameter pointing to the current Edge.
         * @return The return value is the next Edge to be processed.
         */
        public Edge getNext(Edge e){
            int i = 0;
            Edge next = new Edge(0,0);
            boolean nextFound = false;
            for(; nextFound == false && i < table.size(); i++){
                if(table.get(i).left == e.right){
                    next = table.get(i);
                    nextFound = true;
                }
            }
            return next;

        }

        /**
         * Method needsResource
         * A process requests a resource. If the resource is taken then the process must wait. If the resource is not on the left side of the table(taken) then
         * the new Edge is created and added to the end of the table and the resource is allocated to the process.
         *
         * @param resource A parameter containing the resource needed.
         * @param process A parameter containing the process requesting the resource.
         */
        public void needsResource(int resource, int process){
            System.out.print("Process " + Integer.toString(process)+ " needs resource " +Integer.toString(resource*-1) + " - ");
            boolean exists = false;
            int i = 0;
            while(exists == false && i < table.size()){
                if(table.get(i).left == resource){
                    exists = true; //Resource is taken right now
                    System.out.println("Process " +Integer.toString(process) + " must wait.");
                }
                i++;
            }
            if(exists == false){ //If the resource isn't taken add the edge to the table
                Edge e = new Edge(resource, process);
                table.add(e);
                System.out.println("Resource " +Integer.toString(resource*-1) + " is allocated to process " + Integer.toString(process)+".");
            }
            else{
                Edge e = new Edge(process, resource); //process waiting on left side of table
                table.add(e); //add to end
            }
        }

        
        /**
         * Method releaseResource
         * A process is realeasing a resource.  The method searches for the Edge in the table.  If it exists then the Edge is removed. If it is not found, an error occurs and is printed to the output.
         * Once an Edge is found and removed, the method searches for a process waiting on the specified resource.  If no process found waiting on resource then the resource is declared free.  
         * If a process is waiting on this resource then the longest waiting process is chosen by traversing down the table and grabbing the first process waiting.  
         * If a process is found waiting then the left and right of the Edge are swapped and the resource is now allocated to the process.
         *
         * @param resource A parameter containing the resource to release.
         * @param process A parameter containing the process holding the resource to release.
         */
        public void releaseResource(int resource, int process){
            System.out.print("Process " +Integer.toString(process)+ " releases resource " +Integer.toString(resource*-1)+ " - ");
            //create edge to find equal edge in table if it exists
            Edge e = new Edge(resource, process);
            boolean edgeFound = false;
            int j = 0;
            while(edgeFound == false && j < table.size()){//find edge
                if(e.equals(table.get(j)))
                    edgeFound = true;
                else
                    j++;
            }

            //remove edge if the edge was found
            if(edgeFound)
                table.remove(j);
            else{
                System.out.println("ERROR EDGE NOT FOUND");
                errorOccuredInFormatting();
            }

            //Look for a process waiting for resource (resource will be on right side of table)
            //Returns first instance index of edge with resource on right side of table (process waiting on resource)
            int i = 0;
            boolean resourceFoundBeingWaitedOn = false;
            while(resourceFoundBeingWaitedOn == false && i < table.size()){ 
                if(table.get(i).right == resource)
                    resourceFoundBeingWaitedOn = true;
                else
                    i++;
            }

            if(resourceFoundBeingWaitedOn){//Swap left and right in table so now resource is allocated to process
                int newLeft = table.get(i).right;
                int newRight = table.get(i).left;
                table.get(i).left = newLeft;
                table.get(i).right = newRight;
                System.out.println("Resource "+(resource*-1)+" is allocated to process "+newRight+".");
            }
            //else resource is now free
            else{
                System.out.println("Resource " +(resource*-1)+" is now free.");
            }

        }   

    }
}
