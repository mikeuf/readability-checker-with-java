package readability.checker;

/**
 * RCTracker.java
 * 
 * Stores counters of items found by the scanner and performs Flesch-Kincaid 
 * calculations using the counters
 * 
 */
public class RCTracker {

  // counters of different items found in provided text 
	private int numWords;
  private int numSentences;
  private int numSyllables;
  private int numClosingPTags;
  private int numIPs;
  private int numLists;
  private int numNBSPs;
	
  // calculated from the counter values
  private double numWordsPerSentence;
  private double numSyllablesPerWord;

  // after calculations, used to display readability scores
  private double FKGrade;
  private double FKScore;

  // constructor initializes counters
  public RCTracker() {
    numWords = 0;
    numSentences = 0;
    numSyllables = 0;
    numClosingPTags = 0;
    numIPs = 0;
    numLists = 0;
    numNBSPs = 0;
    numWordsPerSentence = 0;
    numSyllablesPerWord = 0;
    FKGrade = 0;
    FKScore = 0;
  } 
  
// getters
public Integer getNumWords() {return this.numWords;}
public Integer getNumSentences() {return this.numSentences;}
public Integer getNumSyllables() {return this.numSyllables;}
public Integer getNumIPs() {return this.numIPs;}
public Double getNumWordsPerSentence() {return numWordsPerSentence;} 
public Double getNumSyllablesPerWord() {return numSyllablesPerWord;} 
public Double getFKScore() {return this.FKScore;}
public Double getFKGrade() {return this.FKGrade;}

// setters
public void setNumWords(int numWords) {this.numWords = numWords;}
public void setNumSentences(int numSentences) {this.numSentences = numSentences;}
public void setNumSyllables(int numSyllables) {this.numSyllables = numSyllables;}
public void setNumIPs(int numIPs) {this.numIPs = numIPs;}
public void setNumWordsPerSentence(double numWordsPerSentence) {this.numWordsPerSentence = numWordsPerSentence;} 
public void setNumSyllablesPerWord(double numSyllablesPerWord) {this.numSyllablesPerWord = numSyllablesPerWord;} 
public void setFKScore(double FKScore) {this.FKScore = FKScore;}
public void setFKGrade(double FKGrade) {this.FKGrade = FKGrade;}


public void applyModifiers() {
    if (numIPs > 0) {
      // Don't count every octet of an IP address as a word
      numWords -= (numIPs * 4);

      // Count entire IP address as exactly 1 word and 1 syllable
      numWords += numIPs;
      numSyllables += numIPs;

      // Since IP addresses have periods, they will artificially boost sentence count by 3
      // To correct this, substract 3 sentences per IP detected
      numSentences -= (numIPs * 3);
    }
}
	
  public void calculateNumWordsPerSentence() {
    if (numSentences > 0) {		
      setNumWordsPerSentence((double)getNumWords()/(double)getNumSentences());
    }
	}
	
	public void calculateNumSyllablesPerWord() {
    if (numWords > 0) {
      setNumSyllablesPerWord((double)numSyllables/(double)numWords);
    }
  } 
  
  public void calculateFKScore() {
    // calculate the Flesch-Kincaid Score
    FKScore = (206.835 - (1.015 * numWordsPerSentence) - (84.6 * numSyllablesPerWord));
    if (FKScore < 0) {
			FKScore = 0;  // minimum score = 0
		} 
    if (FKScore > 100) {
			FKScore = 100;  // max score = 100
		} 
  }

  public void calculateFKGrade() {
    // calculate the Flesch-Kincaid Grade
    FKGrade = ((0.39 * numWordsPerSentence) + (11.8 * numSyllablesPerWord - 15.59));
    if (FKGrade < 0) {
			FKGrade = 0;
		} //minimum score = 0
  }	
}