/*
Linus Chen
ICS4U1
Strings set
*/
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.lang.*;

public class CLStrings {

	static Scanner sc;

	public static boolean isPalindrome(String str){ //checks if string is a palindrome
		String temp = ""; //string to hold modified string
		for(int i=0; i<str.length(); i++){
			char c = str.charAt(i);
			if((c>='a'&&c<='z')||(c>='A'&&c<='Z')||(c>='0'&&c<='9')) temp += Character.toString(c); //only keep the letters and numbers
		}
		temp = temp.toUpperCase();	//make sure every character is same case
		int start = 0, end = temp.length() - 1;
		for(int i=0; i<str.length()/2; i++){ 
			if(temp.charAt(start)!=temp.charAt(end)) //if start and end don't match then string is not a palindrome
				return false;
			start++;
			end--;
		}
		return true;
	}

	public static String _shift(String str, int shift){ //shifts a given string by a given number
		String temp = ""; //string to store result
		for(int i=0; i<str.length(); i++){
			char c = str.charAt(i);
			if(c>='a'&&c<='z'){ int ascii=(int)c-'a'; temp += Character.toString((char)((ascii+shift)%26+'a'));}
			else if(c>='A'&&c<='Z'){ int ascii=(int)c-'A'; temp += Character.toString((char)((ascii+shift)%26+'A'));}
			else temp += Character.toString(c);
		}
		return temp;
	}

	public static String encrypt(String str, String scramble){ //scrambles a given string based on a given scrambled alphabet
		for(int i=0; i<str.length(); i++){
			char c = str.charAt(i);
			if(c>='A'&&c<='Z'){
				char replace = scramble.charAt((int)(c-'A'));
				str = str.substring(0,i) + replace + str.substring(i+1);
			}
		}
		return str;
	}

	public static void Palindrome(){ //Driver for palidrome
		System.out.print("Gimme a string: ");
		String s = sc.nextLine();
		boolean pal = isPalindrome(s);
		System.out.printf(pal ? "%s is a palindrome!" : "%s is not a palindrome!", s);
	}

	public static void ShiftCode(){ //Driver for _shift
		System.out.print("Gimme a string: ");
		String s = sc.nextLine();
		System.out.print("How many characters do you want to shift by? ");
		int shift = sc.nextInt();
		String res = _shift(s,shift);
		System.out.printf("Shifted string: %s",res);
	}

	public static void CryptoCode(){ //Driver for cryptocode
		System.out.print("Gimme a string: ");
		String s = sc.nextLine();
		String scramble = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; //start off with normal alphabet
		char[] alphabet = scramble.toCharArray(); 
		for(int i=0; i<25; i++){ //scramble the alphabet
			int swap = (int) (Math.random() * 25) + 1;
			char temp = alphabet[i];
			alphabet[i] = alphabet[swap];
			alphabet[swap] = temp;
		}
		scramble = "";
		for(int i=0; i<25; i++)
			scramble += alphabet[i]; //store new alphabet
		String res = encrypt(s, scramble);
		System.out.printf("%s encrypted with the scrambled alphabet %s becomes: %s", s,scramble,res);
	}

	public static void SecretCode(){ //Secret Code function
		System.out.printf("Input file name:\n");
		String s = sc.nextLine();
		try{
			Scanner filereader = new Scanner(new File(s)); //new scanner for file reader
		    String keyphrase = filereader.nextLine(); //holds key phrase
		    String res = ""; //the decoded message
		    while(filereader.hasNextInt()){ //loop until no more stuff in file
		        int num = filereader.nextInt();
		        res += keyphrase.charAt(num);
		    }
		    System.out.printf("\nThe decrypted message is: %s\n",res);
		} catch (FileNotFoundException e) { //catch if file doens't exist
			System.out.printf("LOL STOP TROLLING!!! NO SUCH FILE EXISTS!!!\n");
		}
	}

	public static void main(String[] args){
		sc = new Scanner(System.in);

		char choice;
		do //menu
        {
            System.out.println ("\n\n\nStrings Menu");
            System.out.println ("--------------");
            System.out.println ("1. Palindrome");
            System.out.println ("2. Shift Code");
            System.out.println ("3. Crypto Code");
            System.out.println ("4. Secret Code");
            System.out.println ("0. Quit");
            choice = sc.next().charAt(0);
            sc.nextLine(); //clear
            if (choice == '1')
                Palindrome(); // call function
            else if (choice == '2')
            	ShiftCode();
            else if (choice == '3')
            	CryptoCode();
            else if (choice == '4')
            	SecretCode();
        }
        while (choice != '0'); // exit when 0   
	}
}