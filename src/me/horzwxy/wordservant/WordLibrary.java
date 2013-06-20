package me.horzwxy.wordservant;

import java.util.HashMap;

/**
 * Word list that contains all words I know.
 * @author horz
 *
 */
public class WordLibrary {
	
	// the local storage which gets the vocabulary from XML
	private HashMap< String, Word > wordLib;
	private HashMap< String, Word > ignoreList;
	private HashMap< String, Word > newWordList;
	private HashMap< String, Word > knownList;
	
	public WordLibrary( HashMap< String, Word > wordLib, HashMap< String, Word > ignoreList, HashMap< String, Word > newWordList, HashMap< String, Word > knownList ) {
		this.wordLib = wordLib;
		this.setIgnoreList(ignoreList);
		this.setNewWordList(newWordList);
		this.setKnownList( knownList );
		for( int i = 0; i < knownList.size(); i++ ) {
			this.wordLib.put( knownList.get( i ).getEnglishContent(), knownList.get( i ) );
		}
	}
	
	/**
	 * Check whether a word is on the list.
	 * @param word
	 * @return
	 */
	public boolean contains( String word ) {
		// if the word should be ignored
		if( ignoreList.containsKey( word ) ) {
			return true;
		}
		// if it's NOT a new word
		else if ( wordLib.containsKey( word ) ) {
			return true;
		}
		// if it's recently added
		else if ( newWordList.containsKey( word ) ) {
			return true;
		}
		return false;
	}
	
	/**
	 * Return the word instance by word string.
	 * @param word
	 * @return
	 */
	public Word getWord( String word ) {
		if( contains( word ) ) {
			return wordLib.get( word );
		}
		return null;
	}
	
	/**
	 * Return the storage instance of WordLibrary.
	 * @return
	 */
	public HashMap< String, Word > getWordLib() {
		return this.wordLib;
	}
	
	/**
	 * Fill the attributes of Word from Evernote content
	 * @param w the result Word instance
	 * @param content XML doc of Evernote content
	 */
	private void parseEvernoteContent( Word w, String content ) {
		content = content.substring( content.indexOf( "#english" ), content.indexOf( "</span>" ) );
		String[] fields = content.split( "#" );
		
		for( int i = 1; i < fields.length; i++ ) {
			String[] pair = fields[ i ].split( "=" );
			String name = pair[0];
			String value = pair[1];
			if( name.equals( "english" ) ) {
				// nothing
			}
			else if( name.equals( "type" ) ) {
				w.setType( Word.WordType.getEnum( value ) );
			}
			else if( name.equals( "in_time" ) ) {
				w.setIn_time( value );
			}
			else if( name.equals( "last_time" ) ) {
				w.setLast_time( value );
			}
			else if( name.equals( "total_times" ) ) {
				w.setTotal_times( Integer.parseInt( value ) );
			}
			else if( name.equals( "peak_times" ) ) {
				w.setPeak_times( Integer.parseInt( value ) );
			}
			else if( name.equals( "article_frequency" ) ) {
				w.setPeak_times( Integer.parseInt( value ) );
			}
			else if( name.equals( "sentence" ) ) {
				w.addSentence( value );
			}
			else {
				throw new RuntimeException( "Invalid name: " + name );
			}
		}
	}

	public HashMap< String, Word > getIgnoreList() {
		return ignoreList;
	}

	public void setIgnoreList(HashMap< String, Word > ignoreList) {
		this.ignoreList = ignoreList;
	}

	public HashMap< String, Word > getNewWordList() {
		return newWordList;
	}

	public void setNewWordList(HashMap< String, Word > newWordList) {
		this.newWordList = newWordList;
	}
	
	/**
	 * Add a new word into the lib.
	 * @param w
	 */
	public void addNewWord( Word w ) {
		if( newWordList.containsKey( w.getEnglishContent().toLowerCase() ) ) {
			Word word = newWordList.get( w.getEnglishContent().toLowerCase() );
			for( int i = 0; i < w.getSentences().size(); i++ ) {
				if( !word.getSentences().contains( w.getSentences().get( i ) ) ) {
					word.addSentence( w.getSentences().get( i ) );
				}
			}
		}
		else {
			newWordList.put( w.getEnglishContent().toLowerCase(), w );
		}
	}
	
	/**
	 * Add a new word into the lib.
	 * @param w
	 */
	public void addIgnoredWord( Word w ) {
		ignoreList.put( w.getEnglishContent().toLowerCase(), w );
	}

	public HashMap< String, Word > getKnownList() {
		return knownList;
	}

	public void setKnownList(HashMap< String, Word > knownList) {
		this.knownList = knownList;
	}
	
	/**
	 * Add a word that is already known.
	 * @param w
	 */
	public void addKnownWord( Word w ) {
		knownList.put( w.getEnglishContent(), w );
	}
}