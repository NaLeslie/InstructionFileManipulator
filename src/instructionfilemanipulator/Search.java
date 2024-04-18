/*
 * Created: 2020-12-14
 * Updated: 2024-01-11
 * Nathaniel Leslie
 */
package instructionfilemanipulator;

/**
 * A class containing functions for searching lists efficiently.
 * @author Nathaniel
 */
public class Search {
    /**
     * Returns the index in the array that has the greatest value that is strictly less than the key.
     * @param key the value against which array elements will be compared.
     * @param array an array of values sorted in order of lowest to highest.
     * @param min the minimum index to be considered
     * @param max the maximum index to be considered
     * @return -1 if all values in the array above the minimum index are greater than or equal to the key. Otherwise returns the highest index for which array[index] &lt; key is true.
     */
    private static int findSmaller(double key, double[] array, int min, int max){
        if(array[max] < key){
            return max;
        }
        else if(array[min] >= key){
            return -1;
        }
        else if(max-min <=1){
            return min;
        }
        else{
            int mid = (max - min) / 2 + min;
            if(array[mid] == key){
                return mid - 1;
            }
            else if(array[mid] < key){
                return findSmaller(key, array, mid, max);
            }
            else{
                return findSmaller(key, array, min, mid);
            }
        }
    }
    
    /**
     * Returns the index in the array that has the greatest value that is strictly less than the key. Employs a binary-search.
     * @param key the value against which array elements will be compared.
     * @param array an array of values sorted in order of lowest to highest.
     * @return -1 if all values in the array are greater than or equal to the key. Otherwise returns the highest index for which array[index] &lt; key is true.
     */
    public static int findSmaller(double key, double[] array){
        return findSmaller(key, array, 0, array.length - 1);
    }
    
    /**
     * Returns the index in the array that has the greatest value that is strictly less than the key.
     * @param key the value against which array elements will be compared.
     * @param array an array of values sorted in order of lowest to highest.
     * @param min the minimum index to be considered
     * @param max the maximum index to be considered
     * @return -1 if all values in the array above the minimum index are greater than or equal to the key. Otherwise returns the highest index for which array[index] &lt; key is true.
     */
    private static int findExact(double key, double[] array, int min, int max){
        if(array[max] < key){
            return -1;
        }
        else if(array[min] > key){
            return -1;
        }
        else if(max-min <=1){
            if(array[min] == key){
                return min;
            }
            if(array[max] == key){
                return max;
            }
            return -1;
        }
        else{
            int mid = (max - min) / 2 + min;
            if(array[mid] == key){
                return mid;
            }
            else if(array[mid] < key){
                return findExact(key, array, mid, max);
            }
            else{
                return findExact(key, array, min, mid);
            }
        }
    }
    
    /**
     * Returns the index in the array that has the greatest value that is strictly less than the key. Employs a binary-search.
     * @param key the value against which array elements will be compared.
     * @param array an array of values sorted in order of lowest to highest.
     * @return -1 if all values in the array are greater than or equal to the key. Otherwise returns the highest index for which array[index] &lt; key is true.
     */
    public static int findExact(double key, double[] array){
        return findExact(key, array, 0, array.length - 1);
    }
}
