import java.util.LinkedList;
import java.util.List; 

public class CenterEmbeddings {

  private static final String[] transitive = {"knew", "chased", "liked", "loved", "saw"};
  private static final String[] intransitive = {"snored", "laughed", "ran"};
  
  private static class Relation {
    
    String predicate; 
    String subject; 
    String object;    
    
    public Relation(String predicate, String subject, String object) {
      this.predicate = predicate; 
      this.subject = subject; 
      this.object = object;
    }
    
    public String toString(){
      
      if (object != null)
        return "(" + subject + "," + predicate +"," + object +")";
      else
        return "(" + subject + "," + predicate+")";
    }
    
  }
  
  public static List<Relation> parseSentence(String sentence) throws IllegalArgumentException {
    
    List<Relation> result = new LinkedList<>();

    // complete this method
    ArrayStack<String> stack = new ArrayStack<String>() ;
    
    String input = sentence;
    String[] tokens = new String[input.length()];
    int i = 0;
    boolean verbFound = false;
    int numVerbs = 0;
    int numNouns = 0;
    for(String token: sentence.split(" ")){   
      // make an array of tokens that we can then analyze individually
      // we don't know the size yet, so initilize array by an upper bound string length, then cut it down once we know how many 
      // tokens (i) we are dealing with here
      if(tokens.length > 0){
        tokens[i] = token;
        i++;  
      }
      else{
        Relation r = new Relation("", "", "");
        result.add(r);
        return result;
      }
    
    }    
    String[] old = tokens;
    tokens = new String[i];
    // adjust size of our initial array
    for(int j = 0; j < i; j++){      
      tokens[j] = old[j];      
      //System.out.println(tokens[j]);
    }
    
    for(int k = 0; k< tokens.length; k++){
      
      if(!tokens[k].equals("the") && !tokens[k].equals("that")){
        for(String l: transitive){
          
          if(tokens[k].equals(l)){
            // then there is a transitive verb 
            verbFound = true;
            numVerbs +=1;
            String thisSubject = "";
            String thisObject = "";
            
            if(stack.isEmpty() == false){
              thisSubject = stack.pop();
              
              if(stack.isEmpty() == false){
                thisObject = stack.top();
              }
              else{
                
                if(!tokens[k+1].equals("the")){
                  
                  if(!tokens[k+1].equals("that")){
                      stack.push(tokens[k+1]);
                  }
                  
                  else{
                    k++;
                  }
                  
                }
                
                else{
                  stack.push(tokens[k+2]);
                }
                
                thisObject = stack.top();
              }
            }
            else{
              throw new IllegalArgumentException();
            }

            
            Relation r = new Relation(tokens[k], thisSubject, thisObject);
            result.add(r);
            
            // if its a transitive verb, then we apply the verb to the relevant object

          }          
        }
        for(String m: intransitive){
          if(tokens[k].equals(m)){
            // then there is an intransitive verb 
            verbFound = true;
            numVerbs +=1;            
            Relation q = new Relation(tokens[k], stack.pop() , null);
            result.add(q);
          }          
        }
        
        if(verbFound == false){
          stack.push(tokens[k]);
          numNouns +=1;
        }
        
        
        
      }
      
      verbFound = false;
    }
    
    if(numVerbs > numNouns){
      throw new IllegalArgumentException();
    }
    
    return result;
  }
    
  
  
  public static void main(String[] args) {
    
    String test1 = "the child laughed";    
    String test2 = "the child that the the woman loved laughed";    
    String test3 = "the child that the the woman that the man knew loved laughed";    
    String test4 = "the child saw the cat"; // hard 
    String test5 = "the child that the man knew saw the cat"; // hard
    String test6 = "the child saw the cat that the man loved"; // even harder 
    String test7 = "";
    
    List<Relation> result = parseSentence(test5);
    for (Relation r :result) {
      System.out.println(r);
    } 
  }
  
  
}
