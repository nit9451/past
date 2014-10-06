import java.util.*;

/**
 * A sorter for a generic array of elements, so long as the element implements Comparable interface.
 * 
 * @Philip Schwartz 
 * 
 */ 
public class GenericSorter
{
  

  
  public static <T extends Comparable<T>> void sort(T A[]){
    if(A.length == 0){
    }
    else{
      int size = A.length;
      quicksort(A, 0, size-1);
    }
  }
  
 
  private static <T extends Comparable<T>> void quicksort(T A[], int low, int high){
    int i = low;
    int j = high;
    int pivot = (low + (high-low)/2);
    

    while(i <= j){
      while(A[i].compareTo(A[pivot]) < 0){
        i++;
      }

      while(A[j].compareTo(A[pivot]) > 0){
        j--;
      }
      
      if(i <= j){
        exchange(A, i, j);
        i++;
        j--;
      }
    }
    
    if(low < j)
      quicksort(A, low, j);
    if(i < high)
      quicksort(A, i, high);
  }
  
  //Helper exchange method.
   private static <T> void exchange(T A[], int i, int j){
      T temp = A[i];
      A[i] = A[j];
      A[j] = temp;
    }
   
  }
  
  
