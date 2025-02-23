/*
Name: Ganesh Kumar
Team Name: TheBugatti
Instructor: Professor Mirsaeid Abolghasemi
Date: 02/17/2025
Project Information: Create a class HashedDictionary. 
A class that implements the ADT Hashing by using a chain of TableEntry. 
*/
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.hashing_ex2;

/**
 *
 * @author Ganesh Kumar
 */
import java.util.Hashtable;
import java.util.Iterator;

import java.util.NoSuchElementException;

public class HashedDictionary<K, V> implements DictionaryInterface<K, V>
{
   // The dictionary:
    private int numberOfEntries;
    private int locationsUsed; // number of table locations not null
    private static final int DEFAULT_CAPACITY = 5;     // Must be prime
    private static final int MAX_CAPACITY = 10000;

   // The hash table:

    private TableEntry<K, V>[] hashTable;
    private int tableSize;                             // Must be prime
    private static final int MAX_SIZE = 2 * MAX_CAPACITY;
    private boolean initialized = false;
    private static final double MAX_LOAD_FACTOR = 0.5; // Fraction of hash table that can be filled
    public HashedDictionary()
    {
        this(DEFAULT_CAPACITY); // Call next constructor
    } // end default constructor

    public HashedDictionary(int initialCapacity)
    {
        checkCapacity(initialCapacity);
        numberOfEntries = 0;    // Dictionary is empty
        // Set up hash table:
        // Initial size of hash table is same as initialCapacity if it is prime;
        // otherwise increase it until it is prime size
        int tableSize = getNextPrime(initialCapacity);
        checkSize(tableSize);
        // The cast is safe because the new array contains null entries
        @SuppressWarnings("unchecked")
        TableEntry<K, V>[] temp = (TableEntry<K, V>[])new TableEntry[tableSize];
        hashTable = temp;
        initialized = true;
    } // end constructor

// -------------------------

// We've added this method to display the hash table for illustration and testing

// -------------------------

  public void displayHashTable()
  {
      checkInitialization();
      for (int index = 0; index < hashTable.length; index++)
      {
          if (hashTable[index] == null)
              System.out.println("null ");
          else if (hashTable[index].isRemoved())
              System.out.println("removed state");
          else
              System.out.println(hashTable[index].getKey() + " " + hashTable[index].getValue());
      } // end for

      System.out.println();
    } // end displayHashTable

// -------------------------
    // Adds a new key-value entry to the dictionary. If key is already in the dictionary,
    // returns its corresponding value and replaces it in the dictionary with value.
    public V add(K key, V value)
    {
        V oldValue; // value to return
        if (isHashTableTooFull())
            rehash();
        int index = getHashIndex(key);
        index = probe(index, key); // check for and resolve collision
        // Assertion: index is within legal range for hashTable
        assert (index >= 0) && (index < hashTable.length);
        if ( (hashTable[index] == null) || hashTable[index].isRemoved())
        { // key not found, so insert new entry
            hashTable[index] = new TableEntry<K,V>(key, value);
            numberOfEntries++;
            locationsUsed++;
            oldValue = null;
        }
        else
        { // key found; get old value for return and then replace it
            oldValue = hashTable[index].getValue();
            hashTable[index].setValue(value);
        } // end if
        return oldValue;
    } // end add
    
    private void rehash()
    {
        TableEntry<K, V>[] oldTable = hashTable;
        int oldSize = hashTable.length;
        int newSize = getNextPrime(oldSize + oldSize);
        hashTable = new TableEntry[newSize]; // increase size of array
        numberOfEntries = 0; // reset number of dictionary entries, since
        // it will be incremented by add during rehash
        locationsUsed = 0;
        // rehash dictionary entries from old array to the new and bigger
        // array; skip both null locations and removed entries
        for (int index = 0; index < oldSize; index++)
        {
            if ( (oldTable[index] != null) && oldTable[index].isIn() )
                add(oldTable[index].getKey(), oldTable[index].getValue());
        } // end for
    } // end rehash
    
