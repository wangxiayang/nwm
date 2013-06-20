package me.horzwxy.wordservant;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Word implements Comparable< Word >, Serializable {
	
	private String englishContent;
	private WordType type;
	private ArrayList< String > sentences;
	private String in_time;
	private String last_time;
	private int total_times;
	private int peak_times;
	private int article_frequency;	

	public Word( String englishContent ) {
		this.setEnglishContent(englishContent);
		this.type = WordType.NORMAL;
		this.sentences = new ArrayList< String >();
		this.in_time = "2013-01-01";
		this.last_time = "2013-01-01";
		this.total_times = 0;
		this.peak_times = 0;
		this.article_frequency = 0;
	}

	public String getEnglishContent() {
		return englishContent;
	}

	public void setEnglishContent(String englishContent) {
		this.englishContent = englishContent;
	}

	public WordType getType() {
		return type;
	}

	public void setType(WordType type) {
		this.type = type;
	}

	public ArrayList< String > getSentences() {
		return sentences;
	}

	public void setSentences(ArrayList< String > sentences) {
		this.sentences = sentences;
	}
	
	/**
	 * Add one new sentence example.
	 * @param sentence
	 */
	public void addSentence( String sentence ) {
		this.sentences.add( sentence );
	}

	public String getIn_time() {
		return in_time;
	}

	public void setIn_time(String in_time) {
		this.in_time = in_time;
	}

	public String getLast_time() {
		return last_time;
	}

	public void setLast_time(String last_time) {
		this.last_time = last_time;
	}

	public int getTotal_times() {
		return total_times;
	}

	public void setTotal_times(int total_times) {
		this.total_times = total_times;
	}

	public int getPeak_times() {
		return peak_times;
	}

	public void setPeak_times(int peak_times) {
		this.peak_times = peak_times;
	}

	public int getArticle_frequency() {
		return article_frequency;
	}

	public void setArticle_frequency(int article_frequency) {
		this.article_frequency = article_frequency;
	}
	
	@Override
	public int compareTo( Word w ) {
		return englishContent.compareTo( w.getEnglishContent() );
	}
	
	@Override
	public boolean equals( Object o ) {
		if( o instanceof Word ) {
			return this.englishContent.equals( ( ( Word )o ).getEnglishContent() );
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.englishContent.hashCode();
	}

	/**
	 * List the types which a word can be.
	 * @author horz
	 *
	 */
	public enum WordType implements Serializable {
		NORMAL,
		PERSON_NAME,
		SPOT_NAME;
		
		public static WordType getEnum( String name ) {
			if( name.equals( "NORMAL" ) ) {
				return NORMAL;
			}
			else if( name.equals( "PERSON_NAME" ) ) {
				return PERSON_NAME;
			}
			else if( name.equals( "SPOT_NAME" ) ) {
				return SPOT_NAME;
			}
			else {
				throw new RuntimeException( "Invalid WordType name: " + name );
			}
		}
	}
}
