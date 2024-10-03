/**
 * An implementation of the AutoCompleteInterface using a DLB Trie.
 */

import java.util.ArrayList;

 public class AutoComplete implements AutoCompleteInterface {

  private DLBNode root;
  private StringBuilder currentPrefix;
  private DLBNode currentNode;
  //TODO: Add more instance variables as needed
  private int trailCounter; // This keeps track of how many times we have attempted to append to a word that already doesn't exist in our trie

  public AutoComplete(){
    root = null;
    currentPrefix = new StringBuilder();
    currentNode = null;
    trailCounter = 0;
  }

  /**
   * Adds a word to the dictionary in O(alphabet size*word.length()) time
   * @param word the String to be added to the dictionary
   * @return true if add is successful, false if word already exists
   * @throws IllegalArgumentException if word is the empty string
   */
    public boolean add(String word){
      //TODO: implement this method

      if(word == null || word.trim().length()==0) throw new IllegalArgumentException("calls get() with a null key");

      if(root == null)   // If the root is null (nothing in the trie) we must insert a new node
      {
        root = new DLBNode(word.charAt(0));
      }

      DLBNode curr = root;   // curr node pointer that will help us iterate across our trie

      for(int i = 0; i < word.length(); i++)  // Iterating across all of the letters in the word
      {
        char letter = word.charAt(i);  

        while(curr.nextSibling!=null)    // Checking/Iterating across the sibling list first
        {
          if(curr.data==letter)         
          {
            break;  // Exit if node's data is equal to the letter
          }
          curr = curr.nextSibling;    // otherwise keep iterating across the sibling list
        }

        if(curr.data == letter) // If we find our letter in the trie then we must increment its size
        {
          curr.size++;
        }


        if(curr.data != letter) // If there was no match in the sibling list we must attach our own sibling node into the trie.
        {
          curr.nextSibling = new DLBNode(letter);  
          curr.nextSibling.previousSibling = curr;
          curr.nextSibling.parent = curr.parent;     // In this implementation we are allowing each sibling node to have an independent link to the parent node
          curr = curr.nextSibling;
          curr.size++;
        }

        if(curr.isWord==true && i==word.length()-1) //If we are on the last letter and the node in the trie is already a word then we are attempting to add a dupe word
        {

          for(int j = 0; j < word.length(); j++)  // We must return false and subtact 1 from the size of each char we went over and return false
          {
            curr.size--;
            curr = curr.parent;
          }
          return false; 

        }


        if(i != word.length()-1)   // If we are not at the end of the word then we must keep iterating
        {
    
          if(curr.child==null)    // If the curr child is null we must add a new child node 
          {
            curr.child = new DLBNode(word.charAt(i+1));
            curr.child.parent = curr;
          }
          curr = curr.child;
        }

      }
      curr.isWord = true;      // At the end of the for loop we must make sure that we sign the current node to be the end of a word
      
      return true;
    }




  /**
   * appends the character c to the current prefix in O(alphabet size) time. 
   * This method doesn't modify the dictionary.
   * @param c: the character to append
   * @return true if the current prefix after appending c is a prefix to a word 
   * in the dictionary and false otherwise
   */
    public boolean advance(char c){
      //TODO: implement this method


      currentPrefix.append(c);   // We will always append to our currentPrefix string when advancing a char

      if(trailCounter!=0)  // If our trail counter is not 0 then currentPrefix is not a word in the trie so we can simply increment our counter and return false
      {
        trailCounter++;
        return false;
      }


      DLBNode curr;   // curr pointer

      if(currentNode!=null)   // If our currentNode is not null then we can set our curr pointer to equal our currentNodes child
      {
        if(currentNode.child==null) // If our currentNode does not have a child node then our currenPrefix with the character added is not in our trie/is not a prefix
        {
          trailCounter++;
          return false;
        }
        curr = currentNode.child;
      }

      else   // Our currentNode is null and we must set it to be equal to the root
      {
        if(root==null)  // If the root is null then create a new root  (this will only happen when our dictionary has no words inside of it)
        {
          root = new DLBNode(c);
        }
        currentNode = root;
        curr = currentNode;
      }

      while(curr!=null && curr.nextSibling!=null)  // Now we can traverse the sibling list to find the letter that we are trying to insert into our currentPrefix
      {
        if(curr.data == c)
        {
          break;
        }
        curr = curr.nextSibling;
      }

      if(curr.data==c) // If we find a match then we can set our currentNode equal to that node and return true
      {
        currentNode = curr;
        return true;
      }

      else  // If we were unable to find a match then our currenPrefix is not a prefix in the trie so we can return false and increment out trailCounter
      {
        trailCounter++;
        if(currentPrefix.length()==1) currentNode = null; //This is for when we are trying to add the first letter to our currentPrefix string 
        return false;                                     //but that letter does not exist in the trie. In this case we stop the currentNode from pointing to the root.
      }
      
    }

  /**
   * removes the last character from the current prefix in O(alphabet size) time. This 
   * method doesn't modify the dictionary.
   * @throws IllegalStateException if the current prefix is the empty string
   */
    public void retreat(){
      //TODO: implement this method

      if(currentPrefix.length()==0) // Throw exception if attempting to delete from an empty string
      {
        throw new IllegalStateException("Attempting to delete from empty string");
      }

      else
      {
        if(trailCounter>0)  // If our trail counter is not 0 then we must only delete the last character in the currentPrefix and decrement the trailCounter variable
        {
          currentPrefix.deleteCharAt(currentPrefix.length()-1);
          trailCounter--;

          return;
        }

        currentPrefix.deleteCharAt(currentPrefix.length()-1);

        if(currentPrefix.length()>0)  // If our string length after deleting is greater than 0 we can make our currentNode point to its parent
        {
          currentNode = currentNode.parent;
        }

        else // If our currentPrefix string is empty after deleting then we must make our currentNode point to null
        {
          currentNode = null;
        }
      }

    }

  /**
   * resets the current prefix to the empty string in O(1) time
   */
    public void reset(){
      //TODO: implement this method
      currentPrefix.setLength(0);  // We must set the prefix Length to equal 0
      currentNode=null;   // CurrentNode has to be set to 0 because we are emptying our string
      trailCounter = 0;   // Trail counter must be reset aswell
    }
    
  /**
   * @return true if the current prefix is a word in the dictionary and false
   * otherwise. The running time is O(1).
   */
    public boolean isWord(){
      //TODO: implement this method

      if(trailCounter!=0)  // If trail counter is not equal to 0 then that means we have a word that is not in our trie so we can return false
      {
        return false;
      }

      if(currentPrefix.length()==0) return true; // This allows the user to stop the program by typing . even when they have an empty CurrentPrefix string

      return currentNode.isWord;  // otherwise we will return the CurrentNodes isWord value as this will tell us if our currentPrefix is a word or not

    }

  /**
   * adds the current prefix as a word to the dictionary (if not already a word)
   * The running time is O(alphabet size*length of the current prefix). 
   */
    public void add(){
      //TODO: implement this method

      // This method is almost identical to passing in our currentPrefix string as an argument for our add(string method) except for the fact that
      // it doesn't check for dupes and that it sets the CurrentNodes.isWord value to equal true.
      // Similar To This: add(prefix);

      String word = currentPrefix.toString();
      trailCounter = 0;

      if(word == null || word.trim().length()==0) throw new IllegalArgumentException("calls get() with a null key");

      if(root == null)   // If the root is null (nothing in the trie) insert new node
      {
        root = new DLBNode(word.charAt(0));
      }

      DLBNode curr = root;   // Current node pointer

      for(int i = 0; i < word.length(); i++)  // Iterating across all of the letters in the word
      {
        char letter = word.charAt(i);        // letter that we are on

        while(curr.nextSibling!=null)    // Checking/Iterating across the sibling list first
        {
          if(curr.data==letter)         // Exit if nodes data is equal the letter
          {
            break;
          }
          curr = curr.nextSibling; 
        }

        if(curr.data == letter)   // If we found a match then we must increment the size of the node we found a match on
        {
          curr.size++;
        }


        if(curr.data != letter) // If there was no match in the sibling list we must attach our own sibling
        {
          curr.nextSibling = new DLBNode(letter);
          curr.nextSibling.previousSibling = curr;
          curr.nextSibling.parent = curr.parent;
          curr = curr.nextSibling;
          curr.size++;
        }


        if(i != word.length()-1)   // If we are not at the end of the word then we must keep iterating
        {
          
          if(curr.child==null)    // If the curr child is null we must add a new child node 
          {
            curr.child = new DLBNode(word.charAt(i+1));
            curr.child.parent = curr;
          }
          curr = curr.child;
        }

      }
      curr.isWord = true;      // At the end of a for loop we must make sure that we sign the current node to be the end of a word
      currentNode = curr;
      
    }

  /** 
   * @return the number of words in the dictionary that start with the current 
   * prefix (including the current prefix if it is a word). The running time is 
   * O(1).
   */
    public int getNumberOfPredictions(){
      //TODO: implement this method

      if(trailCounter!=0) return 0;  // If our trail counter variable is not 0 then that means our currentPrefix string is not a word in the dictionary so we return 0

      if(currentNode==null) return 0;  // If our currentPrefix is empty then return 0

      else
      {
        return currentNode.size;    // The size of the current node will tell us how many words start with our currenPrefix string
      }

    }
  
  /**
   * retrieves one word prediction for the current prefix. The running time is 
   * O(prediction.length())
   * @return a String or null if no predictions exist for the current prefix
   */
    public String retrievePrediction(){
      //TODO: implement this method

      if(trailCounter!=0) return null;   // If our trail counter variable is not 0 then that means our current word is not a word, so we can not give a prediction

      if(currentNode==null) return null;


      String word = currentPrefix.toString();
      StringBuilder predictionString = new StringBuilder(); // String that we will return 
      predictionString.append(word);   // Our prediction will have our currentPrefix string in it so we can just add it here 

      DLBNode curr = currentNode;

      if(curr.isWord)   // If our currentPrefix is a word then we can just return our currentPrefix
      {
        return predictionString.toString();
      }

      while(curr.child!=null)  // If our currentPrefix is not a word then we must search for one in our trie by iterating through our currentNodes child list
      {
        predictionString.append(curr.child.data);   // We will append the child character to our prediction string
        if(curr.child.isWord)  
        {
          return predictionString.toString();  // If our child node is a word then we have a prediction and we can return that string 
        }
        curr = curr.child;   // Otherwise keep iterating over the child list
      }

      return null;  // If our currentPrefix did not contain any children and our currentPrefix is not a word then we must return null

    }


  /* ==============================
   * Helper methods for debugging.
   * ==============================
   */

  //print the subtrie rooted at the node at the end of the start String
  public void printTrie(String start){
    System.out.println("==================== START: DLB Trie Starting from \""+ start + "\" ====================");
    if(start.equals("")){
      printTrie(root, 0);
    } else {
      DLBNode startNode = getNode(root, start, 0);
      if(startNode != null){
        printTrie(startNode.child, 0);
      }
    }
    
    System.out.println("==================== END: DLB Trie Starting from \""+ start + "\" ====================");
  }

  //a helper method for printTrie
  private void printTrie(DLBNode node, int depth){
    if(node != null){
      for(int i=0; i<depth; i++){
        System.out.print(" ");
      }
      System.out.print(node.data);
      if(node.isWord){
        System.out.print(" *");
      }
      System.out.println(" (" + node.size + ")");
      printTrie(node.child, depth+1);
      printTrie(node.nextSibling, depth);
    }
  }

  //return a pointer to the node at the end of the start String 
  //in O(start.length() - index)
  private DLBNode getNode(DLBNode node, String start, int index){
    if(start.length() == 0){
      return node;
    }
    DLBNode result = node;
    if(node != null){
      if((index < start.length()-1) && (node.data == start.charAt(index))) {
          result = getNode(node.child, start, index+1);
      } else if((index == start.length()-1) && (node.data == start.charAt(index))) {
          result = node;
      } else {
          result = getNode(node.nextSibling, start, index);
      }
    }
    return result;
  } 

  //The DLB node class
  private class DLBNode{
    private char data;
    private int size;
    private boolean isWord;
    private DLBNode nextSibling;
    private DLBNode previousSibling;
    private DLBNode child;
    private DLBNode parent;

    private DLBNode(char data){
        this.data = data;
        size = 0;
        isWord = false;
        nextSibling = previousSibling = child = parent = null;
    }
  }
}
