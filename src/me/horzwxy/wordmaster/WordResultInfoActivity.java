package me.horzwxy.wordmaster;

import java.util.ArrayList;
import java.util.List;

import me.horzwxy.wordmaster.ShowWordActivity.WordResult;
import me.horzwxy.wordservant.Word;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class WordResultInfoActivity extends WMActivity {
	
	private RadioGroup rbg;
	private List< String > forms;
	private ArrayList< RadioButton > buttons;
	private ArrayList< String > sentences;
	private ArrayList< String > backstore;
	private boolean submitted = false;
	
	private int letterCaseState = 0;	// 0 stands for all lower, 1 for first letter upper, 2 for all upper
	private final static int ALL_LOWER = 0;
	private final static int FIRST_UPPER = 1;
	private final static int ALL_UPPER = 2;
	
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_wordresultinfo );
		SharedMethods.activities.add( WordResultInfoActivity.this );
		
		setResult( 0, null );
		
		WordResult wordresultinfo = SharedMethods.wordResult;
		
		TextView wordContentView = ( TextView )findViewById( R.id.wordresultinfo_word );
		wordContentView.setText( wordresultinfo.getContent() );
		
		rbg = ( RadioGroup )findViewById( R.id.wordresultinfo_forms );
		buttons = new ArrayList< RadioButton >();
		
		forms = wordresultinfo.getForms();
		for( int i = 0; i < forms.size(); i++ ) {
			RadioButton button = new RadioButton( this );
			button.setText( forms.get( i ) );
			button.setTag( ( Object )( "" + i ) );
			buttons.add( button );
			rbg.addView( button );
		}
		
		sentences = wordresultinfo.getSentences();
		backstore = new ArrayList< String >();
		final ListView listView = ( ListView )findViewById( R.id.wordresultinfo_sentences );
		listView.setAdapter( new ArrayAdapter< String >( this, android.R.layout.simple_list_item_1, sentences ) );
		
		listView.setOnItemClickListener( new OnItemClickListener() {
			@Override
			public void onItemClick( AdapterView<?> parent, View view, int position, long id ) {
				//sentences.remove( position );
				backstore.add( sentences.get( position ) );
				sentences.remove( position );
				listView.setAdapter( new ArrayAdapter< String >( WordResultInfoActivity.this, android.R.layout.simple_list_item_1, sentences ) );
				listView.invalidate();
				
			}
		} );
		
		Button changeCase = ( Button )findViewById( R.id.wordresultinfo_changeCase );
		changeCase.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick( View v ) {
				switch( letterCaseState ) {
				case ALL_LOWER: letterCaseState = FIRST_UPPER;
					for( int i = 0; i < buttons.size(); i++ ) {
						// Get the first form string and remove it. Then the second one becomes first.
						String form = ( String )buttons.get( i ).getText();
						if( form.length() >= 1 ) {
							form = Character.toUpperCase( form.charAt( 0 ) ) + form.substring( 1 );
						}
						else
						{
							form = form.toUpperCase();
						}
						buttons.get( i ).setText( form );
					}
					break;
				case FIRST_UPPER: letterCaseState = ALL_UPPER;
					for( int i = 0; i < buttons.size(); i++ ) {
						// Get the first form string and remove it. Then the second one becomes first.
						String form = ( String )buttons.get( i ).getText();
						form = form.toUpperCase();
						buttons.get( i ).setText( form );
					}
					break;
				case ALL_UPPER:	letterCaseState = ALL_LOWER;
					for( int i = 0; i < buttons.size(); i++ ) {
						// Get the first form string and remove it. Then the second one becomes first.
						String form = ( String )buttons.get( i ).getText();
						form = form.toLowerCase();
						buttons.get( i ).setText( form );
					}
					break;
				}
			}
		} );
		
		Button submit = ( Button )findViewById( R.id.wordresultinfo_submit );
		submit.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick( View v ) {
				int id = rbg.getCheckedRadioButtonId();
				
				if( id == -1 ) {
					Toast.makeText( WordResultInfoActivity.this, "You must choose one of the forms!", Toast.LENGTH_SHORT );
					return;
				}
				String indexString = ( String )( ( ( Button )findViewById( id ) ).getTag() );
				int index = Integer.parseInt( indexString );
				if( index == -1 ) {
					return;
				}
				String form = forms.get( index );
				switch( letterCaseState ) {
				case ALL_LOWER: form = form.toLowerCase();
					break;
				case FIRST_UPPER: 
					if( form.length() >= 1 ) {
						form = Character.toUpperCase( form.charAt( 0 ) ) + form.substring( 1 );
					}
					else {
						form = form.toUpperCase();
					}
					break;
				case ALL_UPPER: form = form.toUpperCase();
					break;
				}
				Word w = new Word( form );
				w.setSentences( SharedMethods.wordResult.getSentences() );
				SharedMethods.wordLib.addNewWord( w );

				Toast.makeText( WordResultInfoActivity.this, form + " added successfully!", Toast.LENGTH_SHORT ).show();
				setResult( 1, null );
				submitted = true;
				finish();
			}
		} );
		
		Button ignore = ( Button )findViewById( R.id.wordresultinfo_ignore );
		ignore.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick( View v ) {
				int id = rbg.getCheckedRadioButtonId();
				if( id == -1 ) {
					Toast.makeText( WordResultInfoActivity.this, "You must choose one of the forms!", Toast.LENGTH_SHORT ).show();
					return;
				}
				String indexString = ( String )( ( ( Button )findViewById( id ) ).getTag() );
				int index = Integer.parseInt( indexString );
				String form = forms.get( index ).toLowerCase();
				Word w = new Word( form );
				SharedMethods.wordLib.addIgnoredWord( w );
				Toast.makeText( WordResultInfoActivity.this, form + " ignored successfully!", Toast.LENGTH_SHORT ).show();
				setResult( 1, null );
				finish();
			}
		} );
		
		Button known = ( Button )findViewById( R.id.wordresultinfo_alreadyKnown );
		known.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick( View v ) {
				int id = rbg.getCheckedRadioButtonId();
				if( id == -1 ) {
					Toast.makeText( WordResultInfoActivity.this, "You must choose one of the forms!", Toast.LENGTH_SHORT ).show();
					return;
				}
				String indexString = ( String )( ( ( Button )findViewById( id ) ).getTag() );
				int index = Integer.parseInt( indexString );
				String form = forms.get( index ).toLowerCase();
				Word w = new Word( form );
				SharedMethods.wordLib.addKnownWord( w );
				Toast.makeText( WordResultInfoActivity.this, form + " known successfully!", Toast.LENGTH_SHORT ).show();
				setResult( 1, null );
				finish();
			}
		} );
	}
	
	@Override
	public void onStop() {
		super.onStop();
		if( !submitted ){
			sentences.addAll( backstore );
		}
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
	
	@Override
    public boolean onOptionsItemSelected( MenuItem item ) {
    	return SharedMethods.sharedMenuEventHandler( item, this );
    }
}