       /** Removes a specific entry from this dictionary.
       @param key  An object search key of the entry to be removed.
       @return  Either the value that was associated with the search key
                or null if no such object exists. */
    public V remove(K key)
    {
        V removedValue = null;
        int index = getHashIndex(key);
        index = locate(index, key);
    if (index != -1)
    { // key found; flag entry as removed and return its value
        removedValue = hashTable[index].getValue();
        hashTable[index].setToRemoved();
        numberOfEntries--;
        locationsUsed--;
    } // end if
    // else key not found; return null
    return removedValue;
    } // end remove
    
    /** Retrieves from this dictionary the value associated with a given
       search key.
       @param key  An object search key of the entry to be retrieved.
       @return  Either the value that is associated with the search key
                or null if no such object exists. */
    public V getValue(K key)
    {
        V result = null;
        int index = getHashIndex(key);
        index = locate(index, key);
        if (index != -1)
            result = hashTable[index].getValue(); // key found; get value
        // else key not found; return null
        return result;
    } // end getValue
    
    /** Sees whether a specific entry is in this dictionary.
       @param key  An object search key of the desired entry.
       @return  True if key is associated with an entry in the dictionary. */
    public boolean contains(K key)
    {
        int index = getHashIndex(key);
        index = locate(index, key);
        if (index != -1)
            return true;
        else
            return false;
    } // end contains
    
    /** Sees whether this dictionary is empty.
       @return  True if the dictionary is empty. */
   public boolean isEmpty()
   {
       if(numberOfEntries == 0) {
           return true;
       }
       else {
           return false;
       }
   } // end isEmpty
   
   /** Gets the size of this dictionary.
       @return  The number of entries (key-value pairs) currently
                in the dictionary. */
   public int getSize()
   {
       return numberOfEntries;
   } // end getSize
   
   /** Removes all entries from this dictionary. */
    public final void clear()
    {
        numberOfEntries = 0;
        hashTable = null;
    } // end clear
    
    /** Creates an iterator that traverses all search keys in this dictionary.
       @return  An iterator that provides sequential access to the search
                keys in the dictionary. */
    public Iterator<K> getKeyIterator()
    {
        KeyIterator kiter = new KeyIterator();
        //You need to write "getKeyIterator" method
        return kiter;
    } // end getKeyIterator
    
    /** Creates an iterator that traverses all values in this dictionary.
       @return  An iterator that provides sequential access to the values
                in this dictionary. */
    public Iterator<V> getValueIterator()
    {
        ValueIterator viter = new ValueIterator();
        //You need to write "getValueIterator" method
        return viter;
    } // end getValueIterator
    
    
    private int getHashIndex(K key)
    {
        int hashIndex = key.hashCode() % hashTable.length;
        if (hashIndex < 0)
        {
            hashIndex = hashIndex + hashTable.length;
        } // end if
        return hashIndex;
    } // end getHashIndex
    
   // Precondition: checkInitialization has been called.
    private int probe(int index, K key)
    {
        boolean found = false;
        int removedStateIndex = -1; // Index of first location in removed state
        //  int increment = 1;          // For quadratic probing **********

        while ( !found && (hashTable[index] != null) )
        {
            if (hashTable[index].isIn())
            {
                if (key.equals(hashTable[index].getKey()))
                    found = true; // Key found
                else             // Follow probe sequence
                    index = (index + 1) % hashTable.length;         // Linear probing

                // index = (index + increment) % hashTable.length; // Quadratic probing **********

                // increment = increment + 2;                      // Odd values for quadratic probing **********

            }
            else // Skip entries that were removed
            {
                // Save index of first location in removed state
                if (removedStateIndex == -1)
                    removedStateIndex = index;  
                index = (index + 1) % hashTable.length;            // Linear probing
                // index = (index + increment) % hashTable.length;    // Quadratic probing **********
                // increment = increment + 2;                         // Odd values for quadratic probing **********

            } // end if

      } // end while
      // Assertion: Either key or null is found at hashTable[index]    
      if (found || (removedStateIndex == -1) )
         return index;                                      // Index of either key or null
      else
         return removedStateIndex;                          // Index of an available location
    } // end probe
    
