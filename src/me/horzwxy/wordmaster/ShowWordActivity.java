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
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.List;
import java.util.ArrayList;

import me.horzwxy.wordservant.GlobalInstance;
import me.horzwxy.wordservant.Recognizer;
import me.horzwxy.wordservant.Word;
import me.horzwxy.wordservant.WordLibrary;

public class ShowWordActivity extends Activity {
	
	private List< WordResult > wordList;
	private List< String > items;
	
	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.show_word_list );
		GlobalInstance.activities.add( ShowWordActivity.this );
		
		wordList = new ArrayList< WordResult >();
		items = new ArrayList< String >();
		
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		String article = extras.getString( "article" );
		
		WordLibrary lib = GlobalInstance.wordLib;
		Recognizer recognizer = new Recognizer();
		recognizer.setWordLibrary( lib );
		
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
					String[] rawWords = sentences[ m ].split( " " );	// split by blanks
					for( int i = 0; i < rawWords.length; i++ ) {
						String rawWord = rawWords[ i ];
						
						// purify the word
						int prefixLength = 0;
						for( int j = 0; j < rawWord.length(); j++ ) {
							// skip the heading symbols
							if( rawWord.charAt( j ) < 'A' || rawWord.charAt( j ) > 'z' ) {
								prefixLength++;
							}
							else break;
						}
						int suffixLength = 0;
						for( int j = rawWord.length() - 1; j >= 0; j-- ) {
							// skip the ending symbols
							if( rawWord.charAt( j ) < 'A' || rawWord.charAt( j ) > 'z' ) {
								suffixLength++;
							}
							else break;
						}
						// If the rawWord is full of symbols, skip to the next
						if( prefixLength == rawWord.length() ) {
							continue;
						}
						// get the purified word
						String pw = rawWord.substring( prefixLength, rawWord.length() - suffixLength );
						
						recognizer.setCurrentWord( pw );
						
						// if it's new
						if( recognizer.getResult() ) {
							// if it has been met before in this article
							if( items.contains( pw.toLowerCase() ) ) {
								int index = wordList.indexOf( new WordResult( pw.toLowerCase() ) );
								WordResult word = wordList.get( index );
								if( !sentences.equals( word.getSentences().get( word.getSentences().size() - 1 ) ) ) {
									word.addSentences( sentences[ m ] + "." );
								}
								continue;
							}
							else {
								items.add( pw.toLowerCase() );
								WordResult word = new WordResult( pw );
								word.setForms( recognizer.getPossibleForms() );
								word.addSentences( sentences[ m ] + "." );
								wordList.add( word );
							}
						}
					}
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
				GlobalInstance.wordResult = wordList.get( position );
				Intent intent = new Intent( ShowWordActivity.this, WordResultInfoActivity.class );
				startActivityForResult( intent, position );
			}
		} );
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
	
	@Override
    public boolean onOptionsItemSelected( MenuItem item ) {
    	return GlobalInstance.sharedMenuEventHandler( item, this );
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
