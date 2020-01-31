/*
Linus Chen and Edward Shen

ICS4U1

Recursion presentation
*/

import java.util.*;

public class CoinChange {

	final static int INF = 1000000000; 

	public static int Change(int [] arr, int idx, int val){ //minimum number of coins 
		if(val < 0) return INF;
		if(val == 0) return 0;
		if(idx == -1) return INF;

		int res = INF;
		res = Math.min(res, 1+Change(arr, idx, val-arr[idx]));
		res = Math.min(res, Change(arr, idx-1, val));

		return res;
	}

	public static int numOfWays(int[] arr, int idx, int val){ //number of combinations
		if(idx==-1) return 0;
		if(val<0) return 0;
		if(val==0) return 1;
		return numOfWays(arr, idx-1, val) + numOfWays(arr, idx, val-arr[idx]); 
	}

	public static void main(String [] args){
		Scanner in = new Scanner(System.in);
		System.out.print("How many coins: ");
		int val, size = in.nextInt();
		int[] coins = new int[size];
		for(int i=0; i<size; i++){
			System.out.print("Coin value: ");
			coins[i] = in.nextInt();
		}
		System.out.print("Value to form: ");
		val = in.nextInt();

		Arrays.sort(coins);

		//int solution = Change(coins, size-1, val); //uncomment for minimum number of coins needed
		int ways = numOfWays(coins, size-1, val); //uncomment for number of possible combinations
		/* //uncomment for minimum number of coins needed
		if(solution==INF)
			System.out.printf("No possible solution.");
		else
			System.out.printf("Minimum number of coins needed: %d\n", solution);
			*/
		System.out.printf("There are %d ways to form %d\n", ways, val); //uncomment for number of possible combbinations

	}
}