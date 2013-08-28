package me.horzwxy.wordservant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Word list that contains all words I know.
 * @author horz
 *
 */
public class WordLibrary {
	
	// the local storage which gets the vocabulary from permanent storage
    private HashMap< String, Word > library;

	public WordLibrary( HashMap< String, Word > library ) {
		this.library = library;
	}
	
	/**
	 * Check whether a word is on the list.
	 * @param word
	 * @return
	 */
	public WordState getWordState( String word ) {
        Word wordInstance = library.get( word );
		if( wordInstance != null ) {
            return wordInstance.getState();
        }
        else{
            return WordState.Untracked;
        }
	}
	
	/**
	 * Return the word instance by word string.
	 * @param word
	 * @return
	 */
	public Word getWord( String word ) {
        return library.get( word );
	}

    /**
     * Create a word instance from its content and state. Then put it into library.
     * @param word
     * @param state
     * @return
     */
    public Word createAndAdd( String word, WordState state ) {
        Word wordInstance = new Word( word );
        wordInstance.setState( state );
        library.put( word, wordInstance );
        return wordInstance;
    }
}