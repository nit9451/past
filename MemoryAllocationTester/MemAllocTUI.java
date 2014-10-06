import java.io.*;
import java.util.*;

/**
 * MemAllocTUI, used to get user input for fileName and run different MemoryAlloc sizes.
 * 
 * Name: Phil Schwartz
 * ID: 2368864
 */
public class MemAllocTUI{
    Scanner in;
    String FFfileName;
    String NFfileName;
    String BFfileName;
    String WFfileName;

    public MemAllocTUI(){
        in = new Scanner(System.in);
        String FFfileName = "";
        String NFfileName = "";
        String BFfileName = "";
        String WFfileName = "";

    }

    private String getFileName(String algType){
        String fileName = readMessageWithPrompt("Enter a file path to write "+algType+ " output to (must include .csv file ext for data management): ");
        File file = new File(fileName);
        return fileName;
    }

    private String readMessageWithPrompt(String prompt){
        System.out.print(prompt); System.out.flush();
        while(!in.hasNext()){
            in.nextLine();
            System.out.println("Invalid entry.");
            System.out.println(prompt); System.out.flush();
        }
        String message = in.next();
        in.nextLine();
        return message;
    }

    private void start(){
        PrintWriter FFwriter = null;
        PrintWriter NFwriter = null;
        PrintWriter BFwriter = null;
        PrintWriter WFwriter = null;

        System.out.println("If the file path does not exist, it will be created for you at that file path!");
        //--------------------------------------------------------------1000 Memory Size Printout ------------------------------------
        FFfileName = getFileName("First Fit Algorithm (Mem Size: 1000)");
        NFfileName = getFileName("Next Fit Algorithm (Mem Size: 1000)");
        BFfileName = getFileName("Best Fit Algorithm (Mem Size: 1000)");
        WFfileName = getFileName("Worst Fit Algorithm (Mem Size: 1000)");

        try{
            FFwriter = new PrintWriter(FFfileName);
            NFwriter = new PrintWriter(NFfileName);
            BFwriter = new PrintWriter(BFfileName);
            WFwriter = new PrintWriter(WFfileName);
        }catch(FileNotFoundException e){
            e.printStackTrace();
            System.out.println("An exception occured. Retry!");
            start();
        }
        System.out.println("Running Memory Size 1000...");

        FFwriter.println("FF Avg. Operations, FF Avg. Successes, FF Avg. Failures, FF Avg. New Hole Size,");
        NFwriter.println("NF Avg. Operations, NF Avg. Successes, NF Avg. Failures, NF Avg. New Hole Size,");
        BFwriter.println("BF Avg. Operations, BF Avg. Successes, BF Avg. Failures, BF Avg. New Hole Size,");
        WFwriter.println("WF Avg. Operations, WF Avg. Successes, WF Avg. Failures, WF Avg. New Hole Size,");

        for(int iter = 0; iter < 2000; iter++){
            MemoryAllocation m1000 = new MemoryAllocation(1000);
            int i = 0;
            while(i < 1000){//Do all different fits on memory then println each average to file
                int processSize = m1000.randomProcessSize();
                m1000.firstFit(processSize);
                m1000.nextFit(processSize);
                m1000.bestFit(processSize);
                m1000.worstFit(processSize);
                i++;
            }
            FFwriter.println(m1000.AverageFFosfn(0)+","+m1000.AverageFFosfn(1)+","+m1000.AverageFFosfn(2)+","+m1000.AverageFFosfn(3)+",");
            FFwriter.flush();
            NFwriter.println(m1000.AverageNFosfn(0)+","+m1000.AverageNFosfn(1)+","+m1000.AverageNFosfn(2)+","+m1000.AverageNFosfn(3)+",");    
            NFwriter.flush();
            BFwriter.println(m1000.AverageBFosfn(0)+","+m1000.AverageBFosfn(1)+","+m1000.AverageBFosfn(2)+","+m1000.AverageBFosfn(3)+",");
            BFwriter.flush();
            WFwriter.println(m1000.AverageWFosfn(0)+","+m1000.AverageWFosfn(1)+","+m1000.AverageWFosfn(2)+","+m1000.AverageWFosfn(3)+",");
            WFwriter.flush();
        }

        FFwriter.close();
        NFwriter.close();
        BFwriter.close();
        WFwriter.close();
        System.out.println("Finished Memory Size 1000.");

        //----------------------------------------------------------------10,000 Memory Size Printout ------------------------------------
        FFfileName = getFileName("First Fit Algorithm (Mem Size: 10,000)");
        NFfileName = getFileName("Next Fit Algorithm (Mem Size: 10,000)");
        BFfileName = getFileName("Best Fit Algorithm (Mem Size: 10,000)");
        WFfileName = getFileName("Worst Fit Algorithm (Mem Size: 10,000)");

        try{
            FFwriter = new PrintWriter(FFfileName);
            NFwriter = new PrintWriter(NFfileName);
            BFwriter = new PrintWriter(BFfileName);
            WFwriter = new PrintWriter(WFfileName);
        }catch(FileNotFoundException e){
            e.printStackTrace();
            System.out.println("An exception occured. Retry!");
            start();
        }
        System.out.println("Running Memory Size 10,000...");

        FFwriter.println("FF Avg. Operations, FF Avg. Successes, FF Avg. Failures, FF Avg. New Hole Size,");
        NFwriter.println("NF Avg. Operations, NF Avg. Successes, NF Avg. Failures, NF Avg. New Hole Size,");
        BFwriter.println("BF Avg. Operations, BF Avg. Successes, BF Avg. Failures, BF Avg. New Hole Size,");
        WFwriter.println("WF Avg. Operations, WF Avg. Successes, WF Avg. Failures, WF Avg. New Hole Size,");

        for(int iter = 0; iter < 2000; iter++){
            MemoryAllocation m10000 = new MemoryAllocation(10000);
            int i = 0;
            while(i < 1000){//Do all different fits on memory then println each average to file
                int processSize = m10000.randomProcessSize();
                m10000.firstFit(processSize);
                m10000.nextFit(processSize);
                m10000.bestFit(processSize);
                m10000.worstFit(processSize);
                i++;
            }
            FFwriter.println(m10000.AverageFFosfn(0)+","+m10000.AverageFFosfn(1)+","+m10000.AverageFFosfn(2)+","+m10000.AverageFFosfn(3)+",");
            FFwriter.flush();
            NFwriter.println(m10000.AverageNFosfn(0)+","+m10000.AverageNFosfn(1)+","+m10000.AverageNFosfn(2)+","+m10000.AverageNFosfn(3)+",");    
            NFwriter.flush();
            BFwriter.println(m10000.AverageBFosfn(0)+","+m10000.AverageBFosfn(1)+","+m10000.AverageBFosfn(2)+","+m10000.AverageBFosfn(3)+",");
            BFwriter.flush();
            WFwriter.println(m10000.AverageWFosfn(0)+","+m10000.AverageWFosfn(1)+","+m10000.AverageWFosfn(2)+","+m10000.AverageWFosfn(3)+",");
            WFwriter.flush();
        }

        FFwriter.close();
        NFwriter.close();
        BFwriter.close();
        WFwriter.close();
        System.out.println("Finished Memory Size 10,000.");

        //----------------------------------------------------------------100,000 Memory Size Printout ------------------------------------
        FFfileName = getFileName("First Fit Algorithm (Mem Size: 100,000)");
        NFfileName = getFileName("Next Fit Algorithm (Mem Size: 100,000)");
        BFfileName = getFileName("Best Fit Algorithm (Mem Size: 100,000)");
        WFfileName = getFileName("Worst Fit Algorithm (Mem Size: 100,000)");

        try{
            FFwriter = new PrintWriter(FFfileName);
            NFwriter = new PrintWriter(NFfileName);
            BFwriter = new PrintWriter(BFfileName);
            WFwriter = new PrintWriter(WFfileName);
        }catch(FileNotFoundException e){
            e.printStackTrace();
            System.out.println("An exception occured. Retry!");
            start();
        }
        System.out.println("Running Memory Size 100,000...");

        FFwriter.println("FF Avg. Operations, FF Avg. Successes, FF Avg. Failures, FF Avg. New Hole Size,");
        NFwriter.println("NF Avg. Operations, NF Avg. Successes, NF Avg. Failures, NF Avg. New Hole Size,");
        BFwriter.println("BF Avg. Operations, BF Avg. Successes, BF Avg. Failures, BF Avg. New Hole Size,");
        WFwriter.println("WF Avg. Operations, WF Avg. Successes, WF Avg. Failures, WF Avg. New Hole Size,");

        for(int iter = 0; iter < 2000; iter++){
            MemoryAllocation m100000 = new MemoryAllocation(100000);
            int i = 0;
            while(i < 1000){//Do all different fits on memory then println each average to file
                int processSize = m100000.randomProcessSize();
                m100000.firstFit(processSize);
                m100000.nextFit(processSize);
                m100000.bestFit(processSize);
                m100000.worstFit(processSize);
                i++;
            }
            FFwriter.println(m100000.AverageFFosfn(0)+","+m100000.AverageFFosfn(1)+","+m100000.AverageFFosfn(2)+","+m100000.AverageFFosfn(3)+",");
            FFwriter.flush();
            NFwriter.println(m100000.AverageNFosfn(0)+","+m100000.AverageNFosfn(1)+","+m100000.AverageNFosfn(2)+","+m100000.AverageNFosfn(3)+",");    
            NFwriter.flush();
            BFwriter.println(m100000.AverageBFosfn(0)+","+m100000.AverageBFosfn(1)+","+m100000.AverageBFosfn(2)+","+m100000.AverageBFosfn(3)+",");
            BFwriter.flush();
            WFwriter.println(m100000.AverageWFosfn(0)+","+m100000.AverageWFosfn(1)+","+m100000.AverageWFosfn(2)+","+m100000.AverageWFosfn(3)+",");
            WFwriter.flush();
        }

        FFwriter.close();
        NFwriter.close();
        BFwriter.close();
        WFwriter.close();
        System.out.println("Finished Memory Size 100,000");
    }

    
    static void main(String args){
        (new MemAllocTUI()).start();
    }

}