   // Precondition: checkInitialization has been called.
    private int locate(int index, K key)
    {
        boolean found = false;

        //   int increment = 1;                                    // Quadratic probing **********
        while ( !found && (hashTable[index] != null) )
        {
            if ( hashTable[index].isIn() && key.equals(hashTable[index].getKey()) )
                found = true;                                   // Key found
            else                                               // Follow probe sequence
                index = (index + 1) % hashTable.length;         // Linear probing
            //          index = (index + increment) % hashTable.length; // Quadratic probing **********
            //          increment = increment + 2;                      // Odd values for quadratic probing **********

        } // end while
        // Assertion: Either key or null is found at hashTable[index]   
      int result = -1;
      if (found) 
         result = index;
      return result;
   } // end locate
    
   // Increases the size of the hash table to a prime >= twice its old size.
   // In doing so, this method must rehash the table entries.
   // Precondition: checkInitialization has been called.
    private void enlargeHashTable()
    {
      TableEntry<K, V>[] oldTable = hashTable;
      int oldSize = hashTable.length;
      int newSize = getNextPrime(oldSize + oldSize);
      checkSize(newSize);
      // The cast is safe because the new array contains null entries
      @SuppressWarnings("unchecked")
      TableEntry<K, V>[] tempTable = (TableEntry<K, V>[])new TableEntry[newSize]; // Increase size of array
      hashTable = tempTable;
      numberOfEntries = 0; // Reset number of dictionary entries, since
                           // it will be incremented by add during rehash
      // Rehash dictionary entries from old array to the new and bigger array;
      // skip both null locations and removed entries
      for (int index = 0; index < oldSize; index++)
      {
         if ( (oldTable[index] != null) && oldTable[index].isIn() )
            add(oldTable[index].getKey(), oldTable[index].getValue());
      } // end for
    } // end enlargeHashTable
    
   // Returns true if lambda > MAX_LOAD_FACTOR for hash table;
   // otherwise returns false. 
   private boolean isHashTableTooFull()
   {
      return numberOfEntries > MAX_LOAD_FACTOR * hashTable.length;
   } // end isHashTableTooFull
   // Returns a prime integer that is >= the given integer.
    private int getNextPrime(int integer)
    {
        // if even, add 1 to make odd
        if (integer % 2 == 0)
        {
            integer++;
        } // end if

        // test odd integers
      while (!isPrime(integer))
      {
         integer = integer + 2;
      } // end while
        return integer;
    } // end getNextPrime
    
   // Returns true if the given integer is prime.
    private boolean isPrime(int integer)
    {
        boolean result;
        boolean done = false;
        // 1 and even numbers are not prime
    if ( (integer == 1) || (integer % 2 == 0) )
    {
        result = false; 
    }
    // 2 and 3 are prime
    else if ( (integer == 2) || (integer == 3) )
    {
        result = true;
    }
    else // integer is odd and >= 5
    {
        assert (integer % 2 != 0) && (integer >= 5);
        // a prime is odd and not divisible by every odd integer up to its square root
        result = true; // assume prime
        for (int divisor = 3; !done && (divisor * divisor <= integer); divisor = divisor + 2)
        {
            if (integer % divisor == 0)
            {
                result = false; // divisible; not prime
                done = true;
            } // end if
        } // end for
    } // end if
    return result;
    } // end isPrime
    
    // Throws an exception if this object is not initialized.
    private void checkInitialization()
    {
        if (!initialized)
            throw new SecurityException ("HashedDictionary object is not initialized properly.");
    } // end checkInitialization
    
