package me.horzwxy.wordmaster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.ArrayList;

import me.horzwxy.wordservant.Recognizer;
import me.horzwxy.wordservant.SentenceAnalyzer;
import me.horzwxy.wordservant.WordLibrary;
import me.horzwxy.wordservant.WordRecognizer;

public class ShowWordActivity extends WMActivity {

    private SentenceAnalyzer sentenceAnalyzer;

	private List< WordResult > wordList;
	private List< String > items;
	
	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.show_word_list );
		activities.add( this );

        sentenceAnalyzer = new SentenceAnalyzer( wordLib, new WordRecognizer( wordLib ), tempStorage );
		wordList = new ArrayList< WordResult >();
		items = new ArrayList< String >();
		
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		String article = extras.getString( "article" );
		
		try {
			BufferedReader br = new BufferedReader( new StringReader( article ) );
			
			while( br.ready() ) {
				// read one line
				String line = br.readLine();
				if( line == null ) {
					break;
				}
				
				String[] sentences = line.split( "[\\.\\?!]" );
				for( int m = 0; m < sentences.length; m++ ) {
                    sentenceAnalyzer.analyzeSentence( sentences[ m ] );
				}
			}
			
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
		if( resultCode != 0 )
		{
			items.remove( requestCode );
			wordList.remove( requestCode );
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		ListView listView = ( ListView )findViewById( R.id.word_list );
		listView.setAdapter( new ArrayAdapter< String >( this, android.R.layout.simple_list_item_1, items ) );
		
		listView.setOnItemClickListener( new OnItemClickListener() {
			@Override
			public void onItemClick( AdapterView<?> parent, View view, int position, long id ) {
				Intent intent = new Intent( ShowWordActivity.this, WordResultInfoActivity.class );
                intent.putExtra( "word", wordList.get( position ).getContent() );
				startActivityForResult( intent, position );
			}
		} );
	}

	/**
	 * A wrapper to represent the new word searching result.
	 * @author horz
	 *
	 */
	public class WordResult{
		
		private String content;
		private ArrayList< String > forms;
		private ArrayList< String > sentences;
		
		/**
		 * content is an essential field to initialize the instance.
		 * forms is originally null. Must be set by user.
		 * sentences is original empty. Can be added with a single sentence.
		 * @param content
		 */
		public WordResult( String content ) {
			this.content =  content;
			forms = null;
			sentences = new ArrayList< String >();
		}
		
		public String getContent() {
			return content;
		}
		
		public ArrayList< String > getForms() {
			return forms;
		}
		
		public void setForms(ArrayList< String > forms) {
			this.forms = forms;
		}
		
		public ArrayList< String > getSentences() {
			return sentences;
		}
		
		public void addSentences( String sentence ) {
			this.sentences.add( sentence );
		}
		
		public void setSentences(ArrayList< String > sentences) {
			this.sentences = sentences;
		}
		
		@Override
		public boolean equals( Object o ) {
			if( o instanceof WordResult ) {
				return content.toLowerCase().equals( ( ( WordResult )o ).getContent().toLowerCase() );
			}
			return false;
		}
	}
}
