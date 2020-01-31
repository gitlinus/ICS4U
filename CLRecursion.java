/*
Linus CHen
ICS4U1
Feb 21, 2019
*/

import java.util.*;
public class CLRecursion {
	static Scanner sc;

	static final int size = 1000000;
	static boolean[] sieve = new boolean[size];

	public static void SieveOfEratosthenes(){ //sieve of eratosthenes
		sieve[0] = true;
		sieve[1] = true;
		for(int i=2; i<Math.sqrt(size); i++){ //generate sieve
			for(int j=i*i; j<size; j+=i)
				if(!sieve[j])
					sieve[j] = true;
		}
	}

	public static boolean NonRecursivePrime(int n){ //checks if number is prime by checking sieve
		if(n<0) return false;
		if(!sieve[n]) return true; 
		return false;
	}

	public static boolean RecursivePrime(int num, int fact){ //checks if prime
		if(num<=0||num==1) return false; //is number 1 or less
		if(fact==1) return true; //checked all values from num-1 to 1
		if(num%fact==0) return false; //check if divisible
		return RecursivePrime (num, fact-1); //check next factor
	}

	public static int fibonacci(int n){ 
		if(n<=0) return 0; //base cases
		if(n==1) return 1; //base cases
		return fibonacci(n-1) + fibonacci(n-2);
	}

	public static int nonRecfibonacci(int n){
		if(n<=0) return 0; //base cases
		if(n==1) return 1;
		int fib1 = 0; //starting values
		int fib2 = 1;
		int temp = 1;
		for(int i=0; i<n-1; i++){
			temp = fib1 + fib2; //add previous two terms
			fib1 = fib2; //update last two terms
			fib2 = temp;
		}
		return temp;
	}

	public static void RunPrime(){ //runs recursive prime function
		System.out.printf("Gimme a number you want to check if it's prime: ");
		int num = sc.nextInt();
		boolean isPrime = RecursivePrime(num,num-1);
		System.out.printf(isPrime ?"%d is prime\n":"%d is not prime\n", num);
	}

	public static void RunPrime2(){ //runs non recursive prime function
		System.out.printf("Gimme a number you want to check if it's prime: ");
		int num = sc.nextInt();
		boolean isPrime = NonRecursivePrime(num);
		System.out.printf(isPrime ?"%d is prime\n":"%d is not prime\n", num);
	}

	public static void RunFib(){ //runs recursive fib function
		System.out.printf("Enter nth Fibonacci term: ");
		int n = sc.nextInt();
		int fib = fibonacci(n);
		System.out.printf("The %dth Fibonacci term is %d", n, fib);
	}

	public static void RunFib2(){ //runs non recursive fib function
		System.out.printf("Enter nth Fibonacci term: ");
		int n = sc.nextInt();
		int fib = nonRecfibonacci(n);
		System.out.printf("The %dth Fibonacci term is %d", n, fib);
	}

	public static void main(String[] args){
		sc = new Scanner(System.in);
		SieveOfEratosthenes();

		char choice;
		do //menu
        {
            System.out.println ("\n\n\nRecursion Menu");
            System.out.println ("--------------");
            System.out.println ("1. Recursive Fibonacci");
            System.out.println ("2. Non-recursive Fibonacci");
            System.out.println ("3. Recursive Prime Checker");
            System.out.println ("4. Non-recursive Prime Checker");
            System.out.println ("0. Quit");
            choice = sc.next().charAt(0);

            if (choice == '1')
                RunFib(); // call function
            else if (choice == '2')
            	RunFib2();
            else if (choice == '3')
            	RunPrime();
            else if (choice == '4')
            	RunPrime2();
        }
        while (choice != '0'); // exit when 0   

	}
}