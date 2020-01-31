/*
Linus Chen
ICS4U1
1D Array Review
*/

import java.util.*;
public class CLArrays {
	
	static Scanner sc;
	static int[] arr;

	public static void generate(int n){ //generate data with given size of array, n
		arr = new int[n];
		for(int i=0; i<n; i++) arr[i] = (int) (Math.random() * 21) - 5;  
	} 

	public static void printData(int[] array){ //print given array
		for(int i=0; i<array.length; i++) System.out.printf("%d ", array[i]); 
		System.out.printf("\n\n");
	}

	public static void sums(){ //Exercise 1 - calculate the three sums
		int even=0, odd=0, all=0;
		for(int i=0; i<arr.length; i++) {
			if(arr[i]%2==0) even += arr[i]; //is element even?
			else odd += arr[i]; 
			all += arr[i];
		}
		//display results
		System.out.printf("The sum of all elements in the array is %d\n",all);
		System.out.printf("The sum of all even elements in the array is %d\n",even);
		System.out.printf("The sum of all odd elements in the array is %d\n\n",odd);
	}

	public static void twoLargest(){ //Exercise 2 - find the two largest elements in array
		int a = (int)-1e9, b = (int)-1e9; //set to negative infinity
        for(int i=0;i<arr.length;i++){
              if(arr[i]>a){ //arr[i] is the new largest element
                    b = a; //second largest element is the old largest element
                    a = arr[i];
              }
              else if(arr[i]>b) b = arr[i];
        }
        // displays results
        System.out.printf("The largest value in the array is %d\n",a);
        System.out.printf("The second largest value in the array is %d\n\n",b);
	}

	public static void closestToZero(){ //Exercise 3 - find element that is closest to zero
		int cur = (int)1e9; //set to infinity
		for(int i=0; i<arr.length; i++) if(Math.abs(0-arr[i])<Math.abs(0-cur)) cur = arr[i]; //check distance to zero and replace is necessary
		System.out.printf("The element closest to zero is %d\n\n",cur);
	}

	public static void reverse(){ //Exerrcise 4 - reverse the original array
		int[] res = new int[arr.length];
		for(int i=0; i<arr.length; i++) res[arr.length-1-i] = arr[i]; 
		System.out.printf("Reversed array:\n");
		printData(res);
	}	

	public static void smooth(){ //Exercise 5 - smooth values of array
		int[] smooth = new int[arr.length];
		smooth[0] = (arr[0]+arr[1])/2; //smoothing first and last elements
		smooth[smooth.length-1] = (arr[arr.length-1]+arr[arr.length-2])/2;
		for(int i=1; i<smooth.length-1; i++) smooth[i] = (arr[i-1]+arr[i]+arr[i+1])/3; //smooth middle data
		System.out.printf("Smoothed array:\n");
		printData(smooth);
	}

	public static void dataTweaker(){ //Exercise 6 - finds the average of data without the greatest outlier
		double avg = 0.0, dist = 0, newAvg = 0.0; //original average, distance to average, new Average respectively
		int idx = 0; // holds index of outlier
		for(int i=0; i<arr.length; i++) avg += arr[i]; 
		avg /= arr.length; //calculate original average
		for(int i=0; i<arr.length; i++) //check distance of each element to average
			if(Math.abs(avg-arr[i])>dist){
				dist = Math.abs(avg-arr[i]);
				idx = i;
			}
		for(int i=0; i<arr.length; i++) //calculate new average without greatest outlier
			if(i!=idx) newAvg += arr[i];
		newAvg /= (arr.length-1);
		//display results
		System.out.printf("The original average is %f\n", avg);
		System.out.printf("The most distant value is %d\n", arr[idx]);
		System.out.printf("The tweaked average is %f\n\n", newAvg);
	}

	public static void main(String[] args){
		//call all the methods 
		generate(20);
		System.out.printf("Data set:\n");
		printData(arr);
		sums(); //Exercise 1
		twoLargest(); //Exercise 2
		closestToZero(); //Exercise 3
		reverse(); //Exercise 4
		smooth(); //Exercise 5
		dataTweaker(); //Exercise 6
	}
}