   // Ensures that the client requests a capacity
   // that is not too small or too large.
   private void checkCapacity(int capacity)
   {
      if (capacity < DEFAULT_CAPACITY)
         capacity = DEFAULT_CAPACITY;
      else if (capacity > MAX_CAPACITY)
         throw new IllegalStateException("Attempt to create a dictionary " +
                                         "whose capacity is larger than " +
                                         MAX_CAPACITY);
   } // end checkCapacity
   
   // Throws an exception if the hash table becomes too large.
   private void checkSize(int size)
   {
      if (tableSize > MAX_SIZE) 
         throw new IllegalStateException("Dictionary has become too large.");
   } // end checkSize
   
    private class KeyIterator implements Iterator<K>
    {
        private int currentIndex; // Current position in hash table
        private int numberLeft;   // Number of entries left in iteration
        private KeyIterator() 
        {
            currentIndex = 0;
            numberLeft = numberOfEntries;
        } // end default constructor
        public boolean hasNext() 
        {
            return numberLeft > 0;
        } // end hasNext
        public K next()
        {
            K result = null;
            if (hasNext())
            {
                // Skip table locations that do not contain a current entry
                while ( (hashTable[currentIndex] == null) || hashTable[currentIndex].isRemoved() )
                {
                    currentIndex++;
                } // end while
                result = hashTable[currentIndex].getKey();
                numberLeft--;
                currentIndex++;
            }
            else
                throw new NoSuchElementException();
            return result;
        } // end next
        public void remove()
        {
            throw new UnsupportedOperationException();
        } // end remove
    } // end KeyIterator
    
    private class ValueIterator implements Iterator<V>
    {
        private int currentIndex;
        private int numberLeft; 
        private ValueIterator() 
        {
            currentIndex = 0;
            numberLeft = numberOfEntries;
        } // end default constructor

    public boolean hasNext() 
    {
        return numberLeft > 0; 
    } // end hasNext
    public V next()
    {
        V result = null;
        if (hasNext())
        {
            // Skip table locations that do not contain a current entry
            while ( (hashTable[currentIndex] == null) || hashTable[currentIndex].isRemoved() )
            {
                currentIndex++;
            } // end while
            result = hashTable[currentIndex].getValue();
            numberLeft--;
            currentIndex++;
        }
        else
            throw new NoSuchElementException();
        return result;
    } // end next
    
    public void remove()
    {
        throw new UnsupportedOperationException();
    } // end remove
    } // end ValueIterator
    
    private static class TableEntry<S, T>
    {
        private S key;
        private T value;
        private States state;                  // Flags whether this entry is in the hash table
        private enum States {CURRENT, REMOVED} // Possible values of state
        private TableEntry(S searchKey, T dataValue)
        {
            key = searchKey;
            value = dataValue;
            state = States.CURRENT;
        } // end constructor
        
        private S getKey()
        {
            return key;
        } // end getKey
        
        private T getValue()
        {
            return value;
        } // end getValue
        
        private void setValue(T newValue)
        {
            value = newValue;
        } // end setValue
        // Returns true if this entry is currently in the hash table.
        
        private boolean isIn()
        {
            return state == States.CURRENT;
        } // end isIn
        // Returns true if this entry has been removed from the hash table.
        
        private boolean isRemoved()
        {
            return state == States.REMOVED;
        } // end isRemoved   
        // Sets the state of this entry to be removed.
        
        private void setToRemoved()
        {
            key = null;
            value = null;
            state = States.REMOVED; // Entry not in use, ie deleted from table
        } // end setToRemoved
        
        // Sets the state of this entry to current.
        private void setToIn()     // Not used
        {
            state = States.CURRENT; // Entry in use
        } // end setToIn
        
    } // end TableEntry
    
} // end HashedDictionary

