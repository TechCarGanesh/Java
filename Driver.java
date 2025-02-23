/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.hashing_ex2;

import java.util.Iterator;

/**
 *
 * @author Ganesh Kumar
 */
public class Driver
{
	public static void main(String[] args) 
	{
		testDictionary();
		System.out.println("\n\nDone.");
	}  // end main

	public static void testDictionary()
	{
		String dirk   = "Dirk";
		String abel   = "Abel";
		String miguel = "Miguel";
		String tabbie = "Tabatha";
		String tom    = "Tom";
		String sam    = "Sam";
		String reiss  = "Reiss";
		String bette  = "Bette";
		String carole = "Carole";
		String derek  = "Derek";
		String nancy  = "Nancy";
		String bogus  = "Bo"; 

		// Create a dictionary of initial size 11
		DictionaryInterface<String, String> nameList = new HashedDictionary<>();

		System.out.println("Create a dictionary:\n");
		System.out.println("Initial dictionary should be empty; isEmpty() returns " + 
				nameList.isEmpty());

		// Test add
		System.out.println("\n\nTesting add():\n");
		nameList.add(dirk,   "555-1234");
		nameList.add(abel,   "555-5678");
		nameList.add(miguel, "555-9012");
		nameList.add(tabbie, "555-3456");
		nameList.add(tom,    "555-5555");
		nameList.add(sam,    "555-7890");
		nameList.add(reiss,  "555-2345");
		nameList.add(bette,  "555-7891");
		nameList.add(carole, "555-7892");
		nameList.add(derek,  "555-7893");
		nameList.add(nancy,  "555-7894");

		System.out.println(nameList.getSize() + " (should be 11) items in dictionary, as follows:\n");
		display(nameList);

		// Test getValue
		System.out.println("\n\nTesting getValue():\n");
		System.out.println("\nAbel:   " + nameList.getValue(abel)   + " should be 555-5678");
		System.out.println("\nSam:    " + nameList.getValue(sam)    + " should be 555-7890");
		System.out.println("\nTom:    " + nameList.getValue(tom)    + " should be 555-5555");
		System.out.println("\nReiss:  " + nameList.getValue(reiss)  + " should be 555-2345");
		System.out.println("\nMiguel: " + nameList.getValue(miguel) + " should be 555-9012");

		// Test contains
		System.out.println("\n\n\nTesting contains():\n");

		if ( nameList.contains(sam) )
			System.out.println("\nSam is in dictionary - OK");
		else 
			System.out.println("Error in contains()");

		if ( nameList.contains(abel) )
			System.out.println("\nAbel is in dictionary - OK");
		else 
			System.out.println("Error in contains()");

		if ( nameList.contains(miguel) )
			System.out.println("\nMiguel is in dictionary - OK");
		else 
			System.out.println("Error in contains()");

		if ( nameList.contains(tom) )
			System.out.println("\nTom is in dictionary - OK");
		else 
			System.out.println("Error in contains()");

		if (!nameList.contains(bogus))
			System.out.println("\nBo is not in dictionary - OK");
		else 
			System.out.println("Error in contains()");

		// Remove first item
		System.out.println("\n\n\nRemoving first item Abel - Abel's number is " +
				nameList.remove(abel) + " should be 555-5678");

		// Test replacing value
		System.out.println("Replacing phone number of Reiss and Miguel:\n");
		String oldNumber = nameList.add(reiss,  "555-5432");
		System.out.println("Reiss's old number " + oldNumber + " is replaced by 555-5432");
		oldNumber = nameList.add(miguel, "555-2109");     
		System.out.println("Miguel's old number " + oldNumber + " is replaced by 555-2109");

		System.out.println("\n" + nameList.getSize() + 
				" (should be 10) items in dictionary, as follows:\n");
		display(nameList);

		// Remove interior and last items
		System.out.println("\n\nRemoving interior item Reiss:\n");
		nameList.remove(reiss);
		System.out.println("\n" + nameList.getSize() +
				" (should be 9) items in dictionary, as follows:\n");
		display(nameList);
		System.out.println("\n\nRemoving last item Nancy:\n");
		nameList.remove(nancy);
		System.out.println("\n" + nameList.getSize() +
				" (should be 8) items in dictionary, as follows:\n");
		display(nameList);

		// Remove Bo (not in dictionary)
		System.out.println("\nRemoving Bo (not in dictionary):\n");
		String result = nameList.remove(bogus);
		if (result == null)
			System.out.println("Bo was not found in the dictionary.");
		else
			System.out.println("Error in remove().");

		System.out.println("\n\n" + nameList.getSize() + 
				" (should be 8) items in dictionary, as follows:\n");
		display(nameList);

		// Test clear
		System.out.println("\n\nTesting clear():\n");
		nameList.clear();

		System.out.println("Dictionary should be empty; isEmpty() returns " + 
				nameList.isEmpty());
	} // testDictionary

	public static void display(DictionaryInterface<String, String> dictionary)
	{
		Iterator<String> keyIterator   = dictionary.getKeyIterator();
		Iterator<String> valueIterator = dictionary.getValueIterator();

		while (keyIterator.hasNext() && valueIterator.hasNext())
			System.out.println(keyIterator.next() + " : " + valueIterator.next());
		System.out.println();
	} // end display
}  // end Driver
