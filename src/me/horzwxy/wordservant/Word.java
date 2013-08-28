package me.horzwxy.wordservant;

import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;

public class Word implements Comparable< Word >, Serializable {
	
	private String content;
	private ArrayList< String > sentences;
	private Date in_time;
    private WordState state;

	public Word( String content ) {
		this.setContent(content);
		this.sentences = new ArrayList< String >();
		this.in_time = new Date();
	}

	public String getContent() {
		return content;
	}

	public void setContent(String englishContent) {
		this.content = englishContent;
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

	public Date getIn_time() {
		return in_time;
	}

    public WordState getState() {
        return state;
    }

    public void setState( WordState state ) {
        this.state = state;
    }
	
	@Override
	public int compareTo( Word w ) {
		return content.compareTo( w.getContent() );
	}
	
	@Override
	public boolean equals( Object o ) {
		if( o instanceof Word ) {
			return content.equals( ( ( Word )o ).getContent() );
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return content.hashCode();
	}
}