/*
Result:
cd C:\Users\pkuma\OneDrive\Documents\NetBeansProjects\Hashing; "JAVA_HOME=C:\\Program Files\\Java\\jdk-23" cmd /c "\"C:\\Program Files\\NetBeans-24\\netbeans\\java\\maven\\bin\\mvn.cmd\" -Dexec.vmArgs= \"-Dexec.args=${exec.vmArgs} -classpath %classpath ${exec.mainClass} ${exec.appArgs}\" \"-Dexec.executable=C:\\Program Files\\Java\\jdk-23\\bin\\java.exe\" -Dexec.mainClass=com.mycompany.hashing.Driver -Dexec.classpathScope=runtime -Dexec.appArgs= \"-Dmaven.ext.class.path=C:\\Program Files\\NetBeans-24\\netbeans\\java\\maven-nblib\\netbeans-eventspy.jar\" --no-transfer-progress process-classes org.codehaus.mojo:exec-maven-plugin:3.1.0:exec"
Scanning for projects...

-----------------------< com.mycompany:Hashing >------------------------
Building Hashing 1.0-SNAPSHOT
  from pom.xml
--------------------------------[ jar ]---------------------------------

--- resources:3.3.1:resources (default-resources) @ Hashing ---
skip non existing resourceDirectory C:\Users\pkuma\OneDrive\Documents\NetBeansProjects\Hashing\src\main\resources

--- compiler:3.13.0:compile (default-compile) @ Hashing ---
Nothing to compile - all classes are up to date.

--- exec:3.1.0:exec (default-cli) @ Hashing ---
Create a dictionary:

Initial dictionary should be empty; isEmpty() returns true


Testing add():

11 (should be 11) items in dictionary, as follows:

Nancy : 555-7894
Derek : 555-7893
Carole : 555-7892
Bette : 555-7891
Reiss : 555-2345
Sam : 555-7890
Tom : 555-5555
Tabatha : 555-3456
Miguel : 555-9012
Abel : 555-5678
Dirk : 555-1234



Testing getValue():


Abel:   555-5678 should be 555-5678

Sam:    555-7890 should be 555-7890

Tom:    555-5555 should be 555-5555

Reiss:  555-2345 should be 555-2345

Miguel: 555-9012 should be 555-9012



Testing contains():


Sam is in dictionary - OK

Abel is in dictionary - OK

Miguel is in dictionary - OK

Tom is in dictionary - OK

Bo is not in dictionary - OK



Removing first item Nancy - Nancy's number is 555-7894 should be 555-7894
Replacing phone number of Reiss and Miguel:

Reiss's old number 555-2345 is replaced by 555-5432
Miguel's old number 555-9012 is replaced by 555-2109

10 (should be 10) items in dictionary, as follows:

Derek : 555-7893
Carole : 555-7892
Bette : 555-7891
Reiss : 555-5432
Sam : 555-7890
Tom : 555-5555
Tabatha : 555-3456
Miguel : 555-2109
Abel : 555-5678
Dirk : 555-1234



Removing interior item Reiss:


9 (should be 9) items in dictionary, as follows:

Derek : 555-7893
Carole : 555-7892
Bette : 555-7891
Sam : 555-7890
Tom : 555-5555
Tabatha : 555-3456
Miguel : 555-2109
Abel : 555-5678
Dirk : 555-1234



Removing last item Dirk:


8 (should be 8) items in dictionary, as follows:

Derek : 555-7893
Carole : 555-7892
Bette : 555-7891
Sam : 555-7890
Tom : 555-5555
Tabatha : 555-3456
Miguel : 555-2109
Abel : 555-5678


Removing Bo (not in dictionary):

Bo was not found in the dictionary.


8 (should be 8) items in dictionary, as follows:

Derek : 555-7893
Carole : 555-7892
Bette : 555-7891
Sam : 555-7890
Tom : 555-5555
Tabatha : 555-3456
Miguel : 555-2109
Abel : 555-5678



Testing clear():

Dictionary should be empty; isEmpty() returns true


Done.
------------------------------------------------------------------------
BUILD SUCCESS
------------------------------------------------------------------------
Total time:  0.900 s
Finished at: 2025-02-17T22:00:46-08:00
------------------------------------------------------------------------
*